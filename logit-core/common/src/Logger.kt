package logit

data class Logger(
    internal val tag: String,
    internal val printerFactory: LogPrinterFactory,
    internal val filterFactory: LogFilterFactory = NoLogFilter,
    internal val interceptorFactory: LogInterceptorFactory = NoLogInterceptor,
    internal val context: LogContext = LogContext.Empty
) {
    private val printer: LogPrinter by lazy { printerFactory.getPrinter(tag) }
    private val filter: LogFilter by lazy { filterFactory.getFilter(tag) }
    private val interceptor: LogInterceptor by lazy { interceptorFactory.getInterceptor(tag) }

    @PublishedApi
    internal fun logContext(logContext: LogContext, throwable: Throwable?): LogContext {
        val newContext = this.context + logContext
        return if (throwable != null) newContext + LogContextTag.Error(throwable) else newContext
    }

    @PublishedApi
    internal fun filter(logContext: LogContext, level: LogLevel): LogContext? {
        val filterContext = interceptor.interceptFilter(level, logContext)
        if (filter.isLoggable(level, filterContext) && printer.filter.isLoggable(level, filterContext)) return filterContext
        return null
    }

    @PublishedApi
    internal fun print(filterContext: LogContext, level: LogLevel, message: Result<Any?>) {
        val messageContext = message.fold(
            onSuccess = { msg -> if (msg != null) LogContextTag.Message(msg) else LogContext.Empty },
            onFailure = { LogContextTag.ErrorDuringLogCreation(it) }
        )
        val printContext = interceptor.interceptPrinter(level, filterContext + messageContext)
        printer.print(level, printContext)
    }

    inline fun log(level: LogLevel, throwable: Throwable? = null, context: LogContext = LogContext.Empty, message: () -> Any? = { null }) {
        val logContext = logContext(context, throwable)
        val filterContext = filter(logContext, level) ?: return
        val msg = runCatching(message)
        print(filterContext, level, msg)
    }

}

inline fun Logger.trace(throwable: Throwable? = null, context: LogContext = LogContext.Empty, message: () -> Any? = { null }) =
    log(LogLevel.TRACE, throwable, context, message)

inline fun Logger.debug(throwable: Throwable? = null, context: LogContext = LogContext.Empty, message: () -> Any? = { null }) =
    log(LogLevel.DEBUG, throwable, context, message)

inline fun Logger.info(throwable: Throwable? = null, context: LogContext = LogContext.Empty, message: () -> Any? = { null }) =
    log(LogLevel.INFO, throwable, context, message)

inline fun Logger.warn(throwable: Throwable? = null, context: LogContext = LogContext.Empty, message: () -> Any? = { null }) =
    log(LogLevel.WARN, throwable, context, message)

inline fun Logger.error(throwable: Throwable? = null, context: LogContext = LogContext.Empty, message: () -> Any? = { null }) =
    log(LogLevel.ERROR, throwable, context, message)

fun Logger.withTag(tag: String): Logger = copy(tag = tag)
fun Logger.withChildTag(tag: String, separator: String = "."): Logger = withTag(this.tag + separator + tag)
