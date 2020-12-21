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
}
