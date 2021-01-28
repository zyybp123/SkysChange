package com.bpzzr.skyschange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bpzzr.audiolibrary.audio.AudioControlEntity
import com.bpzzr.audiolibrary.audio.AudioService
import com.bpzzr.audiolibrary.audio.ControlCommand
import com.bpzzr.audiolibrary.audio.PlayerViewHolder
import com.bpzzr.commonlibrary.util.inflate
import com.bpzzr.skyschange.databinding.ActivityAudioTestBinding

class AudioTestActivity : AppCompatActivity() {

    private val binding: ActivityAudioTestBinding by inflate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivityAudioTestBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        binding.flContainer.removeAllViews()
        val playerViewHolder = PlayerViewHolder(this)
        binding.flContainer.addView(playerViewHolder.mView)

        AudioService.startAudioService(this)
        playerViewHolder.mIvPlay.setOnClickListener {
            AudioService.playerCommandReceiver.postValue(
                AudioControlEntity(ControlCommand.START)
            )
        }
    }
}