package com.example.kupujemprodajemzadatak.adapters

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Space
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.data.models.Ad
import com.example.kupujemprodajemzadatak.R
import com.example.kupujemprodajemzadatak.utils.setViewGone
import com.example.kupujemprodajemzadatak.utils.setViewVisible


class AdsPageAdapter(private val context: Context, private val pageSize: Int, private val numberOfPages: Int,  private val listener: AdsAdapterInterface? = null) : PagingDataAdapter<Ad, AdsPageAdapter.AdViewHolder>(AdComparator) {

    interface AdsAdapterInterface {
        fun onAdItemClick(ad: Ad, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ad_item, parent, false)
        return AdViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdViewHolder, position: Int) {

        val item = getItem(position)

        if (item is Ad) {
            item.let { ad ->

                // first item space
                if (position == 0) {
                    holder.spaceTopOfAd.setViewVisible()
                } else {
                    holder.spaceTopOfAd.setViewGone()
                }

                // set page info
                if (position % pageSize == 0 && position > 0) {
                    holder.tvPageInfo.setViewVisible()
                    val pageInfoText = ("${position / pageSize + 1} od $numberOfPages")
                    holder.tvPageInfo.text = pageInfoText
                } else {
                    holder.tvPageInfo.setViewGone()
                }

                // set ad item
                Glide.with(context).load(ad.getPhotoUrl()).centerCrop().into(holder.adPreview)

                holder.adTitle.text = ad.name
                holder.adLocation.text = ad.getLocationAndDate()
                holder.adPrice.text = ad.getPriceAndCurrency()

                holder.adFavorite.setImageResource(getDrawableIdIconFavoriteAd(ad.is_following_ad))

                holder.adHolder.setOnClickListener {
                    listener?.onAdItemClick(ad, position)
                }

                holder.adFavorite.setOnClickListener {
                    onFavoriteBtnClick(it, ad)
                }

                // LAST ITEM copyright
                if (itemCount - 1 == position) {
                    holder.tvCopyright.setViewVisible()
                } else {
                    holder.tvCopyright.setViewGone()
                }
            }
        }

    }

    private fun onFavoriteBtnClick(view: View, ad: Ad) {
        ad.is_following_ad = !ad.is_following_ad

        (view as? ImageView?)?.setImageResource(getDrawableIdIconFavoriteAd(ad.is_following_ad))
    }

    private fun getDrawableIdIconFavoriteAd(isFavorite: Boolean): Int {

        return if (isFavorite) {
            R.drawable.star_full_icon
        } else {
            R.drawable.star_empty_icon
        }

    }

    object AdComparator : DiffUtil.ItemCallback<Ad>() {
        override fun areItemsTheSame(oldItem: Ad, newItem: Ad) =
            oldItem.ad_id == newItem.ad_id

        override fun areContentsTheSame(oldItem: Ad, newItem: Ad) =
            oldItem == newItem
    }

    inner class AdViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvPageInfo: TextView = view.findViewById(R.id.tv_page_info)
        val adHolder: ConstraintLayout = view.findViewById(R.id.cl_ad_holder)
        val adPreview: ImageView = view.findViewById(R.id.iv_preview)
        val adTitle: TextView = view.findViewById(R.id.tv_title)
        val adLocation: TextView = view.findViewById(R.id.tv_location)
        val adPrice: TextView = view.findViewById(R.id.tv_price)
        val adFavorite: ImageView = view.findViewById(R.id.btn_favorite_ad)
        val tvCopyright: TextView = view.findViewById(R.id.tv_kp_copyright)
        val spaceTopOfAd: Space = view.findViewById(R.id.space_top_of_ad)
    }

}