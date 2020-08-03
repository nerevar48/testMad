package ru.test.livanov.di

import dagger.Component
import ru.test.livanov.DetailActivity
import ru.test.livanov.MainActivity
import ru.test.livanov.fragment.MainListFragment
import ru.test.livanov.fragment.SecondListFragment
import ru.test.livanov.api.GitHubRepository
import ru.test.livanov.viewmodel.RepoViewModel
import ru.test.livanov.di.module.AppModule
import ru.test.livanov.viewmodel.RepoCommitViewModel
import ru.test.livanov.viewmodel.RepoDbViewModel
import ru.test.livanov.viewmodel.RepoDetailViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun getRepoViewModel(): RepoViewModel
    fun getRepoDbViewModel(): RepoDbViewModel
    fun getRepoCommitViewModel(): RepoCommitViewModel
    fun injectsMainActivity(activity: MainActivity)
    fun injectsMainFragment(fragment: MainListFragment)
    fun injectsSecondFragment(fragment: SecondListFragment)
    fun injectsDetailActivity(activity: DetailActivity)
    fun injectGitHubRepository(injectTarget: GitHubRepository)
    fun injectRepoViewModel(injectTarget: RepoViewModel)
    fun injectRepoDbViewModel(injectTarget: RepoDbViewModel)
    fun injectRepoDetailViewModel(injectTarget: RepoDetailViewModel)
    fun injectRepoCommitViewModel(injectTarget: RepoCommitViewModel)
}