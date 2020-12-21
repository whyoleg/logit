package logit

//TODO

fun interface RootLogFormatPrinter<T : Any> {
    fun print(tag: String, level: LogLevel, entry: T)
}

fun interface LogFormatPrinter<T : Any> {
    fun print(level: LogLevel, entry: T)
}

fun interface LogFormatPrinterFactory<T : Any> {
    fun getFormatPrinter(tag: String): LogFormatPrinter<T>
}
