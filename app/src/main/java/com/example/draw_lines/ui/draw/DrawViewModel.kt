package com.example.draw_lines.ui.draw

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.draw_lines.data.model.Line
import com.example.draw_lines.data.repository.LineRepository
import com.example.draw_lines.utils.SingleLiveEvent
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class DrawViewModel : ViewModel(), KoinComponent {

    companion object {
        private const val AXIS_LIMIT = 6.0
        private const val ALFA_COLOR = 255
        private const val COLOR = 256
        private const val STATIC_COLOR = -70
    }

    private val repository: LineRepository by inject()

    private val _allData = repository.getAll()
    val allData: LiveData<List<Line>> get() = _allData

    private val map = mutableMapOf<String, LineGraphSeries<DataPoint>>()

    private val _toDraw = SingleLiveEvent<LineGraphSeries<DataPoint>>()
    val toDraw: LiveData<LineGraphSeries<DataPoint>> get() = _toDraw

    private val _toRemove = SingleLiveEvent<LineGraphSeries<DataPoint>>()
    val toRemove: LiveData<LineGraphSeries<DataPoint>> get() = _toRemove

    private val _callError = SingleLiveEvent<Boolean>()
    val callError: LiveData<Boolean> get() = _callError

    val valueA = MutableLiveData<String>()
    val valueB = MutableLiveData<String>()


    fun onClickCallDraw() {
        try {
            drawLine(valueA.value?.toFloat()!!, valueB.value?.toFloat()!!, false)
        } catch (nfe: NumberFormatException) {
            _callError.postValue(true)
        }
    }

    fun update(it: Line) {
        updateLine(it)
        if (!it.draw) {
            val key = if (it.valueB < 0) {
                "${it.valueA}x${it.valueB}"
            } else {
                "${it.valueA}x+${it.valueB}"
            }
            _toRemove.postValue(map[key])
        } else {
            drawLine(it.valueA, it.valueB, true)
        }
    }

    fun drawAll() {
        allData.value?.filter { it -> it.draw }
            ?.forEach { drawLine(it.valueA, it.valueB, true) }
    }

    private fun drawLine(
        valueA: Float,
        valueB: Float,
        reDraw: Boolean
    ) {
        CoroutineScope(IO).launch {
            val series = LineGraphSeries(
                arrayOf(
                    DataPoint(-AXIS_LIMIT, valueA * (-AXIS_LIMIT) + valueB),
                    DataPoint(AXIS_LIMIT, valueA * (AXIS_LIMIT) + valueB)
                )
            )
            val rnd = Random()
            series.color =
                Color.argb(
                    ALFA_COLOR,
                    rnd.nextInt(COLOR) + STATIC_COLOR,
                    rnd.nextInt(COLOR) + STATIC_COLOR,
                    rnd.nextInt(COLOR) + STATIC_COLOR
                )
            if (valueB < 0) {
                series.title = "${valueA}x${valueB}"
            } else {
                series.title = "${valueA}x+${valueB}"
            }
            map[series.title] = series
            if (!reDraw) {
                addLine(Line(0, valueA, valueB, true))
            }
            _toDraw.postValue(series)
        }
    }

    private fun updateLine(line: Line) {
        CoroutineScope(IO).launch {
            repository.update(line)
        }
    }

    private fun addLine(line: Line) {
        CoroutineScope(IO).launch {
            repository.insertAll(line)
        }
    }

}