package ru.test.livanov.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
open class Repo {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    lateinit var name: String
    var description: String? = ""
    var ownerName: String = ""
    var language: String? = ""
    var forksCount: String? = "0"
    var starsCount: String? = "0"
    var commitsCount: String? = "0"
    @Ignore
    lateinit var commits: List<RepoCommits>
    @Ignore
    lateinit var owner: Owner

    fun fillFromDetail(repoDetail: RepoDetail) {
        language = repoDetail.language ?: "N/A"
        forksCount = repoDetail.forksCount ?: "0"
        starsCount = repoDetail.stargazersCount ?: "0"
    }
}

