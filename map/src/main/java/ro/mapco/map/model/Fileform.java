package ro.mapco.map.model;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by joliedd on 7/14/2016.
 */
public class Fileform {

    MultipartFile file;

    public Fileform() {

    }

    public Fileform(MultipartFile file) {
        this.file = file;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
