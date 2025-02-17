package com.programacion.distribuida.authors;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.vertx.ext.consul.CheckOptions;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.ext.consul.ServiceOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.consul.ConsulClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.InetAddress;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class AuthorsLifecycle {

    @Inject
    @ConfigProperty(name = "consul.host", defaultValue = "127.0.0.1")
    String consulIp;

    @Inject
    @ConfigProperty(name = "consul.port", defaultValue = "8500")
    Integer consultPort;

    @Inject
    @ConfigProperty(name = "quarkus.http.port")
    Integer appPort;

    String serviceId;


    public void init(@Observes StartupEvent event, Vertx vertx) throws Exception {
        System.out.println("Iniciando servicio");
        ConsulClient client = ConsulClient.create(vertx,
                new ConsulClientOptions()
                        .setHost(consulIp)
                        .setPort(consultPort));

        serviceId = UUID.randomUUID().toString();
        var ipAddress = InetAddress.getLocalHost();

        client.registerServiceAndAwait(new ServiceOptions()
                .setName("app-authors")
                .setId(serviceId)
                .setAddress(ipAddress.getHostAddress())
                .setPort(appPort)
                .setTags(
                        List.of("traefik.enable=true",
                                "traefik.http.routers.router-app-authors.rule=PathPrefix(`/app-authors`)",
                                "traefik.http.routers.router-app-authors.middlewares=middleware-app-authors",
                                "traefik.http.middlewares.middleware-app-authors.stripPrefix.prefixes=/app-authors")
                ).setCheckOptions(new CheckOptions().setHttp("http://" + ipAddress.getHostAddress() + ":" + appPort + "/q/health/live")
                        .setInterval("10s").setDeregisterAfter("20s"))
        );
    }

    public void stop(@Observes ShutdownEvent event, Vertx vertx) throws Exception {
        System.out.println("Deteniendo servicio...");
        ConsulClient client = ConsulClient.create(vertx,
                new ConsulClientOptions()

                        .setHost(consulIp)
                        .setPort(consultPort));

        client.deregisterServiceAndAwait(serviceId);
    }
}
