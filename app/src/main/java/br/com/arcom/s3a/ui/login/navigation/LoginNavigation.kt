package br.com.arcom.s3a.ui.login.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.arcom.s3a.ui.login.LoginRoute
import br.com.arcom.s3a.ui.navigation.S3ANavigationDestination

object LoginDestination : S3ANavigationDestination {
    override val route = "login_route"
    override val destination = "login_destination"
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
fun NavGraphBuilder.login(
    onBackClick: () -> Unit,
) {
    composable(
        route = LoginDestination.route,
    ) {
        LoginRoute(onBackClick)
    }
}