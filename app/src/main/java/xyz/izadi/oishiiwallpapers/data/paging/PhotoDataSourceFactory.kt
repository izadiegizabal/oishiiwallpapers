package xyz.izadi.oishiiwallpapers.data.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import xyz.izadi.oishiiwallpapers.data.UnsplashPhoto

class PhotoDataSourceFactory : DataSource.Factory<Int, UnsplashPhoto>() {
    val photoLiveDataSource = MutableLiveData<PageKeyedDataSource<Int, UnsplashPhoto>>()

    override fun create(): DataSource<Int, UnsplashPhoto> {
        val photoDataSource = PhotoDataSource()
        photoLiveDataSource.postValue(photoDataSource)

        return photoDataSource
    }

}