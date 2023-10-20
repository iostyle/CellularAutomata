package com.iostyle.cellular.bean

import com.iostyle.cellular.extension.getNeighbor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

abstract class Universe : IUniverse {

    private val objects = mutableListOf<IAtom>()

    private val metaMuteX = Mutex()

    /**
     * 新陈代谢一次
     * 出生与死亡基于当前状态相互独立
     *
     * 生成eden及graveyard
     */
    suspend fun metabolism(): MutableList<IAtom> {
        metaMuteX.withLock {
            val graveyardTemp = arrayListOf<IAtom>()
            val anchorTemp = arrayListOf<IUniverse.Coordinate>()
            val edenTemp = arrayListOf<IAtom>()
            // 获取当前所有的原子
            return withContext(Dispatchers.IO) {
                val map = objects.map { it.coordinate }
                // 判断当前原子存活状态
                objects.forEach {
                    var neighborSize = 0
                    var isAlive = true
                    it.coordinate.getNeighbor().forEach {
                        // TODO
                        if (map.contains(it)) {
                            neighborSize++
                        } else {
                            // 需要探测是否新生的坐标
                            anchorTemp.add(it)
                        }
                    }
                    if (neighborSize < 2 || neighborSize > 3) {
                        // die
                        isAlive = false
                    }
                    if (!isAlive) {
                        // graveyard
                        graveyardTemp.add(
                            it
//                        .also { it.onDie() }
                        )
                    }
                }

                // 移除死掉的原子
                objects.removeAll(graveyardTemp)

//            ground.forEach { it.onAlive() }

                // 添加新生的原子
                anchorTemp.forEach {
                    if (it.getNeighbor().size == 3) {
                        edenTemp.add(Atom.born())
                    }
                }
                objects.addAll(edenTemp)

                return@withContext objects
            }
        }


    }
}