package app.trian.rust.data.domain

import app.trian.rust.data.Response
import app.trian.rust.data.dataSource.local.entity.ChatWithDetail
import app.trian.rust.data.repositories.ChatRepository
import javax.inject.Inject

class GetChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(chatId:String): Response<ChatWithDetail> {
        return chatRepository.getChatWithDetail(chatId)
    }
}
