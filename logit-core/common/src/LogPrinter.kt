package logit

fun interface RootLogPrinter {
    fun print(tag: String, level: LogLevel, context: LogContext)

    companion object {
        val IGNORE: RootLogPrinter = RootLogPrinter { _, _, _ -> }
    }
}

fun interface LogPrinter {
    fun print(level: LogLevel, context: LogContext)

    companion object {
        val IGNORE: LogPrinter = LogPrinter { _, _ -> }
    }
}

fun interface LogPrinterFactory {
    fun getPrinter(tag: String): LogPrinter

    companion object {
        val IGNORE: LogPrinterFactory = LogPrinterFactory { LogPrinter.IGNORE }
    }
}

operator fun LogPrinter.plus(other: LogPrinter): LogPrinter = LogPrinter { level, context ->
    this.print(level, context)
    other.print(level, context)
}

operator fun RootLogPrinter.plus(other: RootLogPrinter): RootLogPrinter = RootLogPrinter { tag, level, context ->
    this.print(tag, level, context)
    other.print(tag, level, context)
}

operator fun RootLogPrinter.plus(other: LogPrinter): RootLogPrinter = RootLogPrinter { tag, level, context ->
    this.print(tag, level, context)
    other.print(level, context)
}

operator fun LogPrinterFactory.plus(other: LogPrinterFactory): LogPrinterFactory = LogPrinterFactory {
    this.getPrinter(it) + other.getPrinter(it)
}

fun LogPrinterFactory(filter: RootLogPrinter): LogPrinterFactory = LogPrinterFactory { tag ->
    LogPrinter { level, context ->
        filter.print(tag, level, context)
    }
}

fun Logger.appendPrinter(factory: LogPrinterFactory): Logger = copy(printerFactory = printerFactory + factory)
fun Logger.appendPrinter(printer: RootLogPrinter): Logger = appendPrinter(LogPrinterFactory(printer))
fun Logger.appendPrinter(printer: LogPrinter): Logger = appendPrinter(LogPrinterFactory { printer })

fun Logger.replacePrinter(factory: LogPrinterFactory): Logger = copy(printerFactory = factory)
fun Logger.replacePrinter(printer: RootLogPrinter): Logger = replacePrinter(LogPrinterFactory(printer))
fun Logger.replacePrinter(printer: LogPrinter): Logger = replacePrinter(LogPrinterFactory { printer })
