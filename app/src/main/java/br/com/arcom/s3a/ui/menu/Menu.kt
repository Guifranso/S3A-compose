package br.com.arcom.s3a.ui.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.arcom.s3a.R
import br.com.arcom.s3a.ui.commons.components.AnimationError
import br.com.arcom.s3a.ui.commons.components.LoadingAnimation

@ExperimentalMaterial3Api
@Composable
fun MenuRoute(
    onBackClick: () -> Unit,
    viewModel: MenuViewModel = hiltViewModel(),
    navigateToChecagem: () -> Unit,
) {
    val cronogramaUiState = viewModel.checagensUiState.collectAsStateWithLifecycle()

    MenuScreen(
        onBackClick = onBackClick,
        checagensUiState = cronogramaUiState.value,
        refresh = viewModel::updateChecagens,
        navigateToChecagem = navigateToChecagem
    )
}

@ExperimentalMaterial3Api
@Composable
fun MenuScreen(
    onBackClick: () -> Unit,
    checagensUiState: ChecagensUiState,
    refresh: () -> Unit,
    navigateToChecagem: () -> Unit,
) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.menu)) },
                actions = {
                    IconButton(onClick = { refresh() }) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            when (checagensUiState) {
                is ChecagensUiState.Success -> {
                    val checagens = checagensUiState.checagens
                    item {
                        Text(
                            style = MaterialTheme.typography.titleLarge,
                            text = "S3A",
                            modifier = Modifier.padding(bottom = 48.dp),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Button(
                            onClick = { navigateToChecagem() },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .padding(top = 24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            enabled = checagens.checagemInicial
                        ) {
                            Text(
                                style = MaterialTheme.typography.bodyMedium,
                                text = "Checagem inicial"
                            )
                        }
                        Button(
                            onClick = { navigateToChecagem() },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .padding(top = 24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ), enabled = checagensUiState.checagens.checagemFinal
                        ) {
                            Text(
                                style = MaterialTheme.typography.bodyMedium,
                                text = "Checagem final"
                            )
                        }
                    }
                }
                is ChecagensUiState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            LoadingAnimation(modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }
                is ChecagensUiState.Error -> {
                    item {
                        AnimationError(
                            modifier = Modifier
                                .fillMaxWidth(),
                            size = 80.dp
                        )
                    }
                }
            }

        }
    }
}
