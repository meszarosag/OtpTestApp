package hu.mag.otptestapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.mag.otptestapp.datas.Photo
import hu.mag.otptestapp.net.WebClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhotosViewModel() : ViewModel() {
    private val mutablePhotosLiveData = MutableLiveData<List<Photo>>()
    val photosLiveData: LiveData<List<Photo>> = mutablePhotosLiveData

    // Adatok lekérése a flickr -ről és a lista feltöltése

    fun loadData(searchString:String, page: Int){
        viewModelScope.launch(Dispatchers.Main) {
            if( searchString != ""){
                val searchResponse = WebClient.client.fetchImages(searchString, page)
                val photosList = searchResponse.photos.photo.map { photo ->
                    Photo(
                        id = photo.id,
                        url = "https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}.jpg",
                        title = photo.title,
                        description = photo.description._content
                    )

                }
                mutablePhotosLiveData.postValue(photosList)
            }
        }
    }
}
