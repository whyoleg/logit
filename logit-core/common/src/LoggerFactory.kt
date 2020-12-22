package logit

expect val DefaultLogger: LoggerFactory

fun interface LoggerFactory {
    fun get(tag: String): Logger
}

inline fun LoggerFactory.modify(crossinline block: Logger.() -> Logger): LoggerFactory = LoggerFactory { get(it).block() }
