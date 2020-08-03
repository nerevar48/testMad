package ru.test.livanov.api

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.test.livanov.model.RepoDetail
import ru.test.livanov.model.Repo
import ru.test.livanov.model.RepoCommits

interface GitHubApi {

    @GET("repositories")
    fun getRepos(@Query("since") page: Int): Deferred<Response<List<Repo>>>

    @GET("users/{owner}/repos")
    fun getUserRepos(@Path("owner") owner:String): Deferred<Response<List<Repo>>>

    @GET("repos/{owner}/{name}")
    fun getRepoDetail(@Path("owner") owner:String, @Path("name") name:String): Deferred<Response<RepoDetail>>

    @GET("repos/{owner}/{name}/commits")
    fun getCommits(@Path("owner") owner:String, @Path("name") name:String, @Query("page") page: Int, @Query("per_page") perPage: Int): Deferred<Response<List<RepoCommits>>>
}