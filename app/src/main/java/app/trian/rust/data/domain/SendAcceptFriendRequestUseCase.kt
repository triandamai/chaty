package app.trian.rust.data.domain

import app.trian.rust.data.Response
import app.trian.rust.data.dataSource.local.entity.ChatAttachment
import app.trian.rust.data.dataSource.local.entity.ChatWithDetail
import app.trian.rust.data.repositories.ChatRepository
import app.trian.rust.data.repositories.UserRepository
import app.trian.rust.model.UserModel
import javax.inject.Inject

class SendAcceptFriendRequestUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        userIdTarget:String
    ): Response<UserModel> {

        return userRepository.acceptFriendListRequest(userIdTarget)
    }
}
