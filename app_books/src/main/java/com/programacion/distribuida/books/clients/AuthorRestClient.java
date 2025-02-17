package com.programacion.distribuida.books.clients;

import com.programacion.distribuida.books.dto.AuthorDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
//ANOTACION PARA QUE SEA EL PROXY AUTOMÁTICO
//@RegisterRestClient(configKey ="authors-api")
@RegisterRestClient(baseUri ="stork://authors-api")

public interface AuthorRestClient {

    @GET
    @Path("/{id}")
    @Retry(maxRetries = 3)
    @Fallback(fallbackMethod = "findByIdFallback")
    AuthorDTO findById(@PathParam("id") Integer id);

    default AuthorDTO findByIdFallback(Integer id){
        var dto = new AuthorDTO();
        dto.setId(-1);
        dto.setName("No");
        dto.setLastName("Available");
        return dto;
    }

}
