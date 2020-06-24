package xyz.izadi.oishiiwallpapers.data.paging

import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.izadi.oishiiwallpapers.data.PhotoRepository
import xyz.izadi.oishiiwallpapers.data.UnsplashPhoto

class PhotoDataSource : PageKeyedDataSource<Int, UnsplashPhoto>() {

    private val repository: PhotoRepository = PhotoRepository()

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
        query: String = "food",
        pagingCallback: (List<UnsplashPhoto>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val photos = repository.getNextPhotos(query, newPageNum)
            pagingCallback(photos.results)
        }
    }

}