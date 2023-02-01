package br.com.arcom.s3a.ui.checagem.navigation

import android.net.Uri
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.*
import androidx.navigation.compose.composable
import br.com.arcom.s3a.data.model.TipoChecagem
import br.com.arcom.s3a.ui.checagem.ChecagemRoute
import br.com.arcom.s3a.ui.checagem.navigation.ChecagemDestination.tipoChecagem
import br.com.arcom.s3a.ui.navigation.S3ANavigationDestination

object ChecagemDestination : S3ANavigationDestination {
    const val tipoChecagem = "tipoChecagem"
    override val route = "checagem_route/{$tipoChecagem}"
    override val destination = "checagem_destination"
}

fun NavController.navigateToChecagem(tipoChecagem: TipoChecagem) {
    val encodedId = Uri.encode(tipoChecagem.id.toString())
    this.navigate("checagem_route/$encodedId")
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.checagem(
    onBackClick: () -> Unit,
) {
    composable(
        route = ChecagemDestination.route,
        arguments = listOf(
            navArgument(tipoChecagem) { type = NavType.StringType }
        )
    ) {
        ChecagemRoute(
            onBackClick,
        )
    }
}