package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Calculator()
                }
            }
        }
    }
}

@Composable
fun Calculator() {
    var displayText by remember { mutableStateOf("0") } // Variavel para armazenar e exibir o texto de entrada
    var lastNumeric by remember { mutableStateOf(false) } // Flag para controlar se o último caractere digitado foi numérico
    var stateError by remember { mutableStateOf(false) } // Flag para indicar se ocorreu um erro de cálculo
    var lastDot by remember { mutableStateOf(false) } // Flag para controlar se o último caractere digitado foi um ponto decimal

    Column(
        modifier = Modifier.run {
            fillMaxSize() // Ocupa o espaço disponivel na tela
                .padding(16.dp)
        }, // Adiciona padding em volta da coluna
        verticalArrangement = Arrangement.spacedBy(8.dp) // Espaçamento vertical entre os elementos
    ) {
        BasicTextField(
            value = displayText,
            onValueChange = {},
            readOnly = true,
            textStyle = TextStyle(fontSize = 32.sp, color = Color.Black, textAlign = TextAlign.End),
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp) // Adiciona padding ao redor do campo de texto
                .padding(end = 16.dp) // Padding adicional à direita para espaçamento visual
        )

        Spacer(modifier = Modifier.weight(1f)) // Espaço para empurrar os botões para a parte inferior

        // Matriz de botões organizada em linhas e colunas
        val buttons = listOf(
            listOf("7", "8", "9", "/"),
            listOf("4", "5", "6", "*"),
            listOf("1", "2", "3", "-"),
            listOf("C", "0", "=", "+"),
            listOf("√", ".", "%", "+/-")
        )

        buttons.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth() // Ocupa toda a largura disponível
                    .padding(horizontal = 16.dp), // Espaçamento horizontal interno
                horizontalArrangement = Arrangement.spacedBy(8.dp) // Espaçamento horizontal entre os botões
            ) {
                row.forEach { label ->
                    Button(
                        onClick = {
                            when (label) {
                                "C" -> {
                                    // Lógica para limpar o texto e os estados
                                    displayText = "0"
                                    lastNumeric = false
                                    stateError = false
                                    lastDot = false
                                }
                                "=" -> {
                                    // Lógica para realizar o cálculo quando "=" é pressionado
                                    if (lastNumeric && !stateError) {
                                        val tvValue = displayText
                                        var prefix = ""
                                        try {
                                            var value = tvValue
                                            if (value.startsWith("-")) {
                                                prefix = "-"
                                                value = value.substring(1)
                                            }

                                            // Lógica para operações matemáticas básicas (+, -, *, /)
                                            if (value.contains("-")) {
                                                val splitValue = value.split("-")
                                                var one = splitValue[0]
                                                val two = splitValue[1]

                                                if (prefix.isNotEmpty()) {
                                                    one = prefix + one
                                                }

                                                displayText = removeZeroAfterDot((one.toDouble() - two.toDouble()).toString())
                                            } else if (value.contains("+")) {
                                                val splitValue = value.split("+")
                                                var one = splitValue[0]
                                                val two = splitValue[1]

                                                if (prefix.isNotEmpty()) {
                                                    one = prefix + one
                                                }

                                                displayText = removeZeroAfterDot((one.toDouble() + two.toDouble()).toString())
                                            } else if (value.contains("*")) {
                                                val splitValue = value.split("*")
                                                var one = splitValue[0]
                                                val two = splitValue[1]

                                                if (prefix.isNotEmpty()) {
                                                    one = prefix + one
                                                }

                                                displayText = removeZeroAfterDot((one.toDouble() * two.toDouble()).toString())
                                            } else if (value.contains("/")) {
                                                val splitValue = value.split("/")
                                                var one = splitValue[0]
                                                val two = splitValue[1]

                                                if (prefix.isNotEmpty()) {
                                                    one = prefix + one
                                                }

                                                displayText = removeZeroAfterDot((one.toDouble() / two.toDouble()).toString())
                                            } else if (value.contains("√")) {
                                                // Lógica para raiz quadrada (√)
                                                var one = value

                                                if (prefix.isNotEmpty()) {
                                                    one = prefix + one
                                                }

                                                displayText = removeZeroAfterDot(Math.sqrt(one.toDouble()).toString())
                                            } else if (value.contains("%")) {
                                                // Lógica para porcentagem (%)
                                                val splitValue = value.split("%")
                                                var one = splitValue[0]
                                                val two = splitValue[1]

                                                if (prefix.isNotEmpty()) {
                                                    one = prefix + one
                                                }

                                                displayText = removeZeroAfterDot((one.toDouble() * two.toDouble() / 100).toString())
                                            }
                                        } catch (e: ArithmeticException) {
                                            e.printStackTrace()
                                            stateError = true
                                            displayText = "Error"
                                        }
                                    }
                                }
                                "+", "-", "*", "/" -> {
                                    // Lógica para adicionar operadores (+, -, *, /)
                                    if (lastNumeric && !stateError) {
                                        displayText += label
                                        lastNumeric = false
                                        lastDot = false
                                    }
                                }
                                "." -> {
                                    // Lógica para adicionar o ponto decimal (.)
                                    if (lastNumeric && !lastDot) {
                                        displayText += label
                                        lastNumeric = false
                                        lastDot = true
                                    }
                                }
                                "+/-" -> {
                                    // Lógica para inverter o sinal (+/-)
                                    if (!stateError) {
                                        if (displayText.startsWith("-")) {
                                            displayText = displayText.substring(1)
                                        } else {
                                            displayText = "-$displayText"
                                        }
                                    }
                                }
                                "√", "%" -> {
                                    // Lógica para calcular a raiz quadrada (√) e porcentagem (%)
                                    if (lastNumeric && !stateError) {
                                        val tvValue = displayText
                                        var prefix = ""
                                        try {
                                            var value = tvValue
                                            if (value.startsWith("-")) {
                                                stateError = true
                                                displayText = "Error"
                                            } else {
                                                if (label == "√") {
                                                    displayText = removeZeroAfterDot(Math.sqrt(value.toDouble()).toString())
                                                } else if (label == "%") {
                                                    displayText = removeZeroAfterDot((value.toDouble() / 100).toString())
                                                }
                                            }
                                        } catch (e: ArithmeticException) {
                                            e.printStackTrace()
                                            stateError = true
                                            displayText = "Error"
                                        }
                                    }
                                }
                                else -> {
                                    // Lógica para adicionar dígitos numéricos
                                    if (stateError) {
                                        displayText = label
                                        stateError = false
                                    } else {
                                        if (displayText == "0") {
                                            displayText = label
                                        } else {
                                            displayText += label
                                        }
                                    }
                                    lastNumeric = true
                                    lastDot = false
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f) // Peso para ocupar espaço igual na linha
                            .aspectRatio(1f) // Proporção para manter o botão quadrado
                    ) {
                        Text(
                            text = label // Texto exibido no botão
                        )
                    }
                }
            }
        }
    }
}

// Função para remover zeros desnecessários após o ponto decimal
private fun removeZeroAfterDot(result: String): String {
    return if (result.contains(".0")) {
        result.substring(0, result.length - 2)
    } else {
        result
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CalculatorTheme {
        Calculator()
    }
}