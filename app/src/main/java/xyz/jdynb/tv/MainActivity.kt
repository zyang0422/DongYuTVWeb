package xyz.jdynb.tv

import android.annotation.SuppressLint
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isInvisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import xyz.jdynb.tv.databinding.ActivityMainBinding
import xyz.jdynb.tv.dialog.ChannelListDialog
import xyz.jdynb.tv.fragment.LivePlayerFragment
import xyz.jdynb.tv.fragment.YspLivePlayerFragment
import xyz.jdynb.tv.utils.isTv

class MainActivity : AppCompatActivity() {

  companion object {

    private const val TAG = "MainActivity"

  }

  private lateinit var binding: ActivityMainBinding

  private val livePlayerFragment: LivePlayerFragment = YspLivePlayerFragment()

  private val handler = Handler(Looper.getMainLooper())

  private lateinit var channelListDialog: ChannelListDialog

  private val mainViewModel by viewModels<MainViewModel>()

  private val menuShowRunnable = Runnable {
    binding.btnMenu.isInvisible = true
  }

  private lateinit var audioManager: AudioManager

  private var lastBackTime = 0L

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    val insetsController = WindowCompat.getInsetsController(window, window.decorView)
    insetsController.hide(WindowInsetsCompat.Type.systemBars())

    audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    binding.m = mainViewModel
    binding.lifecycleOwner = this

    val isTv = isTv(this)
    binding.btnMenu.isInvisible = isTv

    if (!isTv) {
      handler.postDelayed(menuShowRunnable, 5000)
    }

    channelListDialog = ChannelListDialog(this, mainViewModel)

    binding.btnMenu.setOnClickListener {
      if (mainViewModel.channelModelList.value.isEmpty()) {
        return@setOnClickListener
      }
      channelListDialog.show()
    }

    supportFragmentManager.beginTransaction()
      .replace(R.id.fragment, livePlayerFragment)
      .commitNow()

    lifecycleScope.launch {
      mainViewModel.currentChannelModel.collect {
        Log.i(TAG, "currentChannelModel: $it")
      }
    }
  }

  override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
    if (!isTv(this)) {
      binding.btnMenu.isInvisible = false
      handler.removeCallbacks(menuShowRunnable)
      handler.postDelayed(menuShowRunnable, 5000)
    }
    return super.dispatchTouchEvent(ev)
  }

  /**
   * 事件分发时就拦截，避免事件被 webview 拦截
   */
  override fun dispatchKeyEvent(event: KeyEvent): Boolean {
    Log.i(TAG, "dispatchKeyEvent: ${event.keyCode}")
    val keyCode = event.keyCode
    val action = event.action
    if (action != KeyEvent.ACTION_DOWN) {
      return super.dispatchKeyEvent(event)
    }
    when (keyCode) {
      /**
       * 上
       */
      KeyEvent.KEYCODE_DPAD_UP -> {
        if (channelListDialog.isShowing) {
          return true
        }
        mainViewModel.up()
      }

      /**
       * 下
       */
      KeyEvent.KEYCODE_DPAD_DOWN -> {
        if (channelListDialog.isShowing) {
          return true
        }
        mainViewModel.down()
      }

      // ENTER、OK（确认）
      KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_SPACE -> {
        if (channelListDialog.isShowing) {
          return true
        }
        Log.d(TAG, "onKeyDown: Ok")
        livePlayerFragment.playOrPause()
      }

      // 静音
      KeyEvent.KEYCODE_MUTE -> {
        try {
          audioManager.setStreamVolume(
            AudioManager.STREAM_SYSTEM,
            0,
            AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE
          )
        } catch (_: SecurityException) {
        }
      }

      KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_DPAD_LEFT -> {
        if (channelListDialog.isShowing) {
          return true
        }
        try {
          audioManager.adjustStreamVolume(
            AudioManager.STREAM_MUSIC,
            AudioManager.ADJUST_LOWER,
            AudioManager.FLAG_SHOW_UI
          )
        } catch (_: SecurityException) {
        }
      }

      KeyEvent.KEYCODE_VOLUME_UP, KeyEvent.KEYCODE_DPAD_RIGHT -> {
        if (channelListDialog.isShowing) {
          return true
        }
        val volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        if (volume < audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)) {
          try {
            audioManager.adjustStreamVolume(
              AudioManager.STREAM_MUSIC,
              AudioManager.ADJUST_RAISE,
              AudioManager.FLAG_SHOW_UI
            )
          } catch (e: SecurityException) {
            Log.e(TAG, e.message.toString())
          }
        }
      }

      // 返回
      KeyEvent.KEYCODE_BACK, KeyEvent.KEYCODE_ESCAPE -> {
        if (channelListDialog.isShowing) {
          return true
        }
        if (mainViewModel.showCurrentChannel.value) {
          // 如果显示了当前频道
          mainViewModel.showCurrentChannel(false)
          mainViewModel.rollbackIndex() // 回滚之前的频道
        } else {
          if (System.currentTimeMillis() - lastBackTime > 2000) {
            lastBackTime = System.currentTimeMillis()
            Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show()
          } else {
            finish()
          }
        }
      }

      // 主页
      KeyEvent.KEYCODE_HOME -> {

      }

      // 菜单
      KeyEvent.KEYCODE_MENU, KeyEvent.KEYCODE_P -> {
        if (channelListDialog.isShowing) {
          return true
        }
        binding.btnMenu.callOnClick()
      }

      // 0
      // 数字
      KeyEvent.KEYCODE_0, KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_2, KeyEvent.KEYCODE_3,
      KeyEvent.KEYCODE_4, KeyEvent.KEYCODE_5, KeyEvent.KEYCODE_6, KeyEvent.KEYCODE_7,
      KeyEvent.KEYCODE_8, KeyEvent.KEYCODE_9,
      KeyEvent.KEYCODE_NUMPAD_0, KeyEvent.KEYCODE_NUMPAD_1, KeyEvent.KEYCODE_NUMPAD_2,
      KeyEvent.KEYCODE_NUMPAD_3, KeyEvent.KEYCODE_NUMPAD_4, KeyEvent.KEYCODE_NUMPAD_5,
      KeyEvent.KEYCODE_NUMPAD_6, KeyEvent.KEYCODE_NUMPAD_7, KeyEvent.KEYCODE_NUMPAD_8,
      KeyEvent.KEYCODE_NUMPAD_9 -> {
        val num = getNumForKeyCode(keyCode)

        Log.i(TAG, "input number: $num")

        mainViewModel.appendNumber(num)
      }
    }
    return super.dispatchKeyEvent(event)
  }

  private fun getNumForKeyCode(keyCode: Int): String {
    return when (keyCode) {
      KeyEvent.KEYCODE_0, KeyEvent.KEYCODE_NUMPAD_0 -> "0"
      KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_NUMPAD_1 -> "1"
      KeyEvent.KEYCODE_2, KeyEvent.KEYCODE_NUMPAD_2 -> "2"
      KeyEvent.KEYCODE_3, KeyEvent.KEYCODE_NUMPAD_3 -> "3"
      KeyEvent.KEYCODE_4, KeyEvent.KEYCODE_NUMPAD_4 -> "4"
      KeyEvent.KEYCODE_5, KeyEvent.KEYCODE_NUMPAD_5 -> "5"
      KeyEvent.KEYCODE_6, KeyEvent.KEYCODE_NUMPAD_6 -> "6"
      KeyEvent.KEYCODE_7, KeyEvent.KEYCODE_NUMPAD_7 -> "7"
      KeyEvent.KEYCODE_8, KeyEvent.KEYCODE_NUMPAD_8 -> "8"
      KeyEvent.KEYCODE_9, KeyEvent.KEYCODE_NUMPAD_9 -> "9"
      else -> ""
    }
  }
}