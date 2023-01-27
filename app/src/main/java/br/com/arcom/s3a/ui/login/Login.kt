package br.com.arcom.s3a.ui.login

import android.content.ContentValues
import android.telecom.Call
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
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.arcom.s3a.data.model.Cronograma
import br.com.arcom.s3a.data.model.LoginRequest
import br.com.arcom.s3a.data.model.LoginResponse
import com.google.gson.Gson
import retrofit2.Response
import javax.security.auth.callback.Callback

@ExperimentalMaterial3Api
@Composable
fun LoginRoute(
    onBackClick: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {

    LoginScreen(
        onBackClick = onBackClick,

    )
}

@ExperimentalMaterial3Api
@Composable
fun LoginScreen(
    onBackClick: () -> Unit,

) {
    var usuario by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Text(
                text = "S3A",
                modifier = Modifier,
                fontWeight = FontWeight.Black,
                fontSize = 48.sp,
                color = MaterialTheme.colorScheme.primary,
            )
            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
            )
            Button(
                onClick = {
//                    handleLogin(usuario, senha)
                          },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .requiredHeight(56.dp)
                ,
            ) {
                Text(
                    text = "Submit")
            }
        }
    }
}

//fun handleLogin(usuario: String, senha: String) {
//    val loginRequest = LoginRequest(usuario, senha)
//    val call = loginAPI.login(loginRequest)
//    call.enqueue(object : Callback {
//        override fun onResponse(call: Call, response: Response<LoginResponse>) {
//            if (response.isSuccessful) {
//                val loginResponse = response.body()
//                inicializaSessao(usuario)
//            } else {
//                // TODO: implementar handleError
//                inicializaSessao(usuario)
//            }
//        }
//        override fun onFailure(call: Call t: Throwable) {
//            // TODO: implementar onFailure
//            inicializaSessao(usuario)
//        }
//    })
//}

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