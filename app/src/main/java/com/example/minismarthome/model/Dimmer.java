/**
 * Blebox API
 *
 * @see <a href=https://technical.blebox.eu/archives/dimmerBoxAPI/>dimmerBox api</a>
 */

package com.example.minismarthome.model;


import java.util.HashMap;
import java.util.Map;

public class Dimmer extends Device {

    private String ip;
    private String description;

    public Dimmer() {    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
