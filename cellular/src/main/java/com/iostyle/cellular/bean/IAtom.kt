package com.iostyle.cellular.bean

/**
 * 出生：一个空格周围有3个人，诞生新生命
 * 活着：周围有2-3个人，继续存活
 * 死亡：周围人数<2或者>3，死亡
 */
interface IAtom {

    fun onBorn()

    fun onAlive()

    fun onDie()


}