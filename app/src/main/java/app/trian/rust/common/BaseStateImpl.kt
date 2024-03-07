package app.trian.rust.common


interface ImplClass<T> {
    fun default(): T
    fun setCb(listener: OnImplListener<T>)

    interface OnImplListener<T> {
        fun currentState(): T
        fun invalidate(state: T)
    }

}