package com.example.addMulApp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.addMulApp.ui.theme.Oving2RegneOppgaveTheme

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Oving2RegneOppgaveTheme {

                RegneOppgaveCompose()

            }
        }
    }
}



@Composable
fun RegneOppgaveCompose() {
    var number1 by remember { mutableIntStateOf(3) }
    var number2 by remember { mutableIntStateOf(5) }
    var upperLimit by remember { mutableStateOf("10") }
    var answerInp by remember { mutableStateOf("8") }

    var operator by remember { mutableStateOf("+") }

    //Med tanke på hvor en skal plassere resultat etter randomvalueActivity
    val REQ_PLACE_AT_NUMBER1: Int = 1
    val REQ_PLACE_AT_NUMBER2: Int = 2

    //På grunn av måten intent funker, må gjøre det slikt
    //Målet er å kjøre intent 2 ganger uten at ene overskrider
    //den andre
    var numberPlacementQueue = remember { mutableStateListOf(REQ_PLACE_AT_NUMBER1, REQ_PLACE_AT_NUMBER2) }

    fun resetNumberPlacementQueue() {
        numberPlacementQueue = mutableStateListOf(REQ_PLACE_AT_NUMBER1, REQ_PLACE_AT_NUMBER2)
    }

    val context = LocalContext.current




    // rememberLauncherForActivityResult brukes for å gjøre en
    // result API i  composable

    /**
     * Bytter ut vanlige metoder, med tilsvarende gjennom Activity Result API (å gjøre direkte er deprecated)
     * @see https://developer.android.com/training/basics/intents/result
     * @see https://developer.android.com/reference/androidx/activity/result/contract/ActivityResultContracts
     * @see https://developer.android.com/reference/androidx/activity/result/contract/ActivityResultContracts.StartActivityForResult
     *
     */
    val activityForResultStarter = rememberLauncherForActivityResult(
        //Tilsvarende som før, men gjennom Activity result API
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
           if (result.resultCode == Activity.RESULT_OK) {
               val requestCode = numberPlacementQueue.removeFirstOrNull()

               when (requestCode) {
                   REQ_PLACE_AT_NUMBER1 -> {
                       Log.i("onResult", "requestCode = REQ_PLACE_AT_NUMBER1")
                       result.data?.let {
                           number1 = it.getIntExtra("RANDOM_VALUE", 0)
                           Log.i("onResult", "Tilfeldig tall = $number1")
                       }
                   }

                   REQ_PLACE_AT_NUMBER2 -> {
                       Log.i("onResult", "requestCode = REQ_PLACE_AT_NUMBER2")
                       result.data?.let {
                           number2 = it.getIntExtra("RANDOM_VALUE", 0)
                           Log.i("onResult", "Tilfeldig tall = $number2")

                       }

                   }



               }

           }
       }

    /**
     * Sjekker om regnestykket, gitt en operator,
     * gir riktig svar opp mot brukerinput
     */
    fun checkAnswer() {

        val answer = answerInp.toIntOrNull()

        if (answer != null) {
            val correctAnswer = when (operator) {
                "+" -> number1 + number2
                "*" -> number1 * number2
                else -> throw IllegalStateException("Operator is nondefined, in calculation")
            }

            if(answer == correctAnswer){
                val toast = Toast.makeText(context, R.string.resp_riktig, Toast.LENGTH_SHORT)
                toast.show()
            }
            else {
                val msg = context.getString(R.string.resp_feil) + " " + correctAnswer.toString()
                val toast = Toast.makeText(context, msg, Toast.LENGTH_LONG)
                toast.show()
            }
        }
    }

    /**
     *  Genererer 2 tall, som vises på skjerm
     */
    fun generateAndDisplayRandomNumbers() {

        val intent = Intent("RandomvalueActivity")
        val limitUpper = upperLimit.toIntOrNull()
        if( limitUpper != null){
            intent.putExtra("UPPER_LIMIT", upperLimit.toInt())
            activityForResultStarter.launch(intent)
            activityForResultStarter.launch(intent)
            resetNumberPlacementQueue()
        }


    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Spacer(modifier = Modifier.height(32.dp))


        Text(
            text = "Regne oppgave",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color =  Color(0xFF3B5998)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text =stringResource(R.string.inp_label_ovre_grense),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = upperLimit,
                onValueChange = { value ->
                    if (value.all { it.isDigit() }) {
                        upperLimit = value
                    } },

                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = number1.toString(),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {


            Button(

                onClick = {
                    operator = "+"
                    val answer = answerInp.toIntOrNull()
                    if (answer != null) {
                        checkAnswer()
                        generateAndDisplayRandomNumbers()
                    }


                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(0.dp)
            ) {
                Text(stringResource(R.string.btn_adder))
            }

            Button(
                onClick = {
                    operator = "*"
                    val answer = answerInp.toIntOrNull()
                    if (answer != null) {
                        checkAnswer()
                        generateAndDisplayRandomNumbers()
                    }

                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(0.dp)
            ) {
                Text(stringResource(R.string.btn_multipliser))
            }




        }
        Text(
            text = number2.toString(),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "=",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )



        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(R.string.inp_label_svar),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = answerInp,
                onValueChange = { value ->
                    if (value.all { it.isDigit()  }) {
                        answerInp = value
                    } },

                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }





    }
}

