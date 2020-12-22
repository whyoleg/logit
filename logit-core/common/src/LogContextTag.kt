package logit

interface LogContextTag<T : Any> {

    //inserted internally
    object Message : LogContextTag<Any> {
        override fun toString(): String = "LogContextTag.Message"
    }

    object Error : LogContextTag<Throwable> {
        override fun toString(): String = "LogContextTag.Error"
    }

    object ErrorDuringLogCreation : LogContextTag<Throwable> {
        override fun toString(): String = "LogContextTag.ErrorDuringLogCreation"
    }

    object CallerMethod : LogContextTag<String> {
        override fun toString(): String = "LogContextTag.CallerMethod"
    }

    object CallerClass : LogContextTag<String> {
        override fun toString(): String = "LogContextTag.CallerClass"
    }
}

internal operator fun <T : Any> LogContextTag<T>.invoke(value: T): LogContext = LogContext.NonEmpty(mapOf(this to value))

val LogContext.message: Any? get() = get(LogContextTag.Message)
val LogContext.error: Throwable? get() = get(LogContextTag.Error)
val LogContext.errorDuringLogCreation: Throwable? get() = get(LogContextTag.ErrorDuringLogCreation)
val LogContext.callerMethod: String? get() = get(LogContextTag.CallerMethod)
val LogContext.callerClass: String? get() = get(LogContextTag.CallerClass)

//can be used externally
interface CustomLogContextTag<T : Any> : LogContextTag<T>

operator fun <T : Any> CustomLogContextTag<T>.invoke(value: T): LogContext = LogContext.NonEmpty(mapOf(this to value))

