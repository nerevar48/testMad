package ru.test.livanov.model

data class RepoCommit(
    val message: String = "",
    val committer: RepoCommitCommitter
)