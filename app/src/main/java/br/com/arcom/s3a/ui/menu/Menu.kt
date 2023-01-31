package br.com.arcom.s3a.ui.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.arcom.s3a.R
import br.com.arcom.s3a.ui.commons.components.AnimationError
import br.com.arcom.s3a.ui.commons.components.DialogInfo
import br.com.arcom.s3a.ui.commons.components.LoadingAnimation
import br.com.arcom.s3a.ui.commons.components.SwipeDismissSnackbarHost
import br.com.arcom.s3a.util.formatData

@ExperimentalMaterial3Api
@Composable
fun MenuRoute(
    onBackClick: () -> Unit,
    viewModel: MenuViewModel = hiltViewModel(),
    navigateToChecagem: () -> Unit,
) {
    val menuUiState = viewModel.menuUiState.collectAsStateWithLifecycle()

    MenuScreen(
        onBackClick = onBackClick,
        menuUiState = menuUiState.value,
        refresh = viewModel::updateChecagens,
        navigateToChecagem = navigateToChecagem,
        clearMessages = viewModel::clearMessages
    )
}

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalMaterial3Api
@Composable
fun MenuScreen(
    onBackClick: () -> Unit,
    menuUiState: MenuUiState,
    refresh: () -> Unit,
    navigateToChecagem: () -> Unit,
    clearMessages: () -> Unit,
) {

    var openHelp by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(menuUiState.loading, { })
    val snackbarHostState = remember { SnackbarHostState() }
    menuUiState.uiMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message.message)
            clearMessages()
        }
    }

    if (openHelp) {
        DialogInfo(text = "") {
            openHelp = false
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SwipeDismissSnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.background
                            )
                        )
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { refresh() }, modifier = Modifier.padding(end = 4.dp)) {
                        Icon(
                            imageVector = Icons.Default.Refresh, contentDescription = "Refresh",
                            tint = MaterialTheme.colorScheme.background
                        )
                    }
                    IconButton(onClick = { openHelp = true }) {
                        Icon(
                            imageVector = Icons.Default.Help, contentDescription = "Help",
                            tint = MaterialTheme.colorScheme.background
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_travel),
                        contentDescription = "Image travel",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (menuUiState.checagensUiState) {
                        is ChecagensUiState.Success -> {
                            @Composable
                            fun Boolean.toStyle() = if (this) {
                                MaterialTheme.typography.titleLarge
                            } else {
                                MaterialTheme.typography.titleSmall
                            }

                            val checagens = menuUiState.checagensUiState.checagens

                            Text(
                                text = stringResource(
                                    id = R.string.bem_vindo,
                                    menuUiState.authState?.nomeAcessor
                                        ?: stringResource(R.string.acessor)
                                ),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.fillMaxWidth(0.9f)
                            )

                            Surface(
                                onClick = { navigateToChecagem() },
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .padding(top = 16.dp),
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.secondary,
                                shadowElevation = 2.dp,
                                enabled = checagens.checagemInicial != null
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = stringResource(id = R.string.checagem_inicial),
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSecondary
                                        )
                                        checagens.checagemInicial?.dataHora?.let {
                                            Text(
                                                text = "Realizado: ${it.formatData("dd/MM/yyyy HH:mm")}",
                                                style = MaterialTheme.typography.titleSmall,
                                                color = MaterialTheme.colorScheme.onSecondary
                                            )
                                        }
                                        checagens.checagemInicial?.km?.let {
                                            Text(
                                                text = "$it km",
                                                style = MaterialTheme.typography.titleSmall,
                                                color = MaterialTheme.colorScheme.onSecondary
                                            )
                                        }
                                    }

                                    Icon(
                                        imageVector = if (checagens.checagemInicial != null) {
                                            Icons.Default.Check
                                        } else {
                                            Icons.Default.ArrowRight
                                        },
                                        contentDescription = "Arrow right",
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier
                                            .background(
                                                MaterialTheme.colorScheme.primary,
                                                CircleShape
                                            )
                                            .padding(12.dp)
                                    )
                                }
                            }

                            if (checagens.checagemInicial != null) {
                                Surface(
                                    onClick = { navigateToChecagem() },
                                    modifier = Modifier
                                        .fillMaxWidth(0.9f)
                                        .padding(top = 16.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    color = MaterialTheme.colorScheme.secondary,
                                    shadowElevation = 2.dp,
                                    enabled = checagens.checagemFinal == null
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = stringResource(id = R.string.checagem_final),
                                                style = MaterialTheme.typography.titleMedium,
                                                color = MaterialTheme.colorScheme.onSecondary
                                            )
                                            checagens.checagemFinal?.dataHora?.let {
                                                Text(
                                                    text = "Realizado: ${it.formatData("dd/MM/yyyy HH:mm")}",
                                                    style = MaterialTheme.typography.titleSmall,
                                                    color = MaterialTheme.colorScheme.onSecondary
                                                )
                                            }
                                            checagens.checagemFinal?.km?.let {
                                                Text(
                                                    text = "$it km",
                                                    style = MaterialTheme.typography.titleSmall,
                                                    color = MaterialTheme.colorScheme.onSecondary
                                                )
                                            }
                                        }

                                        Icon(
                                            imageVector = if (checagens.checagemFinal != null) {
                                                Icons.Default.Check
                                            } else {
                                                Icons.Default.ArrowRight
                                            },
                                            contentDescription = "Arrow right",
                                            tint = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier
                                                .background(
                                                    MaterialTheme.colorScheme.primary,
                                                    CircleShape
                                                )
                                                .padding(12.dp)
                                        )
                                    }
                                }
                            }
                        }
                        is ChecagensUiState.Loading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                LoadingAnimation(modifier = Modifier.align(Alignment.Center))
                            }
                        }
                        is ChecagensUiState.Error -> {

                            AnimationError(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                size = 80.dp
                            )

                        }
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = menuUiState.loading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}
