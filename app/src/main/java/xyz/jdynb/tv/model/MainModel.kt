package xyz.jdynb.tv.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import xyz.jdynb.tv.BR

class MainModel : BaseObservable() {

  var liveItems: List<LiveChannelModel> = listOf()

  @Bindable
  var currentIndex: Int = 0
    set(value) {
      field = if (value < 0) {
        liveItems.size - 1
      } else if (value >= liveItems.size) {
        0
      } else {
        value
      }
      notifyPropertyChanged(BR.currentIndex)
    }

  @get:Bindable
  var showStatus: Boolean = false
    set(value) {
      field = value
      notifyPropertyChanged(BR.showStatus)
    }

  @get:Bindable("currentIndex")
  val currentLiveItem get() =  liveItems.getOrNull(currentIndex) ?: LiveChannelModel()

  fun up() {
    currentIndex++
  }

  fun down() {
    currentIndex--
  }
}