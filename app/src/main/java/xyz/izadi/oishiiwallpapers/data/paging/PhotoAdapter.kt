package xyz.izadi.oishiiwallpapers.data.paging

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_view_photo_view.view.*
import xyz.izadi.oishiiwallpapers.DetailView
import xyz.izadi.oishiiwallpapers.R
import xyz.izadi.oishiiwallpapers.data.api.UnsplashPhoto
import xyz.izadi.oishiiwallpapers.databinding.RecyclerViewPhotoViewBinding


class PhotoAdapter constructor(private val context: Context) :
    PagedListAdapter<UnsplashPhoto, PhotoAdapter.PhotoViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.recycler_view_photo_view,
                parent,
                false
            ) as RecyclerViewPhotoViewBinding
        return PhotoViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = getItem(position)

        if (photo != null && holder.binding != null) {
            holder.binding.setVariable(BR.photo, photo)
            holder.binding.executePendingBindings()
            holder.view.setOnClickListener {
                val intent = Intent(context, DetailView::class.java)
                intent.putExtra("photo", photo)
                val options = ActivityOptions
                    .makeSceneTransitionAnimation(
                        context as Activity,
                        holder.view.iv_photo,
                        holder.view.iv_photo.transitionName
                    )
                context.startActivity(intent, options.toBundle())
            }
        } else {
            Toast.makeText(context, "Error loading the image", Toast.LENGTH_LONG).show()
        }
    }

    inner class PhotoViewHolder(photoView: View) : RecyclerView.ViewHolder(photoView) {
        val view: View = photoView
        val binding: ViewDataBinding? = DataBindingUtil.bind(photoView)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UnsplashPhoto>() {
            override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: UnsplashPhoto,
                newItem: UnsplashPhoto
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}