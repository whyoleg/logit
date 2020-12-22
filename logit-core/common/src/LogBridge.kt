package logit

abstract class LogBridge<F>(
    val printer: LogFormatPrinterFactory<F>,
    defaultFormat: LogFormatFactory<F>
) {
    constructor(
        printer: LogFormatPrinter<F>,
        defaultFormat: LogFormatFactory<F>
    ) : this(
        LogFormatPrinterFactory { printer },
        defaultFormat
    )

    val default = Default(printer, defaultFormat)

    class Default<F>(
        printer: LogFormatPrinterFactory<F>,
        val format: LogFormatFactory<F>
    ) {
        val printer: LogPrinterFactory = printer.withFormat(format)
        val logger: LoggerFactory = LoggerFactory { Logger(it, this.printer) }
    }
}
