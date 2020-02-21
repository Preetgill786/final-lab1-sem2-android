package com.livedata.with.roomdb.marc.oliva.roomdbwithlivedata.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import java.util.*

/**
 * Created by ThinkSoft on 19/12/2017.
 */
@Entity(tableName = "contactdata")
//@TypeConverters(Converters.class)
data class Contact(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "idContact")
        var id: Int = 0,

        @ColumnInfo(name = "name")
        var name: String = "",

        @ColumnInfo(name = "age")
        var age: Int ,

        @ColumnInfo(name = "tution")
        var tution: Double ,// = "",


        @ColumnInfo(name = "date")
var date: Long// = ""


//        @ColumnInfo(name = "number")
//        var number: String = ""

)