plugins {
    java
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "ca.gbc"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(22)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
}

extra["springCloudVersion"] = "2024.0.0-RC1"

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-gateway-mvc")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-oauth2-resource-server
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-security
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j:3.1.2")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")
    implementation("org.springframework.boot:spring-boot-starter-actuator")


    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
// https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
// https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-api
    testImplementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.6.0")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
