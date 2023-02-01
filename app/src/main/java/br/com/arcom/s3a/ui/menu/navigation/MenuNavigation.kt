package br.com.arcom.s3a.ui.menu.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.arcom.s3a.data.model.TipoChecagem
import br.com.arcom.s3a.ui.menu.MenuRoute
import br.com.arcom.s3a.ui.navigation.S3ANavigationDestination

object MenuDestination : S3ANavigationDestination {
    override val route = "menu_route"
    override val destination = "menu_destination"
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.menu(
    onBackClick: () -> Unit,
    navigateToChecagem: (TipoChecagem) -> Unit,
) {
    composable(
        route = MenuDestination.route,
    ) {
        MenuRoute(onBackClick,
            navigateToChecagem = navigateToChecagem)
    }
}