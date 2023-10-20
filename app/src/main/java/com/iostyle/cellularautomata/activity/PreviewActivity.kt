package com.iostyle.cellularautomata.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.iostyle.cellular.bean.IAtom
import com.iostyle.cellularautomata.R
import com.iostyle.cellularautomata.databinding.ActivityPreviewBinding
import com.iostyle.cellularautomata.universes.FirstUniverse
import kotlinx.coroutines.launch

class PreviewActivity : AppCompatActivity() {

    private val universe = FirstUniverse()
    private val objectsLiveData = MutableLiveData<MutableList<IAtom>>()
    private lateinit var binding: ActivityPreviewBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_preview)

        binding.playground.apply {
            bind(universe)
            setOnTouchListener { view, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_UP) {
                    val clickLocation = getClickLocation(motionEvent.x.toInt(), motionEvent.y.toInt()).also {
                        Log.d("touch up", "${it.x} ${it.y}")
                    }
                    objectsLiveData.postValue(universe.addAtom(clickLocation))
                }
                true
            }
        }

        objectsLiveData.observe(this) {
            binding.playground.resetData(it)
        }

        binding.metabolismButton.setOnClickListener {
            lifecycleScope.launch {
                objectsLiveData.postValue(universe.metabolism())
            }
        }
    }
}