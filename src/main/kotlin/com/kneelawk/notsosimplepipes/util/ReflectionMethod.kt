package com.kneelawk.notsosimplepipes.util

import java.lang.IllegalArgumentException
import kotlin.reflect.KClass

class ReflectionMethod<T, F>(target: Class<T>, methodReturn: Class<F>, methodName: String, methodArgs: Array<out Class<*>>) {
    companion object {
        inline fun <reified T, reified F> new(fieldName: String, vararg methodArgs: KClass<*>): ReflectionMethod<T, F> {
            return ReflectionMethod(T::class.java, F::class.java, fieldName, methodArgs.map { it.java }.toTypedArray())
        }
    }

    private val method = target.getDeclaredMethod(methodName, *methodArgs)

    init {
        if (!methodReturn.isAssignableFrom(method.returnType)) {
            throw IllegalArgumentException("Incompatible method return and requested types")
        }

        method.isAccessible = true
    }

    @Suppress("unchecked_cast")
    operator fun invoke(obj: T?, vararg args: Any): F {
        return method(obj, *args) as F
    }
}