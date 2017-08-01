package ro.mapco.map.model;

import java.io.Serializable;

/**
 * Created by joliedd on 5/20/2016.
 */

public class SignalLayer implements Serializable {

   private static final long serialVersionUID = 1L;

    private String label;
    private String technology;
    private Integer level;

    public SignalLayer() {

    }

    public SignalLayer(String technology, String label, Integer level) {
        this.technology = technology;
        this.label = label;
        this.level = level;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "SignalLayer{" +
                "label='" + label + '\'' +
                ", technology='" + technology + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}
