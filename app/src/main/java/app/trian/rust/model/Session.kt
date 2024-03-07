package app.trian.rust.model

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

data class Session(
    val userId: String,
    val profilePicture: String,
    private val sharedPreferences: SharedPreferences,
) {

    companion object {
        fun from(sharedPreferences: SharedPreferences): Session {
            val userId = sharedPreferences.getString("userId", "").orEmpty()
            val profilePicture = sharedPreferences.getString("profilePicture", "").orEmpty()

            return Session(userId, profilePicture, sharedPreferences)
        }
    }

    fun saveSession(userId: String, profilePicture: String) {
        sharedPreferences.edit {
            putString("userId", userId)
            putString("profilePicture", profilePicture)
            apply()
        }
    }

    fun getCurrentUser():Session{
        val userId = sharedPreferences.getString("userId", "").orEmpty()
        val profilePicture = sharedPreferences.getString("profilePicture", "").orEmpty()

        return Session(userId, profilePicture, sharedPreferences)
    }
}

