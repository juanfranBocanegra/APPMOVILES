import android.content.SharedPreferences
import com.app.dolt.api.ApiService
import com.app.dolt.model.LoginRequest

class LoginRepository(private val apiService: ApiService, private val sharedPreferences: SharedPreferences) {

    suspend fun login(username: String, password: String): Boolean {
        val response = apiService.login(LoginRequest(username, password))

        if (response.isSuccessful) {
            // Si la respuesta es exitosa, guardamos el token en SharedPreferences
            response.body()?.access?.let { token ->
                saveToken(token)
            }
            return true
        }
        return false
    }

    private fun saveToken(token: String) {
        sharedPreferences.edit().putString("access_token", token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }
}
