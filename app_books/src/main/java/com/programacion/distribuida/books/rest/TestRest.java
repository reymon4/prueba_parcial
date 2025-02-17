package com.programacion.distribuida.books.rest;


import jakarta.inject.Inject;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/test")
public class TestRest {

    @Inject//ESTA ANOTACION NO es necesaria en quarkus. Está poniendo todo lo estándar
    @ConfigProperty(name ="quarkus.http.port")
    private Integer port;

    @GET
    public String test() {

        Config cfg = ConfigProvider.getConfig();

        cfg.getConfigSources().forEach(c->{
           System.out.printf("%d - %s\n", c.getOrdinal(),c.getName());
        });

        var valor = cfg.getValue("ejemplo.mensaje", String.class);

        return String.format("Test Rest[%d]: %s", port, valor) ;
    }
}
