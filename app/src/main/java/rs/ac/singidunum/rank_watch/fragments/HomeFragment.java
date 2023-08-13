package rs.ac.singidunum.rank_watch.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import rs.ac.singidunum.rank_watch.R;
import rs.ac.singidunum.rank_watch.activities.LolPlayerActivity;
import rs.ac.singidunum.rank_watch.activities.TftPlayerActivity;
import rs.ac.singidunum.rank_watch.models.LolPlayer;
import rs.ac.singidunum.rank_watch.models.TFTPlayer;
import rs.ac.singidunum.rank_watch.utils.FavoriteLolPlayers;
import rs.ac.singidunum.rank_watch.utils.FavoriteTftPlayers;

import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private LinearLayout tftPlayersLayout;
    private LinearLayout lolPlayersLayout;
    private FavoriteTftPlayers favoriteTftPlayersManager;
    private FavoriteLolPlayers favoriteLolPlayersManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        favoriteTftPlayersManager = FavoriteTftPlayers.getInstance(getActivity());
        favoriteLolPlayersManager = FavoriteLolPlayers.getInstance(getActivity());

        tftPlayersLayout = view.findViewById(R.id.tft_players_layout);
        lolPlayersLayout = view.findViewById(R.id.lol_players_layout);

        displayTftPlayers();
        displayLolPlayers();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshFavoritesList();
    }

    private void displayTftPlayers() {
        tftPlayersLayout.removeAllViews();

        List<TFTPlayer> favoritePlayers = favoriteTftPlayersManager.getFavoritePlayers();
        for (TFTPlayer player : favoritePlayers) {
            View playerCard = getLayoutInflater().inflate(R.layout.player_tft_item, tftPlayersLayout, false);
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
            tftPlayersLayout.addView(playerCard);
        }
    }

    private void displayLolPlayers() {
        lolPlayersLayout.removeAllViews();

        List<LolPlayer> favoritePlayers = favoriteLolPlayersManager.getFavoritePlayers();
        for (LolPlayer player : favoritePlayers) {
            View playerCard = getLayoutInflater().inflate(R.layout.player_lol_item, lolPlayersLayout, false);
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

            lolPlayersLayout.addView(playerCard);
        }
    }

    private void openTftActivity(String playerName,String region) {
        Intent intent = new Intent(getActivity(), TftPlayerActivity.class);
        intent.putExtra("playerName", playerName);
        intent.putExtra("region",region);
        startActivity(intent);
    }

    private void openLolActivity(String playerName, String region) {
        Intent intent = new Intent(getActivity(), LolPlayerActivity.class);
        intent.putExtra("playerName", playerName);
        intent.putExtra("region",region);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < tftPlayersLayout.getChildCount(); i++) {
            View childView = tftPlayersLayout.getChildAt(i);
            if (v == childView) {
                TFTPlayer player = (TFTPlayer) childView.getTag();
                if (player != null) {
                    openTftActivity(player.getSummonerName(),player.getRegion());
                }
                return;
            }
        }

        for (int i = 0; i < lolPlayersLayout.getChildCount(); i++) {
            View childView = lolPlayersLayout.getChildAt(i);
            if (v == childView) {
                LolPlayer player = (LolPlayer) childView.getTag();
                if (player != null) {
                    openLolActivity(player.getSummonerName(),player.getRegion());
                }
                return;
            }
        }
    }

    public void refreshFavoritesList() {
        displayTftPlayers();
        displayLolPlayers();
    }
}
