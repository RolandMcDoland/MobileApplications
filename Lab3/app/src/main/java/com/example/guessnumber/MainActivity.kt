package com.example.guessnumber

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.content.DialogInterface
import java.io.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Pick a random number between 0 and 20
        var numberToGuess = (0..20).random()

        //Initialize the try counter
        var counter = 1

        //Initialize the new game toaster
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, "New game started", duration)

        //Initialize the win dialog
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Congratulations!")

        //After alert is cancelled inform about new game started
        dialog.setOnCancelListener { toast.show() }

        //File name
        val fileName = "BestScore.txt"

        //Set the best score as something easy to beat
        var bestScore = 50

        //Create new file in Internal Storage directory
        val file = File(filesDir, fileName)
        file.createNewFile()

        //Read data from Internal Memory
        var fileInputStream = openFileInput(fileName)
        var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
        val stringBuilder: StringBuilder = StringBuilder()
        var text: String? = null
        while ({ text = bufferedReader.readLine(); text }() != null) {
            stringBuilder.append(text)
        }

        //If there's something in the file
        if(stringBuilder.length != 0) {
            //Get the best score so far from memory
            bestScore = stringBuilder.toString().toInt()

            //Display the best score so far
            textView.setText("Best Score: " + stringBuilder.toString()).toString()
        }
        else {
            //Display information about lack of high scores
            textView.setText("No scores yet").toString()
        }

        button.setOnClickListener {
            //If you win
            if(editText.text.toString().toInt() == numberToGuess) {
                //Show win dialog
                dialog.setMessage("Game won in " + counter.toString() + " tries")
                dialog.show()

                //Pick new number
                numberToGuess = (0..20).random()

                //If new score is better
                if(bestScore > counter) {
                    //Make new best score
                    bestScore = counter

                    //Write best score to memory
                    val fileOutputStream: FileOutputStream
                    try {
                        fileOutputStream = openFileOutput(fileName, MODE_PRIVATE)
                        fileOutputStream.write(bestScore.toString().toByteArray())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    //Display the best score so far
                    textView.setText("Best Score: " + bestScore.toString()).toString()
                }

                //Reset the try counter
                counter = 0
            }
            //If you lose
            else {
                //Increase the try counter
                counter++
            }
        }

        button2.setOnClickListener{
            //Pick new number
            numberToGuess = (0..20).random()

            //Reset the try counter
            counter = 0

            //Inform about new game being started
            toast.show()
        }
    }
}
