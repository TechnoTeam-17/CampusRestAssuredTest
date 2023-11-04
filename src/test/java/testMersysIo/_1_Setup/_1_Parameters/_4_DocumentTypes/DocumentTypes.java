package testMersysIo._1_Setup._1_Parameters._4_DocumentTypes;

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

public class DocumentTypes extends Login {

    @BeforeClass
    public void Setup() {
        baseURI = "https://test.mersys.io/";

        Map<String, String> userCredential = new HashMap<>();
        userCredential.put("username", username);
        userCredential.put("password", password);
        userCredential.put("rememberMe", "true");

        Cookies cookies =
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

    @Test
    public void createDocumentTypes() {

        rndDocumentName = randomUreteci.name().firstName();

        Map<String, String> newDocument = new HashMap<>();
        newDocument.put("name", rndDocumentName);
        //newDocument.("code", rndDocumentCode);
        //JsonPath jsonPath = new JsonPath(response);
        //String selectedValue = jsonPath.get("path.to.desired.field");
        //newDocument.select("name", "CERTIFICATE");

        DocumentID =
                given()
                        .spec(reqSpec)
                        .body(newDocument)
                        //.log().all()
                        .when()
                        .post(url + "attachments/create")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");
        ;
    }

    @Test(dependsOnMethods = "createDocumentTypes")
    public void createDocumentNegative() {
        Map<String, String> newDocument = new HashMap<>();
        newDocument.put("name", rndDocumentName);
        //SELECT?

        given()
                .spec(reqSpec)
                .body(newDocument)

                .when()
                .post(url+"attachments/create")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already"))
        ;
    }

    @Test(dependsOnMethods = "createDocumentNegative")
    public void updateDocument() {
        String newDocumentName = "Updated Document" + randomUreteci.number().digits(5);
        Map<String, String> updDocument = new HashMap<>();
        updDocument.put("id", DocumentID);
        updDocument.put("name", newDocumentName);


        given()
                .spec(reqSpec)
                .body(updDocument)

                .when()
                .put(url+"attachments")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(newDocumentName))
        ;
    }

    @Test(dependsOnMethods = "updateDocument")
    public void deleteDocument() {
        given()
                .spec(reqSpec)
                .when()
                .delete(url + "attachments/" + DocumentID)

                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "deleteDocument")
    public void deleteDocumentNegative() {
        given()
                .spec(reqSpec)
                .when()
                .delete(url + "attachments/" + DocumentID)

                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("Country not found"))
        ;
    }

}