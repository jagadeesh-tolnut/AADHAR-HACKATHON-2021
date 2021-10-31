package com.example.getloaction

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.getloaction.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        binding.getLocationBtn.setOnClickListener{
            fetchLocation()
        }
    }

    private fun fetchLocation() {
        val task :Task<Location> = fusedLocationProviderClient.lastLocation
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        !=PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }
        task.addOnSuccessListener {

            if(it!= null){
                Toast.makeText(this, "${it.latitude}  ${it.longitude}", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this, "No info available", Toast.LENGTH_LONG).show()
            }
        }

    }
}
