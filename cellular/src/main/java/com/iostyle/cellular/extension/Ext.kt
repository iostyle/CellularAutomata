package com.iostyle.cellular.extension

import com.iostyle.cellular.bean.IAtom
import com.iostyle.cellular.bean.IUniverse

// TODO cross the border
fun IUniverse.Coordinate.getNeighbor(): List<IUniverse.Coordinate> {
    val neighbor = arrayListOf<IUniverse.Coordinate>()
    neighbor.add(IUniverse.Coordinate(this.x - 1, this.y - 1))
    neighbor.add(IUniverse.Coordinate(this.x, this.y - 1))
    neighbor.add(IUniverse.Coordinate(this.x + 1, this.y - 1))

    neighbor.add(IUniverse.Coordinate(this.x - 1, this.y))
    neighbor.add(IUniverse.Coordinate(this.x + 1, this.y))

    neighbor.add(IUniverse.Coordinate(this.x - 1, this.y + 1))
    neighbor.add(IUniverse.Coordinate(this.x, this.y + 1))
    neighbor.add(IUniverse.Coordinate(this.x + 1, this.y + 1))

    return neighbor
}

fun MutableList<IAtom>.containsCoordinate(coordinate: IUniverse.Coordinate): Boolean {
    this.forEach {
        if (it.coordinate.equals(coordinate)) {
            return true
        }
    }
    return false
}