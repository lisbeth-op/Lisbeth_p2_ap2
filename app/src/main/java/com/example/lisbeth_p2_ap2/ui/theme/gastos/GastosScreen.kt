package com.example.lisbeth_p2_ap2.ui.theme.gastos

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.lisbeth_p2_ap2.data.remote.dto.GastoDto
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GastosScreen(viewModel: GastoViewModel = hiltViewModel()) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.isMessageShownFlow.collectLatest {
            if (it) {
                snackbarHostState.showSnackbar(
                    message = viewModel.mensaje,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Gastos") }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(8.dp)

        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                item {
                    RegistroGasto(ViewModel = viewModel)

                }

                when {
                    uiState.isLoading -> {
                        item{
                            Box(
                                contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
                            ) {
                                LinearProgressIndicator()
                            }
                        }
                    }

                    uiState.Gastos.isNotEmpty() -> {
                        items(uiState.Gastos){gasto->
                            CardGastos(gasto,viewModel)
                        }
                    }
                    !uiState.error.isNullOrBlank()-> {
                        item{
                            Text(text = "error:"+ uiState.error)
                        }

                    }
                }



            }

        }
    }

}


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RegistroGasto(
    ViewModel: GastoViewModel
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    // Columna principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            CustomOutlinedTextField("Fecha", ViewModel.fecha, ViewModel::onFechaChange, KeyboardType.Text)
            CustomOutlinedTextField("Id Suplidor", ViewModel.idSuplidor, ViewModel::onIdSuplidorChange, KeyboardType.Number)
            CustomOutlinedTextField("Concepto", ViewModel.concepto, ViewModel::onConceptoChange, KeyboardType.Text)
            CustomOutlinedTextField("Ncf", ViewModel.ncf, ViewModel::onNcfChange, KeyboardType.Text)
            CustomOutlinedTextField("Itbis", ViewModel.itbis, ViewModel::onItbisChange, KeyboardType.Number)
            CustomOutlinedTextField("Monto", ViewModel.monto, ViewModel::onMontoChange, KeyboardType.Number)
        }

        // Botón de guardar
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            onClick = {
                keyboardController?.hide()
                ViewModel.GuardarGasto()
            }
        ) {
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "guardar")
            Text(text = "Guardar")
        }
    }
}


@Composable
fun CustomOutlinedTextField(label: String, value: String, onValueChange: (String) -> Unit, keyboardType: KeyboardType) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        label = { Text(text = label) },
        singleLine = true,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
            keyboardType = keyboardType
        )
    )
}




@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CardGastos(
    gastos: GastoDto,
    viewModel:GastoViewModel
){
    val CaptuFecha = LocalDateTime.parse(gastos.fecha, DateTimeFormatter.ISO_DATE_TIME)
    val FechaBien = CaptuFecha.format(DateTimeFormatter.ISO_DATE)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = "Id: " + gastos.idGasto.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .weight(1f)

                )
                Text(
                    text =FechaBien,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    textAlign= TextAlign.End,
                    modifier = Modifier
                        .weight(1f)

                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = gastos.suplidor.toString(),
                style = MaterialTheme.typography.titleLarge,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = gastos.concepto,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Row (

            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .weight(2f)
                ) {
                    Text(
                        text = "NCF:",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign= TextAlign.End
                    )
                    Text(
                        text = "ITBS:",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign= TextAlign.End
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .weight(4f)
                ) {
                    Text(
                        text = gastos.ncf.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1

                    )
                    Text(
                        text = gastos.itbis.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .weight(3f)
                ) {
                    Text(
                        text = "$ " + gastos.monto.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        textAlign = TextAlign.Right,

                        )
                }

            }


        }
        Divider( thickness = 1.dp, color = Color.Black)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )  {
            Row(

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .weight(2f)
                ){
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        colors =  ButtonDefaults.buttonColors(Color.Blue),
                        onClick = {
                            viewModel.update(gastos)

                        })
                    {
                        Icon(imageVector = Icons.Default.BorderColor, contentDescription = "guardar", )
                        Text(text = "Editar")

                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .weight(2f)
                ){
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        colors =  ButtonDefaults.buttonColors(Color.Red),
                        onClick = {
                            gastos.idGasto?.let { viewModel. DeleteGasto(it.toInt()) }
                        })

                    {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "guardar")
                        Text(text = "Eliminar")
                    }
                }




            }

        }


    }

}