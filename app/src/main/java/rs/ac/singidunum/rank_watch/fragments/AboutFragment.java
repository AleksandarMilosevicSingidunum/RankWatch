package rs.ac.singidunum.rank_watch.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import rs.ac.singidunum.rank_watch.R;

public class AboutFragment extends Fragment {

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setContent(view);
    }

    private void setContent(View view) {
        TextView aboutTextView = view.findViewById(R.id.about_text_view);

        String aboutContent = "Welcome to Rank Watch!\n\n" +
                "Rank Watch is an application that allows you to search for League of Legends players and TFT players and view their ranks in different regions (NA, EUNE, KR, EUW).\n\n" +
                "You can also favorite players and their information will be displayed in the Home fragment as well as in the respective League of Legends or TFT fragment.\n\n" +
                "In the Home fragment, you will find both League of Legends and TFT players, while in the League of Legends and TFT fragments, you can search for specific players.\n\n" +
                "I hope you enjoy using Rank Watch and find it helpful in tracking the ranks of your favorite players!\n\n"
                ;

        aboutTextView.setText(aboutContent);
    }
}
