package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {
    public String name;
    public double lat;
    public double lon;
    public String country;
    public String state;

    public String zip;
    public Map<String, String> local_names; // ← Map trực tiếp!
}
