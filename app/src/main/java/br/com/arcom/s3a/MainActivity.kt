package br.com.arcom.s3a

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.arcom.s3a.auth.AuthStateEnum
import br.com.arcom.s3a.ui.app.S3aApp
import br.com.arcom.s3a.ui.telaerro.TelaErro
import br.com.arcom.s3a.ui.theme.S3aTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppActivity() {

    private lateinit var viewModel: MainActivityViewModel

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        setContent {
            RepPlusContent(calculateWindowSizeClass(this))
        }
    }

    override fun handleIntent(intent: Intent) {
        val uri = intent.data
        if (uri != null) {
            val nomeAcessor = uri.getQueryParameter("nomeAcessor")
            val idSetor = uri.getQueryParameter("idSetor")?.toLong()
            viewModel.registrarAcesso(
                nomeAcessor,
                idSetor
            )
        }
    }

    @OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class,
        ExperimentalMaterial3WindowSizeClassApi::class
    )
    @Composable
    private fun RepPlusContent(calculateWindowSizeClass: WindowSizeClass) {
        val viewState = viewModel.state.collectAsStateWithLifecycle().value

        S3aTheme() {
            Box(Modifier.statusBarsPadding()) {
                if (viewState.authState != null
                    && viewState.authState != AuthStateEnum.LOADING
                ) {
                    if (viewState.authState == AuthStateEnum.LOGGED_IN) {
                        S3aApp(calculateWindowSizeClass)
                    } else {
                        TelaErro()
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary)
                            .fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .height(250.dp)
                                .align(Alignment.Center),
                            contentAlignment = Alignment.Center
                        ) {

                            Image(
                                painter = painterResource(id = R.drawable.ic_launcher_background),
                                contentDescription = "Logo splash screen",
                                modifier = Modifier
                                    .requiredWidth(100.dp)
                                    .requiredHeight(250.dp)
                            )

                        }

                        Column(
                            Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "FROM",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White
                            )
                            Image(
                                painter = painterResource(id = R.drawable.logo_arcom),
                                contentDescription = "Logo Arcom",
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
