package com.example.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.view_model.DataViewModel
import com.example.data.models.Ad

class AdPagingDataSource(private val dataViewModel: DataViewModel) : PagingSource<Int, Ad>() {

    override fun getRefreshKey(state: PagingState<Int, Ad>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Ad> {

        val pageNumber = params.key ?: 1 // current page

        return try {

            val data = dataViewModel.getPage(pageNumber)

            data?.let {

                val maxPage = it.pages // number of pages
                val nextPage = if (pageNumber.plus(1) <= maxPage) pageNumber.plus(1) else null // next page to show
                val prevKey = if (pageNumber == 1) null else pageNumber.minus(1) // previous page

                LoadResult.Page(it.ads, prevKey = prevKey, nextKey = nextPage)
            } ?: kotlin.run {
                LoadResult.Invalid()
            }

        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }

    }

}