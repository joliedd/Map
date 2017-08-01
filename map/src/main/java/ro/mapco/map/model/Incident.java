package ro.mapco.map.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by joliedd on 5/20/2016.
 */
@Entity(name = "Incident")
@Table(name="IA_2")
@SecondaryTables({
        @SecondaryTable(name="IA_3"),
        @SecondaryTable(name="IA_4")
})
public class Incident implements Serializable {

    private static final long serialVersionUID = 1L;


    private String technology;

    @Column(name="code")
    private String code;

    @Column(name="sittip_ia")
    private String sittip_ia;

    @Id
    @Column(name="id_ia")
    private Integer id_ia;

    @Column(name="type_ia")
    private String type_ia;

    @Column(name="status_ia")
    private String status_ia;

    @Column(name="servid")
    private Integer servid;

    @Column(name="srvtip")
    private String srvtip;

    @Column(name="srvstat")
    private String srvstat;

    @Column(name="percep")
    private String percep;

    @Column(name="dtst")
    private String dtst;

    @Column(name="orst")
    private String orst;

    @Column(name="dten")
    private String dten;

    @Column(name="oren")
    private String oren;


    public Incident() {
    }

    public Incident(String technology, String code, String sittip_ia, Integer id_ia, String type_ia, String status_ia, Integer servid, String srvtip, String srvstat, String percep, String dtst, String orst, String dten, String oren) {
        this.technology = technology;
        this.code = code;
        this.sittip_ia = sittip_ia;
        this.id_ia = id_ia;
        this.type_ia = type_ia;
        this.status_ia = status_ia;
        this.servid = servid;
        this.srvtip = srvtip;
        this.srvstat = srvstat;
        this.percep = percep;
        this.dtst = dtst;
        this.orst = orst;
        this.dten = dten;
        this.oren = oren;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSittip_ia() {
        return sittip_ia;
    }

    public void setSittip_ia(String sittip_ia) {
        this.sittip_ia = sittip_ia;
    }

    public Integer getId_ia() {
        return id_ia;
    }

    public void setId_ia(Integer id_ia) {
        this.id_ia = id_ia;
    }

    public String getType_ia() {
        return type_ia;
    }

    public void setType_ia(String type_ia) {
        this.type_ia = type_ia;
    }

    public String getStatus_ia() {
        return status_ia;
    }

    public void setStatus_ia(String status_ia) {
        this.status_ia = status_ia;
    }

    public Integer getServid() {
        return servid;
    }

    public void setServid(Integer servid) {
        this.servid = servid;
    }

    public String getSrvtip() {
        return srvtip;
    }

    public void setSrvtip(String srvtip) {
        this.srvtip = srvtip;
    }

    public String getSrvstat() {
        return srvstat;
    }

    public void setSrvstat(String srvstat) {
        this.srvstat = srvstat;
    }

    public String getPercep() {
        return percep;
    }

    public void setPercep(String percep) {
        this.percep = percep;
    }

    public String getDtst() {
        return dtst;
    }

    public void setDtst(String dtst) {
        this.dtst = dtst;
    }

    public String getOrst() {
        return orst;
    }

    public void setOrst(String orst) {
        this.orst = orst;
    }

    public String getDten() {
        return dten;
    }

    public void setDten(String dten) {
        this.dten = dten;
    }

    public String getOren() {
        return oren;
    }

    public void setOren(String oren) {
        this.oren = oren;
    }

    @Override
    public String toString() {
        return "Incident{" +
                "technology='" + technology + '\'' +
                ", code='" + code + '\'' +
                ", sittip_ia='" + sittip_ia + '\'' +
                ", id_ia=" + id_ia +
                ", type_ia='" + type_ia + '\'' +
                ", status_ia='" + status_ia + '\'' +
                ", servid=" + servid +
                ", srvtip='" + srvtip + '\'' +
                ", srvstat='" + srvstat + '\'' +
                ", percep='" + percep + '\'' +
                ", dtst='" + dtst + '\'' +
                ", orst='" + orst + '\'' +
                ", dten='" + dten + '\'' +
                ", oren='" + oren + '\'' +
                '}';
    }
}
