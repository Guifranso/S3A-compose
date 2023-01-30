package br.com.arcom.s3a.ui.checagem.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.arcom.s3a.ui.checagem.ChecagemRoute
import br.com.arcom.s3a.ui.navigation.S3ANavigationDestination

object ChecagemDestination : S3ANavigationDestination {
    override val route = "checagem_route"
    override val destination = "checagem_destination"
}

fun NavController.navigateToChecagem(){
    this.navigate(ChecagemDestination.route)
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.checagem(
    onBackClick: () -> Unit,
) {
    composable(
        route = ChecagemDestination.route,
    ) {
        ChecagemRoute(
            onBackClick,
        )
    }
}