package com.kneelawk.notsosimplepipes.util

import kotlin.reflect.KProperty

class ReflectionField<F>(target: Class<*>, fieldClass: Class<F>, fieldName: String) {
    companion object {
        inline fun <reified T, reified F> new(fieldName: String): ReflectionField<F> {
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
    operator fun get(obj: Any?): F {
        return field[obj] as F
    }

    @Suppress("unchecked_cast")
    operator fun getValue(obj: Any, property: KProperty<*>): F {
        return field[obj] as F
    }
}
