package ch.swissdeals.service;

public class DealParser {
    private String titleXPath;
    private String titleRegex;

    private String descriptionXPath;
    private String descriptionRegex;

    private String imageXPath;
    private String imageRegex;

    private String linkXPath;
    private String linkRegex;

    private String priceXPath;
    private String priceRegex;

    private String oldPriceXPath;
    private String oldPriceRegex;

    public String getTitleXPath() {
        return titleXPath;
    }

    public void setTitleXPath(String titleXPath) {
        this.titleXPath = titleXPath;
    }

    public String getTitleRegex() {
        return titleRegex;
    }

    public void setTitleRegex(String titleRegex) {
        this.titleRegex = titleRegex;
    }

    public String getDescriptionXPath() {
        return descriptionXPath;
    }

    public void setDescriptionXPath(String descriptionXPath) {
        this.descriptionXPath = descriptionXPath;
    }

    public String getDescriptionRegex() {
        return descriptionRegex;
    }

    public void setDescriptionRegex(String descriptionRegex) {
        this.descriptionRegex = descriptionRegex;
    }

    public String getImageXPath() {
        return imageXPath;
    }

    public void setImageXPath(String imageXPath) {
        this.imageXPath = imageXPath;
    }

    public String getImageRegex() {
        return imageRegex;
    }

    public void setImageRegex(String imageRegex) {
        this.imageRegex = imageRegex;
    }

    public String getLinkXPath() {
        return linkXPath;
    }

    public void setLinkXPath(String linkXPath) {
        this.linkXPath = linkXPath;
    }

    public String getLinkRegex() {
        return linkRegex;
    }

    public void setLinkRegex(String linkRegex) {
        this.linkRegex = linkRegex;
    }

    public String getPriceXPath() {
        return priceXPath;
    }

    public void setPriceXPath(String priceXPath) {
        this.priceXPath = priceXPath;
    }

    public String getPriceRegex() {
        return priceRegex;
    }

    public void setPriceRegex(String priceRegex) {
        this.priceRegex = priceRegex;
    }

    public String getOldPriceXPath() {
        return oldPriceXPath;
    }

    public void setOldPriceXPath(String oldPriceXPath) {
        this.oldPriceXPath = oldPriceXPath;
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
        sb.append("titleXPath='").append(titleXPath).append('\'');
        sb.append(", titleRegex='").append(titleRegex).append('\'');
        sb.append(", descriptionXPath='").append(descriptionXPath).append('\'');
        sb.append(", descriptionRegex='").append(descriptionRegex).append('\'');
        sb.append(", imageXPath='").append(imageXPath).append('\'');
        sb.append(", imageRegex='").append(imageRegex).append('\'');
        sb.append(", linkXPath='").append(linkXPath).append('\'');
        sb.append(", linkRegex='").append(linkRegex).append('\'');
        sb.append(", priceXPath='").append(priceXPath).append('\'');
        sb.append(", priceRegex='").append(priceRegex).append('\'');
        sb.append(", oldPriceXPath='").append(oldPriceXPath).append('\'');
        sb.append(", oldPriceRegex='").append(oldPriceRegex).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
