import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import util.*
import java.io.File

fun main() {

    val fileKolesarnice = File("Podatki/GeoJSON_worldGrid/kolesarnice.geojson").readText()
    val fileKoridorji = File("Podatki/GeoJSON_worldGrid/kodirdorji.geojson").readText()
    val filePostaje = File("Podatki/GeoJSON_worldGrid/postaje.geojson").readText()
    val fileKolesarskeSteze = File("Podatki/GeoJSON_worldGrid/kolesarskeSteze.geojson").readText()
    val fileIzposojevalnice = File("Podatki/GeoJSON_worldGrid/rent.geojson").readText()
    val fileTuristicnePoti = File("Podatki/GeoJSON_worldGrid/turisticnePoti.geojson").readText()

    val postaje = Postaje()
    //postaje.convert(filePostaje)
    val kolesarnice = Kolesarnice()
    //kolesarnice.convert(fileKolesarnice)
    val kolesarskeSteze = KolesarskeSteze()
    //kolesarskeSteze.convert(fileKolesarskeSteze)
    val koridorji = Koridorji()
    //koridorji.convert(fileKoridorji)
    val turisticnePoti = TuristicnePoti()
    //turisticnePoti.convert(fileTuristicnePoti)
}