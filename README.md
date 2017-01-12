# Kotlin Template

## Default

```
gradle
```

## Build

```
gradle build
```

## Run

```
gradle run
```

## Continuous build

```
gradle run -t
```

or

```
gradle run --continuous
```

## FatJar

```
gradle fatJar
java -jar build/libs/${NAME}-assembly-${VERSION}.jar
```
## Code snippets 

### Kovenant

```kotlin
// compile 'nl.komponents.kovenant:kovenant:3.0.0'

import nl.komponents.kovenant.*
import nl.komponents.kovenant.functional.*

fun longOperation(url: String): Promise<String, Exception> = task {
    val result = // ...
    result
}

fun foo(key: String): Promise<String, Exception> {
    val url = getUrl(key) ?: return Promise.ofFail(Exception("Not Found"))
    return longOperation(url)
}

val success = Promise.of("aa")
val result: String = success.get
val bar = foo("google.com").map { result -> "foo $result bar" }
val foo3 = combine(foo("google.com"), foo("google.com"), foo("google.com")).success { it.first + it.second + it.third }
val foofoo = foo("google.com") and foo("google.com") success { it.first + it.second }
val foosFutures: List<Promise<String, Exception>> = listOf(foo("google.com"), foo("google.com"))
val futureFoos: Promise<List<String>, Exception> = all(foosFutures)
val nested: Promise<String, Exception> = foo("google.com").bind { result -> foo(result) }
```

### Injekt

```kotlin
// compile 'uy.kohesive.injekt:injekt-core:1.14.+'

import uy.kohesive.injekt.*
import uy.kohesive.injekt.api.*

interface Animal
class Dog : Animal
class Cat : Animal

class AnimalContainer(val animal: Animal)

class A(val b: B, val c: C)
class B
class C(val d: D)
class D

class Boot {
    companion object : InjektMain() {
        @JvmStatic public fun main(args: Array<String>) {
            Boot().run()
        }

        override fun InjektRegistrar.registerInjectables() {
            addLoggerFactory({ byName -> LoggerFactory.getLogger(byName) }, { byClass -> LoggerFactory.getLogger(byClass) })
            addSingleton(D())
            addSingleton(C(Injekt.get()))
            addSingleton(B())
            addFactory { A(Injekt.get(), Injekt.get()) }

            addSingleton(Dog())
            addSingleton<Animal>(Cat())
            addFactory { AnimalContainer(Injekt.get()) }
        }
    }

    private val log: Logger by injectLogger()

    fun run() {
        val locallog = Injekt.logger<Logger>(Boot)
        log.debug("Hello!")

        val a1 = Injekt.get<A>()
        val a2 = Injekt.get<A>()
        log.debug("a: $a1 b: ${a1.b} c: ${a1.c} d: ${a1.c.d}")
        log.debug("a: $a2 b: ${a2.b} c: ${a2.c} d: ${a2.c.d}")

        val animalContainer = Injekt.get<AnimalContainer>()
        log.debug("$animalContainer ${animalContainer.animal}")
    }
}
```

## References
* [Kovenant - Promises for Kotlin](https://github.com/mplatvoet/kovenant)
* [Injekt - Dependency Injection for Kotlin](https://github.com/kohesive/injekt)
* [kotlinx.coroutines - libraries built upon Kotlin coroutines](https://github.com/Kotlin/kotlinx.coroutines)