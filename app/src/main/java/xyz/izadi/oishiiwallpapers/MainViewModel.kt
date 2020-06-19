package xyz.izadi.oishiiwallpapers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import xyz.izadi.oishiiwallpapers.data.PhotoRepository

class MainViewModel : ViewModel() {
    private val repository: PhotoRepository = PhotoRepository()

    val foodPhotos = liveData(Dispatchers.IO) {
        val retrievedPhotos = repository.getPhotos("food")

        emit(retrievedPhotos)
    }
}