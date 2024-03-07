package app.trian.rust.data.domain

import app.trian.rust.data.Response
import app.trian.rust.data.dataSource.local.entity.Friend
import app.trian.rust.data.dataSource.local.entity.Room
import app.trian.rust.data.repositories.RoomRepository
import app.trian.rust.data.repositories.UserRepository
import app.trian.rust.model.RoomModel
import app.trian.rust.model.UserModel
import javax.inject.Inject

class InsertFriendUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(friend: UserModel): Response<Friend> {
        return userRepository.insertFriend(friend)
    }
}