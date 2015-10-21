package ch.swissdeals.database.models;

/**
 * Created by michaelcaraccio on 21/10/15.
 */
public class ModelDeals {


    // ****************************************************************************
    // Constructor
    // ****************************************************************************


    public ModelDeals() {
    }

    public ModelDeals(int fk_provider_id, String title, String description, String image_url, String link, float price, float old_price) {
        this.fk_provider_id = fk_provider_id;
        this.title = title;
        this.description = description;
        this.image_url = image_url;
        this.link = link;
        this.price = price;
        this.old_price = old_price;
    }

    // ****************************************************************************
    // Variables
    // ****************************************************************************

    private int deal_id;
    private int fk_provider_id;
    private String title;
    private String description;
    private String image_url;
    private String link;
    private float price;
    private float old_price;

    // ****************************************************************************
    // Getters and setters
    // ****************************************************************************

    public int getDeal_id() {
        return deal_id;
    }

    public void setDeal_id(int deal_id) {
        this.deal_id = deal_id;
    }

    public int getFk_provider_id() {
        return fk_provider_id;
    }

    public void setFk_provider_id(int fk_provider_id) {
        this.fk_provider_id = fk_provider_id;
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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getOld_price() {
        return old_price;
    }

    public void setOld_price(float old_price) {
        this.old_price = old_price;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ModelDeals{");
        sb.append("deal_id=").append(deal_id);
        sb.append(", fk_provider_id=").append(fk_provider_id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", image_url='").append(image_url).append('\'');
        sb.append(", link='").append(link).append('\'');
        sb.append(", price=").append(price);
        sb.append(", old_price=").append(old_price);
        sb.append('}');
        return sb.toString();
    }
}