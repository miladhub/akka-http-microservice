import akka.event.NoLogging
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.{HttpResponse, HttpRequest}
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.stream.scaladsl.Flow
import org.scalatest._

class ServiceSpec extends FlatSpec with Matchers with ScalatestRouteTest with Service {
  override def testConfigSource = "akka.loglevel = WARNING"
  override def config = testConfig
  override val logger = NoLogging

  "Service" should "respond to GET" in {
    Get(s"/who/there") ~> routes ~> check {
      status shouldBe OK
      contentType.toString() should startWith ("text/plain")
      responseAs[String] shouldBe "hello, there"
    }
  }

  it should "respond to POST" in {
    Post(s"/who", "lullaby") ~> routes ~> check {
      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[MyResponse] shouldBe MyResponse("goodbye, lullaby")
    }
  }
}
