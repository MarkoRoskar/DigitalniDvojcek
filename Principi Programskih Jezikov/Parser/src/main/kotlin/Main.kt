import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import util.*
import java.io.File
import java.io.IOException
import java.net.URL

fun main() {

    val fileKolesarnice = File("Podatki/GeoJSON_worldGrid/kolesarnice.geojson").readText()
    val fileKoridorji = File("Podatki/GeoJSON_worldGrid/kodirdorji.geojson").readText()
    val filePostaje = File("Podatki/GeoJSON_worldGrid/postaje.geojson").readText()
    val fileKolesarskeSteze = File("Podatki/GeoJSON_worldGrid/kolesarskeSteze.geojson").readText()
    val fileIzposojevalnice = File("Podatki/GeoJSON_worldGrid/rent.geojson").readText()
    val fileTuristicnePoti = File("Podatki/GeoJSON_worldGrid/turisticnePoti.geojson").readText()
    val urlMbajk = URL("https://api.jcdecaux.com/vls/v3/stations?apiKey=frifk0jbxfefqqniqez09tw4jvk37wyf823b5j1i&contract=maribor").readText()

    try {
        val postaje = Postaje()
        val mbajk = Mbajk()
        val kolesarnice = Kolesarnice()
        val kolesarskeSteze = KolesarskeSteze()
        val koridorji = Koridorji()
        val turisticnePoti = TuristicnePoti()

        postaje.convert(filePostaje)
        mbajk.convert(urlMbajk)
        kolesarnice.convert(fileKolesarnice)
        kolesarskeSteze.convert(fileKolesarskeSteze)
        koridorji.convert(fileKoridorji)
        turisticnePoti.convert(fileTuristicnePoti)
    } catch (e: IOException) {
        println(e)
    }

}