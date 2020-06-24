package xyz.izadi.oishiiwallpapers.data.paging

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import xyz.izadi.oishiiwallpapers.data.UnsplashPhoto

class PhotosViewModel(application: Application): AndroidViewModel(application) {
    var photoPagedList: LiveData<PagedList<UnsplashPhoto>>? = null
    private var liveDataSource: LiveData<PageKeyedDataSource<Int, UnsplashPhoto>>? = null

    init {
        val photoDataSourceFactory = PhotoDataSourceFactory()

        liveDataSource = photoDataSourceFactory.photoLiveDataSource

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10).build()

        photoPagedList = LivePagedListBuilder(photoDataSourceFactory, pagedListConfig)
            .build()
    }

    class Factory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return PhotosViewModel(application) as T
        }

    }
}