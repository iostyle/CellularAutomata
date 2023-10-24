package com.iostyle.cellularautomata.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.iostyle.cellular.bean.IAtom
import com.iostyle.cellular.bean.IUniverse
import com.iostyle.cellularautomata.R
import com.iostyle.cellularautomata.databinding.ActivityPreviewBinding
import com.iostyle.cellularautomata.playground.IPlayground
import com.iostyle.cellularautomata.universes.FirstUniverse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PreviewActivity : AppCompatActivity() {

    private val universe = FirstUniverse()
    private val objectsLiveData = MutableLiveData<MutableList<IAtom>>()
    private lateinit var binding: ActivityPreviewBinding
    private var autoJob: Job? = null
    private val autoState = MutableLiveData(false)

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_preview)

        binding.playground.apply {
            bind(universe)
            callback = object : IPlayground.PlaygroundCallback {
                override fun onRenderBlank() {
                    autoJob?.cancel()
                }

                override fun onClickCoordinate(coordinate: IUniverse.Coordinate) {
                    lifecycleScope.launch {
                        objectsLiveData.postValue(universe.addOrRemoveAtom(coordinate))
                    }
                }
            }
        }

        objectsLiveData.observe(this) {
            binding.playground.resetData(it)
        }

        autoState.observe(this) {
            binding.autoMetabolismButton.text = if (it) "演变中" else "演变"
        }

        binding.metabolismButton.setOnClickListener {
            metabolism()
        }

        binding.autoMetabolismButton.setOnClickListener {
            if (autoJob?.isActive == true) {
                autoJob?.cancel()
            } else {
                autoJob = lifecycleScope.launch(Dispatchers.IO) {
                    while (true) {
                        delay(500)
                        metabolism()
                    }
                }.also {
                    it.invokeOnCompletion {
                        autoState.postValue(false)
                    }
                }
                autoState.postValue(true)
            }
        }
    }

    private fun metabolism() {
        lifecycleScope.launch {
            objectsLiveData.postValue(universe.metabolism())
        }
    }
}