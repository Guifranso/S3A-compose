package br.com.arcom.s3a.ui.cameraMlkit.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.arcom.s3a.ui.cameraMlkit.CameraMlkitRoute
import br.com.arcom.s3a.ui.menu.navigation.MenuDestination
import br.com.arcom.s3a.ui.navigation.S3ANavigationDestination

object CameraDestination : S3ANavigationDestination {
    override val route = "camera_route"
    override val destination = "camera_destination"
}

fun NavController.navigateToCamera(navOptions: NavOptions? = null){
    this.navigate(CameraDestination.route, navOptions)
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.camera(
    onBackClick: () -> Unit,
) {
    composable(
        route = CameraDestination.route,
    ) {
        CameraMlkitRoute(
            onBackClick,

        )
    }
}