package logit

actual val DefaultLogger: LoggerFactory get() = ConsoleBridge.default.logger

object ConsoleBridge : LogBridge<Array<Any?>>(
    LogFormatPrinter { level, entry ->
        when (level) {
            LogLevel.ERROR -> console.error(*entry)
            LogLevel.WARN  -> console.warn(*entry)
            LogLevel.INFO  -> console.info(*entry)
            LogLevel.DEBUG -> console.log(*entry)
            LogLevel.TRACE -> console.log(*entry)
            else           -> console.log(*entry) //TODO
        }
    },
    LogFormatFactory { tag ->
        LogFormat { level, context ->
            when (val error = context.error) {
                null -> arrayOf("[${level}]", "($tag)", context.message)
                else -> arrayOf("[${level}]", "($tag)", context.message, "Error:", error)
            }
        }
    }
)
