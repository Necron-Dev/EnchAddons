package net.skymoe.enchaddons.impl.event

import net.skymoe.enchaddons.event.Event
import net.skymoe.enchaddons.event.EventDispatcher
import net.skymoe.enchaddons.event.EventHandler
import net.skymoe.enchaddons.util.general.AnyComparator
import net.skymoe.enchaddons.util.general.UniqueHash
import net.skymoe.enchaddons.util.general.inBox
import net.skymoe.enchaddons.util.prepend
import java.util.TreeSet
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write
import kotlin.reflect.KClass
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.full.isSubclassOf

class EventDispatcherImpl : EventDispatcher {
    private val lock = ReentrantReadWriteLock()

    private val anyComparator = AnyComparator(UniqueHash())

    private inner class RegistryEntry(
        val priority: Int,
        val handler: EventHandler<*>,
    ) : Comparable<RegistryEntry> {
        override fun compareTo(other: RegistryEntry): Int =
            if (handler === other.handler) {
                0
            } else {
                priority
                    .compareTo(other.priority)
                    .takeIf { it != 0 }
                    ?: anyComparator.compare(handler, other.handler)
            }

        override fun equals(other: Any?): Boolean =
            (other as? RegistryEntry)?.let {
                handler === other.handler
            } ?: false

        override fun hashCode() = handler.hashCode()
    }

    private val parentTypeHandlerMap = mutableMapOf<KClass<*>, TreeSet<RegistryEntry>>()
    private val onlyTypeHandlerMap = mutableMapOf<KClass<*>, TreeSet<RegistryEntry>>()

    private val parentHandlerCache = mutableMapOf<KClass<*>, EventHandler<*>>()
    private val onlyHandlerCache = mutableMapOf<KClass<*>, EventHandler<*>>()

    private fun clearCache() {
        parentHandlerCache.clear()
        onlyHandlerCache.clear()
    }

    override fun <T : Event> register(
        type: KClass<T>,
        priority: Int,
        handler: EventHandler<T>,
    ) {
        lock.write {
            clearCache()
            unregister(type, handler)
            parentTypeHandlerMap.getOrPut(type) { TreeSet() }.add(RegistryEntry(priority, handler))
        }
    }

    override fun <T : Event> registerOnly(
        type: KClass<T>,
        priority: Int,
        handler: EventHandler<T>,
    ) {
        lock.write {
            clearCache()
            unregisterOnly(type, handler)
            onlyTypeHandlerMap.getOrPut(type) { TreeSet() }.add(RegistryEntry(priority, handler))
        }
    }

    override fun <T : Event> unregister(
        type: KClass<T>,
        handler: EventHandler<T>,
    ) {
        lock.write {
            clearCache()
            val entry = RegistryEntry(0, handler)
            parentTypeHandlerMap.values.forEach {
                it.remove(entry)
            }
        }
    }

    override fun <T : Event> unregisterOnly(
        type: KClass<T>,
        handler: EventHandler<T>,
    ) {
        lock.write {
            clearCache()
            val entry = RegistryEntry(0, handler)
            onlyTypeHandlerMap.values.forEach {
                it.remove(entry)
            }
        }
    }

    override fun unregisterAll(handler: EventHandler<*>) {
        lock.write {
            clearCache()
            val entry = RegistryEntry(0, handler)
            (parentTypeHandlerMap.values + onlyTypeHandlerMap.values).forEach {
                it.remove(entry)
            }
        }
    }

    override fun clear() {
        lock.write {
            parentTypeHandlerMap.clear()
            onlyTypeHandlerMap.clear()
            clearCache()
        }
    }

    override fun <T : Event> getHandler(type: KClass<T>): EventHandler<T> {
        lock.read {
            return parentHandlerCache
                .getOrPut(type) {
                    val set = TreeSet<RegistryEntry>()
                    type.allSuperclasses
                        .prepend(type)
                        .filter { it.isSubclassOf(Event::class) }
                        .forEach { parentTypeHandlerMap[it]?.let(set::addAll) }
                    onlyTypeHandlerMap[type]?.let(set::addAll)
                    EventHandlerHolder(set.toList().map { it.handler })
                }.inBox
                .cast()
        }
    }

    override fun <T : Event> getHandlerOnly(type: KClass<T>): EventHandler<T> {
        lock.read {
            return onlyHandlerCache
                .getOrPut(type) {
                    val set = TreeSet<RegistryEntry>()
                    parentTypeHandlerMap[type]?.let(set::addAll)
                    onlyTypeHandlerMap[type]?.let(set::addAll)
                    EventHandlerHolder(set.toList().map { it.handler })
                }.inBox
                .cast()
        }
    }
}
