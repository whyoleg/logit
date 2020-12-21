package logit

data class Logger(
    internal val tag: String,
    internal val context: LogContext,
    internal val filterFactory: LogFilterFactory,
    internal val printerFactory: LogPrinterFactory
) {
    private val filter: LogFilter by lazy { filterFactory.getFilter(tag) }
    private val printer: LogPrinter by lazy { printerFactory.getPrinter(tag) }

    @PublishedApi
    internal fun filter(logContext: LogContext, level: LogLevel, throwable: Throwable?): LogContext? {
        val filterContext = this.context + logContext + (throwable?.let { LogTag.Error(throwable) } ?: LogContext.Empty)
        if (!filter.isLoggable(level, filterContext)) return null
        return filterContext
    }

    @PublishedApi
    internal fun print(filterContext: LogContext, level: LogLevel, message: Result<Any?>) {
        val printContext = filterContext + message.fold(
            onSuccess = { it?.let { LogTag.Message(it) } ?: LogContext.Empty },
            onFailure = { LogTag.ErrorDuringLogCreation(it) }
        )
        printer.print(level, printContext)
    }

    inline fun log(level: LogLevel, throwable: Throwable? = null, context: LogContext = LogContext.Empty, message: () -> Any? = { null }) {
        print(filter(context, level, throwable) ?: return, level, runCatching(message))
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
