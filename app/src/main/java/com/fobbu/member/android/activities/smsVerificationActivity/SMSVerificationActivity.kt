package com.fobbu.member.android.activities.smsVerificationActivity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.Toast
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.generatePinActivity.GeneratePINActivity
import com.fobbu.member.android.utils.CommonClass 
import kotlinx.android.synthetic.main.activity_sms_verification.*

class SMSVerificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms_verification)
        initialise()
        addClicks()
        checkPinValidations()
    }

    // Method for initializing all the variables that are used in the class
    private fun initialise() {
        val pin = CommonClass(this,this).getString("pin")

        for (i in pin.indices)
        {
            when (i) {
                0 -> edPinOne.setText(pin[i].toString())
                1 -> edPinTwo.setText(pin[i].toString())
                2 -> edPinThree.setText(pin[i].toString())
                3 -> {
                    edPinFour.setText(pin[i].toString())
                edPinFour.requestFocus()
                    edPinFour.setSelection(edPinFour.text.length)
                }
            }
        }
    }

    /// FOR 4 DIGIT PIN
    private fun checkPinValidations() {
        edPinOne.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty())
                    edPinTwo.requestFocus()
            }


            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {}

        })
        edPinTwo.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    edPinThree.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

            }
        })

        edPinThree.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty())
                    edPinFour.requestFocus()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

            }
        })

        edPinFour.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

            }
        })
        edPinTwo.setOnKeyListener { v, keyCode, event ->
            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                edPinOne.requestFocus()
            }
            false
        }

        edPinThree.setOnKeyListener { v, keyCode, event ->
            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
            if (keyCode == KeyEvent.KEYCODE_DEL && edPinThree.text.toString().trim().isEmpty()) {

                edPinTwo.requestFocus()

            }
            false
        }
        edPinFour.setOnKeyListener { v, keyCode, event ->
            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
            if (keyCode == KeyEvent.KEYCODE_DEL && edPinFour.text.toString().trim().isEmpty()) {

                edPinThree.requestFocus()

            }
            false
        }
    }

    // Functionality of  all clicks present in the activity are handled here
    private fun addClicks() {
        tvSave.setOnClickListener {
            val enterPin= edPinOne.text.toString().trim() +
                    edPinTwo.text.toString().trim() +
                    edPinThree.text.toString().trim() +
                    edPinFour.text.toString().trim()

            val pin = CommonClass(this,this).getString("pin")

            if(enterPin == pin)
            {
                Toast.makeText(this,resources.getString(R.string.sms_success), Toast.LENGTH_SHORT).show()
                   startActivity(Intent(this,
                       GeneratePINActivity::class.java))
                finish()
            }
            else
                Toast.makeText(this,resources.getString(R.string.sms_fail), Toast.LENGTH_SHORT).show()
            
        }


    }
}