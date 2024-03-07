package app.trian.rust.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect


@Suppress("UNCHECKED_CAST")
@Composable
inline fun <T> On(type: Class<T>, effect: Either<Any>, crossinline cb: T.() -> Unit) {
    LaunchedEffect(key1 = effect, block = {
        when(effect){
            Either.Left -> Unit
            is Either.Right -> {
                if(effect.effect::class.java == type){
                    cb(effect.effect as T)
                }
            }
        }
    })
}

sealed class Either<out R> {
    data object Left : Either<Nothing>()

    data class Right<out T>(val effect: T) : Either<T>()

    companion object {
        fun left(): Either<Nothing> = Left
        fun <T> right(effect: T) = Right(effect)
    }
}