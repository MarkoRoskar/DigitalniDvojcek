package com.example.cyclinginmaribor

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cyclinginmaribor.databinding.FragmentHomeBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

class HomeFragment: Fragment(), SensorEventListener {

    private lateinit var binding: FragmentHomeBinding
    private var currentImgPath: String = ""
    private val IMAGE_REQUEST = 1

    private lateinit var sensorManager: SensorManager
    private var tempSensor: Sensor? = null
    private var isTempSensorAvailable: Boolean = true
    private lateinit var tempTextView: TextView

    private var temp: Float? = null

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
        Log.i("test", "test")
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
        if (isTempSensorAvailable) {
            sensorManager.unregisterListener(this)
            Log.i("status", "unregister")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        //binding2 = FragmentPictureBinding.inflate(inflater, container, false)
        tempTextView = binding.temp

        //Log.i("temp", temp.toString())

        // open camera with intent
        val camButton = binding.cameraButton
        camButton.setOnClickListener {
            //findNavController().navigate(R.id.cameraFragment)

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val imgFile = getImageFile()
            val imgUri = FileProvider.getUriForFile(requireContext(), "com.example.android.fileprovider", imgFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri)
            startActivityForResult(intent, IMAGE_REQUEST)
        }

        val logoButton = binding.mainScreenLogo
        logoButton.setOnClickListener {
            val intent = Intent(activity, PictureActivity::class.java)
            intent.putExtra("image_path", currentImgPath)
            startActivity(intent)
        }

        val customEventButton = binding.customEventButton
        customEventButton.setOnClickListener {
            findNavController().navigate(R.id.eventFragment)
        }

        val tempSensorButton = binding.tempSensorButton
        tempSensorButton.setOnClickListener {
            findNavController().navigate(R.id.tempSensorFragment)
        }

        val exitButton = binding.exitButton
        exitButton.setOnClickListener {
            activity?.finish()
            exitProcess(0)
        }

        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    private fun getImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imgName: String = "jpg_" + timeStamp + "_"
        val storageDir: File? = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imgFile: File = File.createTempFile(imgName, ".jpg", storageDir)
        currentImgPath = imgFile.absolutePath
        return imgFile
    }

    /*private fun takePicture(){
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = createImageFile()
        val uri = FileProvider.getUriForFile(this.requireContext(), "com.example.retrofittest.fileprovider", photoFile)
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(pictureIntent, PICTURE_FROM_CAMERA)
    }

    private fun createImageFile(): File {
        val timeStamp: String= SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File?=getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply
        {currentPhotoPath = absolutePath}
    }*/

    /*@Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && data != null) {
            image = binding.pic
            image.setImageBitmap(data.extras?.get("data") as Bitmap)
        }
    }*/
}