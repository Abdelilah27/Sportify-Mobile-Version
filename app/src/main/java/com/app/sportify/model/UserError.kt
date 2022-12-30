package com.app.sportify.model

import com.app.sportify.R

data class UserError(
    var usernameError: Int = R.string.empty,
    var passwordError: Int = R.string.empty,
    var confirmedPasswordError: Int = R.string.empty,
    var appRoleError: Int = R.string.empty,
) {
}