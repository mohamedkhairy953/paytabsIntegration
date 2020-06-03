package com.paytabs.sample.kotlin

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.paytabs.paytabs_sdk.payment.ui.activities.PayTabActivity
import com.paytabs.paytabs_sdk.utils.PaymentParams
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.paytabs.sample.kotlin.model.RequestModel
import com.paytabs.sample.kotlin.model.validators.InputValidationSealedClass
import com.paytabs.sample.kotlin.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var requestModel: RequestModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tiet_merchant_email.removeErrorWhenEditing(til_merchant_email)
        tiet_merchant_secret.removeErrorWhenEditing(til_merchant_secret)

        findViewById<Button>(R.id.pay).setOnClickListener {
            requestModel = RequestModel(
                tiet_merchant_email.getStringValue(),
                tiet_merchant_secret.getStringValue()
            )
            viewModel.onPayClicked(
                requestModel
            )
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.validationLD.observe(this, Observer {
            when (it) {
                InputValidationSealedClass.EmptySecret -> {
                    til_merchant_secret.isErrorEnabled = true
                    til_merchant_secret.requestFocus()
                    til_merchant_secret.error = "Empty secret"
                }
                InputValidationSealedClass.EmptyMerchantEmail -> {
                    til_merchant_email.isErrorEnabled = true
                    til_merchant_email.requestFocus()
                    til_merchant_email.error = "Empty email"
                }
                InputValidationSealedClass.ValidationPassed -> {
                    goPayment()
                }
            }
        })
    }

    private fun goPayment() {
        val intent = Intent(applicationContext, PayTabActivity::class.java)
        intent.putExtra(PaymentParams.MERCHANT_EMAIL, requestModel.merchantEmail)
        intent.putExtra(
            PaymentParams.SECRET_KEY,
            requestModel.merchantSecret
        )//Add your Secret Key Here
        intent.putExtra(PaymentParams.LANGUAGE, PaymentParams.ENGLISH)
        intent.putExtra(PaymentParams.TRANSACTION_TITLE, "Test Paytabs android library")
        intent.putExtra(PaymentParams.AMOUNT, 55.0)
        intent.putExtra(PaymentParams.CURRENCY_CODE, tiet_currency.getStringValue())
        intent.putExtra(PaymentParams.CUSTOMER_PHONE_NUMBER, tiet_phone_number.getStringValue())
        intent.putExtra(PaymentParams.CUSTOMER_EMAIL, tiet_cust_email.getStringValue())
        intent.putExtra(PaymentParams.ORDER_ID, tiet_order_id.getStringValue())
        intent.putExtra(PaymentParams.PRODUCT_NAME, tiet_products.getStringValue())

        //Billing Address
        intent.putExtra(PaymentParams.ADDRESS_BILLING, tiet_address.getStringValue())
        intent.putExtra(PaymentParams.CITY_BILLING, tiet_city.getStringValue())
        intent.putExtra(PaymentParams.STATE_BILLING, tiet_state.getStringValue())
        intent.putExtra(PaymentParams.COUNTRY_BILLING, tiet_currency.getStringValue())
        intent.putExtra(
            PaymentParams.POSTAL_CODE_BILLING,
            tiet_postal.getStringValue()
        ) //Put Country Phone code if Postal code not available '00973'

        //Shipping Address
        intent.putExtra(PaymentParams.ADDRESS_SHIPPING, tiet_address_shipping.getStringValue())
        intent.putExtra(PaymentParams.CITY_SHIPPING, tiet_city_shipping.getStringValue())
        intent.putExtra(PaymentParams.STATE_SHIPPING, tiet_state_shipping.getStringValue())
        intent.putExtra(PaymentParams.COUNTRY_SHIPPING, tiet_country_shipping.getStringValue())
        intent.putExtra(
            PaymentParams.POSTAL_CODE_SHIPPING,
            tiet_postal_shipping.getStringValue()
        ) //Put Country Phone code if Postal code not available '00973'

        //Payment Page Style
        intent.putExtra(PaymentParams.PAY_BUTTON_COLOR, "#2474bc")

        //Tokenization
        intent.putExtra(PaymentParams.IS_TOKENIZATION, true)
        startActivityForResult(intent, PaymentParams.PAYMENT_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PaymentParams.PAYMENT_REQUEST_CODE) {

            Log.d("Tag", data!!.getStringExtra(PaymentParams.RESPONSE_CODE))
            Log.d("Tag", data.getStringExtra(PaymentParams.TRANSACTION_ID))
            startActivity(Intent(this, SuccessActivity::class.java))
            if (data.hasExtra(PaymentParams.TOKEN) && !data.getStringExtra(PaymentParams.TOKEN)!!
                    .isEmpty()
            ) {
                Log.d("Tag", data.getStringExtra(PaymentParams.TOKEN))
                Log.d("Tag", data.getStringExtra(PaymentParams.CUSTOMER_EMAIL))
                Log.d("Tag", data.getStringExtra(PaymentParams.CUSTOMER_PASSWORD))
            }
        }
    }
}

fun TextInputEditText.getStringValue() = text.toString().trim()

fun TextInputEditText.removeErrorWhenEditing(til: TextInputLayout) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            til.error = null
            til.isErrorEnabled = false
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    })
}