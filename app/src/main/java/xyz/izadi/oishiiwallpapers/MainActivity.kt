package xyz.izadi.oishiiwallpapers

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import xyz.izadi.oishiiwallpapers.data.paging.PhotoAdapter
import xyz.izadi.oishiiwallpapers.data.paging.PhotosViewModel
import xyz.izadi.oishiiwallpapers.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = setContentView(this, R.layout.activity_main)
        binding.rv.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.rv.setHasFixedSize(true)
        binding.lifecycleOwner = this

        binding.isLoading = true

        val photosViewModel = ViewModelProvider(
            this, PhotosViewModel.Factory(application = application)
        ).get(PhotosViewModel::class.java)


        adapter = PhotoAdapter(this)

        observeViewModel(photosViewModel)

        rv.adapter = adapter
    }

    private fun observeViewModel(photosViewModel: PhotosViewModel) {
        photosViewModel.photoPagedList?.removeObservers(this)
        photosViewModel.photoPagedList?.observe(this, Observer { photos ->

            if (photos != null) {
                adapter.submitList(photos)
                Handler().postDelayed({
                    binding.isLoading = false
                },2000)
            }
        })
    }
}