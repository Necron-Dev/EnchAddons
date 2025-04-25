package net.skymoe.enchaddons.util.scope

import java.lang.AutoCloseable

class WithScopeContext(
    private val resourceList: MutableList<AutoCloseable>,
    private val cleanupList: MutableList<() -> Unit>,
) {
    fun <T : AutoCloseable> use(resource: T): T =
        resource.also {
            if (resource !in resourceList) {
                resourceList.add(it)
            }
        }

    fun use(cleanup: () -> Unit) {
        if (cleanup !in cleanupList) {
            cleanupList.add(cleanup)
        }
    }

    fun <T : AutoCloseable> T.using() = use(this)

    fun <T> T.using(cleanup: () -> Unit) = this@using.also { use(cleanup) }
}

data class ResourceClosureException(
    val failures: List<Pair<Any?, Exception>>,
) : Exception()

inline fun <R> withscope(function: WithScopeContext.() -> R) {
    val resourceList = mutableListOf<AutoCloseable>()
    val cleanupList = mutableListOf<() -> Unit>()
    val context = WithScopeContext(resourceList, cleanupList)
    try {
        function(context)
    } finally {
        val exceptionList = mutableListOf<Pair<Any?, Exception>>()
        resourceList.forEach { resource ->
            noexcept({ exceptionList.add(resource to it) }) { resource.close() }
        }
        cleanupList.forEach { cleanup ->
            noexcept({ exceptionList.add(cleanup to it) }) { cleanup() }
        }
        if (exceptionList.isNotEmpty()) {
            throw ResourceClosureException(exceptionList)
        }
    }
}
