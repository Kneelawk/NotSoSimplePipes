package com.kneelawk.notsosimplepipes.util

import kotlin.reflect.KProperty

class ReflectionField<T, F>(target: Class<T>, fieldClass: Class<F>, fieldName: String) {
    companion object {
        inline fun <reified T, reified F> new(fieldName: String): ReflectionField<T, F> {
            return ReflectionField(T::class.java, F::class.java, fieldName)
        }
    }

    private val field = target.getDeclaredField(fieldName)

    init {
        if (!fieldClass.isAssignableFrom(field.type)) {
            throw IllegalArgumentException("Incompatible field and requested types")
        }

        field.isAccessible = true
    }

    @Suppress("unchecked_cast")
    operator fun get(obj: T?): F {
        return field[obj] as F
    }

    @Suppress("unchecked_cast")
    operator fun getValue(obj: T, property: KProperty<*>): F {
        return field[obj] as F
    }
}
