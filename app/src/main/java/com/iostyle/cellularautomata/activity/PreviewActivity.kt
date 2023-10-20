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
import com.iostyle.cellularautomata.playground.BasePlayground
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
            callback = object : BasePlayground.Callback {
                override fun click(clickLocation: IUniverse.Coordinate) {
                    objectsLiveData.postValue(universe.addOrRemoveAtom(clickLocation))
                }
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