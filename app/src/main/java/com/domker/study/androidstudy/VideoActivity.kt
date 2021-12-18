package com.domker.study.androidstudy

import android.graphics.PixelFormat
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.MediaController
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.VideoView
import kotlinx.android.synthetic.main.layout_video.*
import java.util.*

class VideoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_video)
        buttonPause.setOnClickListener { video.pause() }
        buttonPlay.setOnClickListener { video.start() }
        video.holder.setFormat(PixelFormat.TRANSPARENT)
        video.setMediaController(MediaController(this));
        video.setZOrderOnTop(true)
        video.setVideoPath(getVideoPath(R.raw.big_buck_bunny))
        video.start()
        val timer = Timer()
        var timerPaused = false
        val progressUpdateTask = object : TimerTask() {
            override fun run() {
                if (!timerPaused) {
                    seekbar.progress = video.currentPosition * 100 / video.duration
                }
            }
        }
        timer.schedule(progressUpdateTask, 0,500)

        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    video.seekTo(video.duration * progress / 100)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                video.start()
                video.pause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                video.start()
            }
        })

    }

    private fun getVideoPath(resId: Int): String {
        Log.d("VA", "hhhhhhhhhhhhhhhhhhhh")
        return "android.resource://" + this.packageName + "/" + resId
    }
}