package br.com.arcom.s3a.ui.navigation

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

data class TopLevelDestination(
    override val route: String,
    override val destination: String,
    val selectedIcon: Icon,
    val unselectedIcon: Icon,
    val iconTextId: Int
) : S3ANavigationDestination

sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : Icon()
}