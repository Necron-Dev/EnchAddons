package net.skymoe.enchaddons.util.general

import java.util.WeakHashMap

class UniqueHash : (Any?) -> Long {
    private var counter = 0L

    private val uniqueHash = WeakHashMap<Any, Long>()

    override fun invoke(obj: Any?): Long {
        return obj?.let {
            uniqueHash.getOrPut(obj) { ++counter }
        } ?: 0L
    }
}