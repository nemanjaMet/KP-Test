package com.example.kupujemprodajemzadatak.screens.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.data.view_model.DataViewModel
import com.example.data.models.Ad
import com.example.kupujemprodajemzadatak.R
import com.example.kupujemprodajemzadatak.adapters.AdsPageAdapter
import com.example.kupujemprodajemzadatak.databinding.FragmentHomeBinding
import com.example.kupujemprodajemzadatak.screens.MasterScreenFragment
import com.example.kupujemprodajemzadatak.shared_view_models.SharedViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeScreenFragment : MasterScreenFragment(), AdsPageAdapter.AdsAdapterInterface {

        companion object {
                private const val LOG_TAG = "HomeScreenFragment"
        }

        private var _binding: FragmentHomeBinding? = null
        private val binding get() = _binding

        private val sharedViewModel: SharedViewModel by activityViewModels()
        private val dataViewModel: DataViewModel by activityViewModels()

        override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View? {

                _binding = FragmentHomeBinding.inflate(layoutInflater)
                return binding?.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)

                dataViewModel.loadData(requireContext())
                setViewModelObserver()
        }

        private fun setPageAdAdapter() {
                val pageAdapter = AdsPageAdapter(requireContext(), dataViewModel.getNumberOfAdsPerPage(), dataViewModel.getNumberOfPages(), this)
                binding?.rvDataList?.apply {
                        this.adapter = pageAdapter
                        this.layoutManager = LinearLayoutManager(context)
                        scrollToPosition(sharedViewModel.previewAdPosition)
                }

                dataViewModel.viewModelScope.launch {
                        dataViewModel.pagerDataFlowAd.collectLatest { pagingData ->
                                pageAdapter.submitData(pagingData)
                        }
                }

        }

        private fun setViewModelObserver() {
                dataViewModel.isDataLoaded.observe(viewLifecycleOwner) { isDataLoaded ->
                        if (isDataLoaded == true) {
                                setPageAdAdapter()
                                sharedViewModel.hideSplashScreen()
                        }
                }
        }

        override fun onAdItemClick(ad: Ad, position: Int) {
                sharedViewModel.previewAdPosition = position

                openAdScreenFragment()
        }

        private fun openAdScreenFragment() {
                try {
                        findNavController().navigate(R.id.action_homeScreenFragment_to_openAdScreenFragment)
                } catch (ex: Exception) {
                        Log.e(LOG_TAG, ex.message, ex)
                }
        }

}