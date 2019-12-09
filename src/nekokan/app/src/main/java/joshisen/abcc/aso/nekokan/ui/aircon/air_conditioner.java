package joshisen.abcc.aso.nekokan.ui.aircon;

public class air_conditioner {
    public Integer airconSwitch;
    public Integer temperature;

    public air_conditioner(){
    }

    public air_conditioner(Integer _airconSwitch, Integer _temperature) {
        airconSwitch = _airconSwitch;
        temperature = _temperature;
    }

    public Integer getAirconSwitch() {
        return airconSwitch;
    }

    public void setAirconSwitch(Integer airconSwitch) {
        this.airconSwitch = airconSwitch;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }
}
