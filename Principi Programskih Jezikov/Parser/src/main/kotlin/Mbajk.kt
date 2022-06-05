import com.google.gson.Gson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import util.*
import kotlinx.serialization.Serializable

@Serializable
data class MbajkFormat(
    val number: Int,
    val name: String,
    val address: String,
    val longitude: Double,
    val latitude: Double,
    val status: String,
    val bikes: Int,
    val capacity: Int,
    val lastUpdateSensor: String
)

class Mbajk {

    fun convert(a: String) {
        val gson = Gson()
        val mbajk = Json.decodeFromString<ArrayList<MbajkGEOJSON>>(a)
        for (i in mbajk.indices) {
            val temp = MbajkFormat(mbajk[i].number, mbajk[i].name, mbajk[i].address, mbajk[i].position.longitude, mbajk[i].position.latitude, mbajk[i].status, mbajk[i].totalStands.availabilities.bikes, mbajk[i].mainStands.capacity, mbajk[i].lastUpdate)
            val temp2 = gson.toJson(temp).toString()
            postMbajk(temp2)
            //println(temp2)
        }
        
    }
}

