package ro.mapco.map.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by joliedd on 5/20/2016.
 */
public class LevelList implements Serializable {

   private static final long serialVersionUID = 1L;

   List<Level> list;

    public LevelList() {

    }

    public LevelList(List<Level> list) {
        this.list = list;
    }

    public Level getLevel(int index) {
        return list.get(index);
    }

    public void setList(List<Level> list) {
        this.list = list;
    }

    public List<Level> getPhoneList(){ return this.list;};
    public int getPhoneCount(){ return this.list.size(); };

    @Override
    public String toString() {
        return "LevelList{" +
                "list=" + list +
                '}';
    }


}
