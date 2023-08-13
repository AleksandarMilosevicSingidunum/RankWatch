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
import rs.ac.singidunum.rank_watch.models.TFTPlayer;

public class RankedTftFragment extends Fragment {
    private TFTPlayer tftPlayer;

    public static RankedTftFragment newInstance(TFTPlayer tftPlayer, int profileIconId,String region) {
        RankedTftFragment fragment = new RankedTftFragment();
        Bundle args = new Bundle();
        args.putParcelable("tftPlayer", tftPlayer);
        args.putInt("profileIconId", profileIconId);
        args.putString("region",region);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tftPlayer = getArguments().getParcelable("tftPlayer");
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranked_tft, container, false);

        // Access the UI elements and update them using the tftPlayer object
        if (tftPlayer != null) {
            assert getArguments() != null;
            int profileIconId = getArguments().getInt("profileIconId");
            ImageView profileImageView = view.findViewById(R.id.imageView);
            ImageView rankImageView = view.findViewById(R.id.secondImageView);
            TextView playerSummonerNameTextView = view.findViewById(R.id.textViewName);
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

            // Load tier rank Icon
            String tier = tftPlayer.getTier();
            if (map.containsKey(tier)) {
                String imageName = map.get(tier);
                @SuppressLint("DiscouragedApi") int imageResource = getResources().getIdentifier(imageName, "drawable", requireContext().getPackageName());
                rankImageView.setImageResource(imageResource);
            } else {
                Log.d("RankedTftFragment", "No image found for tier: " + tier);
            }

            // Load profile image using Picasso
            String profileImageUrl = "https://ddragon.leagueoflegends.com/cdn/13.10.1/img/profileicon/" + profileIconId + ".png";
            Picasso.get().load(profileImageUrl).into(profileImageView);
            playerSummonerNameTextView.setText("Summoner: " + tftPlayer.getSummonerName());
            playerTierTextView.setText("Tier: " + tftPlayer.getTier());
            playerRankTextView.setText("Rank: " + tftPlayer.getRank());
            playerLeaguePointsTextView.setText("League Points: " + tftPlayer.getLeaguePoints());
            playerWinsTextView.setText("Wins: " + tftPlayer.getWins());
            playerLossesTextView.setText("Losses: " + tftPlayer.getLosses());
            String regionCode = tftPlayer.getRegion();
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
