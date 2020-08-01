package ru.test.livanov.model

import com.google.gson.annotations.SerializedName

data class RepoDetail(
    val id: Int,
    val name: String,
    @SerializedName("full_name")
    val fullName: String,
    val language: String?,
    @SerializedName("forks_count")
    val forksCount: String? = "0",
    @SerializedName("stargazers_count")
    val stargazersCount: String? = "0",
    var commits: List<RepoCommits>,
    val owner: Owner,
    @SerializedName("html_url")
    val url:  String
)