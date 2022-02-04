package hu.mag.otptestapp.datas

// Fotó adatok leképezése a json struktúrához

data class PhotoResponse (
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String,
    val description: Description
)