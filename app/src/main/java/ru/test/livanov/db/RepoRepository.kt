package ru.test.livanov.db

import ru.test.livanov.model.Repo

class RepoRepository() {

    var db: RepoDao = AppDatabase.getInstance()?.repoDao()!!

    fun getAll(): List<Repo> {
        return db.getAll()
    }
    fun insert(repo: Repo) {
        return db.insert(repo)
    }
    fun update(repo: Repo) {
        return db.update(repo)
    }
    fun delete(repo: Repo) {
        return db.delete(repo)
    }

}