package Apache.HttpClient;
import com.jayway.restassured.response.Response;
import io.restassured.path.json.JsonPath;

import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import org.springframework.security.crypto.codec.Hex;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.testng.Assert.*;


public class APITests {
    private static final String API_URL ="http://api.ustabor.uz/";
    private static final String login = "John@keysoft.su";
    private static final String loginMaster = "john+1@keysoft.su";
    private static final String password = "";
    private static final String vendor_id = "18293";
    private static final String project_id = "7079";
    private static final String advertiser_id = "30";
    private static  String token = null;
    private static  String tokenMaster = null;

    @DataProvider(name = "countrieIdsAndCountryName")
    public Object[][] countrieIdsAndCountryName() {
        return new Object[][]{
                {"529", "Узбекистан"},
                {"530", "Украина"},
                {"531", "США"},
                {"532", "Казахстан"}
        };
    }

    @DataProvider(name = "cityIdsAndCities")
    public Object[][] sityIdsAndCities() {
        return new Object[][]{
                {"2553", "Андижан"},
                {"580", "Бухара"},
                {"2555", "Джизак"},
                {"2573", "Каракалпакия"},
                {"2557", "Кашкадарья"},
                {"2576", "Коканд"},
                {"2559", "Навоий"},
                {"2561", "Наманган"},
                {"2563", "Самарканд"},
                {"2565", "Сурхандарья"},
                {"2567", "Сырдарья"},
                {"372", "Ташкент"},
                {"2575", "Ташкентская область"},
                {"2569", "Фергана"},
        };
    }

    @DataProvider(name = "idsAndExperiences")
    public Object[][] idsAndExperiences() {
        return new Object[][]{
                {"568", "1-3 лет"},
                {"570", "3-5 лет"},
                {"604", "5-10 лет"},
                {"606", "Больше 10 лет"}
        };
    }

    @DataProvider(name = "idsAndDistricts")
    public Object[][] idsAndDistricts() {
        return new Object[][]{
                {"5", "Бектемирский район"},
                {"6", "Чиланзарский район"},
                {"7", "Яшнабадский район"},
                {"8", "Мирабадский район"},
                {"9", "Мирзо-Улугбекский район"},
                {"10", "Сергелийский район"},
                {"11", "Шайхантахурский район"},
                {"12", "Алмазарский район"},
                {"13", "Учтепинский район"},
                {"14", "Яккасарайский район"},
                {"15", "Юнусабадский район"}
        };
    }

    @DataProvider(name = "categoryIdsAndNames")
    public Object[][] categoryIdsAndNames() {
        return new Object[][]{
                {"32","Кухни"},
                {"40", "Ванные комнаты"},
                {"103", "Алюкабонд"},
                {"105", "Гипсокартон"},
                {"259", "Электрики"},
                {"440", "Двери"},
                {"460", "Камины"},
                {"463", "Ландшафтный Дизайн"},
                {"469", "Маляры - Штукатуры"},
                {"472", "Окна"},
                {"475", "Полы"},
                {"478", "Потолки"},
                {"481", "Сантехники"},
                {"484", "Сауны и Бани"},
                {"487", "Отопление"},
                {"490", "Плотники"},
                {"493", "Бетонщики"},
                {"844", "Крыши"},
                {"852", "Бассейны"},
                {"858", "Кладка Кирпича"},
                {"854", "Сварщики"},
                {"863", "Ковка"},
                {"866", "Ремонт под ключ"},
                {"884", "Архитекторы"}
        };
    }

    @BeforeClass
    public String setUpToken(){
        JsonPath jp = getResponseToLogin(login, password);
        return token = "ApiLogin auth=" + jp.get("result.token");
    }
    @BeforeClass
    public String setUpTokenMaster(){
        JsonPath jp = getResponseToLogin(loginMaster, password);
        return tokenMaster = "ApiLogin auth=" + jp.get("result.token");
    }
    @Test(dataProvider = "categoryIdsAndNames")
    public  void  categories_pass(String id, String name){
        Response res = given().
                get(API_URL + "categories/");
        assertEquals(200, res.getStatusCode());
        Map items = filterById(res, id);
        assertEquals(items.get("name"), name);
    }

    @Test(dataProvider = "countrieIdsAndCountryName")
    public void countries_pass(String id, String name) {
        Response res = given().
                get(API_URL + "countries/");
        assertEquals(200, res.getStatusCode());
        assertThat(res.asString(), matchesJsonSchemaInClasspath("countries-schema.json"));
        Map items = filterById(res, id);
        assertEquals(items.get("name"), name);
    }

    @Test(dataProvider = "cityIdsAndCities")
    public void cities_pass(String id, String name) {
        Response res = given().
                get(API_URL + "cities/");
        assertEquals(200, res.getStatusCode());
        assertThat(res.asString(), matchesJsonSchemaInClasspath("cities-schema.json"));
        Map items = filterById(res, id);
        assertEquals(items.get("name"), name);
    }

    @Test(dataProvider = "idsAndExperiences")
    public void experiences_pass(String id, String name) {
        Response res = given().
                get(API_URL + "experiences/");
        assertEquals(200, res.getStatusCode());
        assertThat(res.asString(), matchesJsonSchemaInClasspath("experiences-schema.json"));
        Map items = filterById(res, id);
        assertEquals(items.get("name"), name);
    }

    @Test(dataProvider = "idsAndDistricts")
    public void districts_pass(String id, String name) {
        Response res = given().
                get(API_URL + "districts/");
        assertEquals(200, res.getStatusCode());
        assertThat(res.asString(), matchesJsonSchemaInClasspath("districts-schema.json"));
        Map items = filterById(res, id);
        assertEquals(items.get("name"), name);
    }

    @Test
    public void profile_pass(){
        Response res = given().
                header("Authorization", token).
                get(API_URL + "profile/");
        assertThat(res.asString(), matchesJsonSchemaInClasspath("profile-schema.json"));
        JsonPath jp = getJsonPath(res);
        assertEquals(jp.get("result.email"), login);
    }

    @Test
    public void favoriteUserMasters_pass(){
        Response res = given().
                header("Authorization", token).
                get(API_URL + "profile/masters/");
        assertEquals(200, res.getStatusCode());
    }

    @Test
    public void invalid_login_fail() {
        JsonPath jp = getResponseToLogin("login", "password");
        assertEquals(jp.get("status.message"), "Пароль не совпадает");
    }

    @Test
    public void valid_login_pass() {
        JsonPath jp = getResponseToLogin(login, password);
        assertNotNull(jp.get("result.token"));
    }

    @Test
    public void logout_pass() {
        JsonPath jpLogout = getJsonPath(given().
                header("Authorization", token).
                get(API_URL + "logout/"));
        assertEquals(jpLogout.get("result"), true);
    }

    @Test
    public void remindPassword_pass(){
        JsonPath jp = getJsonPath(given().
                param("email", login).
                post(API_URL + "user/password/"));
        assertEquals(jp.get("result.message"), "Новый пароль отправлен по адресу вашей электронной почты!");
    }

    @Test(dataProvider = "categoryIdsAndNames")
    public void services_pass(String categoryId, String categoryName){
        Response res = given().
                param("category_id", categoryId).
                get(API_URL + "services/");
        assertEquals(200, res.getStatusCode());
        assertThat(res.asString(), matchesJsonSchemaInClasspath("services-schema.json"));

        JsonPath jp = getJsonPath(res);
        int lastIndex = jp.getList("result.category_name").size() - 1;

        assertEquals(jp.get("result.category_name[0]"), categoryName);
        assertEquals(jp.get("result.category_name[" +lastIndex+ "]"), categoryName);    // Checks first and last element in list

    }
    @Test
    public void projects_pass(){
        JsonPath jp = getJsonPath(given().
                get(API_URL + "projects/"));
        assertNotNull(jp.get("result"));
    }

    @Test
    public void getProject_pass(){
        Response res = given().
                pathParam("project_id", project_id).
                get(API_URL + "project/{project_id}/");
        assertEquals(200, res.getStatusCode());
        assertThat(res.asString(), matchesJsonSchemaInClasspath("project-schema.json"));

        JsonPath jp = getJsonPath(res);
        assertEquals(jp.get("result.id"), project_id);
    }

    @Test
    public void favoriteProjects_pass() {
        Response res = given().
                param("favorite", 1).
                header("Authorization", token).
                get(API_URL + "projects/");
        assertEquals(200, res.getStatusCode());

        JsonPath jp = getJsonPath(res);
        assertNotNull(jp.get("result"));
    }

    @Test
    public void banners_pass(){
        Response res = given().
                get(API_URL + "banners/");
        assertEquals(200, res.getStatusCode());
    }

    @Test
    public void advertiser_pass(){
        Response res = given().
                pathParam("advertiser_id", advertiser_id).
                get(API_URL + "advertiser/{advertiser_id}/");
        assertThat(res.asString(), matchesJsonSchemaInClasspath("advertiser-schema.json"));
        JsonPath jp = getJsonPath(res);
        assertEquals(jp.get("result.id"), advertiser_id);
    }

    @Test
    public void vendor_pass(){
        Response res = given().
                pathParam("vendor_id", vendor_id).
                get(API_URL + "vendor/{vendor_id}/");
        assertThat(res.asString(), matchesJsonSchemaInClasspath("vendor-schema.json"));
        JsonPath jp = getJsonPath(res);
        assertEquals(jp.get("result.id"), vendor_id);
    }

    @Test
    public void addVendor_pass(){
        JsonPath jp = getJsonPath(given().
                header("Authorization", token).
                pathParam("vendor_id", vendor_id).
                get(API_URL + "vendor/{vendor_id}/contact/"));
        assertEquals(jp.get("result"), true);
    }

    @Test
    public void removeVendor_pass(){
        JsonPath jp = getJsonPath( given().
                header("Authorization", token).
                pathParam("vendor_id", vendor_id).
                get(API_URL + "vendor/{vendor_id}/remove/"));
        assertEquals(jp.get("result"), true);
    }

    @Test
    public void editUserCategory_pass(){
        JsonPath jp = getJsonPath(given().
                header("Authorization", token).
                post(API_URL + "user/edit/"));
        assertEquals(jp.get("result"), true);
    }

    @Test
    public void editUserAvatar_pass(){
        JsonPath jp = getJsonPath(given().
                header("Authorization", token).
                multiPart(new File("src/test/resources/avatar.jpg")).
                post(API_URL + "user/edit/"));
        assertEquals(jp.get("result"), true);
    }

    @Test
    public void editUser_pass(){
        JsonPath jp = getJsonPath(given().
                header("Authorization", token).
                post(API_URL + "user/edit/"));
        assertEquals(jp.get("result"), true);
    }
    @Test
    public void addFavoriteProject_pass(){
        JsonPath jp = getJsonPath(given().
                header("Authorization", token).
                pathParam("project_id", project_id).
                post(API_URL + "favorite/add/{project_id}/"));
        assertEquals(jp.get("result"), true);
    }
    @Test
    public void deleteFavoriteProject_pass(){
        JsonPath jp = getJsonPath(given().
                header("Authorization", token).
                pathParam("project_id", project_id).
                post(API_URL + "favorite/remove/{project_id}/"));
        assertEquals(jp.get("result"), true);
    }
    @Test
    public void editMaserCategory_pass(){
        JsonPath jp = getJsonPath(given().
                header("Authorization", tokenMaster).
                param("categories[]", 854).           //854 - "Сварка" categoryID
                post(API_URL + "user/edit/"));
        assertEquals(jp.get("result"), true);
    }
    @Test
    public void editMaserCategoryCity_pass(){
        JsonPath jp = getJsonPath(given().
                header("Authorization", tokenMaster).
                param("categories[]", 854).           // 854 - "Сварка" categoryID
                param("cities[]", 2553).              // 2553 - Андижан cityID
                post(API_URL + "user/edit/"));
        assertEquals(jp.get("result"), true);
    }
    @Test
    public void editMaserCategoryAvatar_pass(){
        JsonPath jp = getJsonPath(given().
                header("Authorization", tokenMaster).
                param("categories[]", 854).           //854 - "Сварка" categoryID
                multiPart(new File("src/test/resources/avatar.jpg")).
                post(API_URL + "user/edit/"));
        assertEquals(jp.get("result"), true);
    }
    @Test
    public void editMaserCategoryCityAvatar_pass(){
        JsonPath jp = getJsonPath(given().
                header("Authorization", tokenMaster).
                param("categories[]", 854).           //854 - "Сварка" categoryID
                param("cities[]", 2553).              // 2553 - Андижан cityID
                multiPart(new File("src/test/resources/avatar.jpg")).
                post(API_URL + "user/edit/"));
        assertEquals(jp.get("result"), true);
    }
    @Test
    public void editProject_pass(){
        JsonPath jp = getJsonPath(given().
                header("Authorization", tokenMaster).
                pathParam("project_id", 8840).                //8840 - current project ID
                param("deleteimages[]", "55331bbf41eeaf72ab6cf5172b7f8489").  // 55331bbf41eeaf72ab6cf5172b7f8489 -- name of image
                post(API_URL + "project/edit/{project_id}/"));                //  from image address
        assertEquals(jp.get("result"), true);

    }
    @Test
    public void delelteProject(){
        JsonPath jp = getJsonPath(given().
                header("Authorization", tokenMaster).
                pathParam("project_id", 8840).                //8840 - current project ID
                post(API_URL + "project/delete/{project_id}/"));
        assertEquals(jp.get("result"), true);
    }
    @Test
    public void test(){
        JsonPath jp = getJsonPath(given().
                post(API_URL + "registration/customer/"));
        System.out.print(jp.get("status.message"));
    }

   @Test
    public void loginGooglePlus_pass(){
       JsonPath jp = getJsonPath(given().
               param("provider", "googleplus").
               param("token", getIdToken()).
               get(API_URL + "login/"));
       assertEquals(jp.get("result.user_id"), 18267);

    }
    @Test
    public void loginFacebook_pass() throws Exception {
        System.out.print(calculateAppSecretProof("", ""));
    }
    private String calculateAppSecretProof(String token, String appSecret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(appSecret.getBytes("UTF-8"), "HmacSHA256");
        mac.init(secretKey);
        byte[] digest = mac.doFinal(token.getBytes());
        return new String(Hex.encode(digest));
    }
    @org.jetbrains.annotations.Nullable
    private String getIdToken() {
        try
        {
            Map<String,Object> params = new LinkedHashMap<>();
            params.put("grant_type","refresh_token");
            params.put("client_id","");
            params.put("client_secret","");
            params.put("refresh_token","");

            StringBuilder postData = new StringBuilder();
            for(Map.Entry<String,Object> param : params.entrySet())
            {
                if(postData.length() != 0)
                {
                    postData.append('&');
                }
                postData.append(URLEncoder.encode(param.getKey(),"UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()),"UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            URL url = new URL("https://accounts.google.com/o/oauth2/token");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod("POST");
            con.getOutputStream().write(postDataBytes);

            BufferedReader  reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuffer buffer = new StringBuffer();
            for (String line = reader.readLine(); line != null; line = reader.readLine())
            {
                buffer.append(line);
            }

            JSONObject json = new JSONObject(buffer.toString());
            String IdToken = json.getString("id_token");
            return IdToken;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    private Map filterById(Response res, String id){
        JsonPath jp = getJsonPath(res);
        jp.setRoot("result");
        return jp.get("find {e -> e.id =~ /" + id + "/}");   // Filter by ID with Groovy
    }

    private JsonPath getJsonPath(Response res) {
        assertEquals(200, res.getStatusCode());
        String json = res.asString();
        return new JsonPath(json);
    }

    private JsonPath getResponseToLogin(String login, String password) {
        Response res = given().
                param("login", login).
                param("password", password).
                get(API_URL + "login/");
        return getJsonPath(res);
    }

}
