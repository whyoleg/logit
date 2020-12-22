package logit

class LogLevel(name: String, val value: Int) : Comparable<LogLevel> {
    public val name: String = name.toUpperCase()
    override operator fun compareTo(other: LogLevel): Int = value.compareTo(other.value)

    override fun toString(): String = name

    override fun hashCode(): Int = value

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as LogLevel

        if (value != other.value) return false

        return true
    }

    companion object {
        val MIN: LogLevel = LogLevel("MIN", Int.MIN_VALUE)
        val TRACE: LogLevel = LogLevel("TRACE", 100)
        val DEBUG: LogLevel = LogLevel("DEBUG", 300)
        val INFO: LogLevel = LogLevel("INFO", 500)
        val WARN: LogLevel = LogLevel("WARN", 700)
        val ERROR: LogLevel = LogLevel("ERROR", 900)
        val MAX: LogLevel = LogLevel("MAX>", Int.MAX_VALUE)
    }
}
