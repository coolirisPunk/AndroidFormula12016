package autodromo.punkmkt.com.ahrapp.models;

/**
 * Created by germanpunk on 13/09/16.
 */
public class Zone {
    private String id;
    private String title;
    public Zone(String id, String title){
        this.id = id;
        this.title = title;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
