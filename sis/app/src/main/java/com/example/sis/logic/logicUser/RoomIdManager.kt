import android.content.Context

class RoomIdManager(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUserId(userId: String) {
        sharedPreferences.edit().putString("room_id", userId).apply()
    }

    fun getUserId(): String? {
        return sharedPreferences.getString("room_id", null)
    }

    fun removeUserId() {
        sharedPreferences.edit().remove("room_id").apply()
    }
}
