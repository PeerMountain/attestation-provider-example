package com.kyc3.timestampap.api.rest.validation.validator

import com.kyc3.timestampap.api.rest.model.AttestationDataDto
import com.kyc3.timestampap.api.rest.validation.SignatureConstraint
import com.kyc3.timestampap.service.Web3JService
import org.springframework.stereotype.Component
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

@Component
class SignatureConstraintValidator(
  private val web3JService: Web3JService
) : ConstraintValidator<SignatureConstraint, AttestationDataDto> {
  override fun isValid(value: AttestationDataDto?, context: ConstraintValidatorContext?): Boolean =
    value?.let {  web3JService.verifySignature(it.challenge, it.signature, it.userAddress) }
      ?: true

}