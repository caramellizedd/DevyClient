package owo.caramell.devyclient.client.account;

import owo.caramell.devyclient.client.DevyMainClient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class AccountManager {
    /**
     * AccountManager
     * CRML Studios Login API.
     * Version: 0.0-TEST
     *
     * This is a test code for logging in to a CRML Studios Account.
     * This will be used for cosmetics and donation purposes.
     * These are HARDCODED IPs because it's not meant to be public yet.
     *
     * ====WARNING====
     * PLEASE use HTTPS for production use.
     * It is NOT advised to use HTTP because the request
     * contains the RAW UNENCRYPTED PASSWORD.
     * The password is stored and encrypted in the server api's database
     * using BCrypt.
     * ===============
     **/
    public static AccountManager instance;
    private HttpClient httpClient;
    public static String urlAddr = "https://loginapi.transcatirl.com";
    public AccountManager(){
        instance = this;
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }
    public boolean login(String user, String passwordRaw){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlAddr + "/login"))
                .GET()
                .header("User", user)
                .header("Password", passwordRaw)
                .timeout(Duration.ofSeconds(1))
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            DevyMainClient.logger.info("Status Code: " + response.statusCode());
            if(response.body().contains("LOGSUCCESS")){
                DevyMainClient.logger.info("Session ID: " + response.headers().firstValue("token").get());
                DevyMainClient.instance.initCRMLAccount(response.headers().firstValue("token").get());
            }
            return response.body().contains("LOGSUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean register(String user, String passwordRaw){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlAddr + "/register"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .header("User", user)
                .header("Password", passwordRaw)
                .timeout(Duration.ofSeconds(1))
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            DevyMainClient.logger.info("Status Code: " + response.statusCode());
            DevyMainClient.logger.info("Response Body: " + response.body());
            return response.body().contains("REGSUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
