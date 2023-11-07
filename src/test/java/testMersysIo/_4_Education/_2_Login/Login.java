package testMersysIo._4_Education._2_Login;

import com.github.javafaker.Faker;
import io.restassured.specification.RequestSpecification;

public class Login {

    public String urlLog="/auth/login";
    public String  username="turkeyts";
    public String  password="TechnoStudy123";
    public String url=" https://test.mersys.io/school-service/api/";
    public Faker randomUreteci=new Faker();
    public RequestSpecification reqSpec;

    public String rndCountryName="";
    public String rndCountryCode="";
    public String countryID="";

    public String rndPositionCategory;

    public String categoryID;

    public String rndAttestation;

    public String AttestationID;


}