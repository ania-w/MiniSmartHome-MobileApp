/**
 * Based on
 *
 * @see <a href=https://www.uugear.com/portfolio/dht11-humidity-temperature-sensor-module>uugear.com</a>
 * <p>
 * Created using wiringPi
 * @see <a href=http://wiringpi.com>wiringPi</a>
 * <p>
 * Should be suitable for DHT22, DHT11
 */

package com.example.minismarthome.model;

public class AM2301 extends Device {

    private Integer pin;
    private Integer boardpin;

    public AM2301() {   }

    public void setPin(Integer pin) {this.pin = pin; }

    public Long getPin() {
        return Long.valueOf(pin);
    }

    public Integer getBoardpin() {
        return boardpin;
    }

    public void setBoardpin(Integer boardpin) {
        this.boardpin = boardpin;
    }

}