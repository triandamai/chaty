package app.trian.rust.data.domain

import app.trian.rust.data.Response
import app.trian.rust.data.repositories.UserRepository
import javax.inject.Inject

class SendRejectFriendRequestUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        userIdTarget: String,
    ): Response<String> {
        return userRepository.rejectFriendListRequest(userIdTarget)
    }
}
