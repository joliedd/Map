package ro.mapco.map.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ro.mapco.map.dao.LevelDao;
import ro.mapco.map.model.Level;
import ro.mapco.map.model.SignalLayer;

import java.util.List;

import static org.hibernate.internal.CoreLogging.logger;

/**
 * Created by joliedd on 5/20/2016.
 */
@Service
@Transactional(propagation= Propagation.SUPPORTS)
public class LevelService {

    @Autowired
    private LevelDao levelDao;

    @Transactional(propagation= Propagation.REQUIRED)
    public void add(Level level) {
        levelDao.add(level);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    public void update(Level level) {
        levelDao.update(level);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    public void remove(Level level) {
        levelDao.remove(level);
    }

    Level find(Long key){
       return levelDao.find(key);
    }

    public Iterable<Level> list(){
        return levelDao.list();
    }

    public List<Level> obtainLevels(String layername,Float southWestLng,Float southWestLat,Float northWestLng,Float northWestLat,Float northEastLng,Float northEastLat,Float southEastLng,Float southEastLat){

        logger("##layername="+layername);
        logger("##!!southWestLng="+southWestLng);
        logger("##!!southWestLat="+southWestLat);
        logger("##!!northWestLng="+northWestLng);
        logger("##!!northWestLat="+northWestLat);

        logger("##!!northEastLng="+northEastLng);
        logger("##!!northEastLat="+northEastLat);
        logger("##!!southEastLng="+southEastLng);
        logger("##!!southEastLat="+southEastLat);
       

        if(layername.equals("layer4G")){
            return levelDao.obtainLevels4g(southWestLng,southWestLat,northWestLng,northWestLat,northEastLng,northEastLat,southEastLng,southEastLat);
        }else if(layername.equals("layer3G")){
            return levelDao.obtainLevels3g(southWestLng,southWestLat,northWestLng,northWestLat,northEastLng,northEastLat,southEastLng,southEastLat);
        }else if(layername.equals("layer2G")){
            return levelDao.obtainLevels2g(southWestLng,southWestLat,northWestLng,northWestLat,northEastLng,northEastLat,southEastLng,southEastLat);
        }
        return null;
    }

    public List<SignalLayer>  obtainLevelsByPoint(Float lat, Float lng){

        logger("##!!lat="+lat);
        logger("##!!lng="+lng);

        return levelDao.obtainLevelsByPoint(lat,lng);

    }

}