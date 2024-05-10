package Campus;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

import static org.hamcrest.Matchers.equalTo;

public class _06_CountryTest {

    RequestSpecification reqSpec;
    Faker randomUretici= new Faker();

    String countryCode="";

    String countryName="";
    String countryID="";

    @BeforeClass
    public void LoginCampus() {

        baseURI="https://test.mersys.io";


        String userCredential = "{\n" +
                "        \"username\": \"turkeyts\",\n" +
                "            \"password\": \"TechnoStudy123\",\n" +
                "            \"rememberMe\": true\n" +
                "    }";

        Map<String,String> userCredMap=new HashMap<>();
        userCredMap.put("username","turkeyts");
        userCredMap.put("password","TechnoStudy123");
        userCredMap.put("rememberMe","true");


        Cookies gelenCookies =
                given()
                        //.body(userCredential)
                        .body(userCredMap)
                        .contentType(ContentType.JSON)
                        .when()
                        .post("/auth/login")
                        .then()
                        //.log().all()
                        .statusCode(200)
                        .extract().response().getDetailedCookies();

        //System.out.println("gelenCookies = " + gelenCookies);

         reqSpec = new RequestSpecBuilder()
                .addCookies(gelenCookies)
                .setContentType(ContentType.JSON)
                .build();

    }

    @Test
    public void CreateCountry(){
        //burada gelen tooken ın yine cookies içinde geri gitmesi lazım :spec

        countryName= randomUretici.address().country()+randomUretici.address().countryCode()+ randomUretici.address().latitude();
        countryCode= randomUretici.address().countryCode();

        Map<String,String> newCountry= new HashMap<>();
        newCountry.put("name", countryName);
        newCountry.put("code", countryCode);

        countryID=

        given()

                .spec(reqSpec)  // gelen cookies, yeni istek için login olduğumun kanıtı olarak gönderildi.
                .body(newCountry)
                .when()
                .post("/school-service/api/countries")

                .then()
                .log().body()
                .statusCode(201)
                .extract().path("id")
        ;

    }

    @Test(dependsOnMethods = "CreateCountry")
    public void CreateCountryNegative(){
        //burada gelen tooken ın yine cookies içinde geri gitmesi lazım:spec

        Map<String, String> reNewCountry = new HashMap<>();
        reNewCountry.put("name", countryName);
        reNewCountry.put("code", countryCode);

        given()

                .spec(reqSpec)  // gelen cookies, yeni istek için login olduğumun kanıtı olarak gönderildi.
                .body(reNewCountry)
                .when()
                .post("/school-service/api/countries")

                .then()
                .log().body()
                .statusCode(400)
        ;
    }

    @Test(dependsOnMethods = "CreateCountryNegative") // country i guncelle adli bu class CreateCountryNegative adli test clasina bagli ularak calisacaktir.
    public void UpdateCountry(){
        //burada gelen tooken ın yine cookies içinde geri gitmesi lazım:spec
        String updCountryName="ismet"+ randomUretici.address().country()+randomUretici.address().latitude();

        Map<String,String> updCountry= new HashMap<>();
        updCountry.put("id", countryID);
        updCountry.put("name", updCountryName);
        updCountry.put("code",countryCode);

        given()
                .spec(reqSpec)
                .body(updCountry)
                .when()
                .put("/school-service/api/countries")

                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo(updCountryName))
                ;
    }

    // Delete Country i Testini yapınız.

    @Test(dependsOnMethods = "UpdateCountry")
    public  void  DeleteCountry()
    {

        given()
                .spec(reqSpec)

                .when()
                .delete("/school-service/api/countries/"+countryID)

                .then()
                .statusCode(200)
        ;
    }

    // Delete Country Negatif testini yapınız
    @Test(dependsOnMethods = "DeleteCountry")
    public  void  DeleteCountryNegative()
    {

        given()
                .spec(reqSpec)

                .when()
                .delete("/school-service/api/countries/"+countryID)

                .then()
                .log().all()
                .statusCode(400)
        ;
    }

    // TODO : CitizenShip in API testini yapınız




}