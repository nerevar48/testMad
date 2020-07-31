package ru.test.livanov.model

data class RepoCommits(
    val sha: String,
    val commit: RepoCommit
)