package ru.test.livanov.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ru.test.livanov.App
import ru.test.livanov.api.GitHubRepository
import ru.test.livanov.di.DaggerAppComponent
import javax.inject.Inject

class RepoViewModel : AbstractRepoViewModel() {

    init {
        DaggerAppComponent.create().injectRepoViewModel(this)
    }

    @Inject
    lateinit var repository: GitHubRepository

    fun fetchAll(since: Int = 0): LiveData<Pair<MutableList<AbstractRepoViewModel>, Boolean>> {
        viewModelScope.launch {
            val repos = repository.getRepos(since)
            if (repos != null) {
                if (since == 0) {
                    reposViewList.clear()
                }
                repos.forEach {
                    val reposViewModel = App.component.getRepoViewModel()
                    reposViewModel.repo = it
                    reposViewList.add(reposViewModel)
                }
            }
            reposLiveData.postValue(Pair(reposViewList, since != 0))
        }

        return reposLiveData
    }

    fun fetchRepoDetail() {
        if (repoIsInitialized()) {
            viewModelScope.launch {
                val repoDetail = repository.getRepoDetail(repo.ownerName, repo.name)
                if (repoDetail != null) {
                    repo.fillFromDetail(repoDetail)
                    repoLiveData.postValue(repo)
                }
            }
        }
    }

    fun fetchRepoCommitsCount() {
        if (repoIsInitialized()) {
            viewModelScope.launch {
                val repoCommitsCount = repository.getRepoCommitsCount(repo.ownerName, repo.name)
                repo.commitsCount = repoCommitsCount.toString()
                repoLiveData.postValue(repo)
            }
        }
    }


}