package ru.test.livanov.api

import ru.test.livanov.model.RepoDetail
import ru.test.livanov.model.Repo
import ru.test.livanov.di.DaggerAppComponent
import ru.test.livanov.model.RepoCommits
import javax.inject.Inject

class GitHubRepository : BaseRepository() {

    init {
        DaggerAppComponent.create().injectGitHubRepository(this)
    }

    @Inject
    lateinit var api: GitHubApi

    suspend fun getRepos(since: Int) : List<Repo>? {

        val repositoriesResponse = safeApiCall(
            call = { api.getRepos(since).await() },
            errorMessage = "Error fetching repositories"
        )
        val repos = repositoriesResponse?.toList()
        repos?.map {
            it.ownerName = it.owner.login
        }

        return repos
    }

    suspend fun getUserRepos(owner: String) : List<Repo>? {

        val repositoriesResponse = safeApiCall(
            call = { api.getUserRepos(owner).await() },
            errorMessage = "Error fetching repositories"
        )
        val repos = repositoriesResponse?.toList()
        repos?.map {
            it.ownerName = it.owner.login
        }

        return repos
    }

    suspend fun getRepoDetail(owner: String, name: String): RepoDetail? {
        return safeApiCall(
            call = { api.getRepoDetail(owner, name).await() },
            errorMessage = "Error fetching repository detail info"
        )
    }

    suspend fun getRepoCommits(owner: String, name: String, page: Int = 1, perPage: Int = 10): List<RepoCommits>? {
        return safeApiCall(
            call = { api.getCommits(owner, name, page, perPage).await() },
            errorMessage = "Error fetching repository commits info"
        )
    }
    suspend fun getRepoCommitsCount(owner: String, name: String): Int {
        val headers = safeApiCallHeaders(
            call = { api.getCommits(owner, name, 1, 1).await() }
        )
        if (headers != null) {
            val links = headers["Link"]
            if (links != null) {
                val pages = Regex("""page.*?\?page=(\d+).*?rel="last""").find(links)
                if (pages != null && pages.groups.size == 2) {
                    return pages.groups[1]?.value?.toInt() ?: 0
                }
            }
        }

        return 0
    }


}