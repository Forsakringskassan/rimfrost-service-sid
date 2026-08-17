package se.fk.github.rimfrost.sid.presentation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import se.fk.rimfrost.sid.jaxrsspec.controllers.generatedsource.SidApi;
import se.fk.rimfrost.sid.jaxrsspec.controllers.generatedsource.model.SidStatusRequest;
import se.fk.rimfrost.sid.jaxrsspec.controllers.generatedsource.model.SidStatusResponse;

@Path("")
public class SidController implements SidApi
{
   @Override
   @POST
   @Path("/sid/status")
   @Consumes(
   {
         "application/json"
   })
   @Produces(
   {
         "application/json"
   })
   public SidStatusResponse getSidStatus(@Valid @NotNull SidStatusRequest sidStatusRequest)
   {
      var hasSidStatus = sidStatusRequest.getIndivider().stream().anyMatch(
            individ -> individ.getTypId().equals("c5f2e2b4-9143-4160-8f4b-30c172f0ac05") && individ.getVarde().endsWith("4444"));

      SidStatusResponse sidStatusResponse = new SidStatusResponse();
      sidStatusResponse.setSid(hasSidStatus);

      return sidStatusResponse;
   }
}
