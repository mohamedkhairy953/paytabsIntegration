package com.paytabs.sample.kotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paytabs.sample.kotlin.model.RequestModel
import com.paytabs.sample.kotlin.model.validators.InputValidationSealedClass
import com.paytabs.sample.kotlin.model.validators.InputValidatorImp
import com.paytabs.sample.kotlin.model.validators.InputsValidator

class MainViewModel : ViewModel() {
    var validationLD = MutableLiveData<InputValidationSealedClass>()
    private val validator: InputsValidator = InputValidatorImp(validationLD)

    fun onPayClicked(requestModel: RequestModel) {
        if (validateRequest(requestModel)) {
            validationLD.value = InputValidationSealedClass.ValidationPassed
        }
    }

    private fun validateRequest(requestModel: RequestModel): Boolean {
        return validator.validateMerchantEmail(requestModel.merchantEmail)
                &&
                validator.validateMerchantEmail(requestModel.merchantSecret)
    }
}