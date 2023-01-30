package br.com.arcom.s3a.ui.commons.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import br.com.arcom.s3a.R

@Composable
fun BotaoVoltarParaIVendas(context: Context, text: String) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                try {
                    val intentIvendas =
                        context.packageManager.getLaunchIntentForPackage("br.com.arcom.ivendasx")
                    context.startActivity(intentIvendas)
                } catch (e: Exception) {
                    Toast
                        .makeText(context, "Erro ao ir para o IVendas", Toast.LENGTH_LONG)
                        .show()
                }
            },
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 4.dp)
                .fillMaxWidth()
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_logo_ivendas),
                contentDescription = "",
                modifier = Modifier.padding(end = 8.dp)
            )

            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}