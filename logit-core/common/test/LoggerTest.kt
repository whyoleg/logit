package logit

import kotlin.test.*

class LoggerTest {
    @Test
    fun simpleTest() {
        val factory = TestLogger()
        val logger = factory.get("tag 1")

        logger.info { "Hello world!" }

        assertEquals(1, factory.printers.size)
        val printer = factory.printers["tag 1"]
        assertNotNull(printer)

        assertEquals(1, printer.entries.size)

        val entry = printer.entries.removeFirst()

        assertEquals("tag 1", entry.tag)
        assertEquals(LogLevel.INFO, entry.level)
        assertEquals("Hello world!", entry.message)
        assertNull(entry.error)
    }

    @Test
    fun defaultLogger() {
        val logger = DefaultLogger.get("tag.tag.123")
        logger.trace { "Hello world" }
        logger.debug { "Hello world" }
        logger.info { "Hello world" }
        logger.warn { "Hello world" }
        logger.error { "Hello world" }
        logger.log(LogLevel("TEST", 888)) { "Hello world" }
    }

    @Test
    fun differentFiltersPerLogger() {
        val printers1 = mutableMapOf<String, TestLogPrinter>()
        val printer1 = LogPrinterFactory { tag ->
            TestLogPrinter(tag).also { printers1[tag] = it }
        }.modify {
            filter { level, _ ->
                level >= LogLevel.DEBUG
            }
        }
        val printers2 = mutableMapOf<String, TestLogPrinter>()
        val printer2 = LogPrinterFactory { tag ->
            TestLogPrinter(tag).also { printers2[tag] = it }
        }.modify {
            filter { level, _ ->
                level >= LogLevel.WARN
            }
        }
        val printer = printer1 + printer2
        val factory = LoggerFactory { Logger(it, printer) }

        val logger = factory.get("hello")

        var traceCalled = false

        logger.trace {
            traceCalled = true
            "trace"
        }
        logger.debug { "debug" }
        logger.info { "info" }
        logger.warn { "warn" }
        logger.error { "error" }

        assertFalse(traceCalled)

        assertEquals(1, printers1.size)
        assertEquals(1, printers2.size)

        val entries1 = assertNotNull(printers1["hello"]).entries
        val entries2 = assertNotNull(printers2["hello"]).entries

        assertEquals(4, entries1.size)
        assertEquals(2, entries2.size)

        assertEquals("debug", entries1.removeFirst().message)
        assertEquals("info", entries1.removeFirst().message)
        assertEquals("warn", entries1.removeFirst().message)
        assertEquals("error", entries1.removeFirst().message)

        assertEquals("warn", entries2.removeFirst().message)
        assertEquals("error", entries2.removeFirst().message)
    }
}
