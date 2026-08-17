import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import se.fk.rimfrost.sid.jaxrsspec.controllers.generatedsource.model.Idtyp;
import se.fk.rimfrost.sid.jaxrsspec.controllers.generatedsource.model.SidStatusRequest;
import se.fk.rimfrost.sid.jaxrsspec.controllers.generatedsource.model.SidStatusResponse;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class SidControllerTest
{
   @Test
   void should_return_400_on_null_individer_request()
   {
      var request = new SidStatusRequest();
      request.setIndivider(null);
      sendSidStatusRequest(request, 400);
   }

   @Test
   void should_return_400_on_null_request()
   {
      sendSidStatusRequest(null, 400);
   }

   @Test
   void should_return_400_on_null_id_typ_request()
   {
      var request = createSidStatusRequest(createIdtyp(null, "1234"));
      sendSidStatusRequest(request, 400);
   }

   @Test
   void should_return_400_on_null_varde_request()
   {
      var request = createSidStatusRequest(createIdtyp("1234", null));
      sendSidStatusRequest(request, 400);
   }

   @Test
   void should_return_false_on_empty_list()
   {
      var request = createSidStatusRequest();
      var response = sendSidStatusRequest(request);
      assertFalse(response.getSid());
   }

   @Test
   void should_return_false_on_no_4444_individ_list()
   {
      var request = createSidStatusRequest(createIdtyp("c5f2e2b4-9143-4160-8f4b-30c172f0ac05", "19900101-1234"));
      var response = sendSidStatusRequest(request);
      assertFalse(response.getSid());
   }

   @Test
   void should_return_false_on_4444_but_wrong_id_typ_individ_list()
   {
      var request = createSidStatusRequest(createIdtyp("c5f2e2b4-9143-4160-8f4b-30c172f0ac06", "19900101-4444"));
      var response = sendSidStatusRequest(request);
      assertFalse(response.getSid());
   }

   @Test
   void should_return_true_on_4444_individ_list()
   {
      var request = createSidStatusRequest(createIdtyp("c5f2e2b4-9143-4160-8f4b-30c172f0ac05", "19900101-4444"));
      var response = sendSidStatusRequest(request);
      assertTrue(response.getSid());
   }

   @Test
   void should_return_true_on_4444_individ_multiple_individ_list()
   {
      var request = createSidStatusRequest(createIdtyp("c5f2e2b4-9143-4160-8f4b-30c172f0ac05", "19900101-1234"),
            createIdtyp("c5f2e2b4-9143-4160-8f4b-30c172f0ac05", "19900101-4444"));
      var response = sendSidStatusRequest(request);
      assertTrue(response.getSid());
   }

   private SidStatusResponse sendSidStatusRequest(SidStatusRequest sidStatusRequest)
   {
      return given().contentType(ContentType.JSON).body(sidStatusRequest).post("/sid/status").then().statusCode(200).extract()
            .body().as(SidStatusResponse.class);
   }

   private void sendSidStatusRequest(SidStatusRequest sidStatusRequest, int expectedStatusCode)
   {
      var request = given().contentType(ContentType.JSON);

      if (sidStatusRequest != null)
      {
         request.body(sidStatusRequest);
      }

      request.post("/sid/status").then().statusCode(expectedStatusCode);
   }

   private Idtyp createIdtyp(String typ, String varde)
   {
      Idtyp idtyp = new Idtyp();
      idtyp.setTypId(typ);
      idtyp.setVarde(varde);
      return idtyp;
   }

   private SidStatusRequest createSidStatusRequest(Idtyp... individer)
   {
      var request = new SidStatusRequest();
      request.setIndivider(List.of(individer));
      return request;
   }
}
