package com.paytabs.sample.kotlin.model.validators

import androidx.lifecycle.MutableLiveData
class InputValidatorImp(private val validatorLd: MutableLiveData<InputValidationSealedClass>) :
    InputsValidator {
    override fun validateMerchantEmail(email: String): Boolean {
      if(email.isEmpty()){
          validatorLd.value=InputValidationSealedClass.EmptyMerchantEmail
          return false
      }
        return true
    }

    override fun validateMerchantSecretKey(secretKey: String): Boolean {
        if(secretKey.isEmpty()){
            validatorLd.value=InputValidationSealedClass.EmptySecret
            return false
        }
        return true
    }
}