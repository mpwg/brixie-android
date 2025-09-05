package eu.mpwg.android.brixie.ui.dashboard

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import eu.mpwg.android.brixie.BrixieApplication
import eu.mpwg.android.brixie.R
import eu.mpwg.android.brixie.databinding.FragmentDashboardBinding
import eu.mpwg.android.brixie.ui.ViewModelFactory
import eu.mpwg.android.brixie.ui.sets.SetsAdapter
import eu.mpwg.android.brixie.ui.sets.SetsViewModel
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SetsViewModel by viewModels {
        ViewModelFactory(
            (requireActivity().application as BrixieApplication).container.themeRepository,
            (requireActivity().application as BrixieApplication).container.setRepository
        )
    }
    private lateinit var setsAdapter: SetsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupSearch()
        setupSwipeRefresh()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        setsAdapter = SetsAdapter(
            onSetClick = { set ->
                // Navigate to set detail
                // TODO: Implement navigation to set detail fragment
            },
            onFavoriteClick = { set ->
                viewModel.toggleFavorite(set.setNum)
            }
        )

        binding.recyclerViewSets.apply {
            adapter = setsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupSearch() {
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s?.toString() ?: ""
                viewModel.setSearchQuery(query)
            }
        })
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshSets()
        }
    }

    private fun setupClickListeners() {
        binding.buttonRetry.setOnClickListener {
            viewModel.refreshSets()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                binding.apply {
                    // Update loading states
                    progressBar.isVisible = uiState.isLoading && uiState.sets.isEmpty()
                    
                    // Update error state
                    layoutError.isVisible = uiState.errorMessage != null && uiState.sets.isEmpty()
                    textError.text = uiState.errorMessage ?: getString(R.string.error_loading_sets)
                    
                    // Update empty state
                    val isEmpty = uiState.sets.isEmpty() && !uiState.isLoading && uiState.errorMessage == null
                    layoutEmpty.isVisible = isEmpty
                    
                    // Update sets list
                    setsAdapter.submitList(uiState.sets)
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