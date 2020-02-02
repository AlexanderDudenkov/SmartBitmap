package com.dudencovgmaill.smartbitmaplib.util

import kotlin.reflect.KClass

fun Any.callMethod(
    methodName: String,
    parameterTypes: Array<Class<Any>>? = null,
    returnType: KClass<Any>? = null,
    parameters: Array<Any?>? = null

): Any? {
    return this::class.java.declaredMethods.find { method ->
        method.name == methodName
                && (returnType == null || method.returnType == returnType)
                && (parameterTypes.isNullOrEmpty() || method.parameterTypes.contentEquals(
            parameterTypes
        ))
    }?.let { method ->
        method.isAccessible = true
        if (parameters.isNullOrEmpty()) method.invoke(this) else method.invoke(this, *parameters)
    }
}

fun Any.getField(fieldName: String): Any? {
    return this::class.java.declaredFields.find { field ->
        field.name == fieldName
    }?.let { field ->
        field.isAccessible = true
        field.get(this)
    }
}

fun Any.getClass(className: String): Class<*>? {
    return this::class.java.declaredClasses.find { class_ ->
        class_.simpleName == className
    }
}

fun Class<*>.callConstructor(
    parameters: Array<Any?>? = null,
    parameterTypes: Array<Class<*>>? = null
): Any? {
    return declaredConstructors.find {
        parameterTypes.isNullOrEmpty() || it.parameterTypes.contentEquals(parameterTypes)
    }?.let {
        it.isAccessible = true
        if (parameters.isNullOrEmpty()) it.newInstance() else it.newInstance(*parameters)
    }
}
