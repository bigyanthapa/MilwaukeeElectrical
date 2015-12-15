package bigyan.com.milwaukeeelectrical.model;

import java.io.Serializable;

/**
 * Created by bigyanthapa on 12/14/15.
 */
public class CityModel implements Serializable {

    private String cityName;
    private String cityStatus;
    private String cityHumidity;
    private String cityPressure;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityStatus() {
        return cityStatus;
    }

    public void setCityStatus(String cityStatus) {
        this.cityStatus = cityStatus;
    }

    public String getCityHumidity() {
        return cityHumidity;
    }

    public void setCityHumidity(String cityHumidity) {
        this.cityHumidity = cityHumidity;
    }

    public String getCityPressure() {
        return cityPressure;
    }

    public void setCityPressure(String cityPressure) {
        this.cityPressure = cityPressure;
    }
}
