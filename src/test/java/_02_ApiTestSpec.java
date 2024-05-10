import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class _02_ApiTestSpec {


    String baseURI;

    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;
    @BeforeClass
    public void Setup(){

        baseURI = "https://gorest.co.in/public/v1";

        requestSpec= new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .log(LogDetail.URI)
                .build();
        responseSpec= new ResponseSpecBuilder()
                .expectStatusCode(200)
                .log(LogDetail.BODY)
                .expectContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void Test1() {
        given()
                // REQUEST ozellikleri

                .param("page", 1)  // ? page=1  burayi yazmadigimizda sadece asagidaki yorumu kaldirsak ayni sonuc verecektir
                // param: soru isareti ileparameter gonderecegim
                .log().uri()


                .when()
                .get("/users")
                //.get("https://gorest.co.in/public/v1/users")

                .then()
                .log().body()
        ;
    }
}
