package com.example.cyclinginmaribor

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.cyclinginmaribor.databinding.FragmentHomeBinding
import kotlin.system.exitProcess

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // open camera with intent
        val camButton = binding.cameraButton
        camButton.setOnClickListener {
            //findNavController().navigate(R.id.cameraFragment)
            val intent = Intent()
            intent.action = MediaStore.ACTION_IMAGE_CAPTURE
            startActivity(intent)
        }

        val exitButton = binding.exitButton
        exitButton.setOnClickListener {
            activity?.finish()
            exitProcess(0)
        }

        val customEventButton = binding.customEventButton
        customEventButton.setOnClickListener {
            findNavController().navigate(R.id.eventFragment)
        }

        return binding.root
    }
}