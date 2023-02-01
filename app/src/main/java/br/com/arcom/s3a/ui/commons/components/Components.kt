package br.com.arcom.s3a.ui.commons.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.arcom.s3a.ui.theme.divider


@Composable
fun Etapas(modifier: Modifier = Modifier, steps: List<String>, currentStep: Int) {

    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

        ConstraintLayout(modifier = Modifier.fillMaxWidth(0.7f)) {
            val (etapas, linha) = createRefs()
            Divider(
                modifier = Modifier
                    .constrainAs(linha) {
                        top.linkTo(etapas.top)
                        bottom.linkTo(etapas.bottom)
                        start.linkTo(etapas.start)
                        end.linkTo(etapas.end)
                        width = Dimension.fillToConstraints
                    }
                    .padding(horizontal = 4.dp),
                color = MaterialTheme.colorScheme.onBackground.divider(),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .constrainAs(etapas) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                steps.forEachIndexed { index, s ->
                    if (index < currentStep) {
                        Box(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.background,
                                    CircleShape
                                )
                                .requiredSize(24.dp)
                                .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check, contentDescription = "Icone etapas",
                                modifier = Modifier
                                    .requiredSize(16.dp)
                                    .align(Alignment.Center),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else if (index == currentStep) {
                        Box(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.background,
                                    CircleShape
                                )
                                .requiredSize(24.dp)
                                .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Circle, contentDescription = "Icone etapas",
                                modifier = Modifier
                                    .requiredSize(16.dp)
                                    .align(Alignment.Center),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.Circle,
                            contentDescription = "Icone etapas",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.requiredSize(24.dp)
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(top = 8.dp),
            Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Top
        ) {
            steps.forEachIndexed { index, s ->
                val align = when (index) {
                    steps.lastIndex -> TextAlign.End
                    0 -> TextAlign.Start
                    else -> TextAlign.Center
                }
                val modifier = when (index) {
                    steps.lastIndex -> Modifier
                        .padding(start = 4.dp)
                    0 -> Modifier
                        .padding(end = 4.dp)
                    else -> Modifier
                        .padding(start = 2.dp)
                }

                Text(
                    text = s,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = modifier.weight(1f),
                    textAlign = align,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}