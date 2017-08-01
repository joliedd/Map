package ro.mapco.map.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by joliedd on 6/17/2016.
 */

@Entity(name = "Sitbuf")
@Table(name="sit_buf")

public class Sitbuf implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="ogc_fid")
    private Integer id;

    @Column(name="descriere")
    private String desc;

    public Sitbuf() {
    }

    public Sitbuf(Integer id, String wkb_geometry, String desc) {
        this.id = id;
    //    this.wkb_geometry = wkb_geometry;
        this.desc = desc;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Sitbuf{" +
                "id=" + id +
                ", desc='" + desc + '\'' +
                '}';
    }
}

