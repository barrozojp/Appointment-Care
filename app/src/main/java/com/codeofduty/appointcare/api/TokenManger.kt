package com.codeofduty.appointcare.api

object TokenManger {

    private var authToken: String? = null

    fun setToken(token: String) {
        authToken = token
    }

    fun getToken(): String? {
        return authToken
    }

    fun clearToken() {
        authToken = null
    }

}