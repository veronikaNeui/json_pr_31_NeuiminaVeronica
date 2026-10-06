package com.bignerdranch.android.json_neuimina

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private val TAG = "MyJsonApp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etName   = findViewById<EditText>(R.id.ProductName)
        val etPrice  = findViewById<EditText>(R.id.ProductPrice)
        val etTags   = findViewById<EditText>(R.id.ProductTags)
        val btnDo    = findViewById<Button>(R.id.btnProgress)
        val tvResult = findViewById<TextView>(R.id.Result)

        val gson = Gson()

        val book = Product(name  = "Программирование на Kotlin", price = 1500.0, tags  = listOf("учебник", "программирование", "android"))

        val jsonFromObject = gson.toJson(book)
        Log.d(TAG, "JSON из объекта: $jsonFromObject")

        btnDo.setOnClickListener {
            val nameFromUser  = etName.text.toString().ifBlank { "Без названия" }
            val priceFromUser = etPrice.text.toString().toDoubleOrNull() ?: 0.0

            val tagsFromUser = etTags.text
                .toString()
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            val userProduct = Product(nameFromUser, priceFromUser, tagsFromUser)

            val jsonString = gson.toJson(userProduct)

            val productFromJson = gson.fromJson(jsonString, Product::class.java)

            val resultText = buildString {
                appendLine("JSON-строка:")
                appendLine(jsonString)
                appendLine()
                appendLine("--- После десериализации ---")
                appendLine("Название: ${productFromJson.name}")
                appendLine("Цена: ${productFromJson.price}")
                append("Теги: ${productFromJson.tags.joinToString(", ")}")
            }
            tvResult.text = resultText

            val jsonWithoutPrice = """{"name":"Тест","tags":["a","b"]}"""
            val productNoPrice = gson.fromJson(jsonWithoutPrice, Product::class.java)
            Log.d(TAG, "Без price -> price = ${productNoPrice.price}") // ожидаем 0.0

            val jsonWithExtra =
                """{"name":"Тест","price":100.0,"tags":["x"],"extra":"лишнее"}"""
            val productExtra = gson.fromJson(jsonWithExtra, Product::class.java)
            Log.d(TAG, "С лишним полем -> name = ${productExtra.name}") // "Тест"
        }
    }
}
