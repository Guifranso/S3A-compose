package br.com.arcom.s3a.ui.checagem


import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.arcom.s3a.R
import br.com.arcom.s3a.ui.commons.components.EnvioAnimationSucessSheet
import br.com.arcom.s3a.ui.commons.components.Etapas
import br.com.arcom.s3a.ui.commons.components.SwipeDismissSnackbarHost
import br.com.arcom.s3a.ui.theme.divider
import br.com.arcom.s3a.ui.theme.textoSecundario
import br.com.arcom.s3a.util.ImageRotationUtil
import br.com.arcom.s3a.util.getImageUri
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch
import java.io.File


@ExperimentalMaterial3Api
@Composable
fun ChecagemRoute(
    onBackClick: () -> Unit,
    viewModel: ChecagemViewModel = hiltViewModel(),
) {
    val checagemUiState = viewModel.checagemUiState.collectAsStateWithLifecycle()
    val fields = viewModel.fieldsState.collectAsStateWithLifecycle()

    ChecagemScreen(
        onBackClick = onBackClick,
        recognizeText = viewModel::recognizeText,
        checagemUiState = checagemUiState.value,
        sendChecagemFoto = viewModel::sendChecagemFoto,
        fields = fields.value,
        validarEtapa = viewModel::validarEtapas,
        emitMessage = viewModel::emitMessage,
        clearMessages = viewModel::clearMessages
    )
}

@OptIn(
    ExperimentalPermissionsApi::class, ExperimentalPagerApi::class,
    ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class
)
@ExperimentalMaterial3Api
@Composable
fun ChecagemScreen(
    onBackClick: () -> Unit,
    fields: ChecagemFields,
    checagemUiState: ChecagemUiState,
    recognizeText: (Bitmap) -> Unit,
    sendChecagemFoto: (() -> Unit) -> Unit,
    validarEtapa: (ChecagemFields) -> Boolean,
    emitMessage: (String) -> Unit,
    clearMessages: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var file by remember { mutableStateOf<Pair<File, Uri>?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val pagerState = rememberPagerState(0)
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState =
        BottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )
    var startAnimation by remember { mutableStateOf(false) }
    val listaEtapas = fields.etapas.map { stringResource(id = it.title) }
    val pullRefreshState = rememberPullRefreshState(checagemUiState.loading, { })
    val snackbarHostState = remember { SnackbarHostState() }

    checagemUiState.uiMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message.message)
            clearMessages()
        }
    }

    BackHandler(true) {
        if (pagerState.currentPage == 0) {
            onBackClick()
        } else {
            scope.launch {
                pagerState.animateScrollToPage(
                    pagerState.currentPage - 1
                )
                fields.currentIndex--
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (file != null && success) {
                val bitmap = ImageRotationUtil.rotateAndCompressImage(file!!.first)
                recognizeText(bitmap)
            }
        }
    )

    fun startCamera() {
        scope.launch {
            file = getImageUri(context)
            cameraLauncher.launch(file!!.second)
        }
    }

    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.checagem)) },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Icone voltar"
                        )
                    }
                }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        sheetContent = {
            EnvioAnimationSucessSheet(
                starAnimation = startAnimation,
                funcaoSucesso = { onBackClick() }
            )
        },
        sheetPeekHeight = 0.dp,
        scaffoldState = sheetState,
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        sheetGesturesEnabled = false,
        backgroundColor = MaterialTheme.colorScheme.background,
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

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                item {
                    Etapas(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp), listaEtapas, pagerState.currentPage
                    )
                    Divider(color = MaterialTheme.colorScheme.onBackground.divider())
                }

                item {
                    HorizontalPager(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        count = 2,
                        state = pagerState,
                        userScrollEnabled = false,
                        verticalAlignment = Alignment.Top
                    ) { page ->
                        when (page) {
                            0 -> {
                                QuilometragemContent(
                                    checagemUiState.tipoChecagem!!,
                                    fields
                                ) { keyboardController?.hide() }
                            }
                            1 -> {
                                FotoContent(checagemUiState.tipoChecagem!!, fields) {
                                    startCamera()
                                }
                            }
                        }
                    }
                }
                item {
                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(top = 12.dp)
                    ) {

                        val (anterior, indicator, proxima) = createRefs()
                        val validacao = validarEtapa(fields)

                        AnimatedVisibility(
                            fields.etapas.first { it.index == fields.currentIndex }.showPrevious,
                            modifier = Modifier
                                .constrainAs(anterior) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start)
                                },
                            enter = slideInHorizontally(animationSpec = tween(100)),
                            exit = slideOutHorizontally(animationSpec = tween(100))
                        ) {
                            Button(
                                onClick = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(
                                            pagerState.currentPage - 1
                                        )
                                    }
                                    fields.currentIndex--
                                    keyboardController?.hide()
                                }, colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                )
                            ) {
                                Text(text = stringResource(id = R.string.anterior))
                            }
                        }

                        HorizontalPagerIndicator(
                            pagerState = pagerState, modifier = Modifier
                                .constrainAs(indicator) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    end.linkTo(parent.end)
                                    start.linkTo(parent.start)
                                },
                            activeColor = MaterialTheme.colorScheme.primary,
                            inactiveColor = MaterialTheme.colorScheme.onBackground.textoSecundario()
                        )

                        AnimatedVisibility(
                            fields.etapas.first { it.index == fields.currentIndex }.showDone,
                            modifier = Modifier
                                .constrainAs(proxima) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    end.linkTo(parent.end)
                                },
                            enter = slideInHorizontally(animationSpec = tween(100)) { it / 2 },
                            exit = slideOutHorizontally(animationSpec = tween(100)) { it / 2 }
                        ) {
                            Button(
                                onClick = {
                                    if (fields.etapas.lastIndex == fields.currentIndex) {
                                        if (fields.validado) {
                                            sendChecagemFoto {
                                                scope.launch { sheetState.bottomSheetState.expand() }
                                                startAnimation = !startAnimation
                                            }
                                        } else {
                                            emitMessage("Foto não validada!")
                                        }
                                    } else {
                                        if (validacao) {
                                            scope.launch {
                                                pagerState.animateScrollToPage(
                                                    pagerState.currentPage + 1
                                                )
                                            }
                                            fields.currentIndex++
                                            keyboardController?.hide()
                                        } else {
                                            emitMessage("Preencha todos os campos!")
                                        }
                                    }
                                }, colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            ) {
                                Text(
                                    text = if (fields.etapas.lastIndex == fields.currentIndex) {
                                        stringResource(id = R.string.enviar)
                                    } else {
                                        stringResource(id = R.string.proximo)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = checagemUiState.loading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}


