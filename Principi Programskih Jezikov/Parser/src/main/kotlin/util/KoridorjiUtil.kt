package util
import kotlinx.serialization.Contextual
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

@Serializable
data class KoridorjiGEOJSON(
    val bbox: List<Double>,
    val crs: CrsKoridorji,
    val features: List<FeatureKoridorji>,
    val name: String,
    val numberMatched: Int,
    val numberReturned: Int,
    val timeStamp: String,
    val totalFeatures: Int,
    val type: String
)
@Serializable
data class CrsKoridorji(
    val properties: PropertiesKoridorji,
    val type: String
)
@Serializable
data class FeatureKoridorji(
    val bbox: List<Double>,
    val geometry: GeometryKoridorji,
    val geometry_name: String,
    val id: String,
    val properties: PropertiesXKoridorji,
    val type: String
)
@Serializable
data class PropertiesKoridorji(
    val name: String
)
@Serializable
data class GeometryKoridorji(
    val coordinates: List<List<Double>>,
    val type: String
)
@Serializable
data class PropertiesXKoridorji(
    val datum_prip_podatkov: String,
    val datumvira: String,
    val dolzina: String?,
    val featureid: Int,
    val id_uvoza: Int,
    val nazivkoles: String,
    val opis: String,
    val oznaka: String,
    val spust_h: String?,
    val status_podatka: String,
    val tip: String,
    val vir: String,
    val vzdrzeval: String,
    val vzpon_h: String?,
    val zahtevnost: String?
)