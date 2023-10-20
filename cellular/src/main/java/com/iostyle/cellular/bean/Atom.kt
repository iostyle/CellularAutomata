package com.iostyle.cellular.bean

class Atom(override var coordinate: IUniverse.Coordinate) : IAtom {

    companion object {
        fun born(coordinate: IUniverse.Coordinate): IAtom {
            return Atom(coordinate)
//                .also { it.onBorn() }
        }
    }

    override fun onBorn() {
    }

    override fun onAlive() {
    }

    override fun onDie() {
    }

}