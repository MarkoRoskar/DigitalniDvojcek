import com.google.gson.Gson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import util.*
import kotlinx.serialization.Serializable

@Serializable
data class KolesarskeStezeFormat(
    val coordinates: List<List<Double>>
)

class KolesarskeSteze {

    fun convert(a: String) {
        val gson = Gson()
        val kolesarskeSteze = Json.decodeFromString<KolesarskeStezeGEOJSON>(a)
        for (i in kolesarskeSteze.features.indices) {
            val temp = KolesarskeStezeFormat(kolesarskeSteze.features[i].geometry.coordinates)
            val temp2 = gson.toJson(temp)
            println(temp2)
        }
    }

}