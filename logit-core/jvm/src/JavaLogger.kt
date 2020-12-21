package logit

import java.util.logging.*
import java.util.logging.Logger as JLogger

object JavaLogger : LoggerFactory {
    private val filter = LogFilterFactory { tag ->
        val jLogger = JLogger.getLogger(tag)
        LogFilter { level, _ ->
            jLogger.isLoggable(level.jLevel)
        }
    }
    private val printer = LogPrinterFactory { tag ->
        val jLogger = JLogger.getLogger(tag)
        LogPrinter { level, context ->
            val record = LogRecord(level.jLevel, context.message.toString()).apply {
                loggerName = tag
                sourceClassName = context.callerClass
                sourceMethodName = context.callerMethod
                thrown = context.error
            }
            jLogger.log(record)
        }
    }

    override fun get(tag: String): Logger = Logger(tag, LogContext.Empty, filter, printer)
}

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
