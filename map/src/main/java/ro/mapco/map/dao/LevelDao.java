package ro.mapco.map.dao;

/**
 * Created by juliedd on 5/20/2016.
 */

import ro.mapco.map.model.Level;
import ro.mapco.map.model.SignalLayer;

import java.util.List;

/**
 * DAO of level.
 */
public interface LevelDao extends GenericDao<Level, Long>{

    public List<Level> obtainLevels4g(Float southWestLng,Float southWestLat,Float northWestLng,Float northWestLat,Float northEastLng,Float northEastLat,Float southEastLng,Float southEastLat);

    public List<Level> obtainLevels3g(Float southWestLng,Float southWestLat,Float northWestLng,Float northWestLat,Float northEastLng,Float northEastLat,Float southEastLng,Float southEastLat);

    public List<Level> obtainLevels2g(Float southWestLng,Float southWestLat,Float northWestLng,Float northWestLat,Float northEastLng,Float northEastLat,Float southEastLng,Float southEastLat);

    public List<SignalLayer>  obtainLevelsByPoint(Float lat, Float lng);
}