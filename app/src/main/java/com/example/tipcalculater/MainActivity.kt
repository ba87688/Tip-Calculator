package com.example.tipcalculater

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
import com.example.tipcalculater.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputLayout

private const val TAG = "MainActivity"
private const val INITIAL_TIP = 15

class MainActivity : AppCompatActivity() {
    //viewBiding
    private lateinit var binding: ActivityMainBinding
    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var totalPerPerson: TextView
    private lateinit var perPersonLabel: TextView
    private lateinit var tvTipDescribe: TextView
    private lateinit var numberOfPeople: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        //edittext for bill amount
        etBaseAmount = binding.etBaseAmount
        //tip seeker bar
        seekBarTip = binding.tipSeekBar

        //actual tip
        tvTipAmount = binding.textViewTipAmount

        //total amount text after tip calculation
        tvTotalAmount = binding.textViewTotal

        tvTipPercentLabel = binding.percentageTip
        numberOfPeople = binding.autoCompleteTextView


        totalPerPerson = binding.totalPerPerson
        perPersonLabel = binding.perPersonLabel

        tvTipDescribe = binding.tipTipDescription
        //set initial value
        seekBarTip.progress = INITIAL_TIP
        tvTipPercentLabel.text = "$INITIAL_TIP%"
        tipDescriptionUpdate(INITIAL_TIP)
        totalPerPerson.text = ""
//        totalPerPerson.text = "${numberOfPeople.text.toString().toInt()}"


        //Hide keyboard when clickout of edittext
        binding.root.setOnClickListener {
            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).apply {
                hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            }
            etBaseAmount.clearFocus()
        }

        //seekbar listener
        seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged: $progress")
                tvTipPercentLabel.text = "$progress%"
                calcTipAndTotal()
                tipDescriptionUpdate(progress)

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        //edittext listener
        etBaseAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {

                Log.i(TAG, "afterTextChanged: text changed to ${s}")
//                System.out.println(etBaseAmount.text.toString().toDouble())
                calcTipAndTotal()
            }

        })

        numberOfPeople.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged: INSIDE 3d stuff ${s}")
                calcTipAndTotal()
            }

        })


    }

    private fun tipDescriptionUpdate(amountOfTip:Int) {
        val description = when(amountOfTip){
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"

        }
        tvTipDescribe.text = description

        //color update
        var color = ArgbEvaluator().evaluate(
            amountOfTip.toFloat()/seekBarTip.max,
            ContextCompat.getColor(this,R.color.color_worst_tip),
            ContextCompat.getColor(this,R.color.color_best_tip)
        ) as Int
        tvTipDescribe.setTextColor(color)
    }

    private fun calcTipAndTotal() {
        if (etBaseAmount.text.isEmpty()) {
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            return

        } else {
            //this method is used to 1. get value of base total and tip percentage. 2. calculate the tip
            //3. find total and update the user interface
            val baseAmount = etBaseAmount.text.toString().toDouble()
            //get tip %
            val tipPercentage = seekBarTip.progress
            //calc tip total
            val tipAmount = baseAmount * tipPercentage / 100
            var totalAmount = baseAmount + tipAmount

            var numberOfPpl = numberOfPeople.text.toString().toInt()


//        update ui
            tvTipAmount.text = "%.2f".format(tipAmount)
            tvTotalAmount.text = "%.2f".format(totalAmount)

            val perPersonAmount = "%.2f".format(totalAmount / numberOfPpl)
            totalPerPerson.text = perPersonAmount.toString()

        }
    }


    override fun onResume() {
        super.onResume()
        val languages = resources.getStringArray(R.array.number_of_people)
        val arrayAdapter = ArrayAdapter(this, R.layout.drop_down_item, languages)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
    }

}