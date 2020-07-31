package ru.test.livanov.model

import com.google.gson.annotations.SerializedName

data class RepoDetail(
    val id: Int,
    val language: String?,
    @SerializedName("forks_count")
    val forksCount: String? = "0",
    @SerializedName("stargazers_count")
    val stargazersCount: String? = "0",
    @SerializedName("commits_url")
    val commitsUrl: String = "0"
)