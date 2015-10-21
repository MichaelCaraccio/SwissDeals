package ch.swissdeals.webcrapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProviderParser implements Iterable<DealParser> {

    private final String providerID;
    private final String url;
    private List<DealParser> dealParsers;

    public ProviderParser(String providerID, String url) {
        this.providerID = providerID;
        this.url = url;
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
}
