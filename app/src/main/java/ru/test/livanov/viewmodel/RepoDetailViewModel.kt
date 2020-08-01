package ru.test.livanov.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.test.livanov.api.GitHubRepository
import ru.test.livanov.di.DaggerAppComponent
import ru.test.livanov.model.RepoDetail
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RepoDetailViewModel : ViewModel() {
    init {
        DaggerAppComponent.create().injectRepoDetailViewModel(this)
    }
    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    @Inject
    lateinit var repository: GitHubRepository

    lateinit var repoDetail: RepoDetail
    var ownerName: String? = null
    var name: String? = null

    private val repoLiveData = MutableLiveData<RepoDetail>()

    fun getLiveData(): LiveData<RepoDetail> {
        return repoLiveData
    }

    fun fetchRepoDetail(): LiveData<RepoDetail> {
        if (ownerName != null && name != null) {
            scope.launch {
                val repoDetailData = repository.getRepoDetail(ownerName!!, name!!)
                if (repoDetailData != null) {
                    repoDetail = repoDetailData
                }
                repoLiveData.postValue(repoDetail)
            }
        }
        return repoLiveData
    }

}
