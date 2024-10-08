package com.example.gamefit.activity_list;

import java.io.Serializable;

public class ActivityItem implements Serializable {

    private String distance;
    private String activity;
    private int diamonds;
    private String difficulty;
    private String distanceIconUrl;
    private String activityIconUrl;
    private String diamondsIconUrl;
    private String difficultyIconUrl;

    // Diperlukan untuk deserialisasi Firestore
    public ActivityItem() {}

    public ActivityItem(String distance, String activity, int diamonds, String difficulty, String distanceIconUrl, String activityIconUrl, String diamondsIconUrl, String difficultyIconUrl) {
        this.distance = distance;
        this.activity = activity;
        this.diamonds = diamonds;
        this.difficulty = difficulty;
        this.distanceIconUrl = distanceIconUrl;
        this.activityIconUrl = activityIconUrl;
        this.diamondsIconUrl = diamondsIconUrl;
        this.difficultyIconUrl = difficultyIconUrl;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public int getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(int diamonds) {
        this.diamonds = diamonds;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getDistanceIconUrl() {
        return distanceIconUrl;
    }

    public void setDistanceIconUrl(String distanceIconUrl) {
        this.distanceIconUrl = distanceIconUrl;
    }

    public String getActivityIconUrl() {
        return activityIconUrl;
    }

    public void setActivityIconUrl(String activityIconUrl) {
        this.activityIconUrl = activityIconUrl;
    }

    public String getDiamondsIconUrl() {
        return diamondsIconUrl;
    }

    public void setDiamondsIconUrl(String diamondsIconUrl) {
        this.diamondsIconUrl = diamondsIconUrl;
    }

    public String getDifficultyIconUrl() {
        return difficultyIconUrl;
    }

    public void setDifficultyIconUrl(String difficultyIconUrl) {
        this.difficultyIconUrl = difficultyIconUrl;
    }
}
