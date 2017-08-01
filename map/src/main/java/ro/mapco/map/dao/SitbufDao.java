package ro.mapco.map.dao;

/**
 * Created by juliedd on 6/17/2016.
 */

import ro.mapco.map.model.Sitbuf;

import java.util.List;

/**
 * DAO of level.
 */
public interface SitbufDao extends GenericDao<Sitbuf, Long>{

   // public List<Sitbuf> obtainSitbuf(Float southWestLng, Float southWestLat, Float northWestLng, Float northWestLat, Float northEastLng, Float northEastLat, Float southEastLng, Float southEastLat);
     public List<Sitbuf> obtainSitbufByPoint(Float lat, Float lng);

}