package logit

object NoLogFilter : LogFilterFactory, LogFilter {
    override fun getFilter(tag: String): LogFilter = this
    override fun isLoggable(level: LogLevel, context: LogContext): Boolean = true
}

fun interface LogFilter {
    fun isLoggable(level: LogLevel, context: LogContext): Boolean
}

fun interface LogFilterFactory {
    fun getFilter(tag: String): LogFilter
}

inline fun LogFilterFactory.modify(crossinline block: LogFilter.() -> LogFilter): LogFilterFactory = LogFilterFactory { tag ->
    getFilter(tag).block()
}

//composition

infix fun LogFilter.and(other: LogFilter): LogFilter = LogFilter { level, context ->
    this.isLoggable(level, context) and other.isLoggable(level, context)
}

infix fun LogFilter.or(other: LogFilter): LogFilter = LogFilter { level, context ->
    this.isLoggable(level, context) or other.isLoggable(level, context)
}

infix fun LogFilterFactory.and(other: LogFilterFactory): LogFilterFactory = LogFilterFactory { tag ->
    this.getFilter(tag) and other.getFilter(tag)
}

infix fun LogFilterFactory.or(other: LogFilterFactory): LogFilterFactory = LogFilterFactory { tag ->
    this.getFilter(tag) or other.getFilter(tag)
}

//logger

fun Logger.filter(factory: LogFilterFactory): Logger = copy(filterFactory = filterFactory and factory)
fun Logger.filter(filter: LogFilter): Logger = filter { _ -> filter }
fun Logger.filter(level: LogLevel): Logger = filter { l, _ -> l >= level }
