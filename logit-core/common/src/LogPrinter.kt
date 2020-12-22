package logit

fun interface LogPrinter {
    val filter: LogFilter get() = NoLogFilter
    fun print(level: LogLevel, context: LogContext)
}

fun interface LogPrinterFactory {
    fun getPrinter(tag: String): LogPrinter
}

fun LogPrinterFactory.modify(block: LogPrinter.() -> LogPrinter): LogPrinterFactory = LogPrinterFactory { tag ->
    getPrinter(tag).block()
}

//composition

operator fun LogPrinter.plus(other: LogPrinter): LogPrinter = object : LogPrinter {
    override val filter: LogFilter = this@plus.filter or other.filter
    override fun print(level: LogLevel, context: LogContext) {
        this@plus.printFiltered(level, context)
        other.printFiltered(level, context)
    }
}

operator fun LogPrinterFactory.plus(other: LogPrinterFactory): LogPrinterFactory = LogPrinterFactory { tag ->
    this.getPrinter(tag) + other.getPrinter(tag)
}

//filter

fun LogPrinter.filter(filter: LogFilter): LogPrinter = object : LogPrinter {
    override val filter: LogFilter = this@filter.filter and filter
    override fun print(level: LogLevel, context: LogContext) = this@filter.print(level, context)
}

fun LogPrinterFactory.filter(factory: LogFilterFactory): LogPrinterFactory = LogPrinterFactory { tag ->
    getPrinter(tag).filter(factory.getFilter(tag))
}

//logger

fun Logger.appendPrinter(factory: LogPrinterFactory): Logger = copy(printerFactory = printerFactory + factory)
fun Logger.appendPrinter(printer: LogPrinter): Logger = appendPrinter { _ -> printer }

fun Logger.replacePrinter(factory: LogPrinterFactory): Logger = copy(printerFactory = factory)
fun Logger.replacePrinter(printer: LogPrinter): Logger = replacePrinter { _ -> printer }

//internal

internal fun LogPrinter.printFiltered(level: LogLevel, context: LogContext) {
    if (filter.isLoggable(level, context)) print(level, context)
}
