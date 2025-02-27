package com.fobbu.member.android.activities.loginSignupModule

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.dashboard.DashboardActivity
import com.fobbu.member.android.utils.CommonClass
import kotlinx.android.synthetic.main.activity_generate_pin.*

class GeneratePINActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_generate_pin)

        initView()              //function for initialising all variables of the class

        addClicks()             // Functionality of  all clicks present in the activity are handled here

        checkPinValidations()       /// FOR 4 DIGIT PIN
    }

    //function for initialising all variables of the class
    private fun initView()
    {
        CommonClass(this,this).putString("isSmsVerified","true")

        if (intent.hasExtra("pin"))
        {
            if (intent.getStringExtra("pin") == "security")
            {
                if (CommonClass(this, this).getString("Local_Pin").isNotEmpty())
                {
                    tvCurrnetPin.visibility = View.VISIBLE

                    llCurrentPIN.visibility = View.VISIBLE
                }

                tvDoItLater.visibility = View.GONE

                tvLogin.text = getString(R.string.generate_new_pin)
            }
        }
        else
        {
            tvLogin.text = getString(R.string.generate_pin)

            tvCurrnetPin.visibility = View.GONE

            llCurrentPIN.visibility = View.GONE

            tvDoItLater.visibility = View.VISIBLE
        }
    }

    /// FOR 4 DIGIT PIN
    private fun checkPinValidations()
    {
        edPinOne.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable)
            {
                if (s.trim().isNotEmpty())
                    edPinTwo.requestFocus()
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {}
        })

        edPinTwo.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable)
            {
                if (s.trim().isNotEmpty())
                {
                    edPinThree.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {}
        })

        edPinThree.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable)
            {
                if (s.trim().isNotEmpty())
                    edPinFour.requestFocus()
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

            }
        })

        edPinFour.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable)
            {
                if (s.trim().isNotEmpty())
                    edPinFive.requestFocus()
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {}
        })

        if (intent.getStringExtra("pin") == "security") {
            edCurrentPinOne.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable)
                {
                    if (s.trim().isNotEmpty())
                        edCurrentPinTwo.requestFocus()
                }

                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {}

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {}
            })

            edCurrentPinTwo.addTextChangedListener(object : TextWatcher
            {
                override fun afterTextChanged(s: Editable)
                {
                    if (s.trim().isNotEmpty())
                        edCurrentPinThree.requestFocus()
                }

                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {}

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {}
            })

            edCurrentPinThree.addTextChangedListener(object : TextWatcher
            {
                override fun afterTextChanged(s: Editable)
                {
                    if (s.trim().isNotEmpty())
                        edCurrentPinFour.requestFocus()
                }

                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {}

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {}
            })

            edCurrentPinFour.addTextChangedListener(object : TextWatcher
            {
                override fun afterTextChanged(s: Editable)
                {
                    if (s.isNotEmpty())
                        edPinOne.requestFocus()
                }

                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {}

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {}
            })

            edCurrentPinThree.setOnKeyListener { v, keyCode, event ->
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL && edCurrentPinThree.text.toString().trim().isEmpty())
                    edCurrentPinTwo.requestFocus()

                false
            }

            edCurrentPinTwo.setOnKeyListener { v, keyCode, event ->
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL)
                    edCurrentPinOne.requestFocus()

                false
            }

            edCurrentPinFour.setOnKeyListener { v, keyCode, event ->
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL && edCurrentPinFour.text.toString().trim().isEmpty())
                    edCurrentPinThree.requestFocus()

                false
            }

            edPinOne.setOnKeyListener { v, keyCode, event ->
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL && edPinOne.text.toString().trim().isEmpty())
                    edCurrentPinFour.requestFocus()

                false
            }
        }

        edPinTwo.setOnKeyListener { v, keyCode, event ->
            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
            if (keyCode == KeyEvent.KEYCODE_DEL)
                edPinOne.requestFocus()

            false
        }

        edPinThree.setOnKeyListener { v, keyCode, event ->
            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
            if (keyCode == KeyEvent.KEYCODE_DEL && edPinThree.text.toString().trim().isEmpty())
                edPinTwo.requestFocus()


            false
        }

        edPinFour.setOnKeyListener { v, keyCode, event ->
            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
            if (keyCode == KeyEvent.KEYCODE_DEL && edPinFour.text.toString().trim().isEmpty())
                edPinThree.requestFocus()


            false
        }

        edPinFive.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable)
            {
                if (s.trim().isNotEmpty())
                    edPinSix.requestFocus()
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {}
        })

        edPinSix.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable)
            {
                if (s.trim().isNotEmpty())
                    edPinSeven.requestFocus()

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {}
        })

        edPinSeven.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable)
            {
                if (s.isNotEmpty())
                    edPinEight.requestFocus()
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {}
        })

        edPinEight.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {}
        })

        edPinFive.setOnKeyListener { v, keyCode, event ->
            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
            if (keyCode == KeyEvent.KEYCODE_DEL && edPinFive.text.toString().trim().isEmpty())
                edPinFour.requestFocus()

            false
        }

        edPinSix.setOnKeyListener { v, keyCode, event ->
            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
            if (keyCode == KeyEvent.KEYCODE_DEL && edPinSix.text.toString().trim().isEmpty())
                edPinFive.requestFocus()

            false
        }

        edPinSeven.setOnKeyListener { v, keyCode, event ->
            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
            if (keyCode == KeyEvent.KEYCODE_DEL && edPinSeven.text.toString().trim().isEmpty())
                edPinSix.requestFocus()

            false
        }
        edPinEight.setOnKeyListener { v, keyCode, event ->
            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
            if (keyCode == KeyEvent.KEYCODE_DEL && edPinEight.text.toString().trim().isEmpty())
                edPinSeven.requestFocus()

            false
        }
    }

    // Functionality of  all clicks present in the activity are handled here
    private fun addClicks()
    {
        ivBack.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))

            finish()
        }

        tvDoItLater.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))

            finish()
        }

        tvGenerate.setOnClickListener {
            val currentPin = edCurrentPinOne.text.toString().trim() + edCurrentPinTwo.text.toString().trim() + edCurrentPinThree.text.toString().trim() + edCurrentPinFour.text.toString().trim()

            val enterPin = edPinOne.text.toString().trim() + edPinTwo.text.toString().trim() + edPinThree.text.toString().trim() + edPinFour.text.toString().trim()

            val confirmPin = edPinFive.text.toString().trim() + edPinSix.text.toString().trim() + edPinSeven.text.toString().trim() + edPinEight.text.toString().trim()

            if (intent.getStringExtra("pin") == "security")
            {
                if (CommonClass(this, this).getString("Local_Pin").isNotEmpty())
                {
                    when
                    {
                        currentPin.length!=4 ->
                            Toast.makeText(this, "Please enter Current PIN.", Toast.LENGTH_SHORT).show()

                        currentPin != CommonClass(this, this).getString("Local_Pin") ->
                            Toast.makeText(this, "Current PIN is incorrect.", Toast.LENGTH_SHORT).show()

                        enterPin.length!=4 ->
                            Toast.makeText(this, "Please enter New PIN.", Toast.LENGTH_SHORT).show()

                        confirmPin.length!=4 ->
                            Toast.makeText(this, "Please enter Confirm PIN.", Toast.LENGTH_SHORT).show()

                        currentPin == enterPin ->
                            Toast.makeText(this, "Entered Pin is same as the old pin.Try new pin", Toast.LENGTH_SHORT).show()

                        enterPin != confirmPin ->
                            Toast.makeText(this, resources.getString(R.string.pin_dont_match), Toast.LENGTH_SHORT).show()

                        enterPin.length<4 || confirmPin.length<4->
                            Toast.makeText(this,"Please provide correct pin", Toast.LENGTH_SHORT).show()

                        else ->
                        {
                            Toast.makeText(this, resources.getString(R.string.succesfully_set_pin), Toast.LENGTH_SHORT).show()

                            CommonClass(this, this).putString("Local_Pin", confirmPin)

                            finish()
                        }
                    }
                }
                else
                    firstTimePin(enterPin, confirmPin)
            }
            else
                firstTimePin(enterPin, confirmPin)
        }
    }

    private fun firstTimePin(enterPin: String, confirmPin: String) = when {

        enterPin.length!=4 ->
            Toast.makeText(this, resources.getString(R.string.enter_new_pin_msg), Toast.LENGTH_SHORT).show()

        confirmPin.length!=4 ->
            Toast.makeText(this, resources.getString(R.string.confirm_pin_msg), Toast.LENGTH_SHORT).show()

        enterPin == confirmPin -> {
            Toast.makeText(this, resources.getString(R.string.succesfully_set_pin), Toast.LENGTH_SHORT).show()

            CommonClass(this, this).putString("Local_Pin", confirmPin)

            startActivity(Intent(this, DashboardActivity::class.java))

            finish()
        }
        else -> Toast.makeText(this, resources.getString(R.string.pin_dont_match), Toast.LENGTH_SHORT).show()
    }
}