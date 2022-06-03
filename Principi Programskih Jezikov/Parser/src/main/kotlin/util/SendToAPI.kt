package util
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

private val client = OkHttpClient()

fun postStand(a: String) {
    val requestBody = a.toRequestBody("application/json".toMediaTypeOrNull())
    /*
    val formBody = FormBody.Builder()
        .add("name", "53 - Želežniška postaja Tabor")
        .add("parkSpots", "6")
        .add("longitude", "15.64577618045837")
        .add("latitude", "46.55169014946587")
        .build()
     */
    val request = Request.Builder()
        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Im93bmVyIiwicGFzc3dvcmQiOiJvd25lcnBhc3N3b3JkIiwiZW1haWwiOiJvd25lckBnbWFpbC5jb20iLCJpYXQiOjE2NTQyMDQ2NzB9.qJoJTbn1I7ZaKFqdECm0y5PYz7VxxYTeA--VRf2G4v0")
        .url("https://digitalni-dvojcek-feri.herokuapp.com/stand")
        .post(requestBody)
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        println(response.body!!.string())
    }
}

fun postMbajk(a: String) {
    val requestBody = a.toRequestBody("application/json".toMediaTypeOrNull())

    val request = Request.Builder()
        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Im93bmVyIiwicGFzc3dvcmQiOiJvd25lcnBhc3N3b3JkIiwiZW1haWwiOiJvd25lckBnbWFpbC5jb20iLCJpYXQiOjE2NTQyMDQ2NzB9.qJoJTbn1I7ZaKFqdECm0y5PYz7VxxYTeA--VRf2G4v0")
        .url("https://digitalni-dvojcek-feri.herokuapp.com/mbajk")
        .post(requestBody)
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        println(response.body!!.string())
    }
}

fun postBikePath(a: String) {
    val requestBody = a.toRequestBody("application/json".toMediaTypeOrNull())

    val request = Request.Builder()
        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Im93bmVyIiwicGFzc3dvcmQiOiJvd25lcnBhc3N3b3JkIiwiZW1haWwiOiJvd25lckBnbWFpbC5jb20iLCJpYXQiOjE2NTQyMDQ2NzB9.qJoJTbn1I7ZaKFqdECm0y5PYz7VxxYTeA--VRf2G4v0")
        .url("https://digitalni-dvojcek-feri.herokuapp.com/bikepath")
        .post(requestBody)
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        println(response.body!!.string())
    }
}

fun postBikeShed(a: String) {
    val requestBody = a.toRequestBody("application/json".toMediaTypeOrNull())

    val request = Request.Builder()
        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Im93bmVyIiwicGFzc3dvcmQiOiJvd25lcnBhc3N3b3JkIiwiZW1haWwiOiJvd25lckBnbWFpbC5jb20iLCJpYXQiOjE2NTQyMDQ2NzB9.qJoJTbn1I7ZaKFqdECm0y5PYz7VxxYTeA--VRf2G4v0")
        .url("https://digitalni-dvojcek-feri.herokuapp.com/bikeshed")
        .post(requestBody)
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        println(response.body!!.string())
    }
}

fun postCoridor(a: String) {
    val requestBody = a.toRequestBody("application/json".toMediaTypeOrNull())

    val request = Request.Builder()
        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Im93bmVyIiwicGFzc3dvcmQiOiJvd25lcnBhc3N3b3JkIiwiZW1haWwiOiJvd25lckBnbWFpbC5jb20iLCJpYXQiOjE2NTQyMDQ2NzB9.qJoJTbn1I7ZaKFqdECm0y5PYz7VxxYTeA--VRf2G4v0")
        .url("https://digitalni-dvojcek-feri.herokuapp.com/coridor")
        .post(requestBody)
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        println(response.body!!.string())
    }
}

fun postTourPath(a: String) {
    val requestBody = a.toRequestBody("application/json".toMediaTypeOrNull())

    val request = Request.Builder()
        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Im93bmVyIiwicGFzc3dvcmQiOiJvd25lcnBhc3N3b3JkIiwiZW1haWwiOiJvd25lckBnbWFpbC5jb20iLCJpYXQiOjE2NTQyMDQ2NzB9.qJoJTbn1I7ZaKFqdECm0y5PYz7VxxYTeA--VRf2G4v0")
        .url("https://digitalni-dvojcek-feri.herokuapp.com/tourpath")
        .post(requestBody)
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        println(response.body!!.string())
    }
}