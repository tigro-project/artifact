package com.example.tigro.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contact(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "notes") val notes: String,
    @ColumnInfo(name = "security-key") val securityKeyAlias: String,
    @PrimaryKey(autoGenerate = true) var id: Int = 0 //last so that we don't have to pass ID value

) : Target()
