package rs.ac.singidunum.rank_watch.network;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LolApi {
    //I know api key shouldn't be save like this it is not safe
    private static final String API_KEY = "RGAPI-242365dc-fa79-436e-b4e9-28a0f9b37e39";

    private static OkHttpClient client;

    public interface PlayerInformationListener {
        void onSuccess(JSONArray playerArray, int profileIconId, String playerId) throws JSONException;

        void onSuccess(JSONObject playerObject, int profileIconId, String playerId) throws JSONException;

        void onFailure(String errorMessage);
    }

    public static void init(Context context) {
        // Create a cache directory
        File cacheDirectory = new File(context.getCacheDir(), "okhttp-cache");
        int cacheSize = 10 * 1024 * 1024; // 10 MB

        // Create a cache
        Cache cache = new Cache(cacheDirectory, cacheSize);

        client = new OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public static void getPlayerInformation(Context context, String summonerName, String region, PlayerInformationListener listener) {
        if (client == null) {
            init(context);
        }

        String url = "https://" + region + ".api.riotgames.com/lol/summoner/v4/summoners/by-name/" +
                summonerName + "?api_key=" + API_KEY;
        Log.d("url is: ", url);
        makeApiCall(url, new PlayerInformationListener() {
            @Override
            public void onSuccess(JSONArray playerArray, int profileIconId, String playerId) {

            }

            @Override
            public void onSuccess(JSONObject playerObject, int profileIconId, String playerId) {
                makeSecondApiCall(playerId, profileIconId, region, listener);
            }

            @Override
            public void onFailure(String errorMessage) {
                listener.onFailure(errorMessage);
            }
        });
    }

    public static void updatePlayerInformation(Context context, String summonerName, String region, PlayerInformationListener listener) {
        getPlayerInformation(context, summonerName, region, new PlayerInformationListener() {
            @Override
            public void onSuccess(JSONArray playerArray, int profileIconId, String playerId) throws JSONException {
                // Handle the playerArray if needed
                listener.onFailure("Player not found");
            }

            @Override
            public void onSuccess(JSONObject playerObject, int profileIconId, String playerId) throws JSONException {
                makeSecondApiCall(playerId, profileIconId, region, listener);
            }

            @Override
            public void onFailure(String errorMessage) {
                listener.onFailure(errorMessage);
            }
        });
    }

    private static void makeApiCall(String url, PlayerInformationListener listener) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                listener.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String responseData = response.body().string();

                    try {
                        JSONObject playerObject = new JSONObject(responseData);
                        String playerId = playerObject.getString("id");
                        int profileIconId = playerObject.getInt("profileIconId");

                        listener.onSuccess(playerObject, profileIconId, playerId);
                    } catch (JSONException e) {
                        listener.onFailure(e.getMessage());
                    }
                } else {
                    listener.onFailure("API request unsuccessful: " + response.code());
                }
            }
        });
    }

    private static void makeSecondApiCall(String playerId, int profileIconId, String region, PlayerInformationListener listener) {
        String url = "https://" + region + ".api.riotgames.com/lol/league/v4/entries/by-summoner/" +
                playerId + "?api_key=" + API_KEY;
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                listener.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String responseData = response.body().string();

                    try {
                        JSONArray playerArray = new JSONArray(responseData);
                        listener.onSuccess(playerArray, profileIconId, playerId);
                    } catch (JSONException e) {
                        listener.onFailure(e.getMessage());
                    }
                } else {
                    listener.onFailure("API request unsuccessful: " + response.code());
                }
            }
        });
    }
}
