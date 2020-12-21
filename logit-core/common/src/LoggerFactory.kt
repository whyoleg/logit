package logit

fun interface LoggerFactory {
    fun get(tag: String): Logger
}

fun LoggerFactory(printer: LogPrinterFactory, filter: LogFilterFactory = LogFilterFactory.ALL): LoggerFactory = LoggerFactory {
    Logger(it, LogContext.Empty, filter, printer)
}

inline fun LoggerFactory.modify(crossinline block: Logger.() -> Logger): LoggerFactory = LoggerFactory { get(it).block() }
