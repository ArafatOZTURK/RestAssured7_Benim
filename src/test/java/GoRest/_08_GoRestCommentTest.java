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


public class _08_GoRestCommentTest {




//     {
//        "id": 96434,
//        "post_id": 124985,
//        "name": "Vijay Ganaka",
//        "email": "ganaka_vijay@rau.test",
//        "body": "Rem dolores alias. Dolorum earum deleniti."
//    },

    Faker randomUretici= new Faker();
    int commentID=0;
    RequestSpecification reqSpec;


    @BeforeClass
    public void setUp() {

        baseURI = "https://gorest.co.in/public/v2/comments";

        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer 86283309a7c8d1c29ebf32dc74394f27002a9e45862f6af8198e516ba0145b16")
                .setContentType(ContentType.JSON)
                .build();
    }

    // soru: CreateComment testinig yapiniz
    @Test
    public void CreateComment() {

        String fullName= randomUretici.name().fullName();
        String email= randomUretici.internet().emailAddress();
        String body= randomUretici.lorem().paragraph();
        String postId="124985";

        Map<String, String> newComment= new HashMap<>();
        newComment.put("name", fullName);
        newComment.put("email", email);
        newComment.put("body", body);
        newComment.put("post_id", postId);

        commentID=

        given()
                .spec(reqSpec)
                .body(newComment)

                .when()
                .post("")

                .then()
                .log().body()
                .statusCode(201)
                .extract().path("id")

                ;
            System.out.println("commentID = " + commentID);

    }

    // soru: Create edilen commet i GetCommentById testi cagirarak id sinin kontrolunu yapiniz

    @Test(dependsOnMethods = "CreateComment")
    public void GetCommentById(){

        given()
                .spec(reqSpec)
                .when()
                .get(""+commentID)

                .then()
                .log().body()
                .body("id", equalTo(commentID))

        ;


    }

    // soru: Create edilen comment in name ini guncelleyiniz

    @Test(dependsOnMethods = "GetCommentById")
    public void UpdateComment(){
        String uptName="ismet_"+randomUretici.name().fullName();
        Map<String, String> uptComment= new HashMap<>();
        uptComment.put("name", uptName);



        given()
                .spec(reqSpec)
                .body(uptComment)
                .when()
                .put(""+commentID)

                .then()
                .log().body()
                .body("name", equalTo(uptName))

        ;

    }

    @Test(dependsOnMethods = "UpdateComment")
    public void DeleteComment()
    {
        given()
                .spec(reqSpec)

                .when()
                .delete("/"+commentID)

                .then()
                .log().body()
                .statusCode(204)
        ;

    }

    // DeleteUserNegative testini yapınız
    @Test(dependsOnMethods = "DeleteComment")
    public void DeleteCommentNegative()
    {
        given()
                .spec(reqSpec)

                .when()
                .delete("/"+commentID)

                .then()
                .log().body()
                .statusCode(404)
        ;

    }




}
