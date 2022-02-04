package hu.mag.otptestapp.net


import hu.mag.otptestapp.datas.PhotosSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

// API string létrehozása

interface ApiService {
    @GET("?method=flickr.photos.search&format=json&nojsoncallback=1&api_key=65803e8f6e4a3982200621cad356be51&extras=description&per_page=20")
    //suspend fun fetchImages(): PhotosSearchResponse
    suspend fun fetchImages(
        @Query(value = "text", encoded = true) textTag: String?,
        @Query(value = "page", encoded = true) page: Int?,
    ): PhotosSearchResponse
}
