package logit

sealed class LogContext {
    abstract operator fun <T : Any> get(key: LogTag<T>): T?

    operator fun plus(other: LogContext): LogContext {
        if (this !is NonEmpty) return other
        if (other !is NonEmpty) return this
        return NonEmpty(this.map + other.map)
    }

    object Empty : LogContext() {
        override fun <T : Any> get(key: LogTag<T>): T? = null

        override fun toString(): String = "LogContext[EMPTY]"
    }

    internal class NonEmpty(val map: Map<LogTag<*>, Any>) : LogContext() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : Any> get(key: LogTag<T>): T? = map[key] as T?

        override fun toString(): String = "LogContext[$map]"
    }
}

fun Logger.appendContext(context: LogContext): Logger = copy(context = this.context + context)
fun Logger.replaceContext(context: LogContext): Logger = copy(context = context)
