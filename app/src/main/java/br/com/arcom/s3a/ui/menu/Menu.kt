package br.com.arcom.s3a.ui.menu

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.arcom.s3a.ui.commons.components.AnimationError
import br.com.arcom.s3a.ui.commons.components.LoadingAnimation

@ExperimentalMaterial3Api
@Composable
fun MenuRoute(
    onBackClick: () -> Unit,
    viewModel: MenuViewModel = hiltViewModel(),
) {
    val cronogramaUiState = viewModel.cronogramaUiState.collectAsStateWithLifecycle()

    MenuScreen(
        onBackClick = onBackClick,
        cronogramaUiState = cronogramaUiState.value
    )
}

@ExperimentalMaterial3Api
@Composable
fun MenuScreen(
    onBackClick: () -> Unit,
    cronogramaUiState: CronogramaUiState,

    ) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(32.dp)
        ) {
            when (cronogramaUiState) {
                is CronogramaUiState.Success -> {
                    item {
                        Text(
                            fontSize = 60.sp,
                            text = "S3A",
                            modifier = Modifier.padding(bottom = 48.dp)
                        )
                        Button(
                            onClick = {
                                Log.i(TAG, "MenuScreen: funcionou")
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp)
                                .requiredHeight(56.dp),
                        ) {
                            Text(
                                fontSize = 24.sp,
                                text = "Checagem inicial"
                            )
                        }
                        Button(
                            onClick = {
                                Log.i(TAG, "MenuScreen: funcionou")
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp)
                                .requiredHeight(56.dp),
                        ) {
                            Text(
                                fontSize = 24.sp,
                                text = "Checagem final"
                            )
                        }
                    }
                }
                is CronogramaUiState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            LoadingAnimation(modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }
                is CronogramaUiState.Error -> {
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
