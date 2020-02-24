package yoyo.jassie.labtest2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import yoyo.jassie.labtest2.adapter.BookmarkInfoWindowAdapter
import yoyo.jassie.labtest2.viewmodel.MapsViewModel

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapsViewModel: MapsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        this.setTitle("Map")

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupLocationClient()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        setupMapListeners()
        setupViewModel()
        getCurrentLocation()


    }

    private fun setupMapListeners() {
        map.setInfoWindowAdapter(BookmarkInfoWindowAdapter(this))
        map.setOnInfoWindowClickListener {
            handleInfoWindowClick(it)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                Log.e(TAG, "Location permission denied")
            }
        }
    }

    private fun setupViewModel() {
        mapsViewModel =
            ViewModelProviders.of(this).get(MapsViewModel::class.java)
        createBookmarkMarkerObserver()
    }

    private fun setupLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun startBookmarkDetails(bookmarkId: Long) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("isnew", false)
        intent.putExtra(EXTRA_BOOKMARK_ID, bookmarkId)
        startActivity(intent)
    }

    private fun handleInfoWindowClick(marker: Marker) {
        when (marker.tag) {

            is MapsViewModel.BookmarkMarkerView -> {
                val bookmarkMarkerView = (marker.tag as
                        MapsViewModel.BookmarkMarkerView)
                marker.hideInfoWindow()
                bookmarkMarkerView.id?.let {
                    startBookmarkDetails(it)
                }
            }
        }
    }



    private fun createBookmarkMarkerObserver() {
        mapsViewModel.getBookmarkMarkerViews()?.observe(
            this, androidx.lifecycle
                .Observer<List<MapsViewModel.BookmarkMarkerView>> {

                    map.clear()

                    it?.let {
                        displayAllBookmarks(it)
                    }
                })
    }

    private fun displayAllBookmarks(
        bookmarks: List<MapsViewModel.BookmarkMarkerView>) {
        for (bookmark in bookmarks) {
            addPlaceMarker(bookmark)
        }
    }

    private fun addPlaceMarker(
        bookmark: MapsViewModel.BookmarkMarkerView): Marker? {
        val marker = map.addMarker(MarkerOptions()
            .position(bookmark.location)
            .title(bookmark.title)
            .snippet(bookmark.subTitle)
            .icon(
            BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_AZURE))
            .alpha(0.8f))
        marker.tag = bookmark
        return marker
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            requestLocationPermissions()
        } else {
            map.isMyLocationEnabled = true

            fusedLocationClient.lastLocation.addOnCompleteListener {
                val location = it.result
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    val update = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f)
                    map.moveCamera(update)
                } else {
                    Log.e(TAG, "No location found")
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu):
            Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_add_bookmark, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> {
                //addPlaceMark()
                val intent = Intent(this, DetailsActivity::class.java)
                intent.putExtra("isnew", true)
                intent.putExtra(EXTRA_BOOKMARK_ID, 0)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_LOCATION)
    }

    companion object {
        const val EXTRA_BOOKMARK_ID = "yoyo.jassie.labtest2.EXTRA_BOOKMARK_ID"
        private const val REQUEST_LOCATION = 1
        private const val TAG = "MapsActivity"
    }

}
