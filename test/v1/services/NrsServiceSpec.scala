package v1.services

import utils.BaseSpec

class NrsServiceSpec extends BaseSpec {

    class TestConnector extends NrsConnector {
        def send[A](nrsPayload: NrsPayload): Future[NrsResponse] = Future.Success(Right(NrSubmissionId("123")))
    }

    val testConnector = new TestConnector
    val testService = new NrsService(testConnector)


  
}
