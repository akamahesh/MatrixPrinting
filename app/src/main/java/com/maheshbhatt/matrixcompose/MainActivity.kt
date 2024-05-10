package com.maheshbhatt.matrixcompose

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maheshbhatt.matrixcompose.ui.theme.MatrixComposeTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MatrixComposeTheme {
                HomeScreen()
            }
        }
    }


}

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    var showMatrix by remember { mutableStateOf(false) }
    var matrixSize by remember {
        mutableStateOf(0)
    }
    val focusManager = LocalFocusManager.current
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Author : Mahesh Bhatt ",
                modifier = Modifier.padding(8.dp),
                color = Color.LightGray
            )
            MatrixDimensions(modifier = Modifier.padding(innerPadding)) {
                toasty(context, "Selected Dimension: $it")
                matrixSize = it
                showMatrix = false
            }
            Button(onClick = {
                showMatrix = !showMatrix
                focusManager.clearFocus(true)
            }) {
                Text(text = "Show Matrix")
            }

            if (showMatrix) {
                var values by remember { mutableStateOf(MutableList(matrixSize * matrixSize) { -1 }) }
                LaunchedEffect(Unit) {
                    for (i in 0 until matrixSize * matrixSize) {
                        delay(1000L)
                        val newValues = values.toMutableList()
                        newValues[i] = i
                        values = newValues
                    }
                }
                //reset matrix
                GenerateMatrix(0, values)
                // generate matrix
                GenerateMatrix(matrixSize, values)
            }


        }
    }
}

@Composable
fun GenerateMatrix(size: Int, values: List<Int> = emptyList(), modifier: Modifier = Modifier) {
    Log.d("Matrix ", "GenerateMatrix Values: $values")
    Column(
        modifier = modifier
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
            .horizontalScroll(rememberScrollState())
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var counter = 0
        repeat(size) { rowIndex ->
            Row(Modifier.fillMaxWidth()) {
                repeat(size) { columnIndex ->
                    if (counter < values.size && values[counter] > -1) {
                        GenerateCell(value = values[counter])
                        counter++
                    }

                }
            }
        }
    }
}

@Composable
fun GenerateCell(value: Int) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(50.dp)
            .border(1.dp, Color.Gray),
        contentAlignment = Alignment.Center,

        ) {
        Text(text = value.toString())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownOption(onSelectDimension: (dimension: Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val matrixDimension = arrayOf(0, 2, 3, 4, 6, 8, 10, 12, 16)
    var selectedDimension by remember { mutableStateOf(matrixDimension[0]) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.padding(4.dp)
    ) {
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            TextField(
                value = selectedDimension.toString(),
                onValueChange = {
                    Log.d("TAG", "Value changed")
                },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier.menuAnchor(),
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }) {
                matrixDimension.forEach { dimension ->
                    DropdownMenuItem(
                        text = { Text(text = dimension.toString()) },
                        onClick = {
                            selectedDimension = dimension
                            expanded = false
                            onSelectDimension(selectedDimension)
                        })
                }

            }
        }

    }
}


@Composable
fun MatrixDimensions(modifier: Modifier, onSelectDimension: (dimension: Int) -> Unit) {
    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Select Dimensions")
        DropdownOption(onSelectDimension)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MatrixComposeTheme {
        HomeScreen()
    }
}

private fun toasty(context: Context, text: String) {
    Toast.makeText(
        context,
        text,
        Toast.LENGTH_SHORT
    ).show()
}