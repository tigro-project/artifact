package com.example.tigro.database

import android.content.Context
import androidx.room.Room

/**
 * Singleton to access the tigro AppDatabase
 */
object AppDatabaseSingleton {

    private var instance: AppDatabase? = null

    // can encrypt DB with SQLcipher. see https://commonsware.com/Room/pages/chap-sqlcipher-003.html

    fun getDatabase(context: Context): AppDatabase {
        if (instance == null) {
            synchronized(AppDatabaseSingleton::class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "contact"
                    )
                        .build()
                }
            }
        }
        return instance!!
    }
}