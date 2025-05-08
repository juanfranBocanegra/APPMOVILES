package com.app.dolt.ui.login

class UnauthorizedLoginException(
    message: String = "Not authorized"
) : Exception(message)