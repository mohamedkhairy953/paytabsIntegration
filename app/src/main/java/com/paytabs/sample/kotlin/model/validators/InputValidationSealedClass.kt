package com.paytabs.sample.kotlin.model.validators

sealed class InputValidationSealedClass {
    object EmptyMerchantEmail : InputValidationSealedClass()
    object EmptySecret : InputValidationSealedClass()
    object ValidationPassed : InputValidationSealedClass()
}