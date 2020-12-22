package logit

actual val DefaultLogger: LoggerFactory get() = PrintBridge.default.logger
