package rs.ac.singidunum.rank_watch.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import rs.ac.singidunum.rank_watch.models.LolPlayer;
import rs.ac.singidunum.rank_watch.models.TFTPlayer;

public class FavoriteLolPlayers {
    private static final String PREF_NAME = "FavoriteLolPlayers";
    private static final String KEY_FAVORITE_PLAYERS = "favoriteLolPlayers";

    private static FavoriteLolPlayers instance;
    private final SharedPreferences preferences;
    private final Gson gson;

    private List<LolPlayer> favoriteLolPlayers;

    private FavoriteLolPlayers(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        loadFavoritePlayers();
    }

    public static synchronized FavoriteLolPlayers getInstance(Context context) {
        if (instance == null) {
            instance = new FavoriteLolPlayers(context.getApplicationContext());
        }
        return instance;
    }

    public List<LolPlayer> getFavoritePlayers() {
        return favoriteLolPlayers;
    }

    public void addFavoritePlayer(LolPlayer player) {
        if (player != null) {
            favoriteLolPlayers.add(player);
            saveFavoritePlayers();
        }
    }



    private void loadFavoritePlayers() {
        String json = preferences.getString(KEY_FAVORITE_PLAYERS, null);
        if (!TextUtils.isEmpty(json)) {
            Type type = new TypeToken<List<LolPlayer>>() {}.getType();
            favoriteLolPlayers = gson.fromJson(json, type);
        } else {
            favoriteLolPlayers = new ArrayList<>();
        }
    }

    private void saveFavoritePlayers() {
        String json = gson.toJson(favoriteLolPlayers);
        preferences.edit().putString(KEY_FAVORITE_PLAYERS, json).apply();
    }

    public boolean isPlayerInFavorites(String playerName,String region) {
        List<LolPlayer> favoritePlayers = getFavoritePlayers();
        for (LolPlayer player : favoritePlayers) {
            if (player.getSummonerName().equals(playerName) && player.getRegion().equals(region) ) {
                return true; // Player with the same name already exists in favorites
            }
        }
        return false; // Player not found in favorites
    }

    public void deleteFavoritePlayer(String playerName,String region) {
        // Get the list of favorite players
        List<LolPlayer> favoritePlayers = getFavoritePlayers();

        // Find the player with the specified name in the list
        LolPlayer playerToRemove = null;
        for (LolPlayer player : favoritePlayers) {
            if (player.getSummonerName().equals(playerName)&&player.getRegion().equals(region)) {
                playerToRemove = player;
                break;
            }
        }

        // Remove the player from the list if found
        if (playerToRemove != null) {
            favoritePlayers.remove(playerToRemove);
            saveFavoritePlayers(); // Save the updated list of favorite players
            loadFavoritePlayers(); // Reload the favorite players
        }
    }
    public LolPlayer getFavoritePlayer(String playerName, String region) {
        List<LolPlayer> favoritePlayers = getFavoritePlayers();
        for (LolPlayer player : favoritePlayers) {
            if (player.getSummonerName().equals(playerName) && player.getRegion().equals(region)) {
                return player;
            }
        }
        return null; // Player not found in favorites
    }

    public void updateFavoritePlayer(LolPlayer updatedPlayer) {
        // Get the list of favorite players from SharedPreferences
        List<LolPlayer> favoritePlayers = getFavoritePlayers();

        // Find the index of the updated player in the list
        int index = -1;
        for (int i = 0; i < favoritePlayers.size(); i++) {
            if (favoritePlayers.get(i).getSummonerName().equals(updatedPlayer.getSummonerName())&& favoritePlayers.get(i).getRegion().equals(updatedPlayer.getRegion())) {
                index = i;
                break;
            }
        }

        // Update the player if found
        if (index != -1) {
            favoritePlayers.set(index, updatedPlayer);

            // Save the updated list back to SharedPreferences
            saveFavoritePlayers();
        }
    }


}
