package com.example.addMulApp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log

class RandomvalueActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val upperLimit = intent.getIntExtra("UPPER_LIMIT", 100)
        Log.i("RandomvalueActivity", "Upper limit = $upperLimit")


        val randomValue = (0..upperLimit).random()


        //Toast.makeText(this, "Tilfeldig tall: $randomValue", Toast.LENGTH_SHORT).show()

        Log.i("RandomvalueActivity", "Random value = $randomValue")
        val resultIntent = Intent()
        resultIntent.putExtra("RANDOM_VALUE", randomValue)
        setResult(RESULT_OK, resultIntent)
        Log.i("RandomvalueActivity", "Upper limit = $upperLimit")

        finish()
    }
}