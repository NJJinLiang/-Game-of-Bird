package com.saosao.snow.oma

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import com.saosao.snow.newalpha.myview.brid.CallBack
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var number = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        start.setOnClickListener{
            birdview.start()
        }
        reset.setOnClickListener{
            birdview.reset(spinner.selectedItemPosition)
        }
        stop.setOnClickListener{
            birdview.stop()
        }
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View,
                               pos: Int, id: Long) {
                birdview.reset(pos)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
        birdview.setOnOver(
                object : CallBack {
                    override fun onOver(){
                        Toast.makeText(this@MainActivity , "game over" , Toast.LENGTH_SHORT).show()
                    }

                    override fun onAdd() {
                        number++
                        number_text.setText(number.toString())
                    }

                    override fun onReset() {
                        number = 0
                        number_text.text = number.toString()
                    }
                }
        )

    }
}
