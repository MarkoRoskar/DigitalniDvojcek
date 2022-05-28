package util
import kotlinx.serialization.Serializable

@Serializable
data class KolesarniceGEOJSON(
    val bbox: List<Double>,
    val crs: CrsKolesarnice,
    val features: List<FeatureKolesarnice>,
    val name: String,
    val numberMatched: Int,
    val numberReturned: Int,
    val timeStamp: String,
    val totalFeatures: Int,
    val type: String
)
@Serializable
data class CrsKolesarnice(
    val properties: PropertiesKolesarnice,
    val type: String
)
@Serializable
data class FeatureKolesarnice(
    val bbox: List<Double>,
    val geometry: GeometryKolesarnice,
    val geometry_name: String,
    val id: String,
    val properties: PropertiesXKolesarnice,
    val type: String
)
@Serializable
data class PropertiesKolesarnice(
    val name: String
)
@Serializable
data class GeometryKolesarnice(
    val coordinates: List<Double>,
    val type: String
)
@Serializable
data class PropertiesXKolesarnice(
    val datum_prip_podatkov: String,
    val featureid: Int,
    val id: Int,
    val id_uvoza: Int,
    val kapaciteta: Int,
    val lokacija: String,
    val naziv: String,
    val naziv_ponudnik: String,
    val povezava_ponudnik: String,
    val status_podatka: String
)

