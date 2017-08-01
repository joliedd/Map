package ro.mapco.map.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import ro.mapco.map.model.Level;
import ro.mapco.map.model.SignalLayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juliedd on 6/1/2016.
 */

@Repository("levelDao")
public class LevelDaoImpl extends HibernateDao<Level, Long> implements LevelDao {

    public List<Level> obtainLevels4g(Float southWestLng,Float southWestLat,Float northWestLng,Float northWestLat,Float northEastLng,Float northEastLat,Float southEastLng,Float southEastLat) {
        Query query = currentSession().createSQLQuery(
                "SELECT ST_AsGeoJSON(level4g.wkb_geometry) as wkb_geometry,level4g.ogc_fid as id, level4g.level FROM level4g RIGHT JOIN (SELECT wkb_geometry,ogc_fid,ST_Intersects(wkb_geometry,ST_GeomFromText('POLYGON(( '|| cast(:southWestLng as varchar(6)) ||' '|| cast(:southWestLat as varchar(6)) ||','||cast(:northWestLng as varchar(6))||' '||cast(:northWestLat as varchar(6))||','||cast(:northEastLng as varchar(6))||' '||cast(:northEastLat as varchar(6))||','||cast(:southEastLng as varchar(6))||' '||cast(:southEastLat as varchar(6))||','||cast(:southWestLng as varchar(6))||' '||cast(:southWestLat as varchar(6))||'))',4326 )) as contained  FROM coverage_map.level4g ) x USING (ogc_fid) where x.contained = 't' order by id;")
                .addEntity(Level.class)
                .setFloat("southWestLng", southWestLng)
                .setFloat("southWestLat", southWestLat)
                .setFloat("northWestLng", northWestLng)
                .setFloat("northWestLat", northWestLat)

                .setFloat("northEastLng", northEastLng)
                .setFloat("northEastLat", northEastLat)
                .setFloat("southEastLng", southEastLng)
                .setFloat("southEastLat", southEastLat);

        return (List<Level>) query.list();

    }


    public List<Level> obtainLevels3g(Float southWestLng,Float southWestLat,Float northWestLng,Float northWestLat,Float northEastLng,Float northEastLat,Float southEastLng,Float southEastLat) {
        Query query = currentSession().createSQLQuery(
                "SELECT ST_AsGeoJSON(level3g.wkb_geometry) as wkb_geometry,level3g.ogc_fid as id, level3g.level FROM level3g RIGHT JOIN (SELECT wkb_geometry,ogc_fid,ST_Intersects(wkb_geometry,ST_GeomFromText('POLYGON(( '|| cast(:southWestLng as varchar(6)) ||' '|| cast(:southWestLat as varchar(6)) ||','||cast(:northWestLng as varchar(6))||' '||cast(:northWestLat as varchar(6))||','||cast(:northEastLng as varchar(6))||' '||cast(:northEastLat as varchar(6))||','||cast(:southEastLng as varchar(6))||' '||cast(:southEastLat as varchar(6))||','||cast(:southWestLng as varchar(6))||' '||cast(:southWestLat as varchar(6))||'))',4326 )) as contained  FROM coverage_map.level3g ) x USING (ogc_fid) where x.contained = 't' order by id;")
                .addEntity(Level.class)
                .setFloat("southWestLng", southWestLng)
                .setFloat("southWestLat", southWestLat)
                .setFloat("northWestLng", northWestLng)
                .setFloat("northWestLat", northWestLat)

                .setFloat("northEastLng", northEastLng)
                .setFloat("northEastLat", northEastLat)
                .setFloat("southEastLng", southEastLng)
                .setFloat("southEastLat", southEastLat);

        return (List<Level>) query.list();

    }


    public List<Level> obtainLevels2g(Float southWestLng,Float southWestLat,Float northWestLng,Float northWestLat,Float northEastLng,Float northEastLat,Float southEastLng,Float southEastLat) {

        Query query = currentSession().createSQLQuery(
                "SELECT ST_AsGeoJSON(level2g.wkb_geometry) as wkb_geometry,level2g.ogc_fid as id, level2g.level FROM level2g RIGHT JOIN (SELECT wkb_geometry,ogc_fid,ST_Intersects(wkb_geometry,ST_GeomFromText('POLYGON(( '|| cast(:northWestLng as varchar(6)) ||' '|| cast(:northWestLat as varchar(6)) ||','||cast(:southWestLng as varchar(6))||' '||cast(:southWestLat as varchar(6))||','||cast(:southEastLng as varchar(6))||' '||cast(:southEastLat as varchar(6))||','||cast(:northEastLng as varchar(6))||' '||cast(:northEastLat as varchar(6))||','||cast(:northWestLng as varchar(6))||' '||cast(:northWestLat as varchar(6))||'))',4326 )) as contained  FROM coverage_map.level2g ) x USING (ogc_fid) where x.contained = 't' order by id;")
                .addEntity(Level.class)
                .setFloat("northWestLng", northWestLng)
                .setFloat("northWestLat", northWestLat)
                .setFloat("southWestLng", southWestLng)
                .setFloat("southWestLat", southWestLat)

                .setFloat("southEastLng", southEastLng)
                .setFloat("southEastLat", southEastLat)
                .setFloat("northEastLng", northEastLng)
                .setFloat("northEastLat", northEastLat)
               ;



        return (List<Level>) query.list();
    }

    public List<SignalLayer> obtainLevelsByPoint(Float lat, Float lng){

        List<SignalLayer> layers = new ArrayList<SignalLayer>();

        Query query0 = currentSession().createSQLQuery(
        " SELECT count(level4gplus.ogc_fid)"+
        " FROM level4gplus"+
        " RIGHT JOIN ( SELECT ogc_fid, ST_Intersects(wkb_geometry,ST_GeomFromText('POINT( '|| cast(:lng as varchar(6)) ||' '|| cast(:lat as varchar(6)) ||')',4326 )) as contained" +
        " FROM coverage_map.level4gplus ) x USING (ogc_fid) where x.contained = 't'; ")
        .setFloat("lng", lng)
        .setFloat("lat", lat);
        Object result0 = query0.uniqueResult();

        try {
            if (result0 != null && Integer.parseInt(result0.toString())>0) {
                layers.add(new SignalLayer("layer4GPlus","4G+", 1));
            }
        }catch(NullPointerException e){ e.printStackTrace(); }

        Query query = currentSession().createSQLQuery(
        " SELECT min(level4g.level)"+
        " FROM level4g"+
        " RIGHT JOIN ( SELECT id, ST_Intersects(wkb_geometry,ST_GeomFromText('POINT( '|| cast(:lng as varchar(6)) ||' '|| cast(:lat as varchar(6)) ||')',4326 )) as contained" +
        " FROM coverage_map.level4g ) x USING (id) where x.contained = 't'; ")
        .setFloat("lng", lng)
        .setFloat("lat", lat);
        Object result = query.uniqueResult();

      try {  
          if (result != null) {              
              layers.add(new SignalLayer("layer4G","4G", Integer.parseInt(result.toString())));
          }
      }catch(NullPointerException e){ }

        Query query1 = currentSession().createSQLQuery(
        " SELECT min(level3g.level)"+
        " FROM level3g" +
        " RIGHT JOIN ( SELECT  id,ST_Intersects(wkb_geometry,ST_GeomFromText('POINT( '|| cast(:lng as varchar(6)) ||' '|| cast(:lat as varchar(6)) ||')',4326 )) as contained" +
        " FROM coverage_map.level3g ) x USING (id) where x.contained = 't'; ")
         .setFloat("lng", lng)
         .setFloat("lat", lat);

        Object result1 = query1.uniqueResult();

        try { 
            if (result1 != null) {                
                layers.add(new SignalLayer("layer3G","3G",  Integer.parseInt(result1.toString())));
            }
        }catch(NullPointerException e){  }

        Query query2 = currentSession().createSQLQuery(
        " SELECT min(level2g.level)" +
        " FROM level2g" +
        " RIGHT JOIN ( SELECT  id, ST_Intersects(wkb_geometry,ST_GeomFromText('POINT( '|| cast(:lng as varchar(6)) ||' '|| cast(:lat as varchar(6)) ||')',4326 )) as contained" +
        " FROM coverage_map.level2g ) x USING (id) where x.contained = 't'; ")
         .setFloat("lng", lng)
         .setFloat("lat", lat);

        Object result2 = query2.uniqueResult();

        try {
            if (result2 != null) {                
                layers.add(new SignalLayer("layer2G","2G",  Integer.parseInt(result2.toString())));

            }
        }catch(NullPointerException e){  }

        return layers;
    }
}
