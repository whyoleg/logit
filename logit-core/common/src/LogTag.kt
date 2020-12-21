package logit

interface LogTag<T : Any> {

    //inserted internally
    object Message : LogTag<Any> {
        override fun toString(): String = "LogTag.Message"
    }

    object Error : LogTag<Throwable> {
        override fun toString(): String = "LogTag.Error"
    }

    object ErrorDuringLogCreation : LogTag<Throwable> {
        override fun toString(): String = "LogTag.ErrorDuringLogCreation"
    }

    object CallerMethod : LogTag<String> {
        override fun toString(): String = "LogTag.CallerMethod"
    }

    object CallerClass : LogTag<String> {
        override fun toString(): String = "LogTag.CallerClass"
    }
}

val LogContext.message: Any? get() = get(LogTag.Message)
val LogContext.error: Throwable? get() = get(LogTag.Error)
val LogContext.errorDuringLogCreation: Throwable? get() = get(LogTag.ErrorDuringLogCreation)
val LogContext.callerMethod: String? get() = get(LogTag.CallerMethod)
val LogContext.callerClass: String? get() = get(LogTag.CallerClass)

internal operator fun <T : Any> LogTag<T>.invoke(value: T): LogContext = LogContext.NonEmpty(mapOf(this to value))

//can be used externally
interface CustomLogTag<T : Any> : LogTag<T>

operator fun <T : Any> CustomLogTag<T>.invoke(value: T): LogContext = LogContext.NonEmpty(mapOf(this to value))

