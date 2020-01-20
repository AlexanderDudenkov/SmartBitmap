package com.dudencovgmaill.smartbitmaplib.util

import kotlin.reflect.KClass

fun Any.call(
    methodName: String,
    vararg parameters: Any? = arrayOf(),
    parameterCount: Int? = null,
    parameterTypes: Array<Class<Any>>? = null,
    returnType: KClass<Any>? = null

): Any? {
    return this::class.java.declaredMethods.find { method ->
        method.name == methodName
                && (returnType == null || method.returnType == returnType)
                && (parameterCount == null || method.parameterCount == parameterCount)
                && (parameterTypes.isNullOrEmpty() || method.parameterTypes.contentEquals(
            parameterTypes
        ))
    }?.let { method ->
        method.isAccessible = true
        method.invoke(this, *parameters)
    }
}

fun Any.field(fieldName: String): Any? {
    return this::class.java.declaredFields.find { field ->
        field.name == fieldName
    }?.let { field ->
        field.isAccessible = true
        field.get(this)
    }
}

fun Any.getClass(className: String):Any?{
    return this::class.java.declaredClasses.find { class_ ->
        class_.name == className
    }?.let { class_ ->
        field.isAccessible = true
        field.get(this)
    }
}

fun Any.callConstructor():Any?{
    return null
}
