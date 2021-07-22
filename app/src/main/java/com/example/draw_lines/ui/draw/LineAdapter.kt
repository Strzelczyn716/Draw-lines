package com.example.draw_lines.ui.draw

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.draw_lines.data.model.Line
import com.example.draw_lines.databinding.LayoutLineListItemBinding

class LineAdapter(private val onClick: (Line) -> Unit) :
    RecyclerView.Adapter<LineAdapter.LineViewHolder>() {

    private var lineList = emptyList<Line>()

    inner class LineViewHolder(val binding: LayoutLineListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineViewHolder {
        val binding = LayoutLineListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return LineViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return lineList.size
    }

    override fun onBindViewHolder(holder: LineViewHolder, position: Int) {
        with(holder) {
            with(lineList[position]) {
                binding.checkBox.isChecked = draw
                if (valueB < 0) {
                    binding.textView.text = "${valueA}x${valueB}"
                } else {
                    binding.textView.text = "${valueA}x+${valueB}"
                }
                binding.checkBox.setOnClickListener {
                    draw = !draw
                    onClick.invoke(this)
                }
            }
        }
    }

    fun setData(shop: List<Line>) {
        this.lineList = shop
        notifyDataSetChanged()
    }
}