package com.iostyle.cellular.bean

import java.util.Objects

/**
 * 出生：一个空格周围有3个人，诞生新生命
 * 活着：周围有2-3个人，继续存活
 * 死亡：周围人数<2或者>3，死亡
 *
 * 演化状态：1.震荡 2.静物 3.灭绝
 */
interface IUniverse {
    /**
     *      #   #   #
     *
     *      #       #
     *
     *      #   #   #
     */

    val width: Int
    val height: Int

    data class Coordinate(val x: Int, val y: Int) {
        override fun equals(other: Any?): Boolean {
            return if (other == null || other !is Coordinate) {
                false
            } else {
                this.x == other.x && this.y == other.y
            }
        }

        override fun hashCode(): Int {
            return Objects.hash(x,y)
        }
    }

    /**
     * 手动孵化一个原子
     */
    fun addAtom(coordinate: Coordinate): MutableList<IAtom>

    fun addOrRemoveAtom(coordinate: Coordinate): MutableList<IAtom>

    /**
     * 获取当前原子集合
     */
    fun getCurrentAtoms(): MutableList<IAtom>

}