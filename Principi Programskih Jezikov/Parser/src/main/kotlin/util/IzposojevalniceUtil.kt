package util
import kotlinx.serialization.Serializable

@Serializable
data class IzposojevalniceGEOJSON(
    val bbox: List<Double>,
    val crs: CrsIzposojevalnice,
    val features: List<FeatureIzposojevalnice>,
    val name: String,
    val numberMatched: Int,
    val numberReturned: Int,
    val timeStamp: String,
    val totalFeatures: Int,
    val type: String
)
@Serializable
data class CrsIzposojevalnice(
    val properties: PropertiesIzposojevalnice,
    val type: String
)
@Serializable
data class FeatureIzposojevalnice(
    val bbox: List<Double>,
    val geometry: GeometryIzposojevalnice,
    val geometry_name: String,
    val id: String,
    val properties: PropertiesXIzposojevalnice,
    val type: String
)
@Serializable
data class PropertiesIzposojevalnice(
    val name: String
)
@Serializable
data class GeometryIzposojevalnice(
    val coordinates: List<List<Double>>,
    val type: String
)
@Serializable
data class PropertiesXIzposojevalnice(
    val datum_prip_podatkov: String,
    val featureid: Int,
    val id: String,
    val id_uvoza: Int,
    val kapaciteta: Int?,
    val naziv_ponudnik: String,
    val ponudnik: String,
    val povezava_ponudnik: String,
    val status_podatka: String
)