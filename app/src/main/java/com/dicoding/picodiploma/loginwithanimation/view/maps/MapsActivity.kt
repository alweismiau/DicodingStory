package com.dicoding.picodiploma.loginwithanimation.view.maps
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.dicoding.picodiploma.loginwithanimation.data.api.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.google.android.gms.maps.SupportMapFragment

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val viewModel: MapsViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        viewModel.storiesWithLocation.observe(this) { storyResponse ->
            if (storyResponse.error == null || storyResponse.error == "false") {  // Correctly checks if error is false
                storyResponse.stories?.let { showStoriesOnMap(it) }
            } else {
                Toast.makeText(this, "Failed to load stories", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.fetchStoriesWithLocation()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        // Configure map settings if needed
    }

    private fun showStoriesOnMap(stories: List<ListStoryItem>) {
        stories.forEach { story ->
            val location = LatLng(story.lat ?: 0.0, story.lon ?: 0.0)
            map.addMarker(MarkerOptions().position(location).title(story.name).snippet(story.description))
        }

        // Optionally, move the camera to the first story location
        if (stories.isNotEmpty()) {
            val firstStoryLocation = LatLng(stories.first().lat ?: 0.0, stories.first().lon ?: 0.0)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(firstStoryLocation, 10f))
        }
    }
}
