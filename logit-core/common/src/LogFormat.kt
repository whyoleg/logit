package logit

//TODO

fun interface RootLogFormat<T : Any> {
    fun format(tag: String, level: LogLevel, context: LogContext): T
}

fun interface LogFormat<T : Any> {
    fun format(level: LogLevel, context: LogContext): T
}

fun interface LogFormatFactory<T : Any> {
    fun getFormat(tag: String): LogFormat<T>
}
