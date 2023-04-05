import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**БЛОК 1*/
//ЗАДАНИЕ 1
suspend fun main1() = runBlocking {
    val job = CoroutineScope(EmptyCoroutineContext).launch {
        launch {
            delay(500)
            println("1 line") // <-- NO отменяем(25) до того, как выведем.
        }
        launch {
            delay(500)
            println("2 line")
        }
    }
    delay(100)
    job.cancelAndJoin()
}

//ЗАДАНИЕ 2
fun main2() = runBlocking {
    val job = CoroutineScope(EmptyCoroutineContext).launch {
        val child = launch {
            delay(500)
            println("1 line") // <-- NO отменяем(39) до того, как выведем.
        }
        launch {
            delay(500)
            println("2 line")
        }
        delay(100)
        child.cancel()
    }
    delay(100)
    job.join()
}

/**БЛОК 2*/
//ЗАДАНИЕ 1
fun main3() {
    with(CoroutineScope(EmptyCoroutineContext)) {
        try {
            launch {
                throw Exception("Line 1")
            }
        } catch (e: Exception) {
            e.printStackTrace() // <-- NO, отрабатывает карутина (54)
        }
    }
    Thread.sleep(1000)
}

//ЗАДАНИЕ 2
fun main4() {
    CoroutineScope(EmptyCoroutineContext).launch {
        try {
            coroutineScope {
                throw Exception("Line 1")
            }
        } catch (e: Exception) {
            e.printStackTrace() // <-- NO, отрабатывает карутина (68)
        }
    }
    Thread.sleep(1000)
}

//ЗАДАНИЕ 3
fun main5() {
    CoroutineScope(EmptyCoroutineContext).launch {
        try {
            supervisorScope {
                throw Exception("Line 1")
            }
        } catch (e: Exception) {
            e.printStackTrace() // <-- NO, отрабатывает карутина (82)
        }
    }
    Thread.sleep(1000)
}

fun main6() {
    CoroutineScope(EmptyCoroutineContext).launch {
        try {
            coroutineScope {
                launch {
                    delay(500)
                    throw Exception("Line 1") // <-- NO, тк мы ждём (96), первой успевает отработать карутина (100) и выкинуть ошибку
                }
                launch {
                    throw Exception("Line 2")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    Thread.sleep(1000)
}

fun main7() {
    CoroutineScope(EmptyCoroutineContext).launch {
        try {
            supervisorScope {
                launch {
                    delay(500)
                    throw Exception("1 Line") // <-- YES, дожидаемся завершения карутины
                }
                launch {
                    throw Exception("2 Line")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace() // <--
        }
    }
    Thread.sleep(1000)
}

fun main9() {
    CoroutineScope(EmptyCoroutineContext).launch {
        CoroutineScope(EmptyCoroutineContext).launch {
            launch {
                delay(1000)
                println("Line 1") // <-- NO, выкинули исключение(140) до завершения карутины
            }
            launch {
                delay(500)
                println("Line 2")
            }
            throw Exception("something bad happened")
        }
    }
    Thread.sleep(1000)
}

fun main() {
    CoroutineScope(EmptyCoroutineContext).launch {
        CoroutineScope(EmptyCoroutineContext + SupervisorJob()).launch {
            launch {
                delay(1000)
                println("Line 1") // <-- NO, выкидываем исключение(157) до завершения карутин
            }
            launch {
                delay(500)
                println("Line 2")
            }
            throw Exception("something bad happened")
        }
    }
    Thread.sleep(1000)
}