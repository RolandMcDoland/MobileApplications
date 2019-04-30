package com.example.guessnumber

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.content.DialogInterface



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Pick a random number between 0 and 20
        var numberToGuess = (0..20).random()

        //Initialize the try counter
        var counter = 0

        //Initialize the new game toaster
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, "New game started", duration)

        //Initialize the win dialog
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Androidly Alert")

        //After alert is cancelled inform about new game started
        dialog.setOnCancelListener { toast.show() }

        button.setOnClickListener {
            //If you win
            if(editText.text.toString().toInt() == numberToGuess) {
                //Show win dialog
                dialog.setMessage("Game won in " + counter.toString() + " tries")
                dialog.show()

                //Pick new number
                numberToGuess = (0..20).random()

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
