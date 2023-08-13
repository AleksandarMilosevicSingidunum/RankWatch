package rs.ac.singidunum.rank_watch.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import rs.ac.singidunum.rank_watch.fragments.RankedTftFragment;
import rs.ac.singidunum.rank_watch.models.TFTPlayer;

public class TftPlayerPagerAdapter extends FragmentPagerAdapter {
    private final TFTPlayer tftPlayer;

    public TftPlayerPagerAdapter(FragmentManager fragmentManager, TFTPlayer tftPlayer) {
        super(fragmentManager);
        this.tftPlayer = tftPlayer;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            int profileIconId = tftPlayer.getProfileIconId();
            String region = "Eune";
            return RankedTftFragment.newInstance(tftPlayer, profileIconId, region);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 1; // Number of tabs/fragments
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Ranked TFT";
        }
        return null;
    }
}
