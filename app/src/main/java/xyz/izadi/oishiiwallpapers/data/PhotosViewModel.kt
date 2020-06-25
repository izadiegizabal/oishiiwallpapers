package xyz.izadi.oishiiwallpapers.data

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import xyz.izadi.oishiiwallpapers.data.api.PhotoRepository
import xyz.izadi.oishiiwallpapers.data.api.UnsplashPhoto
import xyz.izadi.oishiiwallpapers.data.api.UnsplashQueryOptions
import xyz.izadi.oishiiwallpapers.data.paging.PhotoDataSourceFactory

@ExperimentalCoroutinesApi
@FlowPreview
class PhotosViewModel(application: Application) : AndroidViewModel(application) {
    var photoPagedList: LiveData<PagedList<UnsplashPhoto>>? = null
    private var liveDataSource: LiveData<PageKeyedDataSource<Int, UnsplashPhoto>>? = null

    val queryChannel = ConflatedBroadcastChannel<UnsplashQueryOptions>()

    init {
        val repository = PhotoRepository()
        val photoDataSourceFactory =
            PhotoDataSourceFactory(
                currentQuery = queryChannel,
                repository = repository,
                scope = viewModelScope
            )

        liveDataSource = photoDataSourceFactory.photoLiveDataSource

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(20).build()

        photoPagedList = LivePagedListBuilder(photoDataSourceFactory, pagedListConfig)
            .build()

        queryChannel.asFlow()
            .debounce(QUERY_DEBOUNCE)
            .onEach { (liveDataSource as MutableLiveData<PageKeyedDataSource<Int, UnsplashPhoto>>).value?.invalidate() }
            .launchIn(viewModelScope)
    }

    class Factory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PhotosViewModel(application) as T
        }

    }

    companion object {
        private const val QUERY_DEBOUNCE = 500L
    }
}