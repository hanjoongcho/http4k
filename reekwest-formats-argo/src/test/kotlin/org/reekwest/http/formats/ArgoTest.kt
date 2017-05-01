package org.reekwest.http.formats

import com.natpryce.hamkrest.anything
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import org.junit.Test
import org.reekwest.http.core.Request.Companion.get
import org.reekwest.http.core.with
import org.reekwest.http.formats.Argo.json
import org.reekwest.http.lens.BiDiLensContract.checkContract
import org.reekwest.http.lens.BiDiLensContract.spec
import org.reekwest.http.lens.Body
import java.math.BigDecimal
import java.math.BigInteger

class ArgoTest {
    val j = Argo

    @Test
    fun `serializes object to j`() {
        val input = j.obj(listOf(
            "string" to j.string("value"),
            "double" to j.number(1.0),
            "long" to j.number(10L),
            "boolean" to j.boolean(true),
            "bigDec" to j.number(BigDecimal(1.2)),
            "bigInt" to j.number(BigInteger("12344")),
            "null" to j.nullNode(),
            "int" to j.number(2),
            "array" to j.array(listOf(
                j.string(""),
                j.number(123)
            ))
        ))
        val expected = """{"string":"value","double":1,"long":10,"boolean":true,"bigDec":1.1999999999999999555910790149937383830547332763671875,"bigInt":12344,"null":null,"int":2,"array":["",123]}"""
        assertThat(j.compact(input), equalTo(expected))
    }

    @Test
    fun `can write and read body as j`() {
        val body = Body.json().required()

        val obj = j.obj(listOf("hello" to j.string("world")))

        val request = get("/bob")

        val requestWithBody = request.with(body to obj)

        assertThat(requestWithBody.bodyString(), equalTo("""{"hello":"world"}"""))

        assertThat(body(requestWithBody), equalTo(obj))
    }

    @Test
    fun `can write and read spec as j`() {
        checkContract(spec.json(), """{"hello":"world"}""", j.obj("hello" to j.string("world")))
    }

    @Test
    fun `invalid j blows up parse`() {
        assertThat({ j.parse("") }, throws(anything))
    }

}