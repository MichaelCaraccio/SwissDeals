package ch.swissdeals.webcrapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProviderParser implements Iterable<DealParser> {

    private final String providerID;
    private final String url;
    private final String faviconUrl;
    private final String category;
    private List<DealParser> dealParsers;

    public ProviderParser(String providerID, String url, String faviconUrl, String category) {
        this.providerID = providerID;
        this.url = url;
        this.faviconUrl = faviconUrl;
        this.category = category;
        this.dealParsers = new ArrayList<>();
    }

    public void addDeal(DealParser dealParser) {
        this.dealParsers.add(dealParser);
    }


    @Override
    public Iterator<DealParser> iterator() {
        return this.dealParsers.iterator();
    }

    public String getProviderID() {
        return providerID;
    }

    public String getUrl() {
        return url;
    }

    public String getFaviconUrl() {
        return faviconUrl;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ProviderParser{");
        sb.append("providerID='").append(providerID).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", faviconUrl='").append(faviconUrl).append('\'');
        sb.append(", category='").append(category).append('\'');
        sb.append(", dealParsers=").append(dealParsers);
        sb.append('}');
        return sb.toString();
    }
}
