package ro.mapco.map.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.mapco.map.model.Responsecode;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by joliedd on 5/16/2016.
 */

@RestController
public class ConvertController {

    @Autowired
    public Responsecode myresponse;

    @Value("${otp.host}")
    private String myHost;
    @Value("${otp.port}")
    private String myPort;
    @Value("${otp.username}")
    private String myUsername;
    @Value("${otp.password}")
    private String myPassword;
    @Value("${otp.database}")
    private String myDatabase;
    @Value("${otp.schema}")
    private String mySchema;
    @Value("${otp.driverClassName}")
    private String driverClassName;


    @Value("${integrity.levels}")
    private String levelsSHA1;
    @Value("${integrity.wifi}")
    private String wifiSHA1;
    @Value("${integrity.sitbuf}")
    private String sitbufSHA1;
    @Value("${integrity.incidents}")
    private String incidentsSHA1;
    @Value("${directory.name}")
    private String dirName;

    Logger log = LoggerFactory.getLogger(ConvertController.class);


    @RequestMapping(value = "/api/v1/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?>  uploadFile(
            @RequestParam("upload") MultipartFile file , HttpServletRequest request) {

        try {

            file.transferTo(new File(dirName+file.getOriginalFilename()));

        }
        catch (Exception e) {
            return new ResponseEntity<MultipartFile>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<MultipartFile>(HttpStatus.OK);

    } 

    @RequestMapping(value = "/api/v1/importLevels", method = RequestMethod.GET, produces = "application/json")
    public  @ResponseBody Responsecode importLevels(Object request) {
        boolean foundError = false;
        String[] data = null;
        String err=null;
        String fileWithError=null;
        String[] files= {"Level1_2G.GeoJSON", "level2G","Level2_2G.GeoJSON", "level2G", "Level3_2G.GeoJSON", "level2G", "Level1_3G.GeoJSON", "level3G", "Level2_3G.GeoJSON",  "level3G","Level3_3G.GeoJSON",  "level3G", "Level1_4G.GeoJSON",  "level4G","Level2_4G.GeoJSON",  "level4G","Level3_4G.GeoJSON",  "level4G", "4Gplus.GeoJSON", "level4Gplus"};
        String[] tables = {"level2G","level3G","level4G"};
        String[] files2= {"Level1_2G.GeoJSON","Level2_2G.GeoJSON", "Level3_2G.GeoJSON", "Level1_3G.GeoJSON", "Level2_3G.GeoJSON", "Level3_3G.GeoJSON", "Level1_4G.GeoJSON", "Level2_4G.GeoJSON", "Level3_4G.GeoJSON" ,  "4Gplus.GeoJSON"};
        String[] sha1Files = {levelsSHA1+".zip",levelsSHA1+".zip.sha1",levelsSHA1+"1.zip.sha1"};

        String mess0 =  checkIntegrity(levelsSHA1);

        if(!mess0.equals("OK")) {
            return myresponse = new Responsecode("100", mess0, "http://confluence...", files2);
        }

        String mess00 =  unzipFiles(levelsSHA1);

        if(!mess00.equals("OK")) {
            return myresponse = new Responsecode("110", mess00, "http://confluence...", files2);
        }
		
		//truncate temporary tables
        String mess000 =  truncateTables(tables,true); 

        if(!mess000.equals("OK")) {
            return myresponse = new Responsecode("1000", mess000, "http://confluence...", tables);
        }

        for(int i=0; i<files.length; i+=2){

            try {

                String[] params = {"-f", "PostgreSQL",
                        "PG:host=" + myHost + " port=" + myPort + " user='" + myUsername + "' password='" + myPassword + "' dbname='" + myDatabase + "'",
                        dirName+files[i],
                        "-nln", mySchema + "."+"tmp_" + files[i+1]
                        , "-append"
                };

                for(String param:params) {
                    log.info(param);
                }
                ro.mapco.map.util.ogr2ogr.convert(params);

                if(files[i]!="4Gplus.GeoJSON") {
                    try {
                        Connection connection = getConnection();
                        if (connection == null) {
                            return myresponse = new Responsecode("101", "Connection to database failed!", "http://confluence...", tables);
                        }

                        try {
                            Statement statement = connection.createStatement();

                            try {
                                String query = "UPDATE " + "tmp_" + files[i + 1] + " SET level='" + files[i].substring(5, files[i].indexOf("_")).toLowerCase() + "' where level IS NULL";

                                log.info(query);
								//executeUpdate return 1 if the query work properly otherwise it will return 0
                                int successOrFailure = statement.executeUpdate(query);
                              

                            } finally {
                                statement.close();
                            }
                        } finally {
                            connection.close();
                        }

                    } catch (Exception e) {
                        log.info("Exception:" + e);
                        err= e.getMessage();
                    }
                }


            } catch (Exception e) {
                err = e.getMessage();
                fileWithError=files[i];
                foundError = true;
                break;               
            }

        }


        if (foundError) {
            data = err.split("###");
            try {
                return myresponse = new Responsecode(data[0], "File:"+fileWithError+",Error:"+data[1], "http://confluence...", new String[]{fileWithError});
            } catch (ArrayIndexOutOfBoundsException ex) {
                return myresponse = new Responsecode(ex.toString(), err, "http://confluence...", new String[]{fileWithError});
            }
        }

		//truncate real tables
        String mess3 =  truncateTables(tables,false); 

        if(!mess3.equals("OK")) {
            return myresponse = new Responsecode("101", mess3, "http://confluence...", tables);
        }


        String mess1 = makeSwitch(tables);
        String mess2 = moveFiles(files2);

        if(!mess1.equals("OK")) {
            return myresponse = new Responsecode("99", mess1, "http://confluence...", tables);
        }

        if(!mess2.equals("OK")) {
            return myresponse = new Responsecode("98", mess1, "http://confluence...", files2);
        }

        String mess4 = moveFiles(sha1Files);
        if(!mess4.equals("OK")) {
            return myresponse = new Responsecode("97", mess4, "http://confluence...", sha1Files);
        }

        return myresponse = new Responsecode("0", "OK", "http://confluence...", new String[]{"Level1_2G.GeoJSON, Level2_2G.GeoJSON, Level3_2G.GeoJSON, Level1_3G.GeoJSON, Level2_3G.GeoJSON, Level3_3G.GeoJSON, Level1_4G.GeoJSON, Level2_4G.GeoJSON, Level3_4G.GeoJSON, 4Gplus.GeoJSON"});


    }


    @RequestMapping(value = "/api/v1/importWifi", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Responsecode importWifi(Object request) {
        boolean foundError = false;
        String[] data = null;
        String err=null;
        String fileWithError=null;
        String[] files= {"Wifi_Ind.GeoJSON",  "Wifi_Out.GeoJSON"};
        String[] tables = {"wifi"};
        String[] sha1Files = {wifiSHA1+".zip",wifiSHA1+".zip.sha1",wifiSHA1+"1.zip.sha1"};

        String mess0 =  checkIntegrity(wifiSHA1);

        if(!mess0.equals("OK")) {
            return myresponse = new Responsecode("100", mess0, "http://confluence...", files);
        }

        String mess00 =  unzipFiles(wifiSHA1);

        if(!mess00.equals("OK")) {
            return myresponse = new Responsecode("110", mess00, "http://confluence...", files);
        }

		//truncate temporary tables
        String mess000 =  truncateTables(tables,true); 

        if(!mess000.equals("OK")) {
            return myresponse = new Responsecode("1000", mess000, "http://confluence...", tables);
        }

        for(int i=0; i<files.length; i++){

            try {

                String[] params = {"-f", "PostgreSQL",
                        "PG:host=" + myHost + " port=" + myPort + " user='" + myUsername + "' password='" + myPassword + "' dbname='" + myDatabase + "'",
                        dirName+files[i],
                        "-nln", mySchema + "." +"tmp_wifi"
                        , "-append"                      
                };

                log.info(params.toString());

                ro.mapco.map.util.ogr2ogr.convert(params);


            } catch (Exception e) {
                err = e.getMessage();
                fileWithError=files[i];
                foundError = true;
                break;
            }

        }


        if (foundError) {
            data = err.split("###");
            try {
                return myresponse = new Responsecode(data[0], "File:"+fileWithError+",Error:"+data[1], "http://confluence...", new String[]{fileWithError});
            } catch (ArrayIndexOutOfBoundsException ex) {
                return myresponse = new Responsecode(ex.toString(), err, "http://confluence...", new String[]{fileWithError});
            }
        }

		//truncate real tables
        String mess3 =  truncateTables(tables,false); 

        if(!mess3.equals("OK")) {
            return myresponse = new Responsecode("101", mess3, "http://confluence...", tables);
        }

        String mess1 = makeSwitch(tables);
        String mess2 = moveFiles(files);

        if(!mess1.equals("OK")) {
            return myresponse = new Responsecode("99", mess1, "http://confluence...", tables);
        }

        if(!mess2.equals("OK")) {
            return myresponse = new Responsecode("98", mess1, "http://confluence...", files);
        }

        String mess4 = moveFiles(sha1Files);
        if(!mess4.equals("OK")) {
            return myresponse = new Responsecode("97", mess4, "http://confluence...", sha1Files);
        }

        return myresponse = new Responsecode("0", "OK", "http://confluence...", new String[]{"Wifi_Ind.GeoJSON, Wifi_Out.GeoJSON"});

    }


    @RequestMapping(value = "/api/v1/importIncidents", method = RequestMethod.GET, produces = "application/json"/*, headers = "Accept=*"*/)
    public @ResponseBody Responsecode importIncidents(Object request) {
        boolean foundError = false;
        String[] data = null;
        String err=null;
        String fileWithError=null;
        String[] files= {"IA_2.GeoJSON", "IA_3.GeoJSON", "IA_4.GeoJSON"};
        String[] tables = {"IA_2", "IA_3", "IA_4"};
        String[] sha1Files = {incidentsSHA1+".zip",incidentsSHA1+".zip.sha1",incidentsSHA1+"1.zip.sha1"};

        String mess0 =  checkIntegrity(incidentsSHA1);

        if(!mess0.equals("OK")) {
            return myresponse = new Responsecode("100", mess0, "http://confluence...", files);
        }

        String mess00 =  unzipFiles(incidentsSHA1);

        if(!mess00.equals("OK")) {
            return myresponse = new Responsecode("110", mess00, "http://confluence...", files);
        }
		
		//truncate temporary tables
        String mess000 =  truncateTables(tables,true); 

        if(!mess000.equals("OK")) {
            return myresponse = new Responsecode("1000", mess000, "http://confluence...", tables);
        }

        for(int i=0; i<files.length; i++){

            try {

                String[] params = {"-f", "PostgreSQL",
                        "PG:host=" + myHost + " port=" + myPort + " user='" + myUsername + "' password='" + myPassword + "' dbname='" + myDatabase + "'",
                        dirName+files[i],
                        "-nln", mySchema + "." +"tmp_"+files[i].substring(0,files[i].indexOf("."))
                        , "-append"
                };

                log.info(params.toString());

                ro.mapco.map.util.ogr2ogr.convert(params);

            } catch (Exception e) {
                err = e.getMessage();
                fileWithError=files[i];
                foundError = true;
                break;
            }

        }


        if (foundError) {
            data = err.split("###");
            try {
                return myresponse = new Responsecode(data[0], "File:"+fileWithError+",Error:"+data[1], "http://confluence...", new String[]{fileWithError});
            } catch (ArrayIndexOutOfBoundsException ex) {
                return myresponse = new Responsecode(ex.toString(), err, "http://confluence...", new String[]{fileWithError});
            }
        }

		//truncate real tables
        String mess3 =  truncateTables(tables,false); 

        if(!mess3.equals("OK")) {
            return myresponse = new Responsecode("101", mess3, "http://confluence...", tables);
        }

        String mess1 = makeSwitch(tables);
        String mess2 = moveFiles(files);

        if(!mess1.equals("OK")) {
            return myresponse = new Responsecode("99", mess1, "http://confluence...", tables);
        }

        if(!mess2.equals("OK")) {
            return myresponse = new Responsecode("98", mess1, "http://confluence...", files);
        }

        String mess4 = moveFiles(sha1Files);
        if(!mess4.equals("OK")) {
            return myresponse = new Responsecode("97", mess4, "http://confluence...", sha1Files);
        }

        return myresponse = new Responsecode("0", "OK", "http://confluence...", new String[]{"IA_2.GeoJSON, IA_3.GeoJSON, IA_4.GeoJSON"});

    }

    @RequestMapping(value = "/api/v1/importSitBuf", method = RequestMethod.GET, produces = "application/json"/*, headers = "Accept=*"*/)
    public @ResponseBody Responsecode importSitBuf(Object request) {
        String[] data = null;
        String err=null;

        String[] tables = {"Sit_Buf"};
        String[] files = {"Sit_Buf.geojson"};
        String[] sha1Files = {sitbufSHA1+".zip",sitbufSHA1+".zip.sha1",sitbufSHA1+"1.zip.sha1"};

        String mess0 =  checkIntegrity(sitbufSHA1);

        if(!mess0.equals("OK")) {
            return myresponse = new Responsecode("100", mess0, "http://confluence...", files);
        }

        String mess00 =  unzipFiles(sitbufSHA1);

        if(!mess00.equals("OK")) {
            return myresponse = new Responsecode("110", mess00, "http://confluence...", files);
        }

		//truncate temporary tables
        String mess000 =  truncateTables(tables,true);

        if(!mess000.equals("OK")) {
            return myresponse = new Responsecode("1000", mess000, "http://confluence...", tables);
        }

        try {

                String[] params = {"-f", "PostgreSQL",
                        "PG:host=" + myHost + " port=" + myPort + " user='" + myUsername + "' password='" + myPassword + "' dbname='" + myDatabase + "'",
                        dirName+"Sit_Buf.geojson",
                        "-nln", mySchema + "." +"tmp_"+"sit_buf"
                        , "-append"
                };

                log.info(params.toString());

                ro.mapco.map.util.ogr2ogr.convert(params);

            } catch (Exception e) {
                err = e.getMessage();
                data = err.split("###");
                try {
                    return myresponse = new Responsecode(data[0], "File:Sit_Buf.geojson,Error:"+data[1], "http://confluence...", new String[]{"Sit_Buf.geojson"});
                } catch (ArrayIndexOutOfBoundsException ex) {
                    return myresponse = new Responsecode(ex.toString(), err, "http://confluence...", new String[]{"Sit_Buf.geojson"});
                }


        }

		//truncate real tables
        String mess3 =  truncateTables(tables,false); 

        if(!mess3.equals("OK")) {
            return myresponse = new Responsecode("101", mess3, "http://confluence...", tables);
        }

        String mess1 = makeSwitch(tables);
        String mess2 = moveFiles(files);

        if(!mess1.equals("OK")) {
            return myresponse = new Responsecode("99", mess1, "http://confluence...", tables);
        }

        if(!mess2.equals("OK")) {
            return myresponse = new Responsecode("98", mess1, "http://confluence...", files);
        }

        String mess4 = moveFiles(sha1Files);
        if(!mess4.equals("OK")) {
            return myresponse = new Responsecode("97", mess4, "http://confluence...", sha1Files);
        }

        return myresponse = new Responsecode("0", "OK", "http://confluence...", new String[]{"Sit_Buf.GeoJSON"});

    }



    private String  checkIntegrity(String SHA1file){

        String err="OK";
        String SHA1sent=null;
        String sha1Code = null;

        BufferedReader br = null;

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader(dirName+SHA1file+".zip.sha1"));

            if ((sCurrentLine = br.readLine()) != null) {
                SHA1sent = sCurrentLine;
            }

        } catch (IOException e) {
            err= e.getMessage();
            return err;
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                err= ex.getMessage();
                return err;
            }
        }

        log.info("SHA1sent="+SHA1sent.toLowerCase());

        String srcFolder = dirName+SHA1file;
        File zipFile = new File(srcFolder+".zip");

        try {
            sha1Code = calcSHA1(zipFile);
            log.info("zipFile= "+ zipFile);
            log.info("sha1Code="+ sha1Code.toLowerCase());
            String filename= srcFolder+"1"+".zip.sha1";
			//the true will append the new data
            FileWriter fw = new FileWriter(filename,true); 
            fw.write(sha1Code+"\n");
            fw.close();
        } catch (IOException e) {
            err= e.getMessage();
            log.info("err= "+ err);
            return err;
        } catch (NoSuchAlgorithmException e) {
            err= e.getMessage();
            log.info("err= "+ err);
            return err;
        }

        if(!sha1Code.toLowerCase().equals(SHA1sent.toLowerCase())){
            err ="SHA1 code sent does not match the one generated;";
            return err;
        }

        return err;

    }

    private String unzipFiles(String zipFile){

        String err="OK";
        String OUTPUT_FOLDER = dirName;
        byte[] buffer = new byte[1024];

        try{
            //get the zip file content
            ZipInputStream zis = new ZipInputStream(new FileInputStream(OUTPUT_FOLDER+ zipFile +".zip"));
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while(ze!=null){

                String fileName = ze.getName();
                File newFile = new File(OUTPUT_FOLDER  + fileName);

                log.info("file unzip : "+ newFile.getAbsoluteFile());

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

            log.info("unzip Done");

        }catch(IOException ex){
            return ex.getMessage();
        }
        return err;

}

    private static String calcSHA1(File file) throws FileNotFoundException,
            IOException, NoSuchAlgorithmException {

        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");

        InputStream input = new FileInputStream(file);
        try {

            byte[] buffer = new byte[8192];
            int len = input.read(buffer);

            while (len != -1) {
                sha1.update(buffer, 0, len);
                len = input.read(buffer);
            }

            return new HexBinaryAdapter().marshal(sha1.digest());
        } finally {
            try {
                if (input != null)input.close();
            } catch (IOException ex) {
                return ex.getMessage();
            }
        }
    }


    private String makeSwitch(String[] tables){

        String err="OK";

        for(String table:tables){
            try
            {
                Connection connection = getConnection();
                if (connection == null){
                    return "Connection to database failed!";
                }

                try {
                    Statement statement  = connection.createStatement();
                    try {
                        String query = "INSERT INTO "+table+" SELECT * FROM tmp_"+table+";";
                        log.info(query);
                        int successOrFailure = statement.executeUpdate(query);
                        //executeUpdate return 1 if the query work properly otherwise it will return 0
                        if(successOrFailure ==0){
                            log.info("Table tmp_"+table+ " could not be switched");
                            err= "Table tmp_"+table+ " could not be switched: Query did not work properly";
                            return err;
                        }
                    } finally {
                        statement.close();
                    }
                } finally {
                    connection.close();
                }

            }catch(Exception e){
                log.info("Exception:"+e);
                err= "Table tmp_"+table+ " could not be switched: "+ e.getMessage();
                return err;
            }
        }

        return err;
    }

    private String  moveFiles(String[] files){

        String err="OK";
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date date = new Date();
        log.info(dateFormat.format(date));

        for(String file:files) {

            try {

                File afile = new File(dirName+file);
                String newFile = dirName+"processed\\" + dateFormat.format(date) + "_" +file;
                log.info(newFile);
                if (afile.renameTo(new File(newFile))) {
                    log.info("File" +file+ " is moved successful!");

                } else {
                    err = "File " +file+ " is failed to move!";
                    return err;
                }

            } catch (Exception e) {
                err= e.getMessage();
                return err;
            }
        }
        return err;

    }

    private String truncateTables(String[] tables, boolean temporary){
        String err="OK";

        for(String table:tables){
            String theTable = null;
            if (temporary){
                theTable = "tmp_"+table;
            }else{
                theTable = table;
            }
            try
            {

                Connection connection = getConnection();
                if (connection == null){
                    return "Connection to database failed!";
                }
                try {
                    Statement statement  = connection.createStatement();
                    try {
						//executeUpdate return 1 if the query work properly otherwise it will return 0
                        int successOrFailure = statement.executeUpdate("TRUNCATE "+theTable);
                       
                    } finally {
                        statement.close();
                    }
                } finally {
                    connection.close();
                }


            }catch(Exception e){
                log.info("Exception:"+e);
                err= "Table "+theTable+ " could not be truncated: "+ e.getMessage();
                return err;
            }
        }

        return err;
    }

    private Connection getConnection(){
        try{
            Class.forName(driverClassName);
            ResultSet acrs;
            String op = "jdbc:postgresql://"+myHost+":"+myPort+"/"+myDatabase;
            Connection cnn = DriverManager.getConnection(op,myUsername, myPassword);
            return cnn;
        }catch(Exception e){
            log.info("Exception:"+e);
            return null;
        }
    }

}
