package yoyo.jassie.labtest2.model

import android.content.Context
import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Bookmark(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var title: String = "",
    var subTitle: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0)


