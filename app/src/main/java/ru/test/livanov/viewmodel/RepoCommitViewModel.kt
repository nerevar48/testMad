package ru.test.livanov.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.test.livanov.App
import ru.test.livanov.api.GitHubRepository
import ru.test.livanov.di.DaggerAppComponent
import ru.test.livanov.model.RepoCommits
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RepoCommitViewModel : ViewModel() {

    init {
        DaggerAppComponent.create().injectRepoCommitViewModel(this)
    }

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    @Inject
    lateinit var repository: GitHubRepository

    private val reposLiveData = MutableLiveData<List<RepoCommitViewModel>>()
    private val reposViewList = mutableListOf<RepoCommitViewModel>()

    lateinit var repoCommit: RepoCommits
    var ownerName: String? = null
    var name: String? = null
    private val repoLiveData = MutableLiveData<RepoCommits>()

    fun getLiveData(): LiveData<RepoCommits> {
        return repoLiveData
    }

    fun fetchRepoCommits(count: Int = 10): LiveData<List<RepoCommitViewModel>> {
        if (ownerName != null && name != null) {
            scope.launch {
                val repoCommits = repository.getRepoCommits(ownerName!!, name!!, 1, count)
                repoCommits?.forEach {
                    val repoCommitViewModel = App.component.getRepoCommitViewModel()
                    repoCommitViewModel.repoCommit = it
                    reposViewList.add(repoCommitViewModel)
                }
                reposLiveData.postValue(reposViewList)
            }
        }
        return reposLiveData
    }

    fun cancelAllRequests() = scope.coroutineContext.cancelChildren()

}