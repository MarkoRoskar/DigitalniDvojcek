package com.example.cyclinginmaribor

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.cyclinginmaribor.databinding.FragmentEventBinding
import android.location.LocationManager;
import android.os.Build
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.example.cyclinginmaribor.utils.Event
import java.text.SimpleDateFormat
import java.util.*
import com.google.gson.Gson
import okhttp3.*
import okhttp3.Request
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

lateinit var selectedEventCategory: String
lateinit var eventDescription: String

private val client = OkHttpClient()

class EventFragment : Fragment(), AdapterView.OnItemSelectedListener, LocationListener {

    private lateinit var binding: FragmentEventBinding

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var locationTextView: TextView

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventBinding.inflate(inflater, container, false)

        //locationManager = context?.getSystemService(Context.LOCALE_SERVICE) as LocationManager
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this)

        /*if (activity?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission
                        .ACCESS_FINE_LOCATION)
            }
            == PackageManager
                .PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission
                    .ACCESS_COARSE_LOCATION)
            == PackageManager
                .PERMISSION_GRANTED) {
            // When permission is granted
            // Call method
            //getCurrentLocation();*/

        val categorySpinner = binding.eventCategoryPicker
        val adapter: ArrayAdapter<CharSequence> =
            context?.let { ArrayAdapter.createFromResource(it, R.array.event_categories, android.R.layout.simple_spinner_dropdown_item) } as ArrayAdapter<CharSequence>
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter
        categorySpinner.onItemSelectedListener = this

        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())


        val input = binding.descriptionInputBox
        val locationButton = binding.locationSelectionButton
        locationButton.setOnClickListener {
            eventDescription = input.text.toString()
            findNavController().navigate(R.id.mapFragment)
        }

        // get location sent from MapFragment
        val location = arguments?.getString("location")


        val confirmButton = binding.confirmButton
        confirmButton.setOnClickListener {
            eventDescription = input.text.toString()

            Log.i("category", selectedEventCategory)
            Log.i("description", eventDescription)
            Log.i("time", "$currentDate $currentTime")
            Log.i("location", location.toString())

            if (location != null && eventDescription != "") {
                val coordinates = location.split(",")
                val latitude = coordinates[0].toDouble()
                val longitude = coordinates[1].toDouble()

                val gson = Gson()
                val event = Event(selectedEventCategory, eventDescription, latitude, longitude)
                val eventJson = gson.toJson(event).toString()

                Log.i("event json", eventJson)

                // post event to heroku server
                val requestBody = eventJson.toRequestBody("application/json".toMediaTypeOrNull())
                val request = Request.Builder()
                    //.header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Ikpha29iIiwicGFzc3dvcmQiOiJqYWtvYjEyMyIsImlhdCI6MTY3MzEwMDM4NH0.6qikZPH1p-EE35pXwJ1EX0p_ZE5SuJ8AUAwEzv42S94")
                    .url("https://digitalni-dvojcek-feri.herokuapp.com/event")
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

                //postToServer(request)

                val bundle = Bundle()
                bundle.putString("input_type", "event")
                findNavController().navigate(R.id.homeFragment)
            }
            else {
                Toast.makeText(context, "No event description or location provided.", Toast.LENGTH_SHORT).show()
            }

        }


        val backButton = binding.backButton
        backButton.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)     // navigate to home screen/fragment
        }

        return binding.root
    }

    /*private fun postToServer(request: Request) {
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            println(response.body!!.string())
        }
    }*/

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedEventCategory = parent?.getItemAtPosition(position) as String
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    @SuppressLint("SetTextI18n")
    override fun onLocationChanged(location: Location) {
        locationTextView.text = "Latitude: ${location.latitude}, Longitude: ${location.longitude}"
    }
}