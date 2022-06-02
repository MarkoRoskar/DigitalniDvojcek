import com.google.gson.Gson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import util.*
import kotlinx.serialization.Serializable

@Serializable
data class PostajeFormat(
    val name: String,
    val parkSpots: Int,
    val longitude: Double,
    val latitude: Double
)

class Postaje {

    fun convert(a: String) {
        val gson = Gson()
        val postaje = Json.decodeFromString<PostajeGEOJSON>(a)
        for (i in postaje.features.indices) {
            val temp = PostajeFormat(postaje.features[i].properties.vsebina, postaje.features[i].properties.stnaslonov, postaje.features[i].geometry.coordinates[0], postaje.features[i].geometry.coordinates[1])
            val temp2 = gson.toJson(temp)
            println(temp2)
        }
    }
}