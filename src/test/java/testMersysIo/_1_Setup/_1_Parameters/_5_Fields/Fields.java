package testMersysIo._1_Setup._1_Parameters._5_Fields;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import testMersysIo._4_Education._1_SubjectCategories._2_Login;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class Fields extends _2_Login {
    Map<String, String> newField = new HashMap<>();

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
    public void createNewField() {

        rndFieldName = "field-" + Faker.number().digits(3);
        rndFieldCode = Faker.number().digits(5);
        newField.put("name", rndFieldName);
        newField.put("code", rndFieldCode);
        newField.put("type", "STRING");
        newField.put("schoolId", "6390f3207a3bcb6a7ac977f9");


        FieldID =

                given()
                        .spec(reqSpec)
                        .body(newField)
                        //.log().all()
                        .when()
                        .post(url + "entity-field")

                        .then()
                        // .log().body()
                        .statusCode(201)
                        .extract().path("id");
        System.out.println("Create New Field Test: Successfully passed !");
    }

    @Test
    public void createFieldNegative() {

        newField.put("name", rndFieldName);
        newField.put("code", rndFieldCode);

        given()
                .spec(reqSpec)
                .body(newField)

                .when()
                .put(url + "entity-field")

                .then()
                //.log().body()
                .statusCode(400)
                .body("message", containsString("already exists"));

        System.out.println("Create Field Negative Test: Successfully passed !");
    }

    @Test
    public void updateField() {

        rndFieldName = "field-" + Faker.number().digits(2);
        rndFieldCode = Faker.number().digits(3);
        newField.put("name", rndFieldName);
        newField.put("code", rndFieldCode);
        newField.put("id", FieldID);

        given()
                .spec(reqSpec)
                .body(newField)
                .when()
                .put(url + "entity-field")
                .then()
                // .log().body()
                .statusCode(200)
                .body("name", equalTo(newField));
        System.out.println("Update Field Test: Successfully passed !");
    }

    @Test
    public void deleteField() {
        given()
                .spec(reqSpec)
                .when()
                .delete(url + "entity-field/" + FieldID)
                .then()
                //.log().body()
                .statusCode(204);
        System.out.println("Delete Field Type Test:Successfully passed !");
    }

    @Test
    public void deleteFieldNegative() {
        given()
                .spec(reqSpec)
                .when()
                .delete(url + "attachments/" + FieldID)
                .then()
                //.log().body()
                .statusCode(400)
                .body("message", equalTo("EntityField not found"));
        System.out.println("Delete Field Negative Test: Successfully passed !");
    }

}
