package com.francotte.android.weatherapp.ui.pager_screen

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class PagerRoute(val index:Int)

fun NavController.navigateToPager(index: Int, navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = PagerRoute(index)) {
        Log.d("debug_navigateToPager", index.toString())
        navOptions()
    }
}

fun NavGraphBuilder.pagerScreen(onNavigationClick:() -> Unit) {
    composable<PagerRoute> {
        PagerScreen(onNavigationClick = onNavigationClick)
    }
}