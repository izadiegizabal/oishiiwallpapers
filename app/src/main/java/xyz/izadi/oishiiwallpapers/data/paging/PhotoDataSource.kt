package xyz.izadi.oishiiwallpapers.data.paging

import android.util.Log
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.launch
import xyz.izadi.oishiiwallpapers.data.api.PhotoRepository
import xyz.izadi.oishiiwallpapers.data.api.UnsplashColor
import xyz.izadi.oishiiwallpapers.data.api.UnsplashPhoto
import xyz.izadi.oishiiwallpapers.data.api.UnsplashQueryOptions

@ExperimentalCoroutinesApi
class PhotoDataSource constructor(
    private val currentQuery: ConflatedBroadcastChannel<UnsplashQueryOptions>,
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
        val finalQuery: String = if (currentQuery.valueOrNull != null
            && currentQuery.value.query != ""
        ) {
            currentQuery.value.query
        } else {
            "food"
        }

        val finalColor =
            if (currentQuery.valueOrNull == null) UnsplashColor.ANY else currentQuery.value.color

        scope.launch {
            try {
                val photos = repository.getNextPhotos(
                    finalQuery,
                    newPageNum,
                    pageSize,
                    finalColor
                )
                pagingCallback(photos.results)
            } catch (e: Exception) {
                e.printStackTrace()
                val errorMessage = "Error while loading page $newPageNum of $finalQuery"
                Log.e(mTAG, errorMessage)
            }
        }
    }

}