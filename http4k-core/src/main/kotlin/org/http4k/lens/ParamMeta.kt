package org.http4k.lens

enum class ParamMeta(val value: String) {
    StringParam("string"),
    ObjectParam("object"),
    BooleanParam("boolean"),
    IntegerParam("integer"),
    FileParam("file"),
    NumberParam("number"),
    NullParam("null")
}