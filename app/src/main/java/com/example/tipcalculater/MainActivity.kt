package com.example.tipcalculater

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import com.example.tipcalculater.databinding.ActivityMainBinding

private const val TAG = "MainActivity"
private const val INITIAL_TIP =15
class MainActivity : AppCompatActivity() {
    //viewBiding
    private lateinit var binding: ActivityMainBinding
    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip : SeekBar
    private lateinit var tvTipPercentLabel:TextView
    private lateinit var tvTipAmount : TextView
    private lateinit var tvTotalAmount :TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //edittext for bill amount
        etBaseAmount = binding.editTextBaseAmount
        //tip seeker bar
        seekBarTip = binding.tipSeekBar


        //actual tip
        tvTipAmount = binding.textViewTipAmount

        //total amount text after tip calculation
        tvTotalAmount = binding.textViewTotal

        tvTipPercentLabel = binding.percentageTip


        //set initial value
        seekBarTip.progress = INITIAL_TIP
        tvTipPercentLabel.text = "$INITIAL_TIP"



        //seekbar listener
        seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged: $progress")
                tvTipPercentLabel.text = "$progress%"
                calcTipAndTotal()

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        //edittext listener
        etBaseAmount.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {

                Log.i(TAG, "afterTextChanged: text changed to ${s}")
                calcTipAndTotal()
            }

        })

    }

    private fun calcTipAndTotal() {
        //this method is used to 1. get value of base total and tip percentage. 2. calculate the tip
        //3. find total and update the user interface
        val baseAmount = etBaseAmount.text.toString().toDouble()
        //get tip %
        val tipPercentage = seekBarTip.progress
        //calc tip total
        val tipAmount = baseAmount*tipPercentage/100
        val totalAmount = baseAmount+ tipAmount

//        update ui
        tvTipAmount.text = tipAmount.toString()
        tvTotalAmount.text = totalAmount.toString()
    }

}