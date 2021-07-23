package com.kyc3.timestampap.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.generated.Bytes2
import java.util.stream.Stream


internal class PackedParametersEncoderTest {

  private val packedParametersEncoder = PackedParametersEncoder()

  @ParameterizedTest
  @MethodSource("packedParametersData")
  fun `should convert parameters properly`(
    description: String,
    types: List<Type<out Any>>,
    expected: String
  ) {
    assertThat(packedParametersEncoder.encodeParameters(types))
      .isEqualTo(expected)
  }

  companion object {
    @JvmStatic
    fun packedParametersData(): Stream<Arguments> = Stream.of(
      Arguments.of(
        "Convert bytes2",
        listOf(Bytes2(listOf(0.toByte(), 1.toByte()).toByteArray())),
        "0001"
      ),
      Arguments.of(
        "Convert bytes2",
        listOf(Bytes2(listOf(1.toByte(), 1.toByte()).toByteArray())),
        "0101"
      ),
      Arguments.of(
        "Convert bytes2",
        listOf(Bytes2(listOf(255.toByte(), 255.toByte()).toByteArray())),
        "ffff"
      ),
      Arguments.of(
        "Convert bytes2",
        listOf(Bytes2(listOf(253.toByte(), 232.toByte()).toByteArray())),
        "fde8"
      ),
    )
  }
}