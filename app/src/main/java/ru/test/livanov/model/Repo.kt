package ru.test.livanov.model

open class Repo {
    var id: String = "0"
    lateinit var name: String
    var description: String? = ""
    var ownerName: String = ""
    var language: String? = ""
    var forksCount: String? = "0"
    var starsCount: String? = "0"
    var commitsCount: String? = "0"
    lateinit var commits: List<RepoCommits>
    lateinit var owner: Owner

    fun fillFromDetail(repoDetail: RepoDetail) {
        language = repoDetail.language ?: "N/A"
        forksCount = repoDetail.forksCount ?: "0"
        starsCount = repoDetail.stargazersCount ?: "0"
    }
}

