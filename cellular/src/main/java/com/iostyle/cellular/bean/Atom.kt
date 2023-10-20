package com.iostyle.cellular.bean

class Atom : IAtom {

    companion object {
        fun born(): IAtom {
            return Atom()
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