package rs.ac.singidunum.rank_watch.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class TFTPlayer implements Parcelable {
    private final String tier;
    private final String rank;
    private final String summonerName;
    private final int leaguePoints;
    private final int wins;
    private final int losses;
    private int profileIconId;
    private String region;


    protected TFTPlayer(Parcel in) {
        tier = in.readString();
        rank = in.readString();
        summonerName = in.readString();
        leaguePoints = in.readInt();
        wins = in.readInt();
        losses = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tier);
        dest.writeString(rank);
        dest.writeString(summonerName);
        dest.writeInt(leaguePoints);
        dest.writeInt(wins);
        dest.writeInt(losses);
    }

    @NonNull
    @Override
    public String toString() {
        return "TFTPlayer{" +
                "tier='" + tier + '\'' +
                ", rank='" + rank + '\'' +
                ", summonerName='" + summonerName + '\'' +
                ", leaguePoints=" + leaguePoints +
                ", wins=" + wins +
                ", losses=" + losses +
                ", profileIconId=" + profileIconId +
                ", region='" + region + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TFTPlayer> CREATOR = new Creator<TFTPlayer>() {
        @Override
        public TFTPlayer createFromParcel(Parcel in) {
            return new TFTPlayer(in);
        }

        @Override
        public TFTPlayer[] newArray(int size) {
            return new TFTPlayer[size];
        }
    };

    public TFTPlayer(String tier, String rank, String summonerName, int leaguePoints, int wins, int losses) {
        this.tier = tier;
        this.rank = rank;
        this.summonerName = summonerName;
        this.leaguePoints = leaguePoints;
        this.wins = wins;
        this.losses = losses;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTier() {
        return tier;
    }

    public String getRank() {
        return rank;
    }

    public String getSummonerName() {
        return summonerName;
    }

    public int getLeaguePoints() {
        return leaguePoints;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getProfileIconId() {
        return profileIconId;
    }

    public void setProfileIconId(int profileIconId) {
        this.profileIconId = profileIconId;
    }
}
