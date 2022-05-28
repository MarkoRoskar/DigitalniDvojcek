package util
import kotlinx.serialization.Serializable

@Serializable
data class KolesarskeStezeGEOJSON(
    val bbox: List<Double>,
    val crs: CrsKolesarskeSteze,
    val features: List<FeatureKolesarskeSteze>,
    val name: String,
    val numberMatched: Int,
    val numberReturned: Int,
    val timeStamp: String,
    val totalFeatures: Int,
    val type: String
)
@Serializable
data class CrsKolesarskeSteze(
    val properties: PropertiesKolesarskeSteze,
    val type: String
)
@Serializable
data class FeatureKolesarskeSteze(
    val bbox: List<Double>,
    val geometry: GeometryKolesarskeSteze,
    val geometry_name: String,
    val id: String,
    val properties: PropertiesXKolesarskeSteze,
    val type: String
)
@Serializable
data class PropertiesKolesarskeSteze(
    val name: String
)
@Serializable
data class GeometryKolesarskeSteze(
    val coordinates: List<List<Double>>,
    val type: String
)
@Serializable
data class PropertiesXKolesarskeSteze(
    val __ver__: Int,
    val dolzina: Double,
    val featureid: Int,
    val id: String,
    val insert_uuid: String,
    val krat_tip: String,
    val mi_style: String,
    val opis_tip: String?,
    val opomba: String?,
    val sif_tip: Int,
    val sifko: Int,
    val ulica: String?,
    val updated: String,
    val updated_by: String
)