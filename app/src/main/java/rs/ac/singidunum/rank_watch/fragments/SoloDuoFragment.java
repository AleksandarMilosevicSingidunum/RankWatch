package rs.ac.singidunum.rank_watch.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import rs.ac.singidunum.rank_watch.R;
import rs.ac.singidunum.rank_watch.models.LolPlayer;

public class SoloDuoFragment extends Fragment {
    private LolPlayer soloDuoPlayer;

    public static SoloDuoFragment newInstance(LolPlayer soloDuoPlayer, int profileIconId,String region) {
        SoloDuoFragment fragment = new SoloDuoFragment();
        Bundle args = new Bundle();
        args.putParcelable("soloDuoPlayer", soloDuoPlayer);
        args.putInt("profileIconId", profileIconId);
        args.putString("region",region);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            soloDuoPlayer = getArguments().getParcelable("soloDuoPlayer");
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solo_duo, container, false);

        // Access the UI elements and update them using the lolPlayer object
        if (soloDuoPlayer != null) {
            assert getArguments() != null;
            int profileIconId = getArguments().getInt("profileIconId");
            ImageView profileImageView = view.findViewById(R.id.imageView);
            ImageView rankImageView = view.findViewById(R.id.secondImageView);
            TextView playerSummonerNameTextView = view.findViewById(R.id.textViewName);
            TextView playerQueueTypeTextView = view.findViewById(R.id.textViewQueueType);
            TextView playerTierTextView = view.findViewById(R.id.textViewTier);
            TextView playerRankTextView = view.findViewById(R.id.textViewRank);
            TextView playerLeaguePointsTextView = view.findViewById(R.id.textViewLP);
            TextView playerWinsTextView = view.findViewById(R.id.textViewWins);
            TextView playerLossesTextView = view.findViewById(R.id.textViewLosses);
            TextView playerRegionTextView = view.findViewById(R.id.textViewRegion);

            Map<String, String> map = new HashMap<>();
            map.put("IRON", "emblemiron");
            map.put("BRONZE", "emblembronze");
            map.put("SILVER", "emblemsilver");
            map.put("GOLD", "emblemgold");
            map.put("PLATINUM", "emblemplatinum");
            map.put("DIAMOND", "emblemdiamond");
            map.put("MASTER", "emblemmaster");
            map.put("GRANDMASTER", "emblemgrandmaster");
            map.put("CHALLENGER", "emblemchallenger");

            //Load tier rank Icon
            String tier = soloDuoPlayer.getTier();
            if (map.containsKey(tier)) {
                String imageName = map.get(tier);
                @SuppressLint("DiscouragedApi") int imageResource = getResources().getIdentifier(imageName, "drawable", requireContext().getPackageName());
                rankImageView.setImageResource(imageResource);
            } else {
                Log.d("LolPlayerFragment", "No image found for tier: " + tier);
            }

            // Load profile image using Picasso
            String profileImageUrl = "https://ddragon.leagueoflegends.com/cdn/13.10.1/img/profileicon/" + profileIconId + ".png";
            Picasso.get().load(profileImageUrl).into(profileImageView);
            playerSummonerNameTextView.setText("Summoner: " + soloDuoPlayer.getSummonerName());
            playerQueueTypeTextView.setText("Queue Type: " + soloDuoPlayer.getQueueType());
            playerTierTextView.setText("Tier: " + soloDuoPlayer.getTier());
            playerRankTextView.setText("Rank: " + soloDuoPlayer.getRank());
            playerLeaguePointsTextView.setText("League Points: " + soloDuoPlayer.getLeaguePoints());
            playerWinsTextView.setText("Wins: " + soloDuoPlayer.getWins());
            playerLossesTextView.setText("Losses: " + soloDuoPlayer.getLosses());
            String regionCode = soloDuoPlayer.getRegion();
            String regionName;
            switch (regionCode) {
                case "eun1":
                    regionName = "EUNE";
                    break;
                case "euw1":
                    regionName = "EUW";
                    break;
                case "na1":
                    regionName = "NA";
                    break;
                case "kr":
                    regionName = "KR";
                    break;
                default:
                    regionName = "Unknown";
                    break;
            }
            playerRegionTextView.setText("Region: " + regionName);
        }

        return view;
    }


}