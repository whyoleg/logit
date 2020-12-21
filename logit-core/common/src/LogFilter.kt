package logit

fun interface RootLogFilter {
    fun isLoggable(tag: String, level: LogLevel, context: LogContext): Boolean

    companion object {
        val ALL: RootLogFilter = RootLogFilter { _, _, _ -> true }
        val NEVER: RootLogFilter = RootLogFilter { _, _, _ -> false }
    }
}

fun interface LogFilter {
    fun isLoggable(level: LogLevel, context: LogContext): Boolean

    companion object {
        val ALL: LogFilter = LogFilter { _, _ -> true }
        val NEVER: LogFilter = LogFilter { _, _ -> false }
    }
}

fun interface LogFilterFactory {
    fun getFilter(tag: String): LogFilter

    companion object {
        val ALL: LogFilterFactory = LogFilterFactory { LogFilter.ALL }
        val NEVER: LogFilterFactory = LogFilterFactory { LogFilter.NEVER }
    }
}


operator fun LogFilter.plus(other: LogFilter): LogFilter = LogFilter { level, context ->
    this.isLoggable(level, context) && other.isLoggable(level, context)
}

operator fun RootLogFilter.plus(other: RootLogFilter): RootLogFilter = RootLogFilter { tag, level, context ->
    this.isLoggable(tag, level, context) && other.isLoggable(tag, level, context)
}

operator fun RootLogFilter.plus(other: LogFilter): RootLogFilter = RootLogFilter { tag, level, context ->
    this.isLoggable(tag, level, context) && other.isLoggable(level, context)
}

operator fun LogFilterFactory.plus(other: LogFilterFactory): LogFilterFactory = LogFilterFactory {
    this.getFilter(it) + other.getFilter(it)
}

fun LogFilterFactory(filter: RootLogFilter): LogFilterFactory = LogFilterFactory { tag ->
    LogFilter { level, context ->
        filter.isLoggable(tag, level, context)
    }
}

fun Logger.filter(factory: LogFilterFactory): Logger = copy(filterFactory = filterFactory + factory)
fun Logger.filter(filter: RootLogFilter): Logger = filter(LogFilterFactory(filter))
fun Logger.filter(filter: LogFilter): Logger = filter(LogFilterFactory { filter })
fun Logger.filter(level: LogLevel): Logger = filter { l, _ -> l >= level }
