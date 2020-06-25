package xyz.izadi.oishiiwallpapers.data.paging

import android.util.Log
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.launch
import xyz.izadi.oishiiwallpapers.data.api.PhotoRepository
import xyz.izadi.oishiiwallpapers.data.api.UnsplashPhoto

@ExperimentalCoroutinesApi
class PhotoDataSource constructor(
    private val currentQuery: ConflatedBroadcastChannel<String>,
    private val repository: PhotoRepository,
    private val scope: CoroutineScope
) : PageKeyedDataSource<Int, UnsplashPhoto>() {
    private val mTAG = this.javaClass.simpleName

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, UnsplashPhoto>
    ) {
        getPhotos(1, params.requestedLoadSize) { photos ->
            callback.onResult(photos, null, 2)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, UnsplashPhoto>) {
        getPhotos(params.key, params.requestedLoadSize) { photos ->
            callback.onResult(photos, params.key + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, UnsplashPhoto>) {
        val adjacentKey = if (params.key > 1) params.key - 1 else null
        getPhotos(params.key, params.requestedLoadSize) { photos ->
            callback.onResult(photos, adjacentKey)
        }
    }

    private fun getPhotos(
        newPageNum: Int,
        pageSize: Int = 20,
        pagingCallback: (List<UnsplashPhoto>) -> Unit
    ) {
        val finalQuery = if (currentQuery.valueOrNull != null && currentQuery.value != "") currentQuery.value else "food"
        scope.launch {
            try {
                val photos = repository.getNextPhotos(finalQuery, newPageNum, pageSize)
                pagingCallback(photos.results)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d(mTAG, "Error while loading page $newPageNum of $finalQuery")

            }
        }
    }

}