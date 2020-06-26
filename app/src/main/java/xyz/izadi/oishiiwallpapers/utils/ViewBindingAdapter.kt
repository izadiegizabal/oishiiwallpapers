package xyz.izadi.oishiiwallpapers.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.api.load

object ViewBindingAdapter {

    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter(value = ["loadImageUrl", "photo_width", "photo_height"], requireAll = false)
    fun loadImage(view: ImageView, imageUrl: String?, width: Int?, height: Int?) {
        if (imageUrl != null) {

            // To prevent image flashes when scrolling up for now knowing the height
            if (width != null && height != null && view.measuredWidth > 0) {
                val rlp = view.layoutParams
                val ratio: Float = height.toFloat() / width.toFloat()
                rlp.height = (view.measuredWidth * ratio).toInt()
                view.layoutParams = rlp
            }

            view.load(imageUrl)
        }
    }
}