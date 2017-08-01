package ro.mapco.map.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import ro.mapco.map.model.Wifi;

import java.util.List;

/**
 * Created by juliedd on 6/17/2016.
 */

@Repository("wifiDao")
public class WifiDaoImpl extends HibernateDao<Wifi, Long> implements WifiDao {


    public List<Wifi> obtainAllWifi() {
        Query query = currentSession().createSQLQuery(
                "SELECT wifi.ogc_fid, wifi.tip, wifi.location, wifi.adresa, wifi.zonaac, wifi.nr_ap_uri, wifi.hotspot, ST_AsGeoJSON(wifi.wkb_geometry) as wkb_geometry FROM wifi;")
                .addEntity(Wifi.class);

        return (List<Wifi>) query.list();

    }

    public List<Wifi> obtainWifi(Float southWestLng, Float southWestLat, Float northWestLng, Float northWestLat, Float northEastLng, Float northEastLat, Float southEastLng, Float southEastLat) {
        Query query = currentSession().createSQLQuery(
                "SELECT wifi.ogc_fid, wifi.tip, wifi.location, wifi.adresa, wifi.zonaac, wifi.nr_ap_uri, wifi.hotspot, ST_AsGeoJSON(wifi.wkb_geometry) as wkb_geometry FROM wifi RIGHT JOIN (SELECT ogc_fid, tip, location, adresa, zonaac, nr_ap_uri, hotspot, wkb_geometry, ST_Intersects(wkb_geometry,ST_GeomFromText('POLYGON(( '|| cast(:southWestLng as varchar(6)) ||' '|| cast(:southWestLat as varchar(6)) ||','||cast(:northWestLng as varchar(6))||' '||cast(:northWestLat as varchar(6))||','||cast(:northEastLng as varchar(6))||' '||cast(:northEastLat as varchar(6))||','||cast(:southEastLng as varchar(6))||' '||cast(:southEastLat as varchar(6))||','||cast(:southWestLng as varchar(6))||' '||cast(:southWestLat as varchar(6))||'))',4326 )) as contained  FROM coverage_map.wifi ) x USING (ogc_fid) where x.contained = 't';")
                .addEntity(Wifi.class)
                .setFloat("southWestLng", southWestLng)
                .setFloat("southWestLat", southWestLat)
                .setFloat("northWestLng", northWestLng)
                .setFloat("northWestLat", northWestLat)

                .setFloat("northEastLng", northEastLng)
                .setFloat("northEastLat", northEastLat)
                .setFloat("southEastLng", southEastLng)
                .setFloat("southEastLat", southEastLat);

        return (List<Wifi>) query.list();

    }

}
