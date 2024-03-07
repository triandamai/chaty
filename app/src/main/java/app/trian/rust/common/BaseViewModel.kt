package app.trian.rust.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

abstract class BaseViewModel<State,Impl:ImplClass<State>, Action, Effect>(
    private val implClass: Impl,
) : ViewModel() {
    private val _effect: Channel<Either<Effect>> = Channel(Channel.BUFFERED)
    val onEffect = _effect.receiveAsFlow()

    private val _uiState: MutableStateFlow<State> = MutableStateFlow(implClass.default())
    val uiState = _uiState.asStateFlow()

    private val action: ConcurrentHashMap<String, Action.(Impl) -> Unit> =
        ConcurrentHashMap()

    val instance: BaseViewModel<State,Impl, Action, Effect>
        get() = this

    init {
        this.onAction()
        this.implClass.setCb(object : ImplClass.OnImplListener<State> {
            override fun invalidate(state: State) {
                _uiState.tryEmit(state)
            }

            override fun currentState(): State = _uiState.value
        })
    }

    abstract fun onAction()


    @Suppress("UNCHECKED_CAST")
    fun <T : Action> on(name: Class<T>, cb: T.(Impl) -> Unit) {
        if (this::class.java.simpleName.isNotEmpty()) {
            action[name.simpleName] = cb as Action.(Impl) -> Unit
        }
    }

    fun emit(cb: Impl.() -> Unit) {
        cb(implClass)
    }

    fun invokeAction(act: Action) {
        action[act!!::class.java.simpleName.orEmpty()]?.invoke(act,implClass)
    }

    protected fun sendEffect(effect: Effect) = viewModelScope.launch {
        _effect.send(Either.right(effect))
        delay(100)
        _effect.send(Either.left())

    }

    infix fun BaseViewModel<State,Impl,Action,Effect>.send(effect: Effect) {
            sendEffect(effect)
    }
}

