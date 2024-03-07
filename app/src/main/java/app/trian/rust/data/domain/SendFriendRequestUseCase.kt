package app.trian.rust.data.domain

import app.trian.rust.data.Response
import app.trian.rust.data.repositories.UserRepository
import app.trian.rust.model.FriendRequestModel
import app.trian.rust.model.UserModel
import javax.inject.Inject

class SendFriendRequestUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        userModel: FriendRequestModel?,
    ): Response<FriendRequestModel> {
        if (userModel == null) return Response.error()
        return userRepository.addToFriendListRequest(userModel.userId, userModel.message)
    }
}