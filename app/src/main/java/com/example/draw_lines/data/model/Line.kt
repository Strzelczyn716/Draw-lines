package com.example.draw_lines.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "line_table")
data class Line(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val valueA: Float,
    val valueB: Float,
    var draw: Boolean
) : Parcelable