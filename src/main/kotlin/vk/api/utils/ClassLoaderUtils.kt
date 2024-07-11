package org.example.vk.api.utils

import java.io.File

fun findClasses(packageName: String, superclass: Class<*> = Object::class.java, annotatedBy: List<Class<out Annotation>> = listOf()): List<Class<*>> {
    val name = packageName.replace('.', '/')
    val classLoader = ClassLoader.getSystemClassLoader()
    val url = classLoader.getResource(name) ?: throw RuntimeException("Url $name Not found")
    val directory = File(url.file)

    if (!directory.exists()) {
        return emptyList();
    }

    return directory.walk()
        .filter { f -> f.isFile() && !f.name.contains('$') && f.name.endsWith(".class") }
        .map { it.canonicalPath }
        .map {
            it.removePrefix(directory.canonicalPath)
                .dropLast(6) // remove .class
                .replace('/', '.')
                .replace('\\', '.')
        }
        .map {
            packageName.plus(it)
        }
        .map { fullClassName -> findClassByName(fullClassName) }
        .filter { it.superclass?.equals(superclass) == true }
        .filter { clazz -> annotatedBy.all { clazz.isAnnotationPresent(it) }}
        .toList()
}

fun findClassByName(fullClassName: String): Class<*> = try {
    Class.forName(fullClassName)
} catch (ex: ClassNotFoundException) {
    throw RuntimeException(ex)
} catch (ex: InstantiationException) {
    throw RuntimeException(ex)
} catch (ex: IllegalAccessException) {
    throw RuntimeException(ex)
}

fun setField(instance: Any, fieldType: Class<*>, fieldValue: Any) {
    instance.javaClass.methods
        .filter { it.name.startsWith("set") }
        .filter { it.parameterTypes.size == 1 }
        .filter { it.parameterTypes[0] == fieldType }
        .forEach { it.invoke(instance, fieldValue) }
}