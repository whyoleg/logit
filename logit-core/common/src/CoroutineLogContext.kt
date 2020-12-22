package logit

import kotlin.coroutines.*

object CoroutineContextLogContextTag : CustomLogContextTag<CoroutineContext> {
    override fun toString(): String = "LogContextTag.CoroutineContext"
}

val LogContext.coroutineContext: CoroutineContext? get() = get(CoroutineContextLogContextTag)

suspend fun coroutineLogContext(): LogContext = CoroutineContextLogContextTag(coroutineContext)
