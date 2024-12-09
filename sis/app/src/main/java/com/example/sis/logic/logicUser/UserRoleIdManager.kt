import android.content.Context

class UserRoleIdManager(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUserRoleId(userId: String) {
        sharedPreferences.edit().putString("userRole_id", userId).apply()
    }

    fun getUserRoleId(): String? {
        return sharedPreferences.getString("userRole_id", null)
    }

    fun removeUserRoleId() {
        sharedPreferences.edit().remove("userRole_id").apply()
    }
}
