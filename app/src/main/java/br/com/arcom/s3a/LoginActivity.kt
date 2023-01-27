//package br.com.arcom.s3a
//
//import android.content.ContentValues
//import android.os.Bundle
//import android.telecom.Call
//import android.util.Log
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import br.com.arcom.s3a.ui.theme.S3aTheme
//import br.com.arcom.s3a.data.model.LoginRequest
//import br.com.arcom.s3a.data.model.LoginResponse
//import javax.security.auth.callback.Callback
//
//@ExperimentalMaterial3Api
//class LoginActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            S3aTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    LoginForm()
//                }
//            }
//        }
//    }
//}
//
//@ExperimentalMaterial3Api
//@Composable
//fun LoginForm() {
//    var usuario by remember { mutableStateOf("") }
//    var senha by remember { mutableStateOf("") }
//
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//        modifier = Modifier.padding(16.dp)
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ){
//            Text(
//                text = "S3A",
//                modifier = Modifier,
//                fontWeight = FontWeight.Black,
//                fontSize = 48.sp,
//                color = MaterialTheme.colorScheme.primary,
//            )
//            OutlinedTextField(
//                value = usuario,
//                onValueChange = { usuario = it },
//                label = { Text("Email") },
//                modifier = Modifier.fillMaxWidth()
//            )
//            OutlinedTextField(
//                value = senha,
//                onValueChange = { senha = it },
//                label = { Text("Password") },
//                modifier = Modifier.fillMaxWidth(),
//                visualTransformation = PasswordVisualTransformation(),
//            )
//            Button(
//                onClick = { handleLogin(usuario, senha) },
//                shape = RoundedCornerShape(8.dp),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 8.dp)
//                    .requiredHeight(56.dp)
//                ,
//            ) {
//                Text(
//                    text = "Submit")
//            }
//        }
//    }
//}
//
//fun handleLogin(usuario: String, senha: String) {
//    val loginRequest = LoginRequest(usuario, senha)
//    val call = loginAPI.login(loginRequest)
//    call.enqueue(object : Callback<LoginResponse> {
//        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
//            if (response.isSuccessful) {
//                val loginResponse = response.body()
//                inicializaSessao(usuario)
//            } else {
//                // TODO: implementar handleError
//                inicializaSessao(usuario)
//            }
//        }
//        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//            // TODO: implementar onFailure
//            inicializaSessao(usuario)
//        }
//    })
//}
//
//private fun inicializaSessao(usuario: String) {
//    finish()
//    val cronograma = Cronograma.getSavedCronograma(this)
//    if(cronograma.usuario == "") {
//        cronograma.usuario = usuario
//    }
//    Log.i(ContentValues.TAG, Gson().toJson(cronograma).toString())
//    cronograma.save(this)
//
//    startActivity(Intent(this, MenuActivity::class.java))
//}
//
//@Composable
//fun Greeting(name: String) {
//    Text(text = "Hello $name!")
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    S3aTheme {
//        Greeting("Android")
//    }
//}