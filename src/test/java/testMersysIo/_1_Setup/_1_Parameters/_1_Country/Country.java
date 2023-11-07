package testMersysIo._1_Setup._1_Parameters._1_Country;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import testMersysIo._4_Education._2_Login.Login;

import java.net.CookieStore;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
public class Country extends Login {


        @BeforeClass
        public void Setup(){

            baseURI = "https://test.mersys.io/";
            Map<String, String> userCredential=new HashMap<>();
            userCredential.put("username",username);
            userCredential.put("password",password);
            userCredential.put("rememberMe","true");


            Cookies cookies=
                    given()
                            .body(userCredential)
                            .contentType(ContentType.JSON)
                            .when()
                            .post(urlLog)
                            .then()
                            //.log().all()
                            .statusCode(200)
                            .extract().response().getDetailedCookies();
            ;

            reqSpec = new RequestSpecBuilder()
                    .addCookies(cookies)
                    .setContentType(ContentType.JSON)
                    .build();

            System.out.println("Login Test: Successfully passed !");

        }

        @Test(priority = 1)
        public void createCountry(){

            rndCountryName=randomUreteci.address().country()+randomUreteci.address().countryCode();
            rndCountryCode= randomUreteci.address().countryCode();

            Map<String,String> newCountry=new HashMap<>();
            newCountry.put("name",rndCountryName);
            newCountry.put("code",rndCountryCode);

            countryID=
                    given()
                            .spec(reqSpec)
                            .body(newCountry)
                            //.log().all()
                            .when()
                            .post(url+"countries")

                            .then()
                            //.log().body()
                            .statusCode(201)
                            .extract().path("id");

            System.out.println("Create Country Test: Successfully passed !");
        }

        @Test(priority = 2, dependsOnMethods = "createCountry")
        public void  createCountryNegative()
        {
            Map<String,String> newCountry=new HashMap<>();
            newCountry.put("name",rndCountryName);
            newCountry.put("code",rndCountryCode);

            given()
                    .spec(reqSpec)
                    .body(newCountry)

                    .when()
                    .post(url+"countries")

                    .then()
                    //.log().body()
                    .statusCode(400)
                    .body("message", containsString("already"));

            System.out.println("Create Country Negative Test: Successfully passed !");
        }

        @Test(priority = 3, dependsOnMethods = "createCountryNegative")
        public void updateCountry(){
            String newCountryName="Updated Country"+randomUreteci.number().digits(5);
            Map<String,String> updCountry=new HashMap<>();
            updCountry.put("id",countryID);
            updCountry.put("name",newCountryName);
            updCountry.put("code","12345");

            given()
                    .spec(reqSpec)
                    .body(updCountry)

                    .when()
                    .put(url+"countries")

                    .then()
                    //.log().body()
                    .statusCode(200)
                    .body("name", equalTo(newCountryName));

            System.out.println("Update Country Test: Successfully passed !");
        }


        @Test(priority = 4, dependsOnMethods = "updateCountry")
        public void deleteCountry()
        {
            given()
                    .spec(reqSpec)
                    .when()
                    .delete(url+"countries/"+countryID)
                    .then()
                    //.log().body()
                    .statusCode(200);

            System.out.println("Delete Country Test: Successfully passed !");
        }

        @Test(priority = 5, dependsOnMethods = "deleteCountry")
        public void deleteCountryNegative()
        {
            given()
                    .spec(reqSpec)
                    .when()
                    .delete(url+"countries/"+countryID)
                    .then()
                    //.log().body()
                    .statusCode(400)
                    .body("message", equalTo("Country not found"));

            System.out.println("Delete Country Negative Test: Successfully passed !");

        }
    }

