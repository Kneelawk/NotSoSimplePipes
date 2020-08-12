package com.kneelawk.notsosimplepipes.util

import java.lang.IllegalArgumentException
import kotlin.reflect.KClass

class ReflectionMethod<R>(target: Class<*>, methodReturn: Class<R>, methodName: String, methodArgs: Array<out Class<*>>) {
    companion object {
        inline fun <reified T, reified R> new(fieldName: String, vararg methodArgs: KClass<*>): ReflectionMethod<R> {
            return ReflectionMethod(T::class.java, R::class.java, fieldName, methodArgs.map { it.java }.toTypedArray())
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
    operator fun invoke(obj: Any?, vararg args: Any): R {
        return method(obj, *args) as R
    }
}