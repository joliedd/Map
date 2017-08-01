package ro.mapco.map.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ro.mapco.map.dao.WifiDao;
import ro.mapco.map.model.Wifi;

import java.util.List;

import static org.hibernate.internal.CoreLogging.logger;

/**
 * Created by joliedd on 6/17/2016.
 */
@Service
@Transactional(propagation= Propagation.SUPPORTS)
public class WifiService {

    @Autowired
    private WifiDao wifiDao;

    @Transactional(propagation= Propagation.REQUIRED)
    public void add(Wifi wifi) {
        wifiDao.add(wifi);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    public void update(Wifi wifi) {
        wifiDao.update(wifi);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    public void remove(Wifi wifi) {
        wifiDao.remove(wifi);
    }

    Wifi find(Long key){
       return wifiDao.find(key);
    }

    public Iterable<Wifi> list(){
        return wifiDao.list();
    }

    public List<Wifi> obtainWifi(Float southWestLng,Float southWestLat,Float northWestLng,Float northWestLat,Float northEastLng,Float northEastLat,Float southEastLng,Float southEastLat){

        logger("##!!southWestLng="+southWestLng);
        logger("##!!southWestLat="+southWestLat);
        logger("##!!northWestLng="+northWestLng);
        logger("##!!northWestLat="+northWestLat);

        logger("##!!northEastLng="+northEastLng);
        logger("##!!northEastLat="+northEastLat);
        logger("##!!southEastLng="+southEastLng);
        logger("##!!southEastLat="+southEastLat);

        return wifiDao.obtainWifi(southWestLng,southWestLat,northWestLng,northWestLat,northEastLng,northEastLat,southEastLng,southEastLat);

    }


    public List<Wifi> obtainAllWifi(){
        return wifiDao.obtainAllWifi();

    }

}