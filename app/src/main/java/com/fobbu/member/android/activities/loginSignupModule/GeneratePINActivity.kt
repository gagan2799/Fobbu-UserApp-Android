package com.fobbu.member.android.activities.loginSignupModule

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.Toast
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.dashboardActivity.DashboardActivity
import com.fobbu.member.android.utils.CommonClass
import kotlinx.android.synthetic.main.activity_generate_pin.*

class GeneratePINActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_pin)
        addClicks()
        checkPinValidations()
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

            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty())
                    edPinFive.requestFocus()
            }

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

        edPinFive.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty())
                    edPinSix.requestFocus()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {}

        })
        edPinSix.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    edPinSeven.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

            }
        })

        edPinSeven.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty())
                    edPinEight.requestFocus()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

            }
        })

        edPinEight.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

            }
        })

        edPinFive.setOnKeyListener { v, keyCode, event ->
            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
            if (keyCode == KeyEvent.KEYCODE_DEL && edPinFive.text.toString().trim().isEmpty()) {
                edPinFour.requestFocus()
            }
            false
        }

        edPinSix.setOnKeyListener { v, keyCode, event ->
            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
            if (keyCode == KeyEvent.KEYCODE_DEL && edPinSix.text.toString().trim().isEmpty()) {
                edPinFive.requestFocus()
            }
            false
        }

        edPinSeven.setOnKeyListener { v, keyCode, event ->
            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
            if (keyCode == KeyEvent.KEYCODE_DEL && edPinSeven.text.toString().trim().isEmpty()) {

                edPinSix.requestFocus()

            }
            false
        }
        edPinEight.setOnKeyListener { v, keyCode, event ->
            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
            if (keyCode == KeyEvent.KEYCODE_DEL && edPinEight.text.toString().trim().isEmpty()) {

                edPinSeven.requestFocus()

            }
            false
        }
    }

    // Functionality of  all clicks present in the activity are handled here
    private fun addClicks() {

        ivBack.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

        tvDoItLater.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

        tvGenerate.setOnClickListener {

            val enterPin= edPinOne.text.toString().trim() +
                    edPinTwo.text.toString().trim() +
                    edPinThree.text.toString().trim() +
                    edPinFour.text.toString().trim()

            val confirmPin= edPinFive.text.toString().trim() +
                    edPinSix.text.toString().trim() +
                    edPinSeven.text.toString().trim() +
                    edPinEight.text.toString().trim()

            if(enterPin == confirmPin)
            {
                Toast.makeText(this,resources.getString(R.string.succesfully_set_pin),Toast.LENGTH_SHORT).show()
                val number = CommonClass(this,this).getString("mobile_number")
                CommonClass(this,this).putString("Local_Number",number)
                CommonClass(this,this).putString("Local_Pin",confirmPin)
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            }
            else
                Toast.makeText(this,resources.getString(R.string.pin_dont_match),Toast.LENGTH_SHORT).show()

        }

    }
}