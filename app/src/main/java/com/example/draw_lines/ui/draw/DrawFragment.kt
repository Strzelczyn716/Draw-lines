package com.example.draw_lines.ui.draw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.draw_lines.R
import com.example.draw_lines.databinding.FragmentDrawBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class DrawFragment : Fragment() {

    companion object {
        private const val AXIS_LIMIT = 5.0
        private const val LEGEND_POSITION = 50
    }

    private var _binding: FragmentDrawBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DrawViewModel by viewModels()
    private val lineAdapter = LineAdapter { it -> viewModel.update(it) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDrawBinding.inflate(layoutInflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.recyclerView.adapter = lineAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val graph = binding.graph
        graph.viewport.isXAxisBoundsManual = true
        graph.viewport.setMinX(-AXIS_LIMIT)
        graph.viewport.setMaxX(AXIS_LIMIT)
        graph.viewport.isYAxisBoundsManual = true
        graph.viewport.setMinY(-AXIS_LIMIT)
        graph.viewport.setMaxY(AXIS_LIMIT)
        graph.legendRenderer.isVisible = true
        graph.legendRenderer.backgroundColor = resources.getColor(android.R.color.white)
        graph.legendRenderer.setFixedPosition(LEGEND_POSITION, LEGEND_POSITION)
        registerObserver()
        CoroutineScope(IO).launch {
            delay(2000)
            viewModel.drawAll()
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun registerObserver() {
        viewModel.allData.observe(viewLifecycleOwner, Observer {
            lineAdapter.setData(it)
        })
        viewModel.toRemove.observe(viewLifecycleOwner, {
            binding.graph.removeSeries(it)
        })
        viewModel.toDraw.observe(viewLifecycleOwner, {
            binding.graph.addSeries(it)
        })
        viewModel.callError.observe(viewLifecycleOwner, {
            Toast.makeText(
                context,
                resources.getText(R.string.bad_data),
                Toast.LENGTH_SHORT
            ).show()
        })
    }
}