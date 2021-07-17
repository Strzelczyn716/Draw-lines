package com.example.draw_lines.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.draw_lines.data.model.Line

@Dao
interface LineDao {

    @Query("SELECT * FROM line_table ORDER BY draw DESC")
    fun getAll(): LiveData<List<Line>>

    @Insert
    fun insertAll(line: Line)

    @Update
    fun update(line: Line)
}