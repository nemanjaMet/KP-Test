package com.example.kupujemprodajemzadatak.screens.openad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.viewpager2.widget.ViewPager2
import com.example.data.view_model.DataViewModel
import com.example.kupujemprodajemzadatak.adapters.PreviewAdAdapter
import com.example.kupujemprodajemzadatak.databinding.FragmentOpenAdBinding
import com.example.kupujemprodajemzadatak.screens.MasterScreenFragment
import com.example.kupujemprodajemzadatak.shared_view_models.SharedViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class OpenAdScreenFragment : MasterScreenFragment(), PreviewAdAdapter.PreviewAdAdapterInterface {

    private var _binding: FragmentOpenAdBinding? = null
    private val binding get() = _binding

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val dataViewModel: DataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpenAdBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBackBtnVisible()
        setViewPager()

    }

    private fun setViewPager() {
        val pageAdapter = PreviewAdAdapter(requireContext(), dataViewModel, this)

        dataViewModel.viewModelScope.launch {
            dataViewModel.pagerDataFlowAd.collectLatest { pagingData ->
                pageAdapter.submitData(pagingData)
            }
        }

        binding?.apply {
            viewPager.adapter = pageAdapter
            viewPager.setCurrentItem(sharedViewModel.previewAdPosition, false)
        }

        setOnPageChangeListener()
    }

    private fun setOnPageChangeListener() {
        binding?.apply {
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    sharedViewModel.previewAdPosition = position
                }
            })
        }
    }

    override fun onDetach() {
        hideBackBtn()

        super.onDetach()
    }

    override fun onPreviewAd(position: Int) {

    }

}