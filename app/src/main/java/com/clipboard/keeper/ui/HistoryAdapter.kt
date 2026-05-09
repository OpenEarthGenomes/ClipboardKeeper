package com.clipboard.keeper.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.clipboard.keeper.databinding.ItemClipboardBinding
import com.clipboard.keeper.data.ClipboardPreview
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private var items = listOf<ClipboardPreview>()
    private val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault())
    private var lastAnimatedPosition = -1

    fun submitList(newList: List<ClipboardPreview>) {
        items = newList
        lastAnimatedPosition = -1
        notifyDataSetChanged()
    }
    
    fun updateItem(updated: ClipboardPreview) {
        val index = items.indexOfFirst { it.id == updated.id }
        if (index != -1) {
            items = items.toMutableList().apply { set(index, updated) }
            notifyItemChanged(index)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemClipboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvPreview.text = item.preview
        holder.binding.tvTimestamp.text = dateFormat.format(Date(item.timestamp))
        
        // JAVÍTÁS: Az animáció láncolása javítva
        if (position > lastAnimatedPosition) {
            holder.itemView.alpha = 0f
            holder.itemView.translationY = 100f
            holder.itemView.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(300 + (position * 20L))
                .start()
            lastAnimatedPosition = position
        }
        
        holder.itemView.setOnClickListener { onItemClick(item.id) }
    }

    override fun getItemCount() = items.size

    class ViewHolder(val binding: ItemClipboardBinding) : RecyclerView.ViewHolder(binding.root)
}
