package autodromo.punkmkt.com.ahrapp.databases;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by germanpunk on 28/09/16.
 */

@Table(database = AppDatabase.class)
public class Notification extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    private String name;

    @Column
    private String type;

    @Column
    int active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }




}