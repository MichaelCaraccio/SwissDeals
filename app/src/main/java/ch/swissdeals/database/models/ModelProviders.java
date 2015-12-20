package ch.swissdeals.database.models;

public class ModelProviders {

    // ****************************************************************************
    // Constructor
    // ****************************************************************************


    public ModelProviders() {
        this.provider_id = ModelProviders.DEFAULT_ID;
    }

    public ModelProviders(String name, String displayName, String url, String favicon_url, String category) {
        this.name = name;
        this.displayName = displayName;
        this.url = url;
        this.favicon_url = favicon_url;
        this.category = category;
    }

    // ****************************************************************************
    // Variables
    // ****************************************************************************

    private int provider_id;
    private String name;
    private String displayName;
    private String url;
    private String favicon_url;
    private String category;
    private boolean userSubscribed;

    public static final int DEFAULT_ID = -1;

    // ****************************************************************************
    // Getters and setters
    // ****************************************************************************

    public int getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(int provider_id) {
        this.provider_id = provider_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFavicon_url() {
        return favicon_url;
    }

    public void setFavicon_url(String favicon_url) {
        this.favicon_url = favicon_url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isUserSubscribed() {
        return userSubscribed;
    }

    public void setUserSubscribed(boolean userSubscribed) {
        this.userSubscribed = userSubscribed;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ModelProviders{");
        sb.append("provider_id=").append(provider_id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", displayName='").append(displayName).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", favicon_url='").append(favicon_url).append('\'');
        sb.append(", category='").append(category).append('\'');
        sb.append(", userSubscribed=").append(userSubscribed);
        sb.append('}');
        return sb.toString();
    }
}
