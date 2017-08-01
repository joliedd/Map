package ro.mapco.map.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ro.mapco.map.dao.IncidentDao;
import ro.mapco.map.model.Incident;

import java.util.List;

import static org.hibernate.internal.CoreLogging.logger;

/**
 * Created by joliedd on 6/17/2016.
 */
@Service
@Transactional(propagation= Propagation.SUPPORTS)
public class IncidentService {

    @Autowired
    private IncidentDao incidentDao;

    @Transactional(propagation= Propagation.REQUIRED)
    public void add(Incident incident) {
        incidentDao.add(incident);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    public void update(Incident incident) {
        incidentDao.update(incident);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    public void remove(Incident incident) {
        incidentDao.remove(incident);
    }

    Incident find(Long key){
       return incidentDao.find(key);
    }

    public Iterable<Incident> list(){
        return incidentDao.list();
    }

    public List<Incident> obtainIncidentsByPoint(Float lat,Float lng){

        logger("##!!lat="+lat);
        logger("##!!lng="+lng);

        return incidentDao.obtainIncidentsByPoint(lat,lng);

    }

}