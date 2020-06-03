package com.paytabs.sample.kotlin.model.validators

import javax.crypto.SecretKey

interface InputsValidator {
    fun validateMerchantEmail(email :String):Boolean
    fun validateMerchantSecretKey(secretKey: String):Boolean
}