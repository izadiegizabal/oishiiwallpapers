package xyz.izadi.oishiiwallpapers.data.paging

import android.util.Log
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.launch
import xyz.izadi.oishiiwallpapers.data.PhotoRepository
import xyz.izadi.oishiiwallpapers.data.UnsplashPhoto

@ExperimentalCoroutinesApi
class PhotoDataSource constructor(
    private val currentQuery: ConflatedBroadcastChannel<String>,
    private val repository: PhotoRepository,
    private val scope: CoroutineScope
) : PageKeyedDataSource<Int, UnsplashPhoto>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, UnsplashPhoto>
    ) {
        getPhotos(1) { photos ->
            callback.onResult(photos, null, 2)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, UnsplashPhoto>) {
        getPhotos(params.key) { photos ->
            callback.onResult(photos, params.key + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, UnsplashPhoto>) {
        val adjacentKey = if (params.key > 1) params.key - 1 else null
        getPhotos(params.key) { photos ->
            callback.onResult(photos, adjacentKey)
        }
    }

    private fun getPhotos(
        newPageNum: Int,
        pagingCallback: (List<UnsplashPhoto>) -> Unit
    ) {
        scope.launch {
            val photos = repository.getNextPhotos("food ${currentQuery.valueOrNull.orEmpty()}", newPageNum)
            pagingCallback(photos.results)
        }
    }

}