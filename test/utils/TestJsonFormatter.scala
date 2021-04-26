package utils

case class TestJsonFormatter(cr008Enabled: Boolean) extends JsonFormatters

object TestJsonFormatter {
  val cr008EnabledJsonFormatter: TestJsonFormatter = TestJsonFormatter(true)
  val cr008DisabledJsonFormatter: TestJsonFormatter = TestJsonFormatter(false)
}