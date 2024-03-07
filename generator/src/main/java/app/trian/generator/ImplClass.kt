package app.trian.generator

abstract class ImplClass<T> {
    abstract fun default(): T
    abstract fun setCb(listener: OnImplListener<T>)

    interface OnImplListener<T> {
        fun currentState(): T
        fun update(state: T)
    }
}