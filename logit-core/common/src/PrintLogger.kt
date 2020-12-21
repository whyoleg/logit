package logit

object PrintLogger : LoggerFactory {
    private val printer = LogPrinterFactory { tag ->
        LogPrinter { level, context ->
            val error = context.error?.stackTraceToString()?.let { "Error: $it" } ?: ""
            val msg = "[$level] ($tag) ${context.message} $error"
            println(msg)
        }
    }

    override fun get(tag: String): Logger = Logger(tag, LogContext.Empty, LogFilterFactory.ALL, printer)
}
