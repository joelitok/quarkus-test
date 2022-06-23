package org.tetv;

import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;


@Path("/episodes")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class GreetingResource {

    @GET
    //@Produces(MediaType.APPLICATION_JSON)
    public List<Episode> episodes() {
        return Episode.listAll();
    }

    @GET
    @Path("/{episodeTitle}")
   // @Produces(MediaType.APPLICATION_JSON)
    public List<Episode> findEpisode(@PathParam("episodeTitle") String episodeTitle) {
        if(episodeTitle!=null){
            return Episode.find("title", episodeTitle).list();
        }
            return Episode.listAll();
    }


    @GET
    @Path("/find/{episodeId}")
    public Episode getSingleEpisode(@PathParam Long episodeId) {
        Episode entity = Episode.findById(episodeId);
        if (entity == null) {
            throw new WebApplicationException("Episode with id of " + episodeId + " does not exist..", 404);
        }
           return entity;
    }


    @PUT
    @Path("/update/{episodeId}")
    @Transactional
    public Episode updateEpisode(@PathParam Long episodeId, Episode episode) {
        if (episode.title == null) {
            throw new WebApplicationException("episode title was not set on request.", 422);
            }

        Episode entity = Episode.findById(episodeId);
        if (entity == null) {
            throw new WebApplicationException("episode with id of " + episodeId + " does not exist.", 404);
        }
        entity.title = episode.title;
        entity.description =episode.description;
        return entity;
    }


    
    @DELETE
    @Path("/delete/{episodeId}")
    @Transactional
    public Response deleteEpisode(@PathParam Long episodeId) {
        Episode entity = Episode.findById(episodeId);
        if (entity == null) {
            throw new WebApplicationException("Episode with id of " + episodeId + " does not exist.", 404);
        }
        entity.delete();
        return Response.status(204).build();
    }



    @POST
    //@Produces(MediaType.APPLICATION_JSON)
   // @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveEpisode(Episode episode){
        episode.persist();
        return Response.status(Status.CREATED).entity(episode).build();
    }





    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {
        @Override
        public Response toResponse(Exception exception) {
            int code = 500;
            if (exception instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }
        return Response.status(code)
                    .entity(Json.createObjectBuilder().add("error", exception.getMessage()).add("code", code).build())
                    .build();
        }
    }
    




}