package logit

//TODO

fun interface RootLogInterceptor {
    fun intercept(tag: String, level: LogLevel, context: LogContext): LogContext
}

fun interface LogInterceptor {
    fun intercept(level: LogLevel, context: LogContext): LogContext
}

fun interface LogInterceptorFactory {
    fun getInterceptor(tag: String): LogInterceptor
}
