package com.example.jsonapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var curencyDetails: Datum? = null
    private lateinit var userInput : EditText
    private lateinit var convert : Button
    private lateinit var spinner: Spinner
    private lateinit var total : TextView
    private lateinit var swap :Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userInput = findViewById(R.id.userinput)
         convert = findViewById(R.id.btn)
         spinner = findViewById(R.id.spr)
        total = findViewById(R.id.tvTotal)
        swap = findViewById(R.id.btnSwap)
        total.isVisible= false


        val cur = arrayListOf("inr", "usd", "aud", "sar", "cny", "jpy")

        var selected: Int = 0

        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, cur
            )
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    selected = position
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
        total.text =  "result ${0}"
        swap.setOnClickListener {

                total.isVisible= true
                try {

                    var sel = userInput.text.toString()
                    var currency: Double = sel.toDouble()

                    getCurrency(onResult = {
                        curencyDetails = it

                        when (selected) {
                            0 -> displaySwap(swap(curencyDetails?.eur?.inr?.toDouble(), currency));
                            1 -> displaySwap(swap(curencyDetails?.eur?.usd?.toDouble(), currency));
                            2 -> displaySwap(swap(curencyDetails?.eur?.aud?.toDouble(), currency));
                            3 -> displaySwap(swap(curencyDetails?.eur?.sar?.toDouble(), currency));
                            4 -> displaySwap(swap(curencyDetails?.eur?.cny?.toDouble(), currency));
                            5 -> displaySwap(swap(curencyDetails?.eur?.jpy?.toDouble(), currency));
                        }
                    })
                }catch (e :Exception){
                    Toast.makeText(applicationContext, "Please Enter your value" , Toast.LENGTH_LONG).show();

                    total.text =  "result ${0}"
                }
        }

        convert.setOnClickListener {
            total.isVisible= true
            try {

                var sel = userInput.text.toString()
                var currency: Double = sel.toDouble()

                getCurrency(onResult = {
                    curencyDetails = it

                    when (selected) {
                        0 -> display(calc(curencyDetails?.eur?.inr?.toDouble(), currency));
                        1 -> display(calc(curencyDetails?.eur?.usd?.toDouble(), currency));
                        2 -> display(calc(curencyDetails?.eur?.aud?.toDouble(), currency));
                        3 -> display(calc(curencyDetails?.eur?.sar?.toDouble(), currency));
                        4 -> display(calc(curencyDetails?.eur?.cny?.toDouble(), currency));
                        5 -> display(calc(curencyDetails?.eur?.jpy?.toDouble(), currency));
                    }
                })
            }catch (e :Exception){
                Toast.makeText(applicationContext, "Please Enter your value" , Toast.LENGTH_LONG).show();

                total.text =  "result ${0}"
            }
        }


    }

    private fun display(calc: Double) {



        total.text = "${userInput.text} Euro = $calc ${spinner.selectedItem}"
    }
    private fun displaySwap(calc1: Double) {



        total.text = "${userInput.text} ${spinner.selectedItem}  = $calc1 Euro "
    }

    private fun calc(i: Double?, sel: Double): Double {
        var s = 0.0
        if (i != null) {
            s = (i * sel)
        }
        return s
    }
    private fun swap(i: Double?, sel: Double): Double {
        var s = 0.0
        if (i != null) {
            s = (sel / i)
        }
        return s
    }

    private fun getCurrency(onResult: (Datum?) -> Unit) {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

        if (apiInterface != null) {
            apiInterface.getCurrency()?.enqueue(object : Callback<Datum> {
                override fun onResponse(
                    call: Call<Datum>,
                    response: Response<Datum>

                ) {
                    onResult(response.body())

                }

                override fun onFailure(call: Call<Datum>, t: Throwable) {
                    onResult(null)
                    Toast.makeText(applicationContext, "" + t.message, Toast.LENGTH_SHORT).show();
                }

            })
        }
    }
}
