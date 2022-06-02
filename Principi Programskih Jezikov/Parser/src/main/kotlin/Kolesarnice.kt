import com.google.gson.Gson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import util.*
import kotlinx.serialization.Serializable

@Serializable
data class KolesarniceFormat(
    val providerName: String,
    val providerLink: String,
    val address: String,
    val quantity: Int,
    val longitude: Double,
    val latitude: Double
)


class Kolesarnice {

    fun convert(a: String) {
        val gson = Gson()
        val kolesarnice = Json.decodeFromString<KolesarniceGEOJSON>(a)
        for (i in kolesarnice.features.indices) {
            val temp = KolesarniceFormat(kolesarnice.features[i].properties.naziv_ponudnik, kolesarnice.features[i].properties.povezava_ponudnik, kolesarnice.features[i].properties.lokacija, kolesarnice.features[i].properties.kapaciteta, kolesarnice.features[i].geometry.coordinates[0], kolesarnice.features[i].geometry.coordinates[1])
            val temp2 = gson.toJson(temp)
            println(temp2)
        }
    }

}