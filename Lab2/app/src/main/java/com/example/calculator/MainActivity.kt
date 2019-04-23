package com.example.calculator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            if(radioButton.isChecked()) {
                textView2.text = (editText2.text.toString().toFloat() / editText.text.toString().toFloat()).toString();
            }
            else if(radioButton2.isChecked()) {
                textView2.text = (editText2.text.toString().toFloat() * editText.text.toString().toFloat()).toString();
            }
            else if(radioButton3.isChecked()) {
                textView2.text = (editText2.text.toString().toFloat() - editText.text.toString().toFloat()).toString();
            }
            else if(radioButton4.isChecked()) {
                textView2.text = (editText2.text.toString().toFloat() + editText.text.toString().toFloat()).toString();
            }
        }
    }
}
