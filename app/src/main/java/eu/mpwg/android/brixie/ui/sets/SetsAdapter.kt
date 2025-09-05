package eu.mpwg.android.brixie.ui.sets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import eu.mpwg.android.brixie.R
import eu.mpwg.android.brixie.data.local.entities.SetEntity
import eu.mpwg.android.brixie.databinding.ItemSetBinding

class SetsAdapter(
    private val onSetClick: (SetEntity) -> Unit,
    private val onFavoriteClick: (SetEntity) -> Unit
) : ListAdapter<SetEntity, SetsAdapter.SetViewHolder>(SetDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val binding = ItemSetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SetViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class SetViewHolder(
        private val binding: ItemSetBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(set: SetEntity) {
            binding.apply {
                textSetName.text = set.name ?: "Unbekanntes Set"
                textSetNumber.text = set.setNum
                
                // Show parts count if available
                if (set.numParts != null && set.numParts > 0) {
                    textSetParts.text = root.context.getString(R.string.set_parts, set.numParts)
                    textSetParts.isVisible = true
                } else {
                    textSetParts.isVisible = false
                }
                
                // Show year if available
                if (set.year != null && set.year > 0) {
                    textSetYear.text = root.context.getString(R.string.set_year, set.year)
                    textSetYear.isVisible = true
                } else {
                    textSetYear.isVisible = false
                }
                
                // Load set image with caching
                imageSet.load(set.setImgUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_image_placeholder)
                    error(R.drawable.ic_image_error)
                    memoryCachePolicy(CachePolicy.ENABLED)
                    diskCachePolicy(CachePolicy.ENABLED)
                }
                
                // Set favorite status
                buttonFavorite.setIconResource(
                    if (set.isFavorite) R.drawable.ic_favorite_filled_24
                    else R.drawable.ic_favorite_outline_24
                )
                
                // Click listeners
                root.setOnClickListener {
                    onSetClick(set)
                }
                
                buttonFavorite.setOnClickListener {
                    onFavoriteClick(set)
                }
            }
        }
    }
    
    private class SetDiffCallback : DiffUtil.ItemCallback<SetEntity>() {
        override fun areItemsTheSame(oldItem: SetEntity, newItem: SetEntity): Boolean {
            return oldItem.setNum == newItem.setNum
        }
        
        override fun areContentsTheSame(oldItem: SetEntity, newItem: SetEntity): Boolean {
            return oldItem == newItem
        }
    }
}