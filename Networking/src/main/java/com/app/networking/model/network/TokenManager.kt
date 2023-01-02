package com.app.networking.model.network

import android.content.Context
import com.app.networking.model.app.response.TokenResponse
import com.app.networking.utils.ConstUtil.ACCESS_TOKEN
import com.app.networking.utils.ConstUtil.PREFS_FILE
import com.app.networking.utils.ConstUtil.REFRESH_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context: Context) {
    private var prefs = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)


    fun saveToken(tokenResponse: TokenResponse) {
        val editor = prefs.edit()
        editor.putString(ACCESS_TOKEN, tokenResponse.access_token)
        editor.putString(REFRESH_TOKEN, tokenResponse.refresh_token)
        editor.apply()
    }

    fun clearToken() {
        val editor = prefs.edit()
        editor.clear().commit()
    }

    fun getAccessToken(): String? = prefs.getString(ACCESS_TOKEN, null)
    fun getRefreshToken(): String? = prefs.getString(REFRESH_TOKEN, null)
}