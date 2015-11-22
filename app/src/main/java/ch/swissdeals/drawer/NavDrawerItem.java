package ch.swissdeals.drawer;

/**
 * Created by michaelcaraccio on 12/10/15.
 */
public class NavDrawerItem {

    private String title;
    private String icon;
    private int count = 0;
    private int isSubscribed = 0;
    // boolean to set visiblity of the counter
    private boolean isCounterVisible = false;

    public NavDrawerItem(String title, String icon, boolean isCounterVisible, int count, int isSubscribed){
        this.title = title;
        this.icon = icon;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
        this.isSubscribed = isSubscribed;
    }

    public String getTitle(){
        return this.title;
    }

    public String getIcon(){
        return this.icon;
    }

    public int getCount(){
        return this.count;
    }

    public boolean getCounterVisibility(){
        return this.isCounterVisible;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setIcon(String icon){
        this.icon = icon;
    }

    public void setCount(int count){
        this.count = count;
    }

    public void setCounterVisibility(boolean isCounterVisible){
        this.isCounterVisible = isCounterVisible;
    }
}
