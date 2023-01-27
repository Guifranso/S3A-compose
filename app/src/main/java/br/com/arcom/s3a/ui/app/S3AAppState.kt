package br.com.arcom.s3a.ui.app

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Login
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.tracing.trace
import br.com.arcom.s3a.R
import br.com.arcom.s3a.ui.login.navigation.LoginDestination
import br.com.arcom.s3a.ui.navigation.Icon
import br.com.arcom.s3a.ui.navigation.S3ANavigationDestination
import br.com.arcom.s3a.ui.navigation.TopLevelDestination
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun rememberS3AAppState(
    navController: NavHostController = rememberAnimatedNavController()
): S3AAppState {

    return remember(navController) {
        S3AAppState(navController)
    }
}

@Stable
class S3AAppState(
    val navController: NavHostController
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination


    val topLevelDestinations: List<TopLevelDestination> = listOf(
        TopLevelDestination(
            LoginDestination.route,
            LoginDestination.destination,
            Icon.ImageVectorIcon(Icons.Rounded.Login),
            Icon.ImageVectorIcon(Icons.Rounded.Login),
            R.string.login_title
        )
    )

    fun navigate(destination: S3ANavigationDestination, route: String? = null) {
        trace("Navigation: $destination") {
            if (destination is TopLevelDestination) {
                navController.navigate(route ?: destination.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            } else {
                navController.navigate(route ?: destination.route)
            }
        }
    }

    fun onBackClick() {
        navController.popBackStack()
    }
}