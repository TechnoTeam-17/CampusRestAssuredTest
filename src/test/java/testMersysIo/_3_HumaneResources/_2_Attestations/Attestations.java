package testMersysIo._3_HumaneResources._2_Attestations;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import testMersysIo._4_Education._2_Login.Login;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class Attestations extends Login {


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
    public void createAttestation(){

        rndAttestation=randomUreteci.job().title()+"Document";


        Map<String, String> newAttestation=new HashMap<>();
        newAttestation.put("name",rndAttestation);


        AttestationID=
                given()
                        .spec(reqSpec)
                        .body(newAttestation)
                        //.log().all()
                        .when()
                        .post(url+"attestation")

                        .then()
                        //.log().body()
                        .statusCode(201)
                        .extract().path("id");

        System.out.println("Create Attestation Test: Successfully passed !");
    }

    @Test(priority = 2, dependsOnMethods = "createAttestation")
    public void  createAttestationNegative()
    {
        Map<String, String> newAttestation=new HashMap<>();
        newAttestation.put("name",rndAttestation);


        given()
                .spec(reqSpec)
                .body(newAttestation)

                .when()
                .post(url+"attestation")

                .then()
                //.log().body()
                .statusCode(400)
                .body("message", containsString("already"));

        System.out.println("Create Attestation Negative Test: Successfully passed !");
    }

    @Test(priority = 3, dependsOnMethods = "createAttestationNegative")
    public void updateAttestation(){
        String newAttestation2="UpdatedTeam17"+randomUreteci.number().digits(5);
        Map<String,String> updAttestation=new HashMap<>();
        updAttestation.put("id",AttestationID);
        updAttestation.put("name",newAttestation2);


        given()
                .spec(reqSpec)
                .body(updAttestation)

                .when()
                .put(url+"attestation")

                .then()
                //.log().body()
                .statusCode(200)
                .body("name", equalTo(newAttestation2));

        System.out.println("Update Attestation Test: Successfully passed !");
    }

    @Test(dependsOnMethods = "updateAttestation")
    public void deleteCategory()
    {
        given()
                .spec(reqSpec)
                .when()
                .delete(url+"attestation/"+AttestationID)

                .then()
                .log().body()
                .statusCode(204);
        System.out.println("Delete Attestation Test: Successfully passed !");
    }

    @Test(priority = 5, dependsOnMethods = "deleteCategory")
    public void deleteCategoryNegative()
    {
        given()
                .spec(reqSpec)
                .when()
                .delete(url+"attestation/"+AttestationID)
                .then()
                //.log().body()
                .statusCode(400)
                .body("message", equalTo("attestation not found"));

        System.out.println("Delete Attestation Negative Test: Successfully passed !");

    }
}
