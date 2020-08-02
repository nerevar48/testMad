package ru.test.livanov.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ru.test.livanov.api.GitHubRepository
import ru.test.livanov.di.DaggerAppComponent
import ru.test.livanov.model.RepoDetail
import javax.inject.Inject

class RepoDetailViewModel : ViewModel() {
    init {
        DaggerAppComponent.create().injectRepoDetailViewModel(this)
    }

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
            viewModelScope.launch {
                val repoDetailData = repository.getRepoDetail(ownerName!!, name!!)
                if (repoDetailData != null) {
                    repoDetail = repoDetailData
                }
                repoLiveData.postValue(repoDetail)
            }
        }
        return repoLiveData
    }

    fun cancelAllRequests() = viewModelScope.coroutineContext.cancelChildren()

}
