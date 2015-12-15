package bigyan.com.milwaukeeelectrical.model;

import com.google.gson.annotations.Expose;

/**
 * Created by bigyanthapa on 12/14/15.
 */
public class Clouds {

    @Expose
    private Integer all;

    /**
     *
     * @return
     * The all
     */
    public Integer getAll() {
        return all;
    }

    /**
     *
     * @param all
     * The all
     */
    public void setAll(Integer all) {
        this.all = all;
    }
}
