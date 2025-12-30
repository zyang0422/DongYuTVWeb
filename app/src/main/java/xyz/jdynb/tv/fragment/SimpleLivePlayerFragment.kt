package xyz.jdynb.tv.fragment

import android.util.Log

class SimpleLivePlayerFragment : LivePlayerFragment() {

  companion object {

    private const val TAG = "SimpleLivePlayerFragment"

  }

  override fun onLoadUrl(url: String?) {
    webView.loadUrl("file:///android_asset/html/simple_player.html")
  }
}