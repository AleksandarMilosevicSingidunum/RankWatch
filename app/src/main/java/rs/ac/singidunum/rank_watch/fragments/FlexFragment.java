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
import java.util.Objects;

import rs.ac.singidunum.rank_watch.R;
import rs.ac.singidunum.rank_watch.models.LolPlayer;

public class FlexFragment extends Fragment {
    private LolPlayer flexPlayer;

    public static FlexFragment newInstance(LolPlayer flexPlayer, int profileIconId, String region) {
        FlexFragment fragment = new FlexFragment();
        Bundle args = new Bundle();
        args.putParcelable("flexPlayer", flexPlayer);
        args.putInt("profileIconId", profileIconId);
        args.putString("region",region);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            flexPlayer = getArguments().getParcelable("flexPlayer");
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flex, container, false);

        // Access the UI elements and update them using the lolPlayer object
        if (flexPlayer != null) {
            assert getArguments() != null;
            int profileIconId = getArguments().getInt("profileIconId");
            String region = getArguments().getString("region");
            ImageView profileImageView = view.findViewById(R.id.imageView);
            ImageView rankImageView = view.findViewById(R.id.secondImageView);
            TextView playerSummonerNameTextViewFlex = view.findViewById(R.id.textViewNameFlex);
            TextView playerQueueTypeTextViewFlex = view.findViewById(R.id.textViewQueueTypeFlex);
            TextView playerTierTextViewFlex = view.findViewById(R.id.textViewTierFlex);
            TextView playerRankTextViewFlex = view.findViewById(R.id.textViewRankFlex);
            TextView playerLeaguePointsTextViewFlex = view.findViewById(R.id.textViewLPFlex);
            TextView playerWinsTextViewFlex = view.findViewById(R.id.textViewWinsFlex);
            TextView playerLossesTextViewFlex = view.findViewById(R.id.textViewLossesFlex);
            TextView playerRegion = view.findViewById(R.id.textViewRegionFlex);
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


            String tier = flexPlayer.getTier();
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
            playerSummonerNameTextViewFlex.setText("Summoner: " + flexPlayer.getSummonerName());
            playerQueueTypeTextViewFlex.setText("Queue Type: " + flexPlayer.getQueueType());
            playerTierTextViewFlex.setText("Tier: " + flexPlayer.getTier());
            playerRankTextViewFlex.setText("Rank: " + flexPlayer.getRank());
            playerLeaguePointsTextViewFlex.setText("League Points: " + flexPlayer.getLeaguePoints());
            playerWinsTextViewFlex.setText("Wins: " + flexPlayer.getWins());
            playerLossesTextViewFlex.setText("Losses: " + flexPlayer.getLosses());
            Log.d("onCreateView: ", region != null ? region : "Region code is null");
            String regionName;
            switch (Objects.requireNonNull(region)) {
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
            playerRegion.setText("Region: " + regionName);
        }

        return view;
    }
}