package ro.mapco.map.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import ro.mapco.map.model.Sitbuf;

import java.util.List;

/**
 * Created by juliedd on 6/17/2016.
 */

@Repository("sitbufDao")
public class SitbufDaoImpl extends HibernateDao<Sitbuf, Long> implements SitbufDao {

   
    public List<Sitbuf> obtainSitbufByPoint(Float lat, Float lng) {
        Query query = currentSession().createSQLQuery(
                " SELECT ogc_fid, descriere \n" +
                " FROM sit_buf \n" +
                " RIGHT JOIN (SELECT ogc_fid, wkb_geometry,ST_Intersects(wkb_geometry,\n" +
                " ST_GeomFromText('POINT( '|| cast(:lng as varchar(6)) ||' '|| cast(:lat as varchar(6)) ||')',4326 ) ) as contained  FROM coverage_map.sit_buf ) x USING (ogc_fid) \n" +
                " where x.contained = 't' ;")
                .addEntity(Sitbuf.class)
                .setFloat("lng", lng)
                .setFloat("lat", lat);

        return (List<Sitbuf>) query.list();
    }

}
