package yoyo.jassie.labtest2.adapter

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import yoyo.jassie.labtest2.R
import yoyo.jassie.labtest2.viewmodel.MapsViewModel


class BookmarkInfoWindowAdapter(val context: Activity) :
    GoogleMap.InfoWindowAdapter {

    private val contents: View

    init {
        contents = context.layoutInflater.inflate(
            R.layout.marker_popup, null)
    }

    override fun getInfoWindow(marker: Marker): View? {
        // This function is required, but can return null if
        // not replacing the entire info window
        return null
    }

    override fun getInfoContents(marker: Marker): View? {
        val titleView = contents.findViewById<TextView>(R.id.title)
        titleView.text = marker.title ?: ""

        val subTitleView = contents.findViewById<TextView>(R.id.subTitle)
        subTitleView.text = marker.snippet ?: ""
        return contents
    }
}
