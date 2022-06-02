import com.google.gson.Gson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import util.*
import kotlinx.serialization.Serializable

@Serializable
data class KoridorjiFormat(
    val coordinates: List<List<Double>>
)

class Koridorji {

    fun convert(a: String) {
        val gson = Gson()
        val koridorji = Json.decodeFromString<KoridorjiGEOJSON>(a)
        for (i in koridorji.features.indices) {
            val temp = KoridorjiFormat(koridorji.features[i].geometry.coordinates)
            val temp2 = gson.toJson(temp)
            println(temp2)
        }
    }
}