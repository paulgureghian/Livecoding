
package com.example.paul.livecoding.pojo_deserializer;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Result {

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

    /**
     * 
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 
     * @return
     *     The user
     */
    public String getUser() {
        return user;
    }

    /**
     * 
     * @param user
     *     The user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * 
     * @return
     *     The userSlug
     */
    public String getUserSlug() {
        return userSlug;
    }

    /**
     * 
     * @param userSlug
     *     The user__slug
     */
    public void setUserSlug(String userSlug) {
        this.userSlug = userSlug;
    }

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The codingCategory
     */
    public String getCodingCategory() {
        return codingCategory;
    }

    /**
     * 
     * @param codingCategory
     *     The coding_category
     */
    public void setCodingCategory(String codingCategory) {
        this.codingCategory = codingCategory;
    }

    /**
     * 
     * @return
     *     The difficulty
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * 
     * @param difficulty
     *     The difficulty
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * 
     * @return
     *     The language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * 
     * @param language
     *     The language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * 
     * @return
     *     The tags
     */
    public String getTags() {
        return tags;
    }

    /**
     * 
     * @param tags
     *     The tags
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * 
     * @return
     *     The isLive
     */
    public Boolean getIsLive() {
        return isLive;
    }

    /**
     * 
     * @param isLive
     *     The is_live
     */
    public void setIsLive(Boolean isLive) {
        this.isLive = isLive;
    }

    /**
     * 
     * @return
     *     The viewersLive
     */
    public Integer getViewersLive() {
        return viewersLive;
    }

    /**
     * 
     * @param viewersLive
     *     The viewers_live
     */
    public void setViewersLive(Integer viewersLive) {
        this.viewersLive = viewersLive;
    }

    /**
     * 
     * @return
     *     The viewingUrls
     */
    public List<String> getViewingUrls() {
        return viewingUrls;
    }

    /**
     * 
     * @param viewingUrls
     *     The viewing_urls
     */
    public void setViewingUrls(List<String> viewingUrls) {
        this.viewingUrls = viewingUrls;
    }

    /**
     * 
     * @return
     *     The thumbnailUrl
     */
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    /**
     * 
     * @param thumbnailUrl
     *     The thumbnail_url
     */
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    /**
     * 
     * @return
     *     The embedUrl
     */
    public String getEmbedUrl() {
        return embedUrl;
    }

    /**
     * 
     * @param embedUrl
     *     The embed_url
     */
    public void setEmbedUrl(String embedUrl) {
        this.embedUrl = embedUrl;
    }

}
