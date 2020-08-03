package ru.test.livanov.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.test.livanov.App
import ru.test.livanov.model.Repo
import ru.test.livanov.util.DATABASE_NAME

@Database(entities = [Repo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getInstance(): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        App.context,
                        AppDatabase::class.java, DATABASE_NAME).allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}