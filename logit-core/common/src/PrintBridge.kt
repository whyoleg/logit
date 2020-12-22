package logit

object PrintBridge : LogBridge<Any?>(
    LogFormatPrinter { _, entry -> println(entry) },
    LogFormatFactory { tag ->
        LogFormat { level, context ->
            val error = context.error?.stackTraceToString()?.let { "Error: $it" } ?: ""
            "[$level] ($tag) ${context.message} $error"
        }
    }
)
