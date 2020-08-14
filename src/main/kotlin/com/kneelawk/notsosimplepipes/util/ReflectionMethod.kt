package com.kneelawk.notsosimplepipes.util

import kotlin.reflect.KClass

class ReflectionMethod<R>(
    target: Class<*>,
    methodReturn: Class<R>,
    methodName: String,
    methodArgs: Array<out Class<*>>
) {
    companion object {
        inline fun <reified T, reified R> new(methodName: String, vararg methodArgs: KClass<*>): ReflectionMethod<R> {
            return ReflectionMethod(T::class.java, R::class.java, methodName, methodArgs.map { it.java }.toTypedArray())
        }

        inline fun <reified T, reified R : Any> primitive(
            methodName: String,
            vararg methodArgs: KClass<*>
        ): ReflectionMethod<R> {
            R::class.javaPrimitiveType?.let { ret ->
                return ReflectionMethod(T::class.java, ret, methodName, methodArgs.map { it.java }.toTypedArray())
            } ?: throw IllegalArgumentException("${R::class} does not have a primitive type!")
        }
    }

    private val method = target.getDeclaredMethod(methodName, *methodArgs)

    init {
        if (!methodReturn.isAssignableFrom(method.returnType)) {
            throw IllegalArgumentException("Incompatible method return (${method.returnType}) and requested ($methodReturn) types for method $methodName")
        }

        method.isAccessible = true
    }

    @Suppress("unchecked_cast")
    operator fun invoke(obj: Any?, vararg args: Any): R {
        return method(obj, *args) as R
    }
}