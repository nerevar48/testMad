package ru.test.livanov.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ru.test.livanov.App
import ru.test.livanov.api.GitHubRepository
import ru.test.livanov.di.DaggerAppComponent
import ru.test.livanov.model.RepoCommits
import javax.inject.Inject

class RepoCommitViewModel : ViewModel() {

    init {
        DaggerAppComponent.create().injectRepoCommitViewModel(this)
    }

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
            viewModelScope.launch {
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

    fun cancelAllRequests() = viewModelScope.coroutineContext.cancelChildren()

}