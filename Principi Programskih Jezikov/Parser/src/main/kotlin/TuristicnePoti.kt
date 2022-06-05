import com.google.gson.Gson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import util.*
import kotlinx.serialization.Serializable

@Serializable
data class TuristicnePotiFormat(
    val coordinates: List<List<Double>>
)

class TuristicnePoti {

    fun convert(a: String) {
        val gson = Gson()
        val turisticnePoti = Json.decodeFromString<TuristicnePotiGEOJSON>(a)
        for (i in turisticnePoti.features.indices) {
            val temp = TuristicnePotiFormat(turisticnePoti.features[i].geometry.coordinates)
            val temp2 = gson.toJson(temp).toString()
            postTourPath(temp2)
            //println(temp2)
        }
    }
}