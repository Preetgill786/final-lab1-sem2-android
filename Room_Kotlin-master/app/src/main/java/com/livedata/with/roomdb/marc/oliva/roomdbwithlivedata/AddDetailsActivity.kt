package com.livedata.with.roomdb.marc.oliva.roomdbwithlivedata

import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.arch.persistence.room.TypeConverter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import com.livedata.with.roomdb.marc.oliva.roomdbwithlivedata.data.Contact
import com.livedata.with.roomdb.marc.oliva.roomdbwithlivedata.data.ContactDb
import com.livedata.with.roomdb.marc.oliva.roomdbwithlivedata.data.DaoContact
import kotlinx.android.synthetic.main.activity_contact_details.*
import java.text.SimpleDateFormat
import java.util.*

class AddDetailsActivity : AppCompatActivity() {


    private var daoContact: DaoContact? = null
    private var viewModel: ContactListViewModel? = null


    var cal = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_details)
        var db: ContactDb = ContactDb.getDataBase(this)

        daoContact = db.daoContact()

        viewModel = ViewModelProviders.of(this).get(ContactListViewModel::class.java)

            setTitle(R.string.add_contact_title)
            invalidateOptionsMenu()


        // create an OnDateSetListener
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
    }

        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        date_edit_text!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@AddDetailsActivity,
                        dateSetListener,
                        // set DatePickerDialog to point to today's date when it loads up
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })


    }

    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        date_edit_text!!.setText(sdf.format(cal.getTime()).toString())
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.done_item -> {

                    saveContact()
                    Toast.makeText(this, getString(R.string.save_contact), Toast.LENGTH_SHORT).show()


                finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        super.onPrepareOptionsMenu(menu)

            menu.findItem(R.id.delete_item).isVisible = false

        return true
    }

    private fun saveContact() {
        var nameContact = name_edit_text.text.toString()
        var age = number_edit_text.text.toString().toInt()
        var tution = tution_edit_text.text.toString().toDouble()
        var date = date_edit_text.text.toString()

        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        var contact = Contact(0, nameContact, age , tution, fromDate(sdf.parse(date)))
        viewModel!!.addContact(contact)
    }




    @TypeConverter
    fun fromDate(date: Date): Long {
        return (date!!.getTime()).toLong()
    }
    @TypeConverter
    fun toDate(dateLong: Long): Date {
        return  Date(dateLong)
    }
}
