package app.trian.rust.data.domain

import app.trian.rust.data.Response
import app.trian.rust.data.repositories.UserRepository
import app.trian.rust.model.UserModel
import javax.inject.Inject

class GetListFriendUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): Response<List<UserModel>> {
        return userRepository.getListFriend()
    }
}