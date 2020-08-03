package ru.test.livanov.db

import androidx.room.*
import ru.test.livanov.model.Repo

@Dao
interface RepoDao {

    @Query("SELECT * FROM repo")
    fun getAll(): List<Repo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(repo: Repo)

    @Update
    fun update(repo: Repo)

    @Delete
    fun delete(repo: Repo)
}