package ro.mapco.map.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ro.mapco.map.dao.SitbufDao;
import ro.mapco.map.model.Sitbuf;

import java.util.List;

import static org.hibernate.internal.CoreLogging.logger;

/**
 * Created by joliedd on 6/17/2016.
 */
@Service
@Transactional(propagation= Propagation.SUPPORTS)
public class SitbufService {

    @Autowired
    private SitbufDao sitbufDao;

    @Transactional(propagation= Propagation.REQUIRED)
    public void add(Sitbuf sitbuf) {
        sitbufDao.add(sitbuf);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    public void update(Sitbuf sitbuf) {
        sitbufDao.update(sitbuf);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    public void remove(Sitbuf sitbuf) {
        sitbufDao.remove(sitbuf);
    }

    Sitbuf find(Long key){
       return sitbufDao.find(key);
    }

    public Iterable<Sitbuf> list(){
        return sitbufDao.list();
    }

    public List<Sitbuf> obtainSitbufByPoint(Float lat,Float lng){

        logger("##!!lat="+lat);
        logger("##!!lng="+lng);

        return sitbufDao.obtainSitbufByPoint(lat,lng);

    }


}