package util
import kotlinx.serialization.Serializable

@Serializable
data class MbajkGEOJSON(
    val address: String,
    val banking: Boolean,
    val bonus: Boolean,
    val connected: Boolean,
    val contractName: String,
    val lastUpdate: String,
    val mainStands: MainStands,
    val name: String,
    val number: Int,
    val overflow: Boolean,
    val overflowStands: String?,
    val position: Position,
    val shape: String?,
    val status: String,
    val totalStands: TotalStands
)
@Serializable
data class MainStands(
    val availabilities: Availabilities,
    val capacity: Int
)
@Serializable
data class Position(
    val latitude: Double,
    val longitude: Double
)
@Serializable
data class TotalStands(
    val availabilities: AvailabilitiesX,
    val capacity: Int
)
@Serializable
data class Availabilities(
    val bikes: Int,
    val electricalBikes: Int,
    val electricalInternalBatteryBikes: Int,
    val electricalRemovableBatteryBikes: Int,
    val mechanicalBikes: Int,
    val stands: Int
)
@Serializable
data class AvailabilitiesX(
    val bikes: Int,
    val electricalBikes: Int,
    val electricalInternalBatteryBikes: Int,
    val electricalRemovableBatteryBikes: Int,
    val mechanicalBikes: Int,
    val stands: Int
)