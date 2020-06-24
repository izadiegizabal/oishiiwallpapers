package xyz.izadi.oishiiwallpapers.data

class PhotoRepository {
    private var client: UnsplashApi = RetrofitClient.apiService

    suspend fun getNextPhotos(query: String, pageNum: Int) = client.getPics(query, pageNum)
}