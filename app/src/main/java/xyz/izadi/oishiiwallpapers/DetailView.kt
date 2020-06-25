package xyz.izadi.oishiiwallpapers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.activity_detail_view.*
import xyz.izadi.oishiiwallpapers.data.api.UnsplashPhoto
import xyz.izadi.oishiiwallpapers.databinding.ActivityDetailViewBinding

class DetailView : AppCompatActivity() {
    private lateinit var binding: ActivityDetailViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_view)
        val photo = intent.getParcelableExtra<UnsplashPhoto>("photo")
        binding.photo = photo
        supportStartPostponedEnterTransition()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = photo?.user?.name
        supportActionBar?.subtitle =
            if (photo?.user?.instagram_username != null) "@${photo.user.instagram_username}"
            else ""

        iv_download.setOnClickListener{
            if (photo == null) return@setOnClickListener
            val downloadIntent = Intent(Intent.ACTION_VIEW)
            downloadIntent.data = Uri.parse(photo.links.download)
            if (downloadIntent.resolveActivity(packageManager) != null) {
                startActivity(downloadIntent)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // In order for the sharedelement transition to work
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}