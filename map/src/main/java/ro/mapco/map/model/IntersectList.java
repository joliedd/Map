package ro.mapco.map.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by joliedd on 5/20/2016.
 */
public class IntersectList implements Serializable {

   private static final long serialVersionUID = 1L;

    List<SignalLayer> signalLayers;

    List<Sitbuf> newSits;

    List<Incident> incidents;

    public IntersectList() {

    }

    public IntersectList(List<SignalLayer> signalLayers, List<Sitbuf> newSits, List<Incident> incidents) {
        this.signalLayers = signalLayers;
        this.newSits = newSits;
        this.incidents = incidents;
    }

    public List<SignalLayer> getSignalLayers() {
        return signalLayers;
    }

    public void setSignalLayers(List<SignalLayer> signalLayers) {
        this.signalLayers = signalLayers;
    }

    public List<Sitbuf> getNewSits() {
        return newSits;
    }

    public void setNewSits(List<Sitbuf> newSits) {
        this.newSits = newSits;
    }

    public List<Incident> getIncidents() {
        return incidents;
    }

    public void setIncidents(List<Incident> incidents) {
        this.incidents = incidents;
    }

    @Override
    public String toString() {
        return "IntersectList{" +
                "signalLayers=" + signalLayers +
                ", newSits=" + newSits +
                ", incidents=" + incidents +
                '}';
    }
}


