plugins {
    id("java")
    id("io.quarkus") version "3.17.0"
    id("io.freefair.lombok") version "8.11"

}

group = "com.programacion.distribuida"
version = "unspecified"

var quarkusVersion = "3.17.0"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(enforcedPlatform("io.quarkus.platform:quarkus-bom:${quarkusVersion}"))

    //CDI
    implementation("io.quarkus:quarkus-arc")

    //REST
    implementation(("io.quarkus:quarkus-rest"))

    //JSON

    implementation(("io.quarkus:quarkus-rest-jsonb"))
    implementation(("io.quarkus:quarkus-rest-client"))
    implementation(("io.quarkus:quarkus-rest-client-jsonb"))
    //JPA
    implementation("io.quarkus:quarkus-hibernate-orm-panache")

    implementation("io.quarkus:quarkus-jdbc-postgresql")
    implementation("org.postgresql:postgresql:42.7.4")

    //Registro
    implementation("io.smallrye.reactive:smallrye-mutiny-vertx-consul-client")

    //Discovery
    implementation("io.smallrye.stork:stork-service-discovery-consul")
//Health -> Actuator de Spring
    implementation("io.quarkus:quarkus-smallrye-health")

    //Metrics
    //implementation("io.quarkus:quarkus-micrometer-registry-prometheus")
   // implementation("io.quarkus:quarkus-jackson")

    //TELEMETRY
    //implementation("io.quarkus:quarkus-opentelemetry")
    //fAULT TOLERANCE
    implementation("io.quarkus:quarkus-smallrye-fault-tolerance")
    //Control de versiones DB
    implementation("io.quarkus:quarkus-flyway")
    implementation("org.flywaydb:flyway-database-postgresql")

    //PRUEBA PARCIAL - OPEN API
    implementation("io.quarkus:quarkus-smallrye-openapi")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}