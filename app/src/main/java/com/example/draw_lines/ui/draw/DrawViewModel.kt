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
        private const val STATIC_COLOR = -50
    }

    private val repository: LineRepository by inject()

    private val _callDraw = SingleLiveEvent<Boolean>()
    val callDraw: LiveData<Boolean> get() = _callDraw

    val valueA = MutableLiveData<String>()
    val valueB = MutableLiveData<String>()

    fun onClickCallDraw() {
        _callDraw.postValue(true)
    }

    fun formatData(): LineGraphSeries<DataPoint>? {
        return try {
            drawLine(valueA.value?.toFloat()!!, valueB.value?.toFloat()!!)
        } catch (nfe: NumberFormatException) {
            null
        }
    }

    private fun drawLine(valueA: Float, valueB: Float): LineGraphSeries<DataPoint> {
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
        return series
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