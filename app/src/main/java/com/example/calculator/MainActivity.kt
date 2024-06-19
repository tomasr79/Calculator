package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
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
    var displayText by remember { mutableStateOf("0") }
    var lastNumeric by remember { mutableStateOf(false) }
    var stateError by remember { mutableStateOf(false) }
    var lastDot by remember { mutableStateOf(false) }

    BoxWithConstraints(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        val isPortrait = constraints.maxHeight > constraints.maxWidth

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BasicTextField(
                value = displayText,
                onValueChange = {},
                readOnly = true,
                textStyle = TextStyle(fontSize = 32.sp, color = Color.Black, textAlign = TextAlign.End),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp)
                    .padding(end = 16.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

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
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { label ->
                        Button(
                            onClick = {
                                when (label) {
                                    "C" -> {
                                        displayText = "0"
                                        lastNumeric = false
                                        stateError = false
                                        lastDot = false
                                    }
                                    "=" -> {
                                        if (lastNumeric && !stateError) {
                                            val tvValue = displayText
                                            var prefix = ""
                                            try {
                                                var value = tvValue
                                                if (value.startsWith("-")) {
                                                    prefix = "-"
                                                    value = value.substring(1)
                                                }

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
                                                    var one = value

                                                    if (prefix.isNotEmpty()) {
                                                        one = prefix + one
                                                    }

                                                    displayText = removeZeroAfterDot(Math.sqrt(one.toDouble()).toString())
                                                } else if (value.contains("%")) {
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
                                        if (lastNumeric && !stateError) {
                                            displayText += label
                                            lastNumeric = false
                                            lastDot = false
                                        }
                                    }
                                    "." -> {
                                        if (lastNumeric && !lastDot) {
                                            displayText += label
                                            lastNumeric = false
                                            lastDot = true
                                        }
                                    }
                                    "+/-" -> {
                                        if (!stateError) {
                                            if (displayText.startsWith("-")) {
                                                displayText = displayText.substring(1)
                                            } else {
                                                displayText = "-$displayText"
                                            }
                                        }
                                    }
                                    "√", "%" -> {
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
                                .weight(1f)
                                .aspectRatio(if (isPortrait) 1f else 6f) // Ajusta a proporção com base na orientação
                        ) {
                            Text(
                                text = label
                            )
                        }
                    }
                }
            }
        }
    }
}

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