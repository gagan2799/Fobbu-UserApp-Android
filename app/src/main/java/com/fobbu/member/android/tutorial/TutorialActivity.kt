package com.fobbu.member.android.tutorial

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.loginSignupModule.SignUpActivity
import com.fobbu.member.android.utils.CommonClass
import kotlinx.android.synthetic.main.activity_tutorial.*
import me.relex.circleindicator.CircleIndicator

class TutorialActivity : AppCompatActivity()
{
    lateinit var viewPager: ViewPager

    lateinit var adapterMainPager: MyPagerAdapter

    lateinit var circleIndicator: CircleIndicator

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_tutorial)

        initialise()  // function for initialising all the variables and views of the class

        clicks()  // function for handling clicks of the class
    }

    // function for initialising all the variables and views of the class
    private fun initialise()
    {
        CommonClass(this, this).putString("CoachMark_first_time", "true")

        circleIndicator = findViewById(R.id.circleIndicator)

        viewPager = findViewById(R.id.viewPager)

        adapterMainPager = MyPagerAdapter(this.supportFragmentManager)

        viewPager.adapter = adapterMainPager

        viewPager.offscreenPageLimit = 1

        circleIndicator.setViewPager(viewPager)
    }

    // function for handling clicks of the class
    private fun clicks()
    {
        viewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                viewPager.currentItem = position

                viewPager.adapter!!.notifyDataSetChanged()

                if (position == 6) {
                } else {
                }

                val intent = Intent()

                intent.action = "com.fobbu.position_start_"

                intent.putExtra("position", position.toString() + "")

                sendBroadcast(intent)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        tvTryFobbu.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                finishAffinity()
            else
                finish()
        }
    }

    // inner fragment adapter class
    class MyPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
        internal var pos: Int = 0

        // Returns total number of pages
        override fun getCount(): Int {
            return 4
        }

        // Returns the fragment to display for that page,
        override fun getItem(position: Int): Fragment?
        {
            var fragment: Fragment? = null

            when (position)
            {
                0 -> fragment = TutorialFragment1()

                1 -> fragment = TutorialFragment2()

                2 -> fragment = TutorialFragment3()

                3 -> fragment = TutorialFragment4()
            }

            return fragment
        }

        // Returns the page title for the top indicator
        override fun getPageTitle(position: Int): CharSequence? {
            return "Page $position"
        }
    }
}