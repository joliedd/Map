package ro.mapco.map.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import ro.mapco.map.model.*;
import ro.mapco.map.service.IncidentService;
import ro.mapco.map.service.LevelService;
import ro.mapco.map.service.SitbufService;
import ro.mapco.map.service.WifiService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.internal.CoreLogging.logger;


/**
 * Created by joliedd on 5/20/2016.
 */

@RestController
public class QueryController {

    @Autowired
    public LevelService levelService;

    @Autowired
    public WifiService wifiService;

    @Autowired
    public IncidentService incidentService;

    @Autowired
    public SitbufService sitbufService;

    @Value("${directory.name}")
    private String dirName;

    @RequestMapping(value = "/api/v1/layers/{layername}", method = RequestMethod.POST,produces = "application/x-protobuf",headers = "Accept=*")
    public byte[] query(@PathVariable String layername,
                        @RequestParam("northEastLat") Float northEastLat,
                        @RequestParam("northEastLng") Float northEastLng,
                        @RequestParam("northWestLat") Float northWestLat,
                        @RequestParam("northWestLng") Float northWestLng,

                        @RequestParam("southEastLat") Float southEastLat,
                        @RequestParam("southEastLng") Float southEastLng,
                        @RequestParam("southWestLat") Float southWestLat,
                        @RequestParam("southWestLng") Float southWestLng
    ) {


        List<Level> poligons=levelService.obtainLevels(layername,southWestLng,southWestLat,northWestLng,northWestLat,northEastLng,northEastLat,southEastLng,southEastLat);
        LevelList list = new LevelList(poligons);

        byte[] bytes;

        LevellistProtos.Levellist.Builder levellist = LevellistProtos.Levellist.newBuilder();

        int i=1;
        for (Level p : poligons) {
           
            p.setType(layername);
            LevellistProtos.Levellist.Level mylevel = new LevellistProtos.Levellist.Level.Builder()
                            .setWkbGeometry(p.getWkb_geometry())
                            .setId(p.getId())
                            .setLevel(p.getLevel())
                            .setType(p.getType())
                            .build();

            levellist.addLevel(mylevel);
        }


       return levellist.build().toByteArray();


    }

    @RequestMapping(value = "/api/v1/layers/wifi", method = RequestMethod.GET,  produces = "application/json",headers = "Accept=*")
    public  @ResponseBody List<Wifi>  queryWifi() {

        List<Wifi> poligons=wifiService.obtainAllWifi();

       return poligons;
    }

    @RequestMapping(value = "/api/v1/layers/wifi", method = RequestMethod.POST, produces = "text/plain",headers = "Accept=*")
    public  String  queryWifi2(
          @RequestParam("northEastLat") Float northEastLat,
          @RequestParam("northEastLng") Float northEastLng,
          @RequestParam("northWestLat") Float northWestLat,
          @RequestParam("northWestLng") Float northWestLng,

          @RequestParam("southEastLat") Float southEastLat,
          @RequestParam("southEastLng") Float southEastLng,
          @RequestParam("southWestLat") Float southWestLat,
          @RequestParam("southWestLng") Float southWestLng
    ) {

        logger("!!southWestLng="+southWestLng);
        logger("!!southWestLat="+southWestLat);
        logger("!!northWestLng="+northWestLng);
        logger("!!northWestLat="+northWestLat);

        logger("!!northEastLng="+northEastLng);
        logger("!!northEastLat="+northEastLat);
        logger("!!southEastLng="+southEastLng);
        logger("!!southEastLat="+southEastLat);

        List<Wifi> poligons=wifiService.obtainWifi(southWestLng,southWestLat,northWestLng,northWestLat,northEastLng,northEastLat,southEastLng,southEastLat);

        List<String> poligonsStr = new ArrayList<String>();
        for (Wifi poligon:poligons) {
            String myStr = poligon.toString();

            String myNewStr = myStr.replace("Wifi{", "{");
            String myNewStr2 = myNewStr.replace("=", ":");
            String myNewStr3 = myNewStr2.replaceAll(", id(.*?)}", "\"properties\":{ id$1 }}");;
            String myNewStr4 = myNewStr3.replace("wkb_geometry:'{", "");
            String myNewStr5 = myNewStr4.replace("'", "\"");
            String myNewStr6 = myNewStr5.replace("\"{\"type\":", "{\"type\":");
            String myNewStr7 = myNewStr6.replace("\"\"properties\":{", ",\"properties\":{\"technology\":\"wifi\",");
            String myNewStr8 = myNewStr7.replaceAll("\"coordinates\":(.*?)}", "\"coordinates\":$1");

            poligonsStr.add(myNewStr8);
        }

        String strFinal = poligonsStr.toString();
        String strFinal2= strFinal.substring(1, strFinal.length()-1);;
        return strFinal2;

    }

    @RequestMapping(value = "/api/v1/kml/{pngname}", method = RequestMethod.GET,produces = "image/png",headers = "Accept=*")
    public @ResponseBody byte[] getFile(@PathVariable String pngname)  {
            try {
                // Retrieve image from the classpath.
                InputStream is =  new FileInputStream(dirName+pngname+".png");//this.getClass().getResourceAsStream(dirName+pngname+".png");
  
				// Prepare buffered image.
                BufferedImage img = ImageIO.read(is);

                // Create a byte array output stream.
                ByteArrayOutputStream bao = new ByteArrayOutputStream();

                // Write to output stream
                ImageIO.write(img, "png", bao);

                return bao.toByteArray();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    @RequestMapping(value = "/api/v1/intersected-layers", method = RequestMethod.POST,  produces = "application/json", headers = "Accept=*")
    public @ResponseBody IntersectList queryIntersected(
            @RequestParam("lat") Float lat,
            @RequestParam("lng") Float lng
    ) {

        logger("!!lat="+lat);
        logger("!!lng="+lng);

        IntersectList intersect= new IntersectList();

        intersect.setSignalLayers(levelService.obtainLevelsByPoint(lat, lng));
        List<Sitbuf> newSits=sitbufService.obtainSitbufByPoint(lat, lng);
        intersect.setNewSits(newSits);
        List<Incident> incidents=incidentService.obtainIncidentsByPoint(lat, lng);
        intersect.setIncidents(incidents);

        return intersect;
    }





}
