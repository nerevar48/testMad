package ru.test.livanov.model

import com.google.gson.annotations.SerializedName

data class RepoCommits(
    val sha: String,
    val commit: RepoCommit,
    @SerializedName("html_url")
    val url: String
)