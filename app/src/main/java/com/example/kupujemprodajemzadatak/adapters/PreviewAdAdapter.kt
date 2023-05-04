package com.example.kupujemprodajemzadatak.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.data.models.Ad
import com.example.data.view_model.DataViewModel
import com.example.kupujemprodajemzadatak.R
import com.example.kupujemprodajemzadatak.utils.setViewGone
import com.example.kupujemprodajemzadatak.utils.setViewVisible

class PreviewAdAdapter(private val context: Context, private val dataViewModel: DataViewModel, private val listener: PreviewAdAdapterInterface? = null) : PagingDataAdapter<Ad, PreviewAdAdapter.OpenAdViewHolder>(AdsPageAdapter.AdComparator) {

    interface PreviewAdAdapterInterface {
        fun onPreviewAd(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpenAdViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ad_preview_layout, parent, false)
        return OpenAdViewHolder(view)
    }

    override fun onBindViewHolder(holder: OpenAdViewHolder, position: Int) {

        val ad = getItem(position)

        // scroll to top
        holder.scrollView.scrollTo(0,0)

        ad?.apply {

            val adDetails = dataViewModel.getAdDetails(ad_id.toString())

            adDetails?.apply {

                holder.adFavorite.setOnClickListener {
                    onFavoriteBtnClick(it, ad)
                }

                // Item
                holder.adTitle.setTextColor(context.getColor(R.color.ad_title))
                holder.adTitle.text = ad.name
                holder.adLocation.text = ad.getLocationAndDate()
                holder.adPrice.text = ad.getPriceAndCurrency()
                Glide.with(context).load(ad.getPhotoUrl()).centerCrop().into(holder.adPreview)
                holder.adFavorite.isEnabled = true
                holder.adFavorite.alpha = 1f
                holder.adFavorite.setImageResource(getDrawableIdIconFavoriteAd(ad.is_following_ad))

                // Category
                val builder = SpannableStringBuilder()
                val str1 = SpannableString(context.getString(R.string.kategorija) + ": ")
                str1.setSpan(ForegroundColorSpan(context.getColor(R.color.category)), 0, str1.length, 0)
                builder.append(str1)

                val str2 = SpannableString("$category_name > $group_name")
                str2.setSpan(ForegroundColorSpan(context.getColor(R.color.ad_category)), 0, str2.length, 0)
                builder.append(str2)

                holder.tvCategory.setText(builder, TextView.BufferType.SPANNABLE)

                if (category_name.isEmpty() && group_name.isEmpty()) {
                    holder.tvCategory.setViewGone()
                } else {
                    holder.tvCategory.setViewVisible()
                }

                // Description
                val spanned = HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT)
                holder.tvAdDescription.text = spanned

                if (spanned.length > 1) {
                    holder.tvAdDescription.setViewVisible()
                } else {
                    holder.tvAdDescription.setViewGone()
                }

                // Image
                Glide.with(context).load(getPhotoUrl()).listener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.ivAdPreviewImage.setViewGone()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.ivAdPreviewImage.setViewVisible()
                        return false
                    }

                }) .into(holder.ivAdPreviewImage)

            } ?: kotlin.run {
                // Deleted ad
                holder.adTitle.text = context.getString(R.string.oglas_obrisan)
                holder.adTitle.setTextColor(context.getColor(R.color.ad_title_deleted))
                holder.adLocation.text = ""
                holder.adPrice.text = ""
                holder.tvCategory.setViewGone()
                holder.tvAdDescription.setViewGone()
                holder.ivAdPreviewImage.setViewGone()
                holder.adFavorite.isEnabled = false
                holder.adFavorite.alpha = 0.1f
            }

            listener?.onPreviewAd(position)
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

    inner class OpenAdViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val scrollView: ScrollView = view.findViewById(R.id.scroll_view)
        val adPreview: ImageView = view.findViewById(R.id.iv_preview)
        val adTitle: TextView = view.findViewById(R.id.tv_title)
        val adLocation: TextView = view.findViewById(R.id.tv_location)
        val adPrice: TextView = view.findViewById(R.id.tv_price)
        val adFavorite: ImageView = view.findViewById(R.id.btn_favorite_ad)

        val tvCategory: TextView = view.findViewById(R.id.tv_category)
        val tvAdDescription: TextView = view.findViewById(R.id.tv_ad_description)
        val ivAdPreviewImage: ImageView = view.findViewById(R.id.iv_ad_preview_image)
    }

}