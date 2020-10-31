package com.felipefernandes.desafiousemobile.extensions

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.felipefernandes.desafiousemobile.R

private val navOptions = NavOptions.Builder()
    .setEnterAnim(R.anim.slide_in_right)
    .setExitAnim(R.anim.slide_out_left)
    .setPopEnterAnim(R.anim.slide_in_left)
    .setPopExitAnim(R.anim.slide_out_right)
    .build()

fun NavController.navigateWithAnimations(destination: Any) {
    when (destination) {
        is Int -> navigate(destination, null, navOptions)
        is NavDirections -> navigate(destination, navOptions)
    }
}
