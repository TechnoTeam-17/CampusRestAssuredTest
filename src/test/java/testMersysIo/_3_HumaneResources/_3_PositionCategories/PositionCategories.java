package testMersysIo._3_HumaneResources._3_PositionCategories;

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

public class PositionCategories extends Login {

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
    public void createPositionCategories(){

        rndPositionCategory=randomUreteci.job().position()+"Team17";


        Map<String, String> newPositionCategory=new HashMap<>();
        newPositionCategory.put("name",rndPositionCategory);


        categoryID=
                given()
                        .spec(reqSpec)
                        .body(newPositionCategory)
                        //.log().all()
                        .when()
                        .post(url+"position-category")

                        .then()
                        //.log().body()
                        .statusCode(201)
                        .extract().path("id");

        System.out.println("Create Position Category Test: Successfully passed !");
    }

    @Test(priority = 2, dependsOnMethods = "createPositionCategories")
    public void  createPositionCategoriesNegative()
    {
        Map<String, String> newPositionCategory=new HashMap<>();
        newPositionCategory.put("name",rndPositionCategory);


        given()
                .spec(reqSpec)
                .body(newPositionCategory)

                .when()
                .post(url+"position-category")

                .then()
                //.log().body()
                .statusCode(400)
                .body("message", containsString("already"));

        System.out.println("Create Position Category Negative Test: Successfully passed !");
    }

    @Test(priority = 3, dependsOnMethods = "createPositionCategoriesNegative")
    public void updatePositionCategory(){
        String newPositionCategory2="UpdatedTeam17"+randomUreteci.number().digits(5);
        Map<String,String> updCategory=new HashMap<>();
       updCategory.put("id",categoryID);
       updCategory.put("name",newPositionCategory2);


        given()
                .spec(reqSpec)
                .body(updCategory)

                .when()
                .put(url+"position-category")

                .then()
                //.log().body()
                .statusCode(200)
                .body("name", equalTo(newPositionCategory2));

        System.out.println("Update Category Test: Successfully passed !");
    }

    @Test(dependsOnMethods = "updatePositionCategory")
    public void deleteCategory()
    {
        given()
                .spec(reqSpec)
                .when()
                .delete(url+"position-category/"+categoryID)

                .then()
                .log().body()
                .statusCode(204);
        System.out.println("Delete Category Test: Successfully passed !");
    }

    @Test(priority = 5, dependsOnMethods = "deleteCategory")
    public void deleteCategoryNegative()
    {
        given()
                .spec(reqSpec)
                .when()
                .delete(url+"position-category/"+categoryID)
                .then()
                //.log().body()
                .statusCode(400)
                .body("message", equalTo("PositionCategory not  found"));

        System.out.println("Delete Category Negative Test: Successfully passed !");

    }
}
