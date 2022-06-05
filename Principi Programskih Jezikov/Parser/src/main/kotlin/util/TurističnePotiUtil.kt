package util
import kotlinx.serialization.Serializable

@Serializable
data class TuristicnePotiGEOJSON(
    val features: List<FeatureTuristicnePoti>,
    val type: String
)
@Serializable
data class FeatureTuristicnePoti(
    val geometry: GeometryTuristicnePoti,
    val id: String,
    val properties: PropertiesTuristicnePoti,
    val type: String
)
@Serializable
data class GeometryTuristicnePoti(
    val coordinates: List<List<Double>>,
    val type: String
)
@Serializable
data class PropertiesTuristicnePoti(
    val datum_prip_podatkov: String,
    val datumvira: String,
    val dolzina: String?,
    val featureid: Int,
    val id_uvoza: Int,
    val nazivkoles: String,
    val opis: String?,
    val oznaka: String?,
    val spust_h: String?,
    val status_podatka: String,
    val tip: String,
    val vir: String,
    val vzdrzeval: String?,
    val vzpon_h: String?,
    val zahtevnost: String?
)