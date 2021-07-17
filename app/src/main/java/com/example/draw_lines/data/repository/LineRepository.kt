package com.example.draw_lines.data.repository

import androidx.lifecycle.LiveData
import com.example.draw_lines.data.db.LineDao
import com.example.draw_lines.data.model.Line

class LineRepository(private val lineDao: LineDao) {

    fun getAll(): LiveData<List<Line>> {
        return lineDao.getAll()
    }

    fun insertAll(line: Line) {
        lineDao.insertAll(line)
    }

    fun update(line: Line) {
        lineDao.update(line)
    }

}