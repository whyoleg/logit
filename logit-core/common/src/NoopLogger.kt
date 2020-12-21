package logit

object NoopLogger : LoggerFactory {
    private val logger = Logger("", LogContext.Empty, LogFilterFactory.NEVER, LogPrinterFactory.IGNORE)
    override fun get(tag: String): Logger = logger
}
