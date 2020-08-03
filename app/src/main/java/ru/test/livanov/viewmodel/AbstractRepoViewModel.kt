package ru.test.livanov.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancelChildren
import ru.test.livanov.model.Repo

abstract class AbstractRepoViewModel : ViewModel() {

    protected val reposLiveData = MutableLiveData<Pair<MutableList<AbstractRepoViewModel>, Boolean>>()
    protected val reposViewList = mutableListOf<AbstractRepoViewModel>()

    lateinit var repo: Repo
    protected val repoLiveData = MutableLiveData<Repo>()

    fun getLiveData(): LiveData<Repo> {
        return repoLiveData
    }

    fun getListLiveData(): LiveData<Pair<MutableList<AbstractRepoViewModel>, Boolean>> {
        return reposLiveData
    }

    fun cancelAllRequests() = viewModelScope.coroutineContext.cancelChildren()

    fun repoIsInitialized(): Boolean {
        return ::repo.isInitialized
    }
}
