package rs.ac.singidunum.rank_watch.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

import rs.ac.singidunum.rank_watch.R;
import rs.ac.singidunum.rank_watch.activities.TftPlayerActivity;
import rs.ac.singidunum.rank_watch.models.TFTPlayer;
import rs.ac.singidunum.rank_watch.network.TftApi;
import rs.ac.singidunum.rank_watch.utils.FavoriteTftPlayers;

public class TftFragment extends Fragment implements View.OnClickListener {
    private EditText searchEditText;
    private Button searchButton;
    private FavoriteTftPlayers favoritePlayersManager;
    private LinearLayout favoritePlayersLayout;
    private Spinner region_spinner;
    public TftFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tft, container, false);

        favoritePlayersManager = FavoriteTftPlayers.getInstance(getActivity());

        searchEditText = view.findViewById(R.id.search_edit_text);
        searchButton = view.findViewById(R.id.search_button);
        region_spinner = view.findViewById(R.id.region_spinner);
        favoritePlayersLayout = view.findViewById(R.id.favorite_players_layout);

        searchButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        displayFavoritePlayers();
    }

    @Override
    public void onClick(View v) {
        if (v == searchButton) {
            performSearch();
        } else {
            // Handle player card click
            for (int i = 0; i < favoritePlayersLayout.getChildCount(); i++) {
                View childView = favoritePlayersLayout.getChildAt(i);
                if (v == childView) {
                    TFTPlayer player = (TFTPlayer) childView.getTag();
                    openPlayerActivity(player.getSummonerName(),player.getRegion());
                    break;
                }
            }
        }
    }

    private void performSearch() {
        String query = searchEditText.getText().toString().trim();
        String region = getTranslatedRegion();
        if (!query.isEmpty()) {
            TftApi.getPlayerInformation(requireContext(),query,region, new TftApi.PlayerInformationListener() {
                @Override
                public void onSuccess(JSONObject playerArray, int profileIconId, String playerId) {
                    if (playerArray.length() == 0) {
                        onFailure("Summoner Did not play any ranked"); // Call the onFailure method with the error message
                    } else {
                        openPlayerActivity(query,region);
                    }
                }


                @Override
                public void onFailure(String errorMessage) {
                    requireActivity().runOnUiThread(() -> {
                        String toastMessage = "Summoner not found";
                        if (errorMessage.contains("Summoner Did not play any ranked")) {
                            toastMessage = errorMessage;
                        }

                        Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();

                        // Log the error message
                        Log.e("TftFragment", "API request failed: " + errorMessage);
                    });
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please enter a player name", Toast.LENGTH_SHORT).show();
        }
    }
    private String getTranslatedRegion() {
        String selectedRegion = (String) region_spinner.getSelectedItem();
        String translatedRegion;

        if (selectedRegion != null) {
            switch (selectedRegion) {
                case "NA":
                    translatedRegion = "na1";
                    break;
                case "EUW":
                    translatedRegion = "euw1";
                    break;
                case "KR":
                    translatedRegion = "kr";
                    break;
                default:
                    translatedRegion = "eun1"; // Default to "EUNE"
                    break;
            }
        } else {
            translatedRegion = "eun1"; // Default to "EUNE"
        }

        return translatedRegion;
    }
    private void openPlayerActivity(String playerName, String region) {
        Intent intent = new Intent(getActivity(), TftPlayerActivity.class);
        intent.putExtra("playerName", playerName);
        intent.putExtra("region",region);
        startActivity(intent);
    }

    private void displayFavoritePlayers() {
        favoritePlayersLayout.removeAllViews();

        List<TFTPlayer> favoritePlayers = favoritePlayersManager.getFavoritePlayers();
        for (TFTPlayer player : favoritePlayers) {
            View playerCard = getLayoutInflater().inflate(R.layout.player_tft_item, favoritePlayersLayout, false);
            int profileIconId = player.getProfileIconId();
            ImageView player_avatar_image_view = playerCard.findViewById(R.id.player_avatar_image_view);
            TextView playerNameTextView = playerCard.findViewById(R.id.player_name_text_view);
            TextView player_rank_text_view = playerCard.findViewById(R.id.player_rank_text_view);
            TextView player_tier_text_view = playerCard.findViewById(R.id.player_tier_text_view);
            TextView player_wins_text_view = playerCard.findViewById(R.id.player_wins_text_view);
            TextView player_losses_text_view = playerCard.findViewById(R.id.player_losses_text_view);
            String profileImageUrl = "https://ddragon.leagueoflegends.com/cdn/13.10.1/img/profileicon/" + profileIconId + ".png";
            Picasso.get().load(profileImageUrl).into(player_avatar_image_view);
            playerNameTextView.setText(player.getSummonerName());
            player_rank_text_view.setText(player.getRank());
            player_tier_text_view.setText(player.getTier());
            player_wins_text_view.setText(String.valueOf(player.getWins()));
            player_losses_text_view.setText(String.valueOf(player.getLosses()));

            playerCard.setTag(player);
            playerCard.setOnClickListener(this);

            favoritePlayersLayout.addView(playerCard);
        }
    }
}
