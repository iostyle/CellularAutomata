package com.iostyle.cellularautomata.playground

import com.iostyle.cellular.bean.IAtom
import com.iostyle.cellular.bean.IUniverse
import com.iostyle.cellular.bean.Universe

interface IPlayground {
    fun bind(universe: Universe)

    fun resetData(data: MutableList<IAtom>)

    fun getClickLocation(x: Int, y: Int): IUniverse.Coordinate

    interface PlaygroundCallback{
        fun onRenderBlank()
        fun onClickCoordinate(coordinate: IUniverse.Coordinate)
    }

    fun bindCallback(callback: PlaygroundCallback)
}