package ru.test.livanov.di

import dagger.Component
import ru.test.livanov.MainActivity
import ru.test.livanov.api.GitHubRepository
import ru.test.livanov.viewmodel.RepoViewModel
import ru.test.livanov.di.module.AppModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun getRepoViewModel(): RepoViewModel
    fun injectsMainActivity(mainActivity: MainActivity)
    fun injectGitHubRepository(injectTarget: GitHubRepository)
    fun injectReposViewModel(injectTarget: RepoViewModel)
}