package app.trian.rust.common

import app.trian.rust.data.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

inline fun <reified T> executeAsFlow(crossinline cb: suspend () -> Response<T>) = flow {
        try {
            emit(cb.invoke())
        } catch (e: Exception) {
            emit(Response.error(e.message))
        }
    }.flowOn(Dispatchers.IO)
