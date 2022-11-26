/**
 * Based on the package io.helins.linux.i2c created by helins.linux.i2c
 *
 * @see <a href=https://github.com/helins/linux-i2c.java>helins/linux-i2c</a>
 */

package com.example.minismarthome.model;


import java.util.HashMap;
import java.util.Map;

public class SGP30 extends Device {

    private Integer busNumber;

    public SGP30() {    }

    public Integer getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(Integer busNumber) {
        this.busNumber = busNumber;
    }

}
