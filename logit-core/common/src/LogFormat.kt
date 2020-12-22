package logit

fun interface LogFormatPrinter<T> {
    val filter: LogFilter get() = NoLogFilter
    fun print(level: LogLevel, entry: T)
}

fun interface LogFormat<T> {
    fun format(level: LogLevel, context: LogContext): T
}

fun <T> LogFormatPrinter<T>.withFormat(format: LogFormat<T>): LogPrinter = LogPrinter { level, context ->
    print(level, format.format(level, context))
}

fun <T> LogFormatPrinter<T>.filter(filter: LogFilter): LogFormatPrinter<T> = object : LogFormatPrinter<T> {
    override val filter: LogFilter = this@filter.filter and filter
    override fun print(level: LogLevel, entry: T) = this@filter.print(level, entry)
}


fun interface LogFormatPrinterFactory<T> {
    fun getFormatPrinter(tag: String): LogFormatPrinter<T>
}

fun interface LogFormatFactory<T> {
    fun getFormat(tag: String): LogFormat<T>
}

fun <T> LogFormatPrinterFactory<T>.withFormat(factory: LogFormatFactory<T>): LogPrinterFactory = LogPrinterFactory { tag ->
    getFormatPrinter(tag).withFormat(factory.getFormat(tag))
}

fun <T> LogFormatPrinterFactory<T>.filter(factory: LogFilterFactory): LogFormatPrinterFactory<T> = LogFormatPrinterFactory { tag ->
    getFormatPrinter(tag).filter(factory.getFilter(tag))
}
