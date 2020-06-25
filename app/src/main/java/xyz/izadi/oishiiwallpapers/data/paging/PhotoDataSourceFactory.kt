package xyz.izadi.oishiiwallpapers.data.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import xyz.izadi.oishiiwallpapers.data.api.PhotoRepository
import xyz.izadi.oishiiwallpapers.data.api.UnsplashPhoto
import xyz.izadi.oishiiwallpapers.data.api.UnsplashQueryOptions

@ExperimentalCoroutinesApi
class PhotoDataSourceFactory(
    private val currentQuery: ConflatedBroadcastChannel<UnsplashQueryOptions>,
    private val repository: PhotoRepository,
    private val scope: CoroutineScope
) : DataSource.Factory<Int, UnsplashPhoto>() {
    val photoLiveDataSource = MutableLiveData<PageKeyedDataSource<Int, UnsplashPhoto>>()

    override fun create(): DataSource<Int, UnsplashPhoto> {
        val photoDataSource = PhotoDataSource(currentQuery, repository, scope)
        photoLiveDataSource.postValue(photoDataSource)

        return photoDataSource
    }

}