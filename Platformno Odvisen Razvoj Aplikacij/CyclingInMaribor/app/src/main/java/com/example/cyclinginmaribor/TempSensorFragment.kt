package com.example.cyclinginmaribor

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.cyclinginmaribor.databinding.FragmentTempSensorBinding
import com.example.cyclinginmaribor.utils.tempData
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import kotlin.properties.Delegates

lateinit var selectedSensorFrequency: String

private val client = OkHttpClient()

class TempSensorFragment : Fragment(), AdapterView.OnItemSelectedListener, LocationListener, SensorEventListener {

    private lateinit var binding: FragmentTempSensorBinding
    private lateinit var locationTextView: TextView
    
    private val REQUEST_CODE = 1

    private lateinit var sensorManager: SensorManager
    private var tempSensor: Sensor? = null
    private var isTempSensorAvailable: Boolean = true
    private lateinit var tempTextView: TextView
    private var temp: Float? = null

    private lateinit var handler: Handler
    private var sensorFrequency by Delegates.notNull<Long>()
    private var sensorDataRunning = false

    private lateinit var locationManager: LocationManager


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // retrieve system sensor service
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager

        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
            tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
            isTempSensorAvailable = true
            Log.i("availability", isTempSensorAvailable.toString())
        }
        else {
            //Toast.makeText(context, "Temperature sensor is unavailable", Toast.LENGTH_SHORT).show()
            tempTextView.text = "Temperature sensor is unavailable"
            Log.i("availability", "no")
            isTempSensorAvailable = false
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent) {
        //if (event.sensor.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
        Log.i("temp", event.values[0].toString() + "°C")
        tempTextView.text = event.values[0].toString() + "°C"
        temp = event.values[0]
        //}
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.i("change", "accuracy change")
    }

    override fun onResume() {
        super.onResume()
        // register listener
        if (isTempSensorAvailable) {
            sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL)
            Log.i("status", "register")
        }
    }

    override fun onPause() {
        super.onPause()
        // unregister listener
        if (isTempSensorAvailable) {
            sensorManager.unregisterListener(this)
            Log.i("status", "unregister")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTempSensorBinding.inflate(inflater, container, false)
        tempTextView = binding.temp

        val frequencySpinner = binding.sensorFrequencyPicker
        val adapter: ArrayAdapter<CharSequence> =
            context?.let { ArrayAdapter.createFromResource(it, R.array.sensor_frequency, android.R.layout.simple_spinner_dropdown_item) } as ArrayAdapter<CharSequence>
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        frequencySpinner.adapter = adapter
        frequencySpinner.onItemSelectedListener = this

        val locationButton = binding.locationSelectionButton
        locationButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("input_type", "temp_sensor")
            findNavController().navigate(R.id.mapFragment, bundle)
        }

        // get location sent from MapFragment
        val location = arguments?.getString("location")

        val confirmButton = binding.confirmButton
        confirmButton.setOnClickListener {
            if (location != null) {
                handler.postDelayed({postToServer(location)}, sensorFrequency)
                /*sensorDataRunning = !sensorDataRunning
                if (sensorDataRunning) {
                    handler.removeCallbacks { postToServer(tempDataJson) }
                }*/
            }
        }

        val backButton = binding.backButton
        backButton.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        return binding.root
    }

    // post sensor temperature data to server via API
    private fun postToServer(location: String) {

        val coordinates = location.split(",")
        val latitude = coordinates[0].toDouble()
        val longitude = coordinates[1].toDouble()

        Log.i("picked location", "$latitude, $longitude")
        Log.i("temp", tempTextView.text.toString())

        val gson = Gson()
        val tempData = tempData(tempTextView.text.toString().dropLast(2).toDouble(), sensorFrequency.toDouble(), latitude, longitude)
        val tempDataJson = gson.toJson(tempData).toString()

        Log.i("temp data json", tempDataJson)

        Log.i("action", "posting sensor temperature to server")
        val requestBody = tempDataJson.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("https://digitalni-dvojcek-feri.herokuapp.com/sensortempdata")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }

                    println(response.body!!.string())
                }
            }
        })
        handler.postDelayed({postToServer(location)}, sensorFrequency)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedSensorFrequency = parent?.getItemAtPosition(position) as String
        handler = Handler()
        when (selectedSensorFrequency) {
            "na 30 sekund" -> {
                sensorFrequency = 10000
            }
            "na 1 minuto" -> {
                sensorFrequency = 60000
            }
            "na 5 minut" -> {
                sensorFrequency = 300000
            }
            "na pol ure" -> {
                sensorFrequency = 1800000
            }
            "na 1 uro" -> {
                sensorFrequency = 3600000
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    @SuppressLint("SetTextI18n")
    override fun onLocationChanged(location: Location) {
        locationTextView.text = "Latitude: ${location.latitude}, Longitude: ${location.longitude}"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (requireContext().checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation()
        } else {
            requestPermissions(arrayOf(ACCESS_FINE_LOCATION), REQUEST_CODE)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocation()
        } else {
            // Handle denied permission
        }
    }

    private fun getLocation() {
        val location = locationManager.getLastKnownLocation(LocationManager.FUSED_PROVIDER)
        val latitude = location?.latitude
        val longitude = location?.longitude
        Log.i("location", "$latitude, $longitude")
        // Use the coordinates as needed
    }
}