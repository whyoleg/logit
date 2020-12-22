# logit

Simple and flexible Kotlin Multi-platform logger.

## Future

Soon it will support multiple platform loggers like slf4j/log4j on JVM, LogCat on Android, MPP firebase, NSLog on iOS/macOS and so on. Even
custom formats or destinations can be easily supported.

## Library structure

Logger is based on several interfaces:

* LogFilter - what to log
* LogPrinter - how to log
* LogFormat and LogFormatPrinter - how to (how to log) :)

To create logger at least LogPrinter is needed. It can be created plain, or with Format. In most complex case, You will have
LogFormatPrinter.

LogFormatPrinter + LogFormat = LogPrinter (LogFilter inside)

Logger can be obtained from LoggerFactory. In simplest case, it's just LoggerFactory(LogPrinterFactory)

`*Factory` is factory to create `*` based on `String` tag.

LogBridge is used to store Formattable Typed printer and default LoggerFactory implementation.
