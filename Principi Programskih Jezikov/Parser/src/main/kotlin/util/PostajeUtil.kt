package util
import kotlinx.serialization.Serializable

@Serializable
data class PostajeGEOJSON(
    val bbox: List<Double>,
    val crs: CrsPostaje,
    val features: List<FeaturePostaje>,
    val name: String,
    val numberMatched: Int,
    val numberReturned: Int,
    val timeStamp: String,
    val totalFeatures: Int,
    val type: String
)
@Serializable
data class CrsPostaje(
    val properties: PropertiesPostaje,
    val type: String
)
@Serializable
data class FeaturePostaje(
    val bbox: List<Double>,
    val geometry: GeometryPostaje,
    val geometry_name: String,
    val id: String,
    val properties: PropertiesXPostaje,
    val type: String
)
@Serializable
data class PropertiesPostaje(
    val name: String
)
@Serializable
data class GeometryPostaje(
    val coordinates: List<Double>,
    val type: String
)
@Serializable
data class PropertiesXPostaje(
    val datum_prip_podatkov: String,
    val featureid: Int,
    val id: Int?,
    val id_uvoza: Int?,
    val opomba: String?,
    val status_podatka: String,
    val stnaslonov: Int,
    val vsebina: String,
    val zapst: Double?
)