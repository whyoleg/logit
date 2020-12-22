package logit

import java.util.logging.*
import java.util.logging.Logger as JLogger

actual val DefaultLogger: LoggerFactory get() = JavaBridge.default.logger

object JavaBridge : LogBridge<LogRecord>(
    LogFormatPrinterFactory { tag ->
        val jLogger = JLogger.getLogger(tag)
        LogFormatPrinter<LogRecord> { level, entry ->
            entry.level = level.jLevel
            jLogger.log(entry)
        }.filter { level, _ ->
            jLogger.isLoggable(level.jLevel)
        }
    },
    LogFormatFactory { tag ->
        LogFormat { _, context ->
            LogRecord(Level.ALL, context.message.toString()).apply {
                loggerName = tag
                sourceClassName = context.callerClass
                sourceMethodName = context.callerMethod
                thrown = context.error
            }
        }
    }
)

private val LogLevel.jLevel: Level
    get() = when (this) {
        LogLevel.TRACE -> Level.FINEST
        LogLevel.DEBUG -> Level.FINE
        LogLevel.INFO  -> Level.INFO
        LogLevel.WARN  -> Level.WARNING
        LogLevel.ERROR -> Level.SEVERE
        else           -> {
            try {
                Level.parse(name)
            } catch (cause: Throwable) {
                //if failed, we need to create new log level with provided name and value
                object : Level(name, value) {}
            }
        }
    }
