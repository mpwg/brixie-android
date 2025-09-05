package eu.mpwg.android.brixie.ui.themes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.mpwg.android.brixie.data.local.entities.ThemeEntity
import eu.mpwg.android.brixie.databinding.ItemThemeBinding

class ThemesAdapter(
    private val onThemeClick: (ThemeEntity) -> Unit
) : ListAdapter<ThemeEntity, ThemesAdapter.ThemeViewHolder>(ThemeDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeViewHolder {
        val binding = ItemThemeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ThemeViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ThemeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ThemeViewHolder(
        private val binding: ItemThemeBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(theme: ThemeEntity) {
            binding.apply {
                textThemeName.text = theme.name ?: "Unbekanntes Thema"
                
                root.setOnClickListener {
                    onThemeClick(theme)
                }
            }
        }
    }
    
    private class ThemeDiffCallback : DiffUtil.ItemCallback<ThemeEntity>() {
        override fun areItemsTheSame(oldItem: ThemeEntity, newItem: ThemeEntity): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: ThemeEntity, newItem: ThemeEntity): Boolean {
            return oldItem == newItem
        }
    }
}