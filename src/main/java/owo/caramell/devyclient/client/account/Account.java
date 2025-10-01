package owo.caramell.devyclient.client.account;

import owo.caramell.devyclient.client.DevyMainClient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static owo.caramell.devyclient.client.account.AccountManager.urlAddr;

public class Account {
    public String sessionID = "0";
    public String accountName = "yuri";
    public String userBIO = "I like women.";
    private HttpClient httpClient;
    public Account(String ID){
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        verifyID(ID);
    }
    private void verifyID(String sessionID){
        DevyMainClient.logger.info("Verifying Session ID...");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlAddr + "/verifyToken"))
                .GET()
                .header("token", sessionID)
                .timeout(Duration.ofSeconds(1))
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            DevyMainClient.logger.info("Status Code: " + response.statusCode());
            if(!response.body().isBlank()){
                // TODO: Get information about the player whether by using headers or body response.,
                // Receive the username in a HTTP body response, although this might be replaced because we want to put different
                // info about the player in said response (like about me which are loaded both client and server side (mostly server side))
                DevyMainClient.logger.info("Token is owned by: " + response.body());
                // Set account username
                accountName = response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Get the user public information.
    // Example: Levels, Donation amount/tier (if i make one), Contribution count, About me.
    private void getUserPublicInfo(){
        // TODO: Get user public information
    }
}
