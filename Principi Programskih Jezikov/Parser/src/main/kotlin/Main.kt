import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import util.*
import java.io.File

fun main() {
    val fileKolesarnice = File("C:\\Users\\Erik\\Desktop\\DigitalniDvojcek\\Principi Programskih Jezikov\\Parser\\Podatki\\GeoJSON_worldGrid\\kolesarnice.geojson").readText()
    val fileKoridorji = File("C:\\Users\\Erik\\Desktop\\DigitalniDvojcek\\Principi Programskih Jezikov\\Parser\\Podatki\\GeoJSON_worldGrid\\kodirdorji.geojson").readText()
    val fileKolesarskeSteze = File("C:\\Users\\Erik\\Desktop\\DigitalniDvojcek\\Principi Programskih Jezikov\\Parser\\Podatki\\GeoJSON_worldGrid\\kolesarskeSteze.geojson").readText()
    val filePostaje = File("C:\\Users\\Erik\\Desktop\\DigitalniDvojcek\\Principi Programskih Jezikov\\Parser\\Podatki\\GeoJSON_worldGrid\\postaje.geojson").readText()
    val fileIzposojevalnice = File("C:\\Users\\Erik\\Desktop\\DigitalniDvojcek\\Principi Programskih Jezikov\\Parser\\Podatki\\GeoJSON_worldGrid\\rent.geojson").readText()
    val fileTuristicnePoti = File("C:\\Users\\Erik\\Desktop\\DigitalniDvojcek\\Principi Programskih Jezikov\\Parser\\Podatki\\GeoJSON_worldGrid\\turisticnePoti.geojson").readText()

    val obj = Json.decodeFromString<KolesarniceGEOJSON>(fileKolesarnice)
    val obj2 = Json.decodeFromString<KoridorjiGEOJSON>(fileKoridorji)
    val obj3 = Json.decodeFromString<KolesarskeStezeGEOJSON>(fileKolesarskeSteze)
    val obj4 = Json.decodeFromString<PostajeGEOJSON>(filePostaje)
    val obj5 = Json.decodeFromString<IzposojevalniceGEOJSON>(fileIzposojevalnice)
    val obj6 = Json.decodeFromString<TuristicnePotiGEOJSON>(fileTuristicnePoti)

    //val obj2 = obj.features[0].geometry.coordinates
    //println(obj)

    //println(obj)
    //println(obj2)
    //println(obj3)
    //println(obj4)
    //println(obj5)
    //println(obj6)
}