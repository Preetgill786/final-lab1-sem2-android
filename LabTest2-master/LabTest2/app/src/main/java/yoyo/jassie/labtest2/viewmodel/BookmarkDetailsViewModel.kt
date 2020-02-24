package yoyo.jassie.labtest2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import yoyo.jassie.labtest2.model.Bookmark
import yoyo.jassie.labtest2.repository.BookmarkRepo


class BookmarkDetailsViewModel(application: Application) :
    AndroidViewModel(application) {

  private var bookmarkRepo: BookmarkRepo =
      BookmarkRepo(getApplication())
  private var bookmarkDetailsView: LiveData<BookmarkDetailsView>? = null

  fun getBookmark(bookmarkId: Long): LiveData<BookmarkDetailsView>? {
    if (bookmarkDetailsView == null) {
      mapBookmarkToBookmarkView(bookmarkId)
    }
    return bookmarkDetailsView
  }

  fun updateBookmark(bookmarkDetailsView: BookmarkDetailsView) {

    GlobalScope.launch {
      val bookmark = bookmarkViewToBookmark(bookmarkDetailsView)
      bookmark?.let { bookmarkRepo.updateBookmark(it) }
    }
  }

  fun deleteBookmark(bookmarkDetailsView: BookmarkDetailsView) {

    GlobalScope.launch {
      val bookmark = bookmarkViewToBookmark(bookmarkDetailsView)
      bookmark?.let { bookmarkRepo.deleteBookmark(it) }
    }
  }

  private fun bookmarkViewToBookmark(bookmarkDetailsView: BookmarkDetailsView):
      Bookmark? {
    val bookmark = bookmarkDetailsView.id?.let {
      bookmarkRepo.getBookmark(it)
    }
    if (bookmark != null) {
     // bookmark.
      bookmark.id = bookmarkDetailsView.id
      bookmark.title = bookmarkDetailsView.title
      bookmark.subTitle = bookmarkDetailsView.subTitle
      bookmark.latitude = bookmarkDetailsView.latitude
      bookmark.longitude = bookmarkDetailsView.longitude
    }
    return bookmark
  }

  private fun mapBookmarkToBookmarkView(bookmarkId: Long) {

    val bookmark = bookmarkRepo.getLiveBookmark(bookmarkId)

      bookmarkDetailsView = Transformations.map(bookmark) { repoBookmark -> bookmarkToBookmarkView(repoBookmark)
    }


  }

  private fun bookmarkToBookmarkView(bookmark: Bookmark): BookmarkDetailsView {
    return BookmarkDetailsView(
        bookmark.id,
        bookmark.title,
        bookmark.subTitle,
        bookmark.latitude,
        bookmark.longitude
    )
  }

  data class BookmarkDetailsView(
          var id: Long? = null,
          var title: String = "",
          var subTitle: String = "",
         var latitude: Double = 0.0,
         var longitude: Double =0.0
  )


}
