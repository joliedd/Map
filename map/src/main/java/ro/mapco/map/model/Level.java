package ro.mapco.map.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by joliedd on 5/20/2016.
 */
@Entity(name = "Level")
@Table(name="Level2G")
@SecondaryTables({
        @SecondaryTable(name="Level3G"),
        @SecondaryTable(name="Level4G")
})
public class Level implements Serializable {

   private static final long serialVersionUID = 1L;

    @Column(name="wkb_geometry")
    private String  wkb_geometry;

    @Id
    @Column(name="id")
    private String id;


    @Column(name="level")
    private String level;

    @Transient
    private String type;

    public Level() {
    }

    public Level(String  wkb_geometry, String id, String level) {
        this.wkb_geometry = wkb_geometry;
        this.id = id;
        this.level = level;
        this.type = "Feature";
    }

    public Level(String  wkb_geometry, String id, String level, String type) {
        this.wkb_geometry = wkb_geometry;
        this.id = id;
        this.level = level;
        this.type = type;
    }

    public String getWkb_geometry() {
        return wkb_geometry;
    }

    public void setWkb_geometry(String  wkb_geometry) {
        this.wkb_geometry = wkb_geometry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Level{" +
                "wkb_geometry=" + wkb_geometry +
                ", id=" + id +
                ", level='" + level + '\'' +
                ", type='" + type + '\'' +
                '}';
    }


}
