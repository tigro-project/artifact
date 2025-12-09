package com.example.tigro.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tigro.data.Contact


// Note: version number must be incremented if Contact changes
@Database(entities = [Contact::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}