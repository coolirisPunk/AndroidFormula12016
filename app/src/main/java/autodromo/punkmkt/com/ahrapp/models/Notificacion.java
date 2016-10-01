package autodromo.punkmkt.com.ahrapp.models;

/**
 * Created by germanpunk on 05/08/16.
 */
public class Notificacion {
    String id;
    String name;
    String status;
    String slug;

    public Notificacion(String id, String name, String type){
        this.id = id;
        this.name = name;
        this.slug = slug;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
