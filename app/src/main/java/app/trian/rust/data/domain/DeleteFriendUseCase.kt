package app.trian.rust.data.domain

import app.trian.rust.data.Response
import app.trian.rust.data.dataSource.local.entity.Room
import app.trian.rust.data.repositories.ChatRepository
import app.trian.rust.data.repositories.RoomRepository
import app.trian.rust.data.repositories.UserRepository
import app.trian.rust.model.ChatModel
import app.trian.rust.model.RoomModel
import app.trian.rust.model.UserModel
import javax.inject.Inject

class DeleteFriendUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(friend: UserModel): Response<Int> {
        val data = userRepository.deleteFriend(friend)
        return Response.success(data)
    }
}