package br.com.arcom.s3a.ui.app

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import br.com.arcom.s3a.ui.navigation.S3ANavHost
import br.com.arcom.s3a.ui.theme.S3aTheme
import kotlinx.coroutines.launch


@OptIn(
    ExperimentalLayoutApi::class,
    ExperimentalComposeUiApi::class, ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class
)
@Composable
fun S3AApp(
    appState: S3AAppState = rememberS3AAppState()
) {
    val state = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    S3aTheme {
        Scaffold(
            scaffoldState = state,
            modifier = Modifier
                .semantics { testTagsAsResourceId = true }
                .navigationBarsPadding(),
            backgroundColor = MaterialTheme.colorScheme.primary,
            drawerContent = {  },
            drawerScrimColor = MaterialTheme.colorScheme.primary.copy(.7f),
            drawerBackgroundColor = MaterialTheme.colorScheme.primary,
            drawerGesturesEnabled = state.drawerState.isOpen,
        ) { padding ->
            S3ANavHost(
                navController = appState.navController,
                onNavigateToDestination = appState::navigate,
                currentDestination = appState.currentDestination,
                onBackClick = appState::onBackClick,
                openDrawer = { scope.launch { state.drawerState.open() } },
                modifier = Modifier
                    .padding(padding)
                    .consumedWindowInsets(padding)
                    .navigationBarsPadding()
            )
        }
    }
}