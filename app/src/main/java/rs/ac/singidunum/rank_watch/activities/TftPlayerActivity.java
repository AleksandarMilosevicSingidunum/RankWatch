package rs.ac.singidunum.rank_watch.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import rs.ac.singidunum.rank_watch.R;
import rs.ac.singidunum.rank_watch.fragments.RankedTftFragment;
import rs.ac.singidunum.rank_watch.models.TFTPlayer;
import rs.ac.singidunum.rank_watch.network.TftApi;
import rs.ac.singidunum.rank_watch.utils.FavoriteTftPlayers;

public class TftPlayerActivity extends AppCompatActivity {

    private Button buttonAddToFavorites;
    private Button buttonDeleteFromFavorites;
    private Button updateButton;
    private FavoriteTftPlayers favoritePlayersManager;
    private TFTPlayer currentSearchedPlayer;
    private String region;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tft_player);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView backButton = findViewById(R.id.back_button);
        buttonAddToFavorites = findViewById(R.id.buttonAddToFavorites);
        buttonDeleteFromFavorites = findViewById(R.id.buttonDeleteFromFavorites);
        updateButton = findViewById(R.id.updateButton);

        backButton.setOnClickListener(v -> onBackPressed());

        favoritePlayersManager = FavoriteTftPlayers.getInstance(this);

        String playerName = getIntent().getStringExtra("playerName");
        region = getIntent().getStringExtra("region");
        TftApi.getPlayerInformation(TftPlayerActivity.this, playerName, region, new TftApi.PlayerInformationListener() {
            @Override
            public void onSuccess(JSONObject playerObject, int profileIconId, String playerId) {
                try {
                    TFTPlayer tftPlayer = new TFTPlayer(
                            playerObject.getString("tier"),
                            playerObject.getString("rank"),
                            playerObject.getString("summonerName"),
                            playerObject.getInt("leaguePoints"),
                            playerObject.getInt("wins"),
                            playerObject.getInt("losses")
                    );
                    tftPlayer.setProfileIconId(profileIconId);

                    currentSearchedPlayer = tftPlayer;
                    currentSearchedPlayer.setRegion(region);
                    currentSearchedPlayer.setProfileIconId(profileIconId);

                    RankedTftFragment rankedTftFragment = RankedTftFragment.newInstance(tftPlayer, profileIconId, region);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_ranked_tft, rankedTftFragment)
                            .commit();

                    // Enable buttons after successful API request
                    buttonDeleteFromFavorites.setEnabled(true);
                    updateButton.setEnabled(true);
                } catch (JSONException e) {
                    onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("TftPlayerActivity", "API request failed: " + errorMessage);
                runOnUiThread(() -> {
                    Toast.makeText(TftPlayerActivity.this, "Failed to retrieve player information", Toast.LENGTH_SHORT).show();
                    promptDeletePlayer();
                });
            }

        });

        buttonAddToFavorites.setOnClickListener(v -> {
            buttonAddToFavorites.setBackgroundColor(Color.BLACK);

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                buttonAddToFavorites.setBackgroundColor(getResources().getColor(R.color.accent));
                buttonAddToFavorites.setBackgroundResource(R.drawable.button_background);
            }, 100); // Delay for 100 milliseconds

            addToFavorites(currentSearchedPlayer, currentSearchedPlayer != null ? currentSearchedPlayer.getProfileIconId() : 0, region);
        });

        buttonDeleteFromFavorites.setOnClickListener(v -> {
            if (currentSearchedPlayer != null) {
                // Display a confirmation dialog before deleting the player
                confirmDeletePlayer();
            }
        });

        updateButton.setOnClickListener(v -> {
            updateButton.setBackgroundColor(Color.BLACK);

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                updateButton.setBackgroundColor(getResources().getColor(R.color.accent));
                updateButton.setBackgroundResource(R.drawable.button_background);
            }, 100);

            if (currentSearchedPlayer != null) {
                if (favoritePlayersManager.isPlayerInFavorites(currentSearchedPlayer.getSummonerName(), currentSearchedPlayer.getRegion())) {
                    // Call TftApi to update the player information
                    TftApi.getPlayerInformation(TftPlayerActivity.this, currentSearchedPlayer.getSummonerName(), currentSearchedPlayer.getRegion(), new TftApi.PlayerInformationListener() {
                        @Override
                        public void onSuccess(JSONObject playerObject, int profileIconId, String playerId) {
                            TFTPlayer updatedPlayer = currentSearchedPlayer;
                            updatedPlayer.setProfileIconId(profileIconId);

                            favoritePlayersManager.updateFavoritePlayer(updatedPlayer);

                            runOnUiThread(() -> Toast.makeText(TftPlayerActivity.this, "Player updated", Toast.LENGTH_SHORT).show());

                            Intent intent = new Intent(TftPlayerActivity.this, TftPlayerActivity.class);
                            intent.putExtra("playerName", updatedPlayer.getSummonerName());
                            intent.putExtra("region", region);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Log.e("TftPlayerActivity", "API request failed: " + errorMessage);
                            runOnUiThread(() -> Toast.makeText(TftPlayerActivity.this, "Failed to update player", Toast.LENGTH_SHORT).show());
                        }
                    });
                } else {
                    Toast.makeText(TftPlayerActivity.this, "Player not found in favorites", Toast.LENGTH_SHORT).show();
                }
            }
        });
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


    private void addToFavorites(TFTPlayer player, int profileIconId, String region) {
        if (player != null) {
            // Check if the player already exists in the favorites list
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

    private void deletePlayer() {
        if (currentSearchedPlayer != null) {
            favoritePlayersManager.deleteFavoritePlayer(currentSearchedPlayer.getSummonerName(), currentSearchedPlayer.getRegion());
            Toast.makeText(TftPlayerActivity.this, "Player deleted from favorites", Toast.LENGTH_SHORT).show();
        } else {
            // Search for the player in favorites using getSummonerName and getRegion
            String summonerName = getIntent().getStringExtra("playerName");
            String region = getIntent().getStringExtra("region");
            TFTPlayer player = favoritePlayersManager.getFavoritePlayer(summonerName,region);
            if (player != null) {
                favoritePlayersManager.deleteFavoritePlayer(player.getSummonerName(), player.getRegion());
                Toast.makeText(TftPlayerActivity.this, "Player deleted from favorites", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TftPlayerActivity.this, "Player not found in favorites", Toast.LENGTH_SHORT).show();
            }
        }
        onBackPressed();
    }


}
