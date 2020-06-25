package xyz.izadi.oishiiwallpapers.data.api

class PhotoRepository {
    private var client: UnsplashApi = RetrofitClient.apiService

    suspend fun getNextPhotos(query: String, pageNum: Int, pageSize: Int) = client.getPics(query, pageNum, pageSize)
}