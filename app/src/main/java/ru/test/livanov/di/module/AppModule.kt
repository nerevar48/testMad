package ru.test.livanov.di.module

import dagger.Module
import dagger.Provides
import ru.test.livanov.api.ApiFactory
import ru.test.livanov.api.GitHubApi
import ru.test.livanov.api.GitHubRepository
import ru.test.livanov.db.RepoRepository
import ru.test.livanov.model.Repo
import ru.test.livanov.ui.RepoCommitListAdapter
import ru.test.livanov.ui.RepoListAdapter
import ru.test.livanov.ui.SwipeController
import ru.test.livanov.viewmodel.RepoCommitViewModel
import ru.test.livanov.viewmodel.RepoDbViewModel
import ru.test.livanov.viewmodel.RepoViewModel
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideApi(): GitHubApi {
        return ApiFactory.getClient()
    }

    @Singleton
    @Provides
    fun provideApiRepository(): GitHubRepository {
        return GitHubRepository()
    }

    @Singleton
    @Provides
    fun provideDbRepository(): RepoRepository {
        return RepoRepository()
    }

    @Provides
    fun provideRepo(): Repo {
        return Repo()
    }

    @Provides
    fun provideRepoViewModel(): RepoViewModel {
        return RepoViewModel()
    }

    @Provides
    fun provideRepoDbViewModel(): RepoDbViewModel {
        return RepoDbViewModel()
    }

    @Provides
    fun provideRepoCommitViewModel(): RepoCommitViewModel {
        return RepoCommitViewModel()
    }

    @Provides
    fun provideRepoListAdapter(): RepoListAdapter {
        return RepoListAdapter()
    }

    @Provides
    fun provideRepoCommitListAdapter(): RepoCommitListAdapter {
        return RepoCommitListAdapter()
    }

    @Provides
    fun provideSwipeController(): SwipeController {
        return SwipeController()
    }

}