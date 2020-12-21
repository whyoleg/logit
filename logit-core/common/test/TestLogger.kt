package logit

class TestLogger : LoggerFactory {
    val printers = mutableMapOf<String, TestLogPrinter>()
    private val printer = LogPrinterFactory { tag ->
        TestLogPrinter(tag).also { printers[tag] = it }
    }

    override fun get(tag: String): Logger = Logger(tag, LogContext.Empty, LogFilterFactory.ALL, printer)
}

class TestLogPrinter(val tag: String) : LogPrinter {
    val entries = ArrayDeque<TestLogEntry>()
    override fun print(level: LogLevel, context: LogContext) {
        entries.addLast(TestLogEntry(tag, level, context.message, context.error, context))
    }
}

class TestLogEntry(
    val tag: String,
    val level: LogLevel,
    val message: Any?,
    val error: Throwable?,
    val context: LogContext
)
