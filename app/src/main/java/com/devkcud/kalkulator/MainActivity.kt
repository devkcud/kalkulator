package com.devkcud.kalkulator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import net.objecthunter.exp4j.ExpressionBuilder
import java.util.Stack

class MainActivity : AppCompatActivity() {
    private lateinit var display: TextView
    private var currentDisplay = ""
    private var onTypeBlank = true

    private lateinit var undo: View
    private lateinit var redo: View

    private val undoStack = Stack<String>()
    private val redoStack = Stack<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        display = findViewById(R.id.display)
        undo = findViewById(R.id.undo)
        redo = findViewById(R.id.redo)

        undo.setOnClickListener { undo() }
        redo.setOnClickListener { redo() }

        initializeButtons()
    }

    private fun initializeButtons() {
        val buttons = listOf(
            R.id.val_0, R.id.val_1, R.id.val_2, R.id.val_3,
            R.id.val_4, R.id.val_5, R.id.val_6, R.id.val_7,
            R.id.val_8, R.id.val_9, R.id.val_00, R.id.comma,
            R.id.op_add, R.id.op_subtract, R.id.op_multiply,
            R.id.op_divide, R.id.clear, R.id.del,
            R.id.ac, R.id.op_result
        )

        for (id in buttons) {
            findViewById<Button>(id).setOnClickListener { onButtonClick(it) }
        }
    }

    private fun onButtonClick(view: View) {
        saveToUndoStack()

        when (val buttonText = (view as Button).text.toString()) {
            "C" -> clearDisplay()
            "DEL" -> deleteLastCharacter()
            "=" -> calculateResult()
            "AC" -> removeAll()
            else -> appendToDisplay(buttonText)
        }
    }

    private fun removeAll() {
        clearDisplay()
        onTypeBlank = true
        undoStack.clear()
        redoStack.clear()
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

    private fun evaluate(expression: String): String {
        return try {
            val e = ExpressionBuilder(expression).build()
            e.evaluate().toString()
        } catch (e: Exception) {
            return "Invalid expression"
        }
    }

    private fun saveToUndoStack() {
        undoStack.push(currentDisplay)
        redoStack.clear()
    }

    private fun undo() {
        if (undoStack.isEmpty()) {
            return
        }

        onTypeBlank = false
        redoStack.push(currentDisplay)
        currentDisplay = undoStack.pop()
        if (currentDisplay == "") currentDisplay = "0"
        display.text = currentDisplay
        if (currentDisplay == "0") currentDisplay = ""
    }

    private fun redo() {
        if (redoStack.isEmpty()) {
            return
        }

        onTypeBlank = false
        undoStack.push(currentDisplay)
        currentDisplay = redoStack.pop()
        if (currentDisplay == "") currentDisplay = "0"
        display.text = currentDisplay
        if (currentDisplay == "0") currentDisplay = ""
    }
}