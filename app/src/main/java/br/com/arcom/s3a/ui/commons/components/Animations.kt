package br.com.arcom.s3a.ui.commons.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.arcom.s3a.R
import com.airbnb.lottie.compose.*

@Composable
fun Animation(
    modifier: Modifier = Modifier, rawDrawable: Int, startAnimation: Boolean,
    functionAfterAnimationComplete: (() -> Unit)? = null
) {
    var speed by remember {
        mutableStateOf(2f)
    }
    val composition by rememberLottieComposition(
        LottieCompositionSpec
            .RawRes(rawDrawable)
    )

    val progress by animateLottieCompositionAsState(
        composition,
        isPlaying = startAnimation,
        speed = speed,
        restartOnPlay = false
    )

    LottieAnimation(
        composition,
        progress,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
    if (progress == 1f) {
        if (functionAfterAnimationComplete != null) {
            functionAfterAnimationComplete()
        }
    }
}

@Composable
fun InfiniteAnimation(modifier: Modifier = Modifier, rawDrawable: Int, startAnimation: Boolean) {
    var speed by remember {
        mutableStateOf(1f)
    }
    val composition by rememberLottieComposition(
        LottieCompositionSpec
            .RawRes(rawDrawable)
    )

    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = startAnimation,
        speed = speed,
        restartOnPlay = false

    )

    LottieAnimation(
        composition,
        progress,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}

@Composable
fun AnimationNaoEncontrado(
    modifier: Modifier,
    text: String = stringResource(id = R.string.voce_nao_possui_dados),
    size: Dp = 200.dp
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        InfiniteAnimation(
            modifier = Modifier
                .requiredSize(size),
            R.raw.not_found, true
        )
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun AnimationError(
    modifier: Modifier,
    text: String = stringResource(id = R.string.erro_ao_buscar_dados),
    size: Dp = 200.dp
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        InfiniteAnimation(
            modifier = Modifier
                .requiredSize(size),
            R.raw.error, true
        )
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    size: Dp = 56.dp
) {
    Box(modifier = modifier.requiredSize(size)) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primary
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Logo app",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .requiredSize(size / 2)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun EnvioAnimationSucessSheet(
    starAnimation: Boolean,
    funcaoSucesso: () -> Unit,
) {

    val speed by remember {
        mutableStateOf(2f)
    }
    val composition by rememberLottieComposition(
        LottieCompositionSpec
            .RawRes(R.raw.check)
    )

    val progress by animateLottieCompositionAsState(
        composition,
        isPlaying = starAnimation,
        speed = speed,
        restartOnPlay = false
    )

    Box(
        Modifier.fillMaxSize()
    ) {
        LottieAnimation(
            composition,
            progress,
            modifier = Modifier
                .fillMaxSize(0.5f)
                .align(Alignment.Center),
            contentScale = ContentScale.Crop
        )
    }

    if (progress == 1f) {
        funcaoSucesso()
    }
}