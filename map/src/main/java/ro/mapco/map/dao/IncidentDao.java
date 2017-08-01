package ro.mapco.map.dao;

/**
 * Created by juliedd on 6/17/2016.
 */

import ro.mapco.map.model.Incident;

import java.util.List;

/**
 * DAO of level.
 */
public interface IncidentDao extends GenericDao<Incident, Long>{

    public List<Incident> obtainIncidentsByPoint(Float lat, Float lng);
}