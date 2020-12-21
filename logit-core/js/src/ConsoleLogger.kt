package logit

object ConsoleLogger : LoggerFactory {
    private val printer = LogPrinterFactory { tag ->
        LogPrinter { level, context ->
            val msg = when (val error = context.error) {
                null -> arrayOf("[${level}]", "($tag)", context.message)
                else -> arrayOf("[${level}]", "($tag)", context.message, "Error:", error)
            }
            when (level) {
                LogLevel.ERROR -> console.error(*msg)
                LogLevel.WARN  -> console.warn(*msg)
                LogLevel.INFO  -> console.info(*msg)
                LogLevel.DEBUG -> console.log(*msg)
                LogLevel.TRACE -> console.log(*msg)
                else           -> console.log(*msg)
            }
        }
    }

    override fun get(tag: String): Logger = Logger(tag, LogContext.Empty, LogFilterFactory.ALL, printer)
}
