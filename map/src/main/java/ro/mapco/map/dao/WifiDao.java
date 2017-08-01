package ro.mapco.map.dao;

/**
 * Created by juliedd on 6/17/2016.
 */

import ro.mapco.map.model.Wifi;

import java.util.List;

/**
 * DAO of level.
 */
public interface WifiDao extends GenericDao<Wifi, Long>{

    public List<Wifi> obtainAllWifi();
    public List<Wifi> obtainWifi(Float southWestLng, Float southWestLat, Float northWestLng, Float northWestLat, Float northEastLng, Float northEastLat, Float southEastLng, Float southEastLat);

}