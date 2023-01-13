package com.example.cyclinginmaribor

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.example.cyclinginmaribor.databinding.FragmentHomeBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var currentImgPath: String
    private val IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        // open camera with intent
        val camButton = binding.cameraButton
        camButton.setOnClickListener {
            //findNavController().navigate(R.id.cameraFragment)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val imgFile = getImageFile()
            val imgUri = FileProvider.getUriForFile(requireContext(), "com.example.android.fileprovider", imgFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri)
            startActivityForResult(intent, IMAGE_REQUEST)

            //if (intent.resolveActivity(activity?.packageManager!!) != null) {

            //}
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

}