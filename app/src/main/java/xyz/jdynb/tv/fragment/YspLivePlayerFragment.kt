package xyz.jdynb.tv.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import com.tencent.smtt.export.external.interfaces.WebResourceResponse
import xyz.jdynb.tv.enums.JsType
import xyz.jdynb.tv.model.LiveChannelModel
import xyz.jdynb.tv.utils.JsManager.execJs

class YspLivePlayerFragment : LivePlayerFragment() {

  companion object {

    private const val YSP_HOME = "https://www.yangshipin.cn/tv/home"

    private const val TAG = "YspLivePlayerFragment"

  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    webView.loadUrl("$YSP_HOME?pid=${mainViewModel.currentChannelModel.value.pid}")
  }

  /**
   * 播放指定直播
   *
   * @param  channel 直播频道
   */
  override fun play(channel: LiveChannelModel) {
    webView.execJs(JsType.PLAY_YSP, "pid" to channel.pid, "vid" to channel.streamId)
  }

  /**
   * 播放或暂停
   */
  override fun playOrPause() {
    Log.i(TAG, "playOrPause")
    webView.execJs(JsType.PLAY_PAUSE_YSP)
  }

  /**
   * 页面加载完成时的回调
   *
   * @param url 加载的 url
   */
  override fun onPageFinished(url: String) {
    val currentChannelModel = mainViewModel.currentChannelModel.value
    webView.execJs(JsType.CLEAR_YSP)
    webView.execJs(JsType.FULLSCREEN_YSP)
    webView.execJs(
      JsType.PLAY_YSP,
      "pid" to currentChannelModel.pid,
      "vid" to currentChannelModel.streamId
    )
  }

  /**
   * 拦截请求
   *
   * @param url 请求的地址 url
   */
  override fun shouldInterceptRequest(url: String): WebResourceResponse? {
    if (url.endsWith(".webp")) {
      // 禁止加载图片资源
      return createEmptyResponse("image/*")
    }
    return null
  }
}