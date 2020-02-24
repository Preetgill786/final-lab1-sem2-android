package yoyo.jassie.labtest2

import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_details.*
import yoyo.jassie.labtest2.repository.BookmarkRepo
import yoyo.jassie.labtest2.viewmodel.BookmarkDetailsViewModel
import yoyo.jassie.labtest2.viewmodel.MapsViewModel

class DetailsActivity : AppCompatActivity() {

    var isNew = true
    var bookid = 0L

    private var bookmarkRepo: BookmarkRepo = BookmarkRepo(this)
    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var bookmarkDetailsViewModel: BookmarkDetailsViewModel
    private var bookmarkDetailsView:
            BookmarkDetailsViewModel.BookmarkDetailsView? = null

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setupViewModel()

        getIntentData()
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu):
            Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_update_bookmark, menu)

        var itemToHide = menu.findItem(R.id.action_delete)

        if (isNew) {
            itemToHide.setVisible(false)
        } else {
            itemToHide.setVisible(true)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                if (isNew) {
                    addNewData()
                } else {
                    updateChanges()
                }
                return true
            }

            R.id.action_delete -> {
                delete()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun addNew() {
        val name = editTextTitle.text.toString()
        if (name.isEmpty()) {
            return
        }

        val bookmark = bookmarkRepo.createBookmark()
        // bookmark.placeId = "123"

        bookmark.title = editTextTitle.text.toString()
        bookmark.subTitle = editTextSubTitle.text.toString()
        bookmark.latitude = java.lang.Double.parseDouble(editTextLat.text.toString())
        bookmark.longitude = java.lang.Double.parseDouble(editTextLong.text.toString())

        mapsViewModel = ViewModelProviders.of(this).get(MapsViewModel::class.java)
        mapsViewModel.addBookmarkFromPlace(bookmark)

        finish()
    }

    private fun addNewData() {

        val thread = object : Thread() {
            public override fun run() {
                Looper.prepare()
                val handler = Handler()
                handler.postDelayed(object : Runnable {
                    public override fun run() {

                        addNew()


                        handler.removeCallbacks(this)
                        Looper.myLooper()?.quit()
                    }
                }, 10)
                Looper.loop()
            }
        }
        thread.start()

    }

    private fun delete() {

        bookmarkDetailsView?.let { bookmarkView ->
            bookmarkDetailsViewModel.deleteBookmark(bookmarkView)
            bookmarkDetailsViewModel.getBookmark(bookid)?.removeObservers(this);
        }
        finish()
    }


    private fun updateChanges() {
        val name = editTextTitle.text.toString()
        if (name.isEmpty()) {
            return
        }
        bookmarkDetailsView?.let { bookmarkView ->
            bookmarkView.title = editTextTitle.text.toString()
            bookmarkView.subTitle = editTextSubTitle.text.toString()
            bookmarkView.latitude = java.lang.Double.parseDouble(editTextLat.text.toString())
            bookmarkView.longitude = java.lang.Double.parseDouble(editTextLong.text.toString())
            bookmarkDetailsViewModel.updateBookmark(bookmarkView)
        }
        finish()
    }

    private fun getIntentData() {

        isNew = intent.getBooleanExtra("isnew", true)

        if (!isNew) {
            this.setTitle("Update Bookmark")

            val bookmarkId = intent.getLongExtra(
                MapsActivity.Companion.EXTRA_BOOKMARK_ID, 0
            )

            bookid = bookmarkId

            bookmarkDetailsViewModel.getBookmark(bookmarkId)?.observe(
                this, Observer<BookmarkDetailsViewModel.BookmarkDetailsView> {

                    it?.let {
                        bookmarkDetailsView = it
                        // Populate fields from bookmark
                        populateFields()
                        //   populateImageView()
                    }
                })


        }else{
            this.setTitle("Add Bookmark")
        }
    }

    private fun setupViewModel() {
        bookmarkDetailsViewModel =
            ViewModelProviders.of(this).get(
                BookmarkDetailsViewModel::class.java
            )
    }

    private fun populateFields() {
        bookmarkDetailsView?.let { bookmarkView ->
            editTextTitle.setText(bookmarkView.title)
            editTextSubTitle.setText(bookmarkView.subTitle)
            editTextLat.setText(bookmarkView.latitude.toString())
            editTextLong.setText(bookmarkView.longitude.toString())
        }
    }


}

