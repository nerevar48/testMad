package ru.test.livanov.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.test.livanov.App
import ru.test.livanov.api.GitHubRepository
import ru.test.livanov.model.Repo
import ru.test.livanov.di.DaggerAppComponent
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RepoViewModel : ViewModel() {

    init {
        DaggerAppComponent.create().injectRepoViewModel(this)
    }

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    @Inject
    lateinit var repository: GitHubRepository

    private val reposLiveData = MutableLiveData<List<RepoViewModel>>()
    private val reposViewList = mutableListOf<RepoViewModel>()

    lateinit var repo: Repo
    private val repoLiveData = MutableLiveData<Repo>()

    fun getLiveData(): LiveData<Repo> {
        return repoLiveData
    }

    fun fetchRepos(): LiveData<List<RepoViewModel>> {
        scope.launch {
            val repos = repository.getRepos()
            repos?.forEach {
                val reposViewModel = App.component.getRepoViewModel()
                reposViewModel.repo = it
                reposViewList.add(reposViewModel)
            }

            reposLiveData.postValue(reposViewList)
        }

        return reposLiveData
    }

    fun fetchRepoDetail() {
        if (this::repo.isInitialized) {
            scope.launch {
                val repoDetail = repository.getRepoDetail(repo.ownerName, repo.name)
                if (repoDetail != null) {
                    repo.fillFromDetail(repoDetail)
                    repoLiveData.postValue(repo)
                }
            }
        }
    }

    fun fetchRepoCommitsCount() {
        if (this::repo.isInitialized) {
            scope.launch {
                val repoCommitsCount = repository.getRepoCommitsCount(repo.ownerName, repo.name)
                repo.commitsCount = repoCommitsCount.toString()
                repoLiveData.postValue(repo)
            }
        }
    }

    fun cancelAllRequests() = coroutineContext.cancel()

}