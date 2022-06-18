package org.tetv;

import java.util.List;

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

@Path("/episodes")
public class GreetingResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Episode> episodes() {
        return Episode.listAll();
    }

    @GET
    @Path("/{episode}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Episode> findEpisode(@PathParam("episode") String episode) {
        if(episode!=null){
            return Episode.find("title", episode).list();
        }
            return Episode.listAll();
    }

    @GET
    @Path("/{episodeId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteEpisode(@PathParam("episodeId") Long episode){
        if(episode!=null){
            Episode.deleteById(episode);
            return "episode supprimer";
        }else{
            return "cette episode n existe pas";
        }
        
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveEpisode(Episode episode){
        episode.persist();
        return Response.status(Status.CREATED).entity(episode).build();
    }




    




}