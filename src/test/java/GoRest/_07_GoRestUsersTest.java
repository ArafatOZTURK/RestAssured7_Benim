package GoRest;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class _07_GoRestUsersTest {
    // Token ı aldım ,
    // usersCreate için neler lazım, body(user bilgileri)
    // enpoint i aldım gidiş metodu
    // BeforeClass ın içinde yapılacaklar var mı? nelerdir ?  url set ve spec hazırlanmalı

    Faker randomUretici= new Faker();
    int userID=0;
    RequestSpecification reqSpec;

    @BeforeClass
    public void Setup()
    {
         baseURI = "https://gorest.co.in/public/v2/users";

         reqSpec=new RequestSpecBuilder()
                 .addHeader("Authorization", "Bearer 86283309a7c8d1c29ebf32dc74394f27002a9e45862f6af8198e516ba0145b16" )
                 .setContentType(ContentType.JSON)
                 .build();

    }
    // Create User Testini yapınız

    // bu sorunun cozumu asagidadir

    @Test
    public void CreateUser(){
        //burada gelen tooken ın yine cookies içinde geri gitmesi lazım :spec
        String rndFullName= randomUretici.name().fullName();
        String rndEmail= randomUretici.internet().emailAddress();

        Map<String,String> newUser= new HashMap<>();
        newUser.put("name", rndFullName);
        newUser.put("email", rndEmail);
        newUser.put("gender", "female");
        newUser.put("status", "active");

        userID=
                given()

                        .spec(reqSpec)  // gelen cookies, yeni istek için login olduğumun kanıtı olarak gönderildi.
                        // http ile baslamiyorsa baseURI gecerli demektir
                        .body(newUser)
                        .when()
                        .post("")
                        // .post("https://gorest.co.in/public/v2/users")  yukardaki baseURI NI kullanmiyorsak direk bunu kullanabiliriz

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;

    }

    // GetUserById testini yapiniz

    @Test(dependsOnMethods = "CreateUser")
    public void GetUserById()
    {
        given()

                .spec(reqSpec)
                .when()
                .get("/"+userID)
                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(userID))

        ;
    }

    // updateUser testini yapiniz

    @Test(dependsOnMethods = "GetUserById")
    public  void  UpdateUser()
    {

        String updName="Ismet Temur";

        Map<String,String> updUser= new HashMap<>();
        updUser.put("name", updName);



        given()
                .spec(reqSpec)
                .body(updUser)

                .when()
                .put("/"+userID)

                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(userID))
                .body("name", equalTo(updName))
        ;
    }

    // DeleteUser testini yapiniz

    @Test(dependsOnMethods = "UpdateUser")
    public void DeleteUser()
    {
        given()
                .spec(reqSpec)

                .when()
                .delete("/"+userID)

                .then()
                //.log().body()
                .statusCode(204)
        ;

    }

    // DeleteUserNegative testini yapınız
    @Test(dependsOnMethods = "DeleteUser")
    public void DeleteUserNegative()
    {
        given()
                .spec(reqSpec)

                .when()
                .delete("/"+userID)

                .then()
                //.log().body()
                .statusCode(404)
        ;

    }











}
