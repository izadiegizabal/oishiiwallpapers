package xyz.izadi.oishiiwallpapers

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import xyz.izadi.oishiiwallpapers.data.paging.PhotoAdapter
import xyz.izadi.oishiiwallpapers.data.paging.PhotosViewModel
import xyz.izadi.oishiiwallpapers.databinding.ActivityMainBinding
import xyz.izadi.oishiiwallpapers.utils.hideKeyboard


@FlowPreview
@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = setContentView(this, R.layout.activity_main)
        binding.rv.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.lifecycleOwner = this

        binding.isLoading = true

        val photosViewModel = ViewModelProvider(
            this, PhotosViewModel.Factory(application = application)
        ).get(PhotosViewModel::class.java)


        adapter = PhotoAdapter(this)

        observeViewModel(photosViewModel)

        rv.adapter = adapter

        setUpQueryListener(photosViewModel)

        setUpScrollListener()
    }

    private fun observeViewModel(photosViewModel: PhotosViewModel) {
        photosViewModel.photoPagedList?.removeObservers(this)
        photosViewModel.photoPagedList?.observe(this, Observer { photos ->

            if (photos != null) {
                adapter.submitList(photos)
                Handler().postDelayed({
                    binding.isLoading = false
                }, 2000)
            }
        })
    }

    private fun setUpQueryListener(photosViewModel: PhotosViewModel) {
        sv_food.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                photosViewModel.queryChannel.offer(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                photosViewModel.queryChannel.offer(query)
                hideKeyboard(this@MainActivity)
                return true
            }
        })
    }

    private fun setUpScrollListener() {
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    hideKeyboard(this@MainActivity)
                }
            }
        })
    }
}