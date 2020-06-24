package xyz.izadi.oishiiwallpapers.data

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
    @BindingAdapter("loadImageUrl")
    fun loadImage(view: ImageView, imageUrl: String?) {
        if(imageUrl != null){
            view.load(imageUrl)
        }
    }
}