package logit

object NoLogInterceptor : LogInterceptorFactory, LogInterceptor {
    override fun getInterceptor(tag: String): LogInterceptor = this
}

interface LogInterceptor {
    fun interceptFilter(level: LogLevel, context: LogContext): LogContext = context
    fun interceptPrinter(level: LogLevel, context: LogContext): LogContext = context
}

fun interface LogInterceptorFactory {
    fun getInterceptor(tag: String): LogInterceptor
}

operator fun LogInterceptor.plus(other: LogInterceptor): LogInterceptor = object : LogInterceptor {
    override fun interceptFilter(level: LogLevel, context: LogContext): LogContext =
        other.interceptFilter(level, this@plus.interceptFilter(level, context))

    override fun interceptPrinter(level: LogLevel, context: LogContext): LogContext =
        other.interceptPrinter(level, this@plus.interceptPrinter(level, context))
}

operator fun LogInterceptorFactory.plus(other: LogInterceptorFactory): LogInterceptorFactory = LogInterceptorFactory { tag ->
    this.getInterceptor(tag) + other.getInterceptor(tag)
}


fun Logger.intercept(factory: LogInterceptorFactory): Logger = copy(interceptorFactory = interceptorFactory + factory)
fun Logger.intercept(interceptor: LogInterceptor): Logger = intercept { interceptor }
