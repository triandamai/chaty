package app.trian.rust.data


sealed class Response<out R> {
    data class Result<out Result>(val data: Result) : Response<Result>()
    data class Error(val message: String = "", val code: Int = 0) : Response<Nothing>()

    companion object {
        fun error(): Response<Nothing> = Error("", 0)
        fun error(message: String?): Response<Nothing> = Error(message.orEmpty(), 0)

        inline fun <reified T> success(data: T): Response<T> = Result(data)
    }
}
