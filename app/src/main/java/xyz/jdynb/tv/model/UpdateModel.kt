package xyz.jdynb.tv.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import kotlinx.serialization.Serializable
import xyz.jdynb.tv.BR

@Serializable
data class UpdateModel(var versionCode: Int = 0, var url: String = ""): BaseObservable() {

  @get:Bindable
  var closeTime: Int = 15
    set(value) {
      field = value
      notifyPropertyChanged(BR.closeTime)
    }

  @get:Bindable
  var progress: Int = 0
    set(value) {
      field = value
      notifyPropertyChanged(BR.progress)
    }
}