package ca.gbc.productservice;



import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;

// Tells Springs Boot to look for a main configuration class (@SpringBootApplication)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {
    // This annotation is used in combination with TestContainers to automatically configure the connection to
    //the Test MongoDbContainer
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @LocalServerPort
    private Integer port;

    //http://localhost:port/api/product

    @BeforeEach
    void setup(){
        RestAssured.baseURI= "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void createProductTest(){

    }
    @Test
    void getAllProductsTest(){

    }
}
