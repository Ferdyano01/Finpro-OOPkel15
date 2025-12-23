package com.Ferdyano.frontend.network;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class BackendFacade {

    // Setup
    private static final String BASE_URL = "http://localhost:8080/api";
    private static final BackendFacade instance = new BackendFacade();
    private String currentPlayerId;

    private BackendFacade() {}

    public static BackendFacade getInstance() {
        return instance;
    }

    // Login
    public void registerOrLogin(String username, final NetworkCallback callback) {
        String jsonString = "{\"username\":\"" + username + "\"}";

        sendRequest("POST", "/players/register", jsonString, new NetworkCallback() {
            @Override
            public void onSuccess(String response) {
                // Parse the ID directly from the text
                JsonValue root = new JsonReader().parse(response);
                currentPlayerId = root.getString("playerId");

                System.out.println("Login OK. ID: " + currentPlayerId);
                callback.onSuccess(response);
            }

            @Override
            public void onFailure(String error) {
                callback.onFailure(error);
            }
        });
    }

    // Save
    public void saveGame(String checkpoint, int hp, int hunger, int thirst, boolean isWin, int foodCount, final NetworkCallback callback) {
        if (currentPlayerId == null) return;

        String jsonString = "{"
            + "\"checkpointId\":\"" + checkpoint + "\","
            + "\"hp\":" + hp + ","
            + "\"hunger\":" + hunger + ","
            + "\"thirst\":" + thirst + ","
            + "\"foodCount\":" + foodCount + ","
            + "\"bossDefeated\":" + isWin
            + "}";

        sendRequest("PUT", "/players/" + currentPlayerId + "/save", jsonString, callback);
    }

    // Helper
    private void sendRequest(String method, String endpoint, String jsonContent, final NetworkCallback callback) {
        HttpRequestBuilder builder = new HttpRequestBuilder();
        Net.HttpRequest request = builder.newRequest()
            .method(method)
            .url(BASE_URL + endpoint)
            .header("Content-Type", "application/json")
            .content(jsonContent)
            .build();

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                int status = httpResponse.getStatus().getStatusCode();
                String result = httpResponse.getResultAsString();

                Gdx.app.postRunnable(() -> {
                    if (status >= 200 && status < 300) {
                        callback.onSuccess(result);
                    } else {
                        callback.onFailure("Error: " + status);
                    }
                });
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.postRunnable(() -> callback.onFailure("Failed: " + t.getMessage()));
            }

            @Override
            public void cancelled() { }
        });
    }

    public interface NetworkCallback {
        void onSuccess(String response);
        void onFailure(String error);
    }
}
