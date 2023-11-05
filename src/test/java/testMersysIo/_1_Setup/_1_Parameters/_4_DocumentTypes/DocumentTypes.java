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

        DocumentID =
                given()
                        .spec(reqSpec)
                        .body("{\n" +
                                "    \"id\":null,\n" +
                                "    \"name\": \"Team17\",\n" +
                                "    \"description\": \"\",\n" +
                                "    \"attachmentStages\": [\n" +
                                "        \"STUDENT_REGISTRATION\"\n" +
                                "    ],\n" +
                                "    \"schoolId\": \"6390f3207a3bcb6a7ac977f9\",\n" +
                                "    \"active\": true,\n" +
                                "    \"required\": true,\n" +
                                "    \"translateName\": [],\n" +
                                "    \"useCamera\": false\n" +
                                "}")

                        //.log().all()
                        .when()
                        .post(url + "attachments/create")

                        .then()
                        // .log().body()
                        .statusCode(201)
                        .extract().path("id");
        System.out.println("Create Document Type Test: Successfully passed !");
    }

    @Test(dependsOnMethods = "createDocumentTypes")
    public void createDocumentNegative() {

        given()
                .spec(reqSpec)
                .body("{\n" +
                        "    \"id\":null,\n" +
                        "    \"name\": \"Team17\",\n" +
                        "    \"description\": \"\",\n" +
                        "    \"attachmentStages\": [\n" +
                        "        \"Certificate\"\n" +
                        "    ],\n" +
                        "    \"schoolId\": \"6390f3207a3bcb6a7ac977f9\",\n" +
                        "    \"active\": true,\n" +
                        "    \"required\": true,\n" +
                        "    \"translateName\": [],\n" +
                        "    \"useCamera\": false\n" +
                        "}")

                .when()
                .post(url + "attachments/create")

                .then()
                //.log().body()
                .statusCode(400)
                .extract().path("id");
        System.out.println("Create Document Type Negative Test: Successfully passed !");
    }

    @Test(dependsOnMethods = "createDocumentNegative")
    public void updateDocument() {
        given()
                .spec(reqSpec)
                .body("{\n" +
                        "  \"id\": \"" + DocumentID + "\",\n" +
                        "    \"name\": \"TechnoTeam17\",\n" +
                        "    \"description\": \"\",\n" +
                        "    \"attachmentStages\": [\n" +
                        "        \"EMPLOYMENT\"\n" +
                        "    ],\n" +
                        "    \"schoolId\": \"6390f3207a3bcb6a7ac977f9\",\n" +
                        "    \"active\": true,\n" +
                        "    \"required\": true,\n" +
                        "    \"translateName\": [],\n" +
                        "    \"useCamera\": false\n" +
                        "}")
                .when()
                .put(url + "attachments")
                .then()
                // .log().body()
                .statusCode(200);
        System.out.println("Update Document Type  Test: Successfully passed !");
    }

    @Test(dependsOnMethods = "updateDocument")
    public void deleteDocument() {
        given()
                .spec(reqSpec)
                .when()
                .delete(url + "attachments/" + DocumentID)
                .then()
                //.log().body()
                .statusCode(200);
        System.out.println("Delete Document Type  Test: Successfully passed !");
    }

    @Test(dependsOnMethods = "deleteDocument")
    public void deleteDocumentNegative() {
        given()
                .spec(reqSpec)
                .when()
                .delete(url + "attachments/" + DocumentID)
                .then()
                //.log().body()
                .statusCode(400)
                .body("message", equalTo("Attachment Type not found"));
     System.out.println("Delete Document Type Negative Test: Successfully passed !");
}


}