package rs.ac.singidunum.rank_watch.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rs.ac.singidunum.rank_watch.R;
import rs.ac.singidunum.rank_watch.adapters.LolPlayerPagerAdapter;
import rs.ac.singidunum.rank_watch.fragments.FlexFragment;
import rs.ac.singidunum.rank_watch.fragments.SoloDuoFragment;
import rs.ac.singidunum.rank_watch.models.LolPlayer;
import rs.ac.singidunum.rank_watch.models.TFTPlayer;
import rs.ac.singidunum.rank_watch.network.LolApi;
import rs.ac.singidunum.rank_watch.utils.FavoriteLolPlayers;

public class LolPlayerActivity extends AppCompatActivity {
    private Button buttonDeleteFromFavorites;
    private FavoriteLolPlayers favoritePlayersManager;
    private LolPlayer currentSearchedPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lol_player);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView backButton = findViewById(R.id.back_button);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);
        Button buttonAddToFavorites = findViewById(R.id.buttonAddToFavorites);
        buttonDeleteFromFavorites = findViewById(R.id.buttonDeleteFromFavorites);
        backButton.setOnClickListener(v -> onBackPressed());

        favoritePlayersManager = FavoriteLolPlayers.getInstance(this);

        String playerName = getIntent().getStringExtra("playerName");
        String region = getIntent().getStringExtra("region");

        new PlayerInformationTask().execute(playerName, region);

        buttonAddToFavorites.setOnClickListener(v -> addToFavorites(currentSearchedPlayer, currentSearchedPlayer != null ? currentSearchedPlayer.getProfileIconId() : 0, region));

        buttonDeleteFromFavorites.setOnClickListener(v -> {
            if (currentSearchedPlayer != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LolPlayerActivity.this);
                builder.setTitle("Confirm Delete");
                builder.setMessage("Are you sure you want to delete this player from favorites?");
                builder.setPositiveButton("Delete", (dialog, which) -> {
                    favoritePlayersManager.deleteFavoritePlayer(currentSearchedPlayer.getSummonerName(), currentSearchedPlayer.getRegion());
                    Toast.makeText(LolPlayerActivity.this, "Player deleted from favorites", Toast.LENGTH_SHORT).show();
                });
                builder.setNegativeButton("Cancel", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        Button updateButton = findViewById(R.id.updateButton);
        updateButton.setOnClickListener(v -> {
            updateButton.setBackgroundColor(Color.BLACK);
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                updateButton.setBackgroundColor(getResources().getColor(R.color.accent));
                updateButton.setBackgroundResource(R.drawable.button_background);
            }, 100);

            if (currentSearchedPlayer != null) {
                if (favoritePlayersManager.isPlayerInFavorites(currentSearchedPlayer.getSummonerName(), currentSearchedPlayer.getRegion())) {
                    new UpdatePlayerTask().execute(currentSearchedPlayer.getSummonerName(), region);
                } else {
                    Toast.makeText(LolPlayerActivity.this, "Player not found in favorites", Toast.LENGTH_SHORT).show();
                }
            }
        });

        LolPlayerPagerAdapter pagerAdapter = new LolPlayerPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void addToFavorites(LolPlayer player, int profileIconId, String region) {
        if (player != null) {
            if (favoritePlayersManager.isPlayerInFavorites(player.getSummonerName(), player.getRegion())) {
                Toast.makeText(this, "Player already in favorites", Toast.LENGTH_SHORT).show();
            } else {
                player.setProfileIconId(profileIconId);
                player.setRegion(region);
                favoritePlayersManager.addFavoritePlayer(player);
                Toast.makeText(this, "Player added to favorites", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private LolPlayer extractLolPlayerFromJSONObject(JSONObject playerObject) throws JSONException {
        String leagueId = playerObject.getString("leagueId");
        String queueType = playerObject.getString("queueType");
        String tier = playerObject.getString("tier");
        String rank = playerObject.getString("rank");
        String summonerId = playerObject.getString("summonerId");
        String summonerName = playerObject.getString("summonerName");
        int leaguePoints = playerObject.getInt("leaguePoints");
        int wins = playerObject.getInt("wins");
        int losses = playerObject.getInt("losses");
        return new LolPlayer(leagueId, queueType, tier, rank, summonerId, summonerName, leaguePoints, wins, losses);
    }

    @SuppressLint("StaticFieldLeak")
    private class PlayerInformationTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String playerName = params[0];
            String region = params[1];

            LolApi.getPlayerInformation(LolPlayerActivity.this, playerName, region, new LolApi.PlayerInformationListener() {
                @Override
                public void onSuccess(JSONArray playerArray, int profileIconId, String playerId) throws JSONException {
                    if (playerArray.length() == 0) {
                        runOnUiThread(() -> Toast.makeText(LolPlayerActivity.this, "Summoner not found", Toast.LENGTH_SHORT).show());
                    } else {
                        LolPlayer soloDuoPlayer = null;
                        LolPlayer flexPlayer = null;

                        for (int i = 0; i < playerArray.length(); i++) {
                            JSONObject playerObject = playerArray.getJSONObject(i);
                            String queueType = playerObject.getString("queueType");
                            LolPlayer player = extractLolPlayerFromJSONObject(playerObject);

                            if (queueType.equals("RANKED_SOLO_5x5")) {
                                soloDuoPlayer = player;
                            } else if (queueType.equals("RANKED_FLEX_SR")) {
                                flexPlayer = player;
                            }
                        }

                        soloDuoPlayer = soloDuoPlayer != null ? soloDuoPlayer : new LolPlayer("", "RANKED_SOLO_5x5", "", "", "", "", 0, 0, 0);
                        flexPlayer = flexPlayer != null ? flexPlayer : new LolPlayer("", "RANKED_FLEX_SR", "", "", "", "", 0, 0, 0);

                        currentSearchedPlayer = soloDuoPlayer;
                        currentSearchedPlayer.setRegion(region);
                        currentSearchedPlayer.setProfileIconId(profileIconId);
                        SoloDuoFragment soloDuoFragment = SoloDuoFragment.newInstance(soloDuoPlayer, profileIconId, region);
                        FlexFragment flexFragment = FlexFragment.newInstance(flexPlayer, profileIconId, region);

                        runOnUiThread(() -> {

                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container_solo_duo, soloDuoFragment)
                                    .commit();


                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container_flex, flexFragment)
                                    .commit();
                        });
                    }
                }

                @Override
                public void onSuccess(JSONObject playerObject, int profileIconId, String playerId) throws JSONException {
                    LolPlayer player = extractLolPlayerFromJSONObject(playerObject);
                    player.setRegion(region);
                    player.setProfileIconId(profileIconId);
                    currentSearchedPlayer = player;

                    SoloDuoFragment soloDuoFragment = SoloDuoFragment.newInstance(player, profileIconId, region);
                    FlexFragment flexFragment = FlexFragment.newInstance(null, profileIconId, region);

                    runOnUiThread(() -> {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container_solo_duo, soloDuoFragment)
                                .commit();

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container_flex, flexFragment)
                                .commit();
                    });
                }

                @Override
                public void onFailure(String errorMessage) {
                    Log.e("TftPlayerActivity", "API request failed: " + errorMessage);
                    runOnUiThread(() -> {
                        Toast.makeText(LolPlayerActivity.this, "Failed to retrieve player information", Toast.LENGTH_SHORT).show();
                        promptDeletePlayer();
                    });
                }
            });

            return null;
        }
    }
    private void promptDeletePlayer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Player");
        builder.setMessage("Failed to retrieve player information. Do you want to delete the summoner from favorite players?");
        builder.setPositiveButton("Delete", (dialog, which) -> deletePlayer());
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void confirmDeletePlayer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this player from favorites?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePlayer();
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Change button color to a different color
        buttonDeleteFromFavorites.setBackgroundColor(Color.BLACK);

        // Use a Handler to delay the color change back to the original color
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            buttonDeleteFromFavorites.setBackgroundColor(getResources().getColor(R.color.accent));
            buttonDeleteFromFavorites.setBackgroundResource(R.drawable.button_background);
        }, 200); // Delay for 200 milliseconds
    }
    @SuppressLint("StaticFieldLeak")
    private class UpdatePlayerTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String playerName = params[0];
            String region = params[1];

            // Pass the context (this) as the first parameter
            LolApi.updatePlayerInformation(LolPlayerActivity.this, playerName, region, new LolApi.PlayerInformationListener() {
                @Override
                public void onSuccess(JSONArray playerArray, int profileIconId, String playerId) throws JSONException {
                    for (int i = 0; i < playerArray.length(); i++) {
                        JSONObject playerObject = playerArray.getJSONObject(i);
                        String queueType = playerObject.getString("queueType");
                        if (queueType.equals("RANKED_SOLO_5x5")) {
                            LolPlayer player = extractLolPlayerFromJSONObject(playerObject);
                            player.setRegion(region);
                            player.setProfileIconId(profileIconId);
                            favoritePlayersManager.updateFavoritePlayer(player);
                            runOnUiThread(() -> Toast.makeText(LolPlayerActivity.this, "Player updated", Toast.LENGTH_SHORT).show());
                            return;
                        }
                    }
                    runOnUiThread(() -> Toast.makeText(LolPlayerActivity.this, "Player not found", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onSuccess(JSONObject playerObject, int profileIconId, String playerId) throws JSONException {
                    LolPlayer player = extractLolPlayerFromJSONObject(playerObject);
                    player.setRegion(region);
                    player.setProfileIconId(profileIconId);
                    favoritePlayersManager.updateFavoritePlayer(player);
                    runOnUiThread(() -> Toast.makeText(LolPlayerActivity.this, "Player updated", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onFailure(String errorMessage) {
                    runOnUiThread(() -> Toast.makeText(LolPlayerActivity.this, errorMessage, Toast.LENGTH_SHORT).show());
                }
            });

            return null;
        }
    }
    private void deletePlayer() {
        if (currentSearchedPlayer != null) {
            favoritePlayersManager.deleteFavoritePlayer(currentSearchedPlayer.getSummonerName(), currentSearchedPlayer.getRegion());
            Toast.makeText(LolPlayerActivity.this, "Player deleted from favorites", Toast.LENGTH_SHORT).show();
        } else {
            // Search for the player in favorites using getSummonerName and getRegion
            String summonerName = getIntent().getStringExtra("playerName");
            String region = getIntent().getStringExtra("region");
            LolPlayer player = favoritePlayersManager.getFavoritePlayer(summonerName,region);
            if (player != null) {
                favoritePlayersManager.deleteFavoritePlayer(player.getSummonerName(), player.getRegion());
                Toast.makeText(LolPlayerActivity.this, "Player deleted from favorites", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LolPlayerActivity.this, "Player not found in favorites", Toast.LENGTH_SHORT).show();
            }
        }
        onBackPressed();
    }
}
