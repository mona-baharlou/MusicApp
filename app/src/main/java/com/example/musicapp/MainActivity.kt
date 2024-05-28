package com.example.musicapp

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.musicapp.databinding.ActivityMainBinding
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mediaPlayer: MediaPlayer
    private var isPlaying = true
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
        val timer = Timer()
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    runOnUiThread {
                        // binding.txtLeft.text = millisToString(mediaPlayer.currentPosition.toLong())
                        binding.sliderMain.value = mediaPlayer.currentPosition.toFloat()
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

    }

    private fun goBeforeMusic() {

    }

    private fun configureVolume() {

    }

    private fun goAfterMusic() {

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
}