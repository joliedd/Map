package ro.mapco.map.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import ro.mapco.map.model.Incident;

import java.util.List;

/**
 * Created by juliedd on 6/17/2016.
 */

@Repository("incidentDao")
public class IncidentDaoImpl extends HibernateDao<Incident, Long> implements IncidentDao {

   
    public List<Incident> obtainIncidentsByPoint(Float lat, Float lng) {

        Query query = currentSession().createSQLQuery(
        " SELECT 'layer2G' as technology, code, sittip_ia, id_ia, type_ia, status_ia, servid, srvtip, srvstat, percep, dtst, orst, dten, oren"+
        " FROM ia_2"+
        " RIGHT JOIN ( SELECT ogc_fid, ST_Intersects(wkb_geometry,ST_GeomFromText('POINT( '|| cast(:lng as varchar(20)) ||' '|| cast(:lat as varchar(20)) ||')',4326 )) as contained"+
        " FROM coverage_map.ia_2 ) x USING (ogc_fid) where x.contained = 't'"+

        " UNION ALL"+

        " SELECT 'layer3G' as technology, code, sittip_ia, id_ia, type_ia, status_ia, servid, srvtip, srvstat, percep, dtst, orst, dten, oren"+
        " FROM ia_3"+
        " RIGHT JOIN ( SELECT ogc_fid, ST_Intersects(wkb_geometry,ST_GeomFromText('POINT( '|| cast(:lng as varchar(20)) ||' '|| cast(:lat as varchar(20)) ||')',4326 )) as contained"+
        " FROM coverage_map.ia_3 ) x USING (ogc_fid) where x.contained = 't'"+

        " UNION ALL"+

        " SELECT 'layer4G' as technology, code, sittip_ia, id_ia, type_ia, status_ia, servid, srvtip, srvstat, percep, dtst, orst, dten, oren"+
        " FROM ia_4"+
        " RIGHT JOIN ( SELECT ogc_fid, ST_Intersects(wkb_geometry,ST_GeomFromText('POINT( '|| cast(:lng as varchar(20)) ||' '|| cast(:lat as varchar(20)) ||')',4326 )) as contained"+
        " FROM coverage_map.ia_4 ) x USING (ogc_fid) where x.contained = 't'")
        .addEntity(Incident.class)
        .setFloat("lng", lng)
        .setFloat("lat", lat);

        return (List<Incident>) query.list();
    }
}
