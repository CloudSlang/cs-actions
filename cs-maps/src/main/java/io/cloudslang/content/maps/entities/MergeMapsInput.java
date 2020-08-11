package io.cloudslang.content.maps.entities;

import com.sun.scenario.effect.Merge;
import org.apache.commons.lang3.StringUtils;

public class MergeMapsInput {

    private final String map1;
    private final String map2;
    private final String pairDelimiter;
    private final String entryDelimiter;
    private final String mapStart;
    private final String mapEnd;

    private MergeMapsInput(Builder builder){
        this.map1 = builder.map1;
        this.map2 = builder.map2;
        this.pairDelimiter = builder.pairDelimiter;
        this.entryDelimiter = builder.entryDelimiter;
        this.mapStart = builder.mapStart;
        this.mapEnd = builder.mapEnd;

    }

    public String getMap1() { return map1;}

    public String getMap2(){ return map2;}

    public String getPairDelimiter() {
        return pairDelimiter;
    }

    public String getEntryDelimiter() {
        return entryDelimiter;
    }

    public String getMapStart() {
        return mapStart;
    }

    public String getMapEnd() {
        return mapEnd;
    }

    public static class Builder{
        private String map1;
        private String map2;
        private String pairDelimiter;
        private String entryDelimiter;
        private String mapStart;
        private String mapEnd;

        public Builder map1(String map1){
            this.map1 = map1;
            return this;
        }

        public Builder map2(String map2){
            this.map2 = map2;
            return this;
        }

        public Builder pairDelimiter(String pairDelimiter) {
            this.pairDelimiter = pairDelimiter;
            return this;
        }


        public Builder entryDelimiter(String entryDelimiter) {
            this.entryDelimiter = entryDelimiter;
            return this;
        }


        public Builder mapStart(String mapStart) {
            this.mapStart = StringUtils.defaultString(mapStart);
            return this;
        }


        public Builder mapEnd(String mapEnd) {
            this.mapEnd = StringUtils.defaultString(mapEnd);
            return this;
        }

        public MergeMapsInput build() {
            return new MergeMapsInput(this);
        }
    }
}
