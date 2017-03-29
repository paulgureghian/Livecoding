package com.example.paul.livecoding.pojo;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiveStreamsOnAirP {

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("user")
    @Expose
    private String user;

    @SerializedName("user__slug")
    @Expose
    private String userSlug;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("coding_category")
    @Expose
    private String codingCategory;

    @SerializedName("difficulty")
    @Expose
    private String difficulty;

    @SerializedName("language")
    @Expose
    private String language;

    @SerializedName("tags")
    @Expose
    private String tags;

    @SerializedName("is_live")
    @Expose
    private Boolean isLive;

    @SerializedName("viewers_live")
    @Expose
    private Integer viewersLive;

    @SerializedName("viewing_urls")
    @Expose
    private List<String> viewingUrls = new ArrayList<String>();

    @SerializedName("thumbnail_url")
    @Expose
    private String thumbnailUrl;

    @SerializedName("embed_url")
    @Expose
    private String embedUrl;

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }

    public String getUserSlug() {
        return userSlug;
    }
    public void setUserSlug(String userSlug) {
        this.userSlug = userSlug;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getCodingCategory() {
        return codingCategory;
    }
    public void setCodingCategory(String codingCategory) {
        this.codingCategory = codingCategory;
    }

    public String getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTags() {
        return tags;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }

    public Boolean getIsLive() {
        return isLive;
    }
    public void setIsLive(Boolean isLive) {
        this.isLive = isLive;
    }

    public Integer getViewersLive() {
        return viewersLive;
    }
    public void setViewersLive(Integer viewersLive) {
        this.viewersLive = viewersLive;
    }

    public List<String> getViewingUrls() {
        return viewingUrls;
    }
    public void setViewingUrls(List<String> viewingUrls) {
        this.viewingUrls = viewingUrls;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getEmbedUrl() {
        return embedUrl;
    }
    public void setEmbedUrl(String embedUrl) {
        this.embedUrl = embedUrl;
    }
}
