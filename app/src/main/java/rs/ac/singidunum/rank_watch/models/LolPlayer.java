package rs.ac.singidunum.rank_watch.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class LolPlayer implements Parcelable {

    private String leagueId;
    private final String queueType;
    private final String tier;
    private final String rank;
    private String summonerId;
    private final String summonerName;
    private final int leaguePoints;
    private final int wins;
    private final int losses;

    private int profileIconId;
    private String region;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }


    protected LolPlayer(Parcel in) {
        summonerName = in.readString();
        queueType = in.readString();
        tier = in.readString();
        rank = in.readString();
        leaguePoints = in.readInt();
        wins = in.readInt();
        losses = in.readInt();
    }
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(summonerName);
        dest.writeString(queueType);
        dest.writeString(tier);
        dest.writeString(rank);
        dest.writeInt(leaguePoints);
        dest.writeInt(wins);
        dest.writeInt(losses);
    }



    public static final Parcelable.Creator<LolPlayer> CREATOR = new Parcelable.Creator<LolPlayer>() {
        @Override
        public LolPlayer createFromParcel(Parcel source) {
            return new LolPlayer(source);
        }

        @Override
        public LolPlayer[] newArray(int size) {
            return new LolPlayer[size];
        }
    };
    public LolPlayer(String leagueId, String queueType, String tier, String rank, String summonerId, String summonerName, int leaguePoints, int wins, int losses) {
        this.leagueId = leagueId;
        this.queueType = queueType;
        this.tier = tier;
        this.rank = rank;
        this.summonerId = summonerId;
        this.summonerName = summonerName;
        this.leaguePoints = leaguePoints;
        this.wins = wins;
        this.losses = losses;
    }

    @NonNull
    @Override
    public String toString() {
        return "LolPlayer{" +
                "leagueId='" + leagueId + '\'' +
                ", queueType='" + queueType + '\'' +
                ", tier='" + tier + '\'' +
                ", rank='" + rank + '\'' +
                ", summonerId='" + summonerId + '\'' +
                ", summonerName='" + summonerName + '\'' +
                ", leaguePoints=" + leaguePoints +
                ", wins=" + wins +
                ", losses=" + losses +
                ", profileIconId=" + profileIconId +
                ", region='" + region + '\'' +
                '}';
    }

    public int getProfileIconId() {
        return profileIconId;
    }

    public void setProfileIconId(int profileIconId) {
        this.profileIconId = profileIconId;
    }

    public String getQueueType() {
        return queueType;
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


}
