package com.example.imgedit.dataBase.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "operation")
data class EditedImageModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var text: String? = null,
    var image: Uri
) : Serializable

