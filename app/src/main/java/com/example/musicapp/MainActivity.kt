package com.example.musicapp

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.musicapp.databinding.ActivityMainBinding
import com.google.android.material.slider.Slider
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mediaPlayer: MediaPlayer
    private var isPlaying = true
    private var isUserChanged = false
    private var isMute = false
    private lateinit var timer: Timer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareMusic()
        setButtonClicks()
    }

    private fun prepareMusic() {
        mediaPlayer = MediaPlayer.create(
            this,
            R.raw.music_file
            //Uri.parse("") //get music from internet
        )
        mediaPlayer.start()
        isPlaying = true
        binding.btnPlayPause.setImageResource(R.drawable.ic_pause)
        binding.sliderMain.valueTo = mediaPlayer.duration.toFloat()
        binding.txtRight.text = millisToString(mediaPlayer.duration.toLong())

        //run this every second
        timer = Timer()
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    runOnUiThread {
                        if (!isUserChanged) {
                            // binding.txtLeft.text = millisToString(mediaPlayer.currentPosition.toLong())
                            binding.sliderMain.value = mediaPlayer.currentPosition.toFloat()
                        }
                    }
                }

            }, 1000,
            1000
        )
    }

    private fun millisToString(duration: Long): String {
        val second = duration / 1000 % 60
        val minute = duration / (1000 * 60) % 60

        return java.lang.String.format(Locale.US, "%02d:%02d", minute, second)

    }

    private fun setButtonClicks() {

        binding.btnPlayPause.setOnClickListener {
            configureMusic()
        }

        binding.btnGoBefore.setOnClickListener {
            goBeforeMusic()
        }


        binding.btnVolumeOnOff.setOnClickListener {
            configureVolume()
        }


        binding.btnGoAfter.setOnClickListener {
            goAfterMusic()
        }

        binding.sliderMain.addOnChangeListener { slider, value, fromUser ->
            binding.txtLeft.text = millisToString(value.toLong())
            isUserChanged = fromUser
        }
        binding.sliderMain.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {

            }

            override fun onStopTrackingTouch(slider: Slider) {
                mediaPlayer.seekTo(slider.value.toInt())
            }

        })

    }

    private fun goBeforeMusic() {

        val now = mediaPlayer.currentPosition
        val newValue = now - 15000 //15000 millisec == 15 sec
        mediaPlayer.seekTo(newValue)

    }

    private fun configureVolume() {

        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        if (isMute) {

            audioManager.adjustVolume(AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_SHOW_UI)
            binding.btnVolumeOnOff.setImageResource(R.drawable.ic_volume_on)
            isMute = false

        } else {
            audioManager.adjustVolume(
                AudioManager.ADJUST_MUTE,
                AudioManager.FLAG_SHOW_UI
            )

            binding.btnVolumeOnOff.setImageResource(R.drawable.ic_volume_off)
            isMute = true

        }
    }

    private fun goAfterMusic() {
        val now = mediaPlayer.currentPosition
        val newValue = now + 15000 //15000 millisec == 15 sec
        mediaPlayer.seekTo(newValue)

    }

    private fun configureMusic() {
        if (isPlaying) {
            mediaPlayer.pause()
            isPlaying = false
            binding.btnPlayPause.setImageResource(R.drawable.ic_play)
        } else {
            mediaPlayer.start()
            isPlaying = true
            binding.btnPlayPause.setImageResource(R.drawable.ic_pause)

        }
    }

    override fun onDestroy() {
        super.onDestroy()

        timer.cancel()
        mediaPlayer.release()

    }
}