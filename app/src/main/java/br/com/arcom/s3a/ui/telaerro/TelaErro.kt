package br.com.arcom.s3a.ui.telaerro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.arcom.s3a.R
import br.com.arcom.s3a.ui.commons.components.BotaoVoltarParaIVendas

@Composable
fun TelaErro() {

    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        item {
            Text(
                text = stringResource(id = R.string.msg_tela_erro_1),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 8.dp, bottom = 16.dp),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Image(
                painter = painterResource(id = R.drawable.imagem_tela_de_erro),
                contentDescription = "Imagem Erro",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(350.dp)
            )

            Text(
                text = stringResource(id = R.string.msg_tela_erro_2),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 16.dp, bottom = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground

            )

            Text(
                text = stringResource(id = R.string.msg_tela_erro_3),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground

            )

            Text(
                text = stringResource(id = R.string.msg_tela_erro_4),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground

            )
        }

        item {
            Row(Modifier.fillMaxWidth(0.8f)) {
                BotaoVoltarParaIVendas(context, stringResource(id = R.string.voltar_para_o_ivendas))
            }
        }
    }

}