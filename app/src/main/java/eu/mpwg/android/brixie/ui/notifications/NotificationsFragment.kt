package eu.mpwg.android.brixie.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import eu.mpwg.android.brixie.BrixieApplication
import eu.mpwg.android.brixie.databinding.FragmentNotificationsBinding
import eu.mpwg.android.brixie.ui.ViewModelFactory
import eu.mpwg.android.brixie.ui.favorites.FavoritesViewModel
import eu.mpwg.android.brixie.ui.sets.SetsAdapter
import kotlinx.coroutines.launch

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModels {
        ViewModelFactory(
            (requireActivity().application as BrixieApplication).container.themeRepository,
            (requireActivity().application as BrixieApplication).container.setRepository
        )
    }
    private lateinit var favoritesAdapter: SetsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        favoritesAdapter = SetsAdapter(
            onSetClick = { set ->
                // Navigate to set detail
                // TODO: Implement navigation to set detail fragment
            },
            onFavoriteClick = { set ->
                viewModel.toggleFavorite(set.setNum)
            }
        )

        binding.recyclerViewFavorites.apply {
            adapter = favoritesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                binding.apply {
                    // Update empty state
                    val isEmpty = uiState.favoriteSets.isEmpty() && !uiState.isLoading
                    layoutEmpty.isVisible = isEmpty
                    
                    // Update favorites list
                    favoritesAdapter.submitList(uiState.favoriteSets)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}