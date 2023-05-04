package com.example.data.view_model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.data.constants.DataConstants
import com.example.data.helpers.FileHelper
import com.example.data.models.Ad
import com.example.data.models.DetaljiOglasa
import com.example.data.models.Oglas
import com.example.paging.AdPagingDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DataViewModel: ViewModel() {

    companion object {
        private const val PAGE_SIZE = 10
        private const val PREFETCH_DISTANCE = 2
        private const val INITIAL_LOAD_SIZE = 10
    }

    var isDataLoaded: MutableLiveData<Boolean> = MutableLiveData(false)
    lateinit var pagerDataFlowAd: Flow<PagingData<Ad>>
    private val pagesMap: HashMap<Int, Oglas> = hashMapOf()
    private val adDetailsMap: HashMap<String, DetaljiOglasa> = hashMapOf()

    private var numberOfPages = 4
    private var itemsPerPage = 30

    private fun setPagingDataFlow() {
        if (!this::pagerDataFlowAd.isInitialized)
            pagerDataFlowAd = Pager(config = PagingConfig(pageSize = PAGE_SIZE, prefetchDistance = PREFETCH_DISTANCE, initialLoadSize = INITIAL_LOAD_SIZE), pagingSourceFactory = { AdPagingDataSource(this) }).flow.cachedIn(viewModelScope)
    }

    fun loadData(context: Context) {
        // loading data from json
        viewModelScope.launch(Dispatchers.IO) {
            val response = FileHelper.loadJson(context, DataConstants.JSON_FILE_NAME)

            response.let {
                // populate hash maps
                pagesMap.putAll(response.listaOglasa.associateBy { it.page }.toMap())
                adDetailsMap.putAll(response.detaljiOglasa.associateBy { it.ad_id }.toMap())

                // getting number of pages and number of items per page
                pagesMap[1]?.apply {
                    numberOfPages = pages
                    itemsPerPage = ads.size
                }
            }

            setPagingDataFlow()

            isDataLoaded.postValue(true)
        }


    }

    fun getNumberOfAdsPerPage(): Int {
        return itemsPerPage
    }

    fun getNumberOfPages(): Int {
        return numberOfPages
    }

    fun getPage(page: Int): Oglas? {

        return if (pagesMap.containsKey(page))
            pagesMap[page]
        else
            null

    }

    fun getAdDetails(adId: String): DetaljiOglasa? {

        return if (adDetailsMap.containsKey(adId))
            adDetailsMap[adId]
        else
            null

    }

}