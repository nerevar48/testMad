package ru.test.livanov.di.module

import dagger.Module
import dagger.Provides
import ru.test.livanov.api.ApiFactory
import ru.test.livanov.api.GitHubApi
import ru.test.livanov.api.GitHubRepository
import ru.test.livanov.model.Repo
import ru.test.livanov.ui.RepoListAdapter
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

    @Provides
    fun provideRepo(): Repo {
        return Repo()
    }

    @Provides
    fun provideRepoViewModel(): RepoViewModel {
        return RepoViewModel()
    }

    @Provides
    fun provideRepoListAdapter(): RepoListAdapter {
        return RepoListAdapter()
    }

}