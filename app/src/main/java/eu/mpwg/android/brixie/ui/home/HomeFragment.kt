package eu.mpwg.android.brixie.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import eu.mpwg.android.brixie.BrixieApplication
import eu.mpwg.android.brixie.R
import eu.mpwg.android.brixie.databinding.FragmentHomeBinding
import eu.mpwg.android.brixie.ui.ViewModelFactory
import eu.mpwg.android.brixie.ui.themes.ThemesAdapter
import eu.mpwg.android.brixie.ui.themes.ThemesViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ThemesViewModel by viewModels {
        ViewModelFactory(
            (requireActivity().application as BrixieApplication).container.themeRepository,
            (requireActivity().application as BrixieApplication).container.setRepository
        )
    }
    private lateinit var themesAdapter: ThemesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupSwipeRefresh()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        themesAdapter = ThemesAdapter { theme ->
            // Navigate to sets list for this theme
            // TODO: Implement navigation to sets fragment
        }

        binding.recyclerViewThemes.apply {
            adapter = themesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshThemes()
        }
    }

    private fun setupClickListeners() {
        binding.buttonRetry.setOnClickListener {
            viewModel.refreshThemes()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                binding.apply {
                    // Update loading states
                    progressBar.isVisible = uiState.isLoading && uiState.themes.isEmpty()
                    
                    // Update error state
                    layoutError.isVisible = uiState.errorMessage != null && uiState.themes.isEmpty()
                    textError.text = uiState.errorMessage ?: getString(R.string.error_loading_themes)
                    
                    // Update themes list
                    themesAdapter.submitList(uiState.themes)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isRefreshing.collect { isRefreshing ->
                binding.swipeRefreshLayout.isRefreshing = isRefreshing
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}