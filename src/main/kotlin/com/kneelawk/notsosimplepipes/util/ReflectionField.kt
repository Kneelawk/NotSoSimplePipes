package com.kneelawk.notsosimplepipes.util

import kotlin.reflect.KProperty

class ReflectionField<F>(target: Class<*>, fieldClass: Class<F>, fieldName: String) {
    companion object {
        inline fun <reified T, reified F> new(fieldName: String): ReflectionField<F> {
            return ReflectionField(T::class.java, F::class.java, fieldName)
        }

        inline fun <reified T, reified F: Any> primitive(fieldName: String): ReflectionField<F> {
            F::class.javaPrimitiveType?.let {
                return ReflectionField(T::class.java, it, fieldName)
            } ?: throw IllegalArgumentException("${F::class} does not have a primitive type!")
        }
    }

    private val field = target.getDeclaredField(fieldName)

    init {
        if (!fieldClass.isAssignableFrom(field.type)) {
            throw IllegalArgumentException("Incompatible field (${field.type}) and requested ($fieldClass) types for field: $fieldName")
        }

        field.isAccessible = true
    }

    @Suppress("unchecked_cast")
    operator fun get(obj: Any?): F {
        return field[obj] as F
    }

    operator fun set(obj: Any?, value: F) {
        field[obj] = value
    }

    @Suppress("unchecked_cast")
    operator fun getValue(obj: Any, property: KProperty<*>): F {
        return field[obj] as F
    }

    operator fun setValue(obj: Any?, property: KProperty<*>, value: F) {
        field[obj] = value
    }
}
