package ch.swissdeals.service;

public class DealParser {
    private String titleRegex;
    private String descriptionRegex;
    private String imageRegex;
    private String linkRegex;
    private String priceRegex;
    private String oldPriceRegex;

    public String getTitleRegex() {
        return titleRegex;
    }

    public void setTitleRegex(String titleRegex) {
        this.titleRegex = titleRegex;
    }


    public String getDescriptionRegex() {
        return descriptionRegex;
    }

    public void setDescriptionRegex(String descriptionRegex) {
        this.descriptionRegex = descriptionRegex;
    }


    public String getImageRegex() {
        return imageRegex;
    }

    public void setImageRegex(String imageRegex) {
        this.imageRegex = imageRegex;
    }


    public String getLinkRegex() {
        return linkRegex;
    }

    public void setLinkRegex(String linkRegex) {
        this.linkRegex = linkRegex;
    }


    public String getPriceRegex() {
        return priceRegex;
    }

    public void setPriceRegex(String priceRegex) {
        this.priceRegex = priceRegex;
    }

    public String getOldPriceRegex() {
        return oldPriceRegex;
    }

    public void setOldPriceRegex(String oldPriceRegex) {
        this.oldPriceRegex = oldPriceRegex;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DealParser{");
        sb.append("titleRegex='").append(titleRegex).append('\'');
        sb.append(", descriptionRegex='").append(descriptionRegex).append('\'');
        sb.append(", imageRegex='").append(imageRegex).append('\'');
        sb.append(", linkRegex='").append(linkRegex).append('\'');
        sb.append(", priceRegex='").append(priceRegex).append('\'');
        sb.append(", oldPriceRegex='").append(oldPriceRegex).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
