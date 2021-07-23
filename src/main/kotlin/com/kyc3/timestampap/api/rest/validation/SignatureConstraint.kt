package com.kyc3.timestampap.api.rest.validation

import com.kyc3.timestampap.api.rest.validation.validator.SignatureConstraintValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [SignatureConstraintValidator::class])
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class SignatureConstraint(
  val message: String = "Invalid Signature",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = []
)
