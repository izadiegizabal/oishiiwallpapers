package xyz.izadi.oishiiwallpapers.data

class PhotoRepository {
    private var client: UnsplashApi = RetrofitClient.apiService

    suspend fun getPhotos(query: String) = client.getPics(query)
}