package com.example.minismarthome.Model;

public class DimmerRequest {
        requestParams dimmer;

        public DimmerRequest(int desiredBrightness) {
            this.dimmer = new requestParams(desiredBrightness);
        }

        class requestParams{
            int loadType;
            int desiredBrightness;
            boolean overloaded;
            boolean overheated;

            public requestParams(int desiredBrightness) {
                this.loadType = 7;
                this.desiredBrightness = desiredBrightness;
                this.overloaded = false;
                this.overheated = false;
            }
        }

}
