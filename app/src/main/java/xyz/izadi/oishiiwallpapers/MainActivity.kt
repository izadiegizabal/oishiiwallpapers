package xyz.izadi.oishiiwallpapers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions
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
    private lateinit var photosViewModel: PhotosViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        Mapbox.getInstance(this, BuildConfig.MAPBOX_API_KEY)

        binding = setContentView(this, R.layout.activity_main)
        binding.rv.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.lifecycleOwner = this
        binding.isLoading = true

        photosViewModel = ViewModelProvider(
            this, PhotosViewModel.Factory(application = application)
        ).get(PhotosViewModel::class.java)
        adapter = PhotoAdapter(this)
        observeViewModel(photosViewModel)
        rv.adapter = adapter

        setUpQueryListener()
        setUpScrollListener()
        setUpModeListeners()
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

    private fun setUpQueryListener() {
        sv_food.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                photosViewModel.offerNewQuery(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                hideKeyboard(this@MainActivity)
                photosViewModel.offerNewQuery(query)
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

    private fun setUpModeListeners() {
        setUpColorListener()
        setUpCountryListener()
    }

    private fun setUpColorListener() {
        iv_color_selector.setOnClickListener {
            if (hsv_colours_selector.isGone) {
                hsv_colours_selector.visibility = View.VISIBLE
                iv_color_selector.visibility = View.GONE
                iv_explore.visibility = View.GONE
            } else {
                hsv_colours_selector.visibility = View.GONE
                iv_color_selector.visibility = View.VISIBLE
                iv_explore.visibility = View.VISIBLE
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

            photosViewModel.offerNewColour(pickedColour)

            hsv_colours_selector.visibility = View.GONE
            iv_color_selector.visibility = View.VISIBLE
            iv_explore.visibility = View.VISIBLE
        }
    }

    private fun setUpCountryListener() {
        iv_explore.setOnClickListener {
            val intent = PlacePicker.IntentBuilder()
                .accessToken(BuildConfig.MAPBOX_API_KEY)
                .placeOptions(
                    PlacePickerOptions.builder()
                        .statingCameraPosition(
                            CameraPosition.Builder()
                                .target(LatLng(35.6762, 139.6503))
                                .zoom(1.0)
                                .build()
                        )
                        .build()
                )
                .build(this)
            startActivityForResult(intent, PLACE_SELECTION_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PLACE_SELECTION_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            val carmenFeature = PlacePicker.getPlace(data)
            if (carmenFeature?.id() == null) {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.error_no_country),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val places = carmenFeature.placeName()?.split(",")
                sv_food.setQuery("${places?.get(places.size - 1)} food", true)
            }

        }
    }

    companion object {
        private val PLACE_SELECTION_REQUEST_CODE = 56789
    }
}