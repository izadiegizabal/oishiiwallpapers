package xyz.izadi.oishiiwallpapers

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import xyz.izadi.oishiiwallpapers.data.PhotosViewModel
import xyz.izadi.oishiiwallpapers.data.api.UnsplashColor
import xyz.izadi.oishiiwallpapers.data.api.UnsplashQueryOptions
import xyz.izadi.oishiiwallpapers.data.paging.PhotoAdapter
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
        binding.rv.setHasFixedSize(true)
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

        setUpModeListeners(photosViewModel)
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
                offerNewQuery(newText, photosViewModel)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                hideKeyboard(this@MainActivity)
                offerNewQuery(query, photosViewModel)
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

    private fun setUpModeListeners(photosViewModel: PhotosViewModel) {
        iv_color_selector.setOnClickListener {
            if (hsv_colours_selector.isGone) {
                hsv_colours_selector.visibility = View.VISIBLE
                iv_color_selector.visibility = View.GONE
            } else {
                hsv_colours_selector.visibility = View.GONE
                iv_color_selector.visibility = View.VISIBLE
            }
        }
        rg_colour_picker.setOnCheckedChangeListener { _, checkedId ->
            var pickedColour: UnsplashColor = UnsplashColor.ANY
            when (checkedId) {
                R.id.rb_colour_rgb -> {
                    pickedColour = UnsplashColor.ANY
                    iv_color_selector.setImageResource(R.drawable.ic_rgb_colour)
                }
                R.id.rb_colour_bnw -> {
                    pickedColour = UnsplashColor.BLACKANDWHITE
                    iv_color_selector.setImageResource(R.drawable.ic_bnw_colour)
                }
                R.id.rb_colour_black -> {
                    pickedColour = UnsplashColor.BLACK
                    iv_color_selector.setImageResource(R.drawable.ic_black_colour)
                }
                R.id.rb_colour_white -> {
                    pickedColour = UnsplashColor.WHITE
                    iv_color_selector.setImageResource(R.drawable.ic_white_colour)
                }
                R.id.rb_colour_yellow -> {
                    pickedColour = UnsplashColor.YELLOW
                    iv_color_selector.setImageResource(R.drawable.ic_yellow_colour)
                }
                R.id.rb_colour_orange -> {
                    pickedColour = UnsplashColor.ORANGE
                    iv_color_selector.setImageResource(R.drawable.ic_orange_colour)
                }
                R.id.rb_colour_red -> {
                    pickedColour = UnsplashColor.RED
                    iv_color_selector.setImageResource(R.drawable.ic_red_colour)
                }
                R.id.rb_colour_pink -> {
                    pickedColour = UnsplashColor.MAGENTA
                    iv_color_selector.setImageResource(R.drawable.ic_pink_colour)
                }
                R.id.rb_colour_purple -> {
                    pickedColour = UnsplashColor.PURPLE
                    iv_color_selector.setImageResource(R.drawable.ic_purple_colour)
                }
                R.id.rb_colour_blue -> {
                    pickedColour = UnsplashColor.BLUE
                    iv_color_selector.setImageResource(R.drawable.ic_blue_colour)
                }
                R.id.rb_colour_teal -> {
                    pickedColour = UnsplashColor.TEAL
                    iv_color_selector.setImageResource(R.drawable.ic_teal_colour)
                }
                R.id.rb_colour_green -> {
                    pickedColour = UnsplashColor.GREEN
                    iv_color_selector.setImageResource(R.drawable.ic_green_colour)
                }
            }

            offerNewColour(pickedColour, photosViewModel)
            hsv_colours_selector.visibility = View.GONE
            iv_color_selector.visibility = View.VISIBLE
        }
    }

    private fun offerNewQuery(newText: String, photosViewModel: PhotosViewModel) {
        val currentQuery = photosViewModel.queryChannel.valueOrNull
        if (currentQuery != null && newText == currentQuery.query) return

        val newQuery = currentQuery ?: UnsplashQueryOptions()
        newQuery.query = newText
        photosViewModel.queryChannel.offer(newQuery)
    }

    private fun offerNewColour(newColor: UnsplashColor, photosViewModel: PhotosViewModel) {
        val currentQuery = photosViewModel.queryChannel.valueOrNull
        if (currentQuery != null && newColor == currentQuery.color) return

        val newQuery = currentQuery ?: UnsplashQueryOptions()
        newQuery.color = newColor
        photosViewModel.queryChannel.offer(newQuery)
    }
}