package ro.mapco.map.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by joliedd on 6/17/2016.
 */

@Entity(name = "Wifi")
@Table(name="Wifi")

public class Wifi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name="wkb_geometry")
    private String wkb_geometry;

    @Id
    @Column(name="ogc_fid")
    private Integer id;


    @Column(name="tip")
    private String tip;

    @Column(name="location")
    private String location;

    @Column(name="adresa")
    private String adresa;

    @Column(name="zonaac")
    private String zonaac;

    @Column(name="nr_ap_uri")
    private String nr_ap_uri;

    @Column(name="hotspot")
    private String hotspot;

    public Wifi() {
    }

    public Wifi(String wkb_geometry, Integer id, String location, String tip, String adresa, String hotspot, String nr_ap_uri, String zonaac) {
        this.id = id;
        this.location = location;
        this.tip = tip;
        this.adresa = adresa;
        this.hotspot = hotspot;
        this.nr_ap_uri = nr_ap_uri;
        this.zonaac = zonaac;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getWkb_geometry() {
       return wkb_geometry;
    }

    public void setWkb_geometry(String wkb_geometry) {
        this.wkb_geometry = wkb_geometry;
   }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getZonaac() {
        return zonaac;
    }

    public void setZonaac(String zonaac) {
        this.zonaac = zonaac;
    }

    public String getNr_ap_uri() {
        return nr_ap_uri;
    }

    public void setNr_ap_uri(String nr_ap_uri) {
        this.nr_ap_uri = nr_ap_uri;
    }

    public String getHotspot() {
        return hotspot;
    }

    public void setHotspot(String hotspot) {
        this.hotspot = hotspot;
    }

    @Override
    public String toString() {
        return "Wifi{" +
               "wkb_geometry='" + wkb_geometry + '\'' +
                ", id=" + id +
                ", tip='" + tip + '\'' +
                ", location='" + location + '\'' +
                ", adresa='" + adresa + '\'' +
                ", zonaac='" + zonaac + '\'' +
                ", nr_ap_uri='" + nr_ap_uri + '\'' +
                ", hotspot='" + hotspot + '\'' +
                '}';
    }
}
