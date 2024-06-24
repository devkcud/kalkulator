package com.devkcud.kalkulator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var display: TextView;
    private var currentDisplay = ""
    private var onTypeBlank = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.display);

        initializeButtons()
    }

    private fun initializeButtons() {
        val buttons = listOf(
            R.id.val_0, R.id.val_1, R.id.val_2, R.id.val_3,
            R.id.val_4, R.id.val_5, R.id.val_6, R.id.val_7,
            R.id.val_8, R.id.val_9, R.id.val_00, R.id.comma,
            R.id.op_add, R.id.op_subtract, R.id.op_multiply,
            R.id.op_divide, R.id.clear, R.id.del,
            R.id.parenthesis, R.id.op_result
        )

        for (id in buttons) {
            findViewById<Button>(id).setOnClickListener { onButtonClick(it) }
        }
    }

    private fun onButtonClick(view: View) {
        when (val buttonText = (view as Button).text.toString()) {
            "C" -> clearDisplay()
            "DEL" -> deleteLastCharacter()
            "=" -> calculateResult()
            "()" -> null
            else -> appendToDisplay(buttonText)
        }
    }

    private fun clearDisplay() {
        currentDisplay = ""
        display.text = "0"
    }

    private fun deleteLastCharacter() {
        if (currentDisplay.isEmpty()) {
            return
        }

        onTypeBlank = false
        currentDisplay = currentDisplay.substring(0, currentDisplay.length - 1)
        display.text = currentDisplay
    }

    private fun calculateResult() {
        onTypeBlank = true
        currentDisplay = evaluate(currentDisplay)
        display.text = currentDisplay
    }

    private fun appendToDisplay(text: String) {
        if (onTypeBlank) {
            clearDisplay()
            onTypeBlank = false
        }
        currentDisplay += text
        display.text = currentDisplay
    }

    fun evaluate(expression: String): String {
        return try {
            val e = ExpressionBuilder(expression).build()
            e.evaluate().toString()
        } catch (e: Exception) {
            return "Invalid expression"
        }
    }
}