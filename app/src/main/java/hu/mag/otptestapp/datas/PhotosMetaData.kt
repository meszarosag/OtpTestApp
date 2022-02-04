package hu.mag.otptestapp.datas

// Fotó lista adatok root objetumának leképezése

data class PhotosMetaData (
    val page: Int,
    val photo: List<PhotoResponse>
)