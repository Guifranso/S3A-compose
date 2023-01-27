package br.com.arcom.s3a.ui.menu

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@ExperimentalMaterial3Api
@Composable
fun MenuRoute(
    onBackClick: () -> Unit,
    viewModel: MenuViewModel = hiltViewModel(),
) {

    MenuScreen(
        onBackClick = onBackClick,

    )
}

@ExperimentalMaterial3Api
@Composable
fun MenuScreen(
    onBackClick: () -> Unit,

) {
    var usuario by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize(),
    ){ innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                Button(
                    onClick = {
                        Log.i(TAG, "MenuScreen: funcionou")
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .requiredHeight(56.dp),
                ) {
                    Text(
                        text = "Submit")
                }
            }
        }
    }
}

//private fun inicializaSessao(usuario: String) {
//    finish()
//    val cronograma = Cronograma.getSavedCronograma(this)
//    if(cronograma.usuario == "") {
//        cronograma.usuario = usuario
//    }
//    Log.i(ContentValues.TAG, Gson().toJson(cronograma).toString())
//    cronograma.save(this)
//
//    ContextCompat.startActivity(Intent(this, MenuActivity::class.java))
//}