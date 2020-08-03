package ru.test.livanov.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.test.livanov.App
import ru.test.livanov.model.Repo
import ru.test.livanov.db.RepoRepository
import ru.test.livanov.di.DaggerAppComponent
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RepoDbViewModel : AbstractRepoViewModel() {

    init {
        DaggerAppComponent.create().injectRepoDbViewModel(this)
    }

    @Inject
    lateinit var repository: RepoRepository

    fun fetchAll(): LiveData<Pair<MutableList<AbstractRepoViewModel>, Boolean>> {
        viewModelScope.launch {
            val repos = repository.getAll()
            reposViewList.clear()
            repos.forEach {
                val reposViewModel = App.component.getRepoDbViewModel()
                reposViewModel.repo = it
                reposViewList.add(reposViewModel)
            }
            reposLiveData.postValue(Pair(reposViewList, false))
        }

        return reposLiveData
    }

    fun insert(repo: Repo) {
        viewModelScope.launch {
            repository.insert(repo)
        }
    }

    fun delete(repo: Repo) {
        viewModelScope.launch {
            repository.delete(repo)
        }
    }
}