package com.example.netflix.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class DetailVideo implements Serializable {

    @SerializedName("page")
    @Expose
    private Integer page;

    @Expose
    private List<DetailVideo.Result> results = null;

    public List<DetailVideo.Result> getResults() {
        return results;
    }
    public void setResults(List<DetailVideo.Result> results) {
        this.results = results;
    }


    public class Result implements Serializable
    {
        @SerializedName("iso_6391")
        @Expose
        private String iso6391;

        @SerializedName("iso_3166_1")
        @Expose
        private  String iso31661;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("key")
        @Expose
        private String key;

        @SerializedName("site")
        @Expose
        private String site;

        @SerializedName("type")
        @Expose
        private String type;

        @SerializedName("official")
        @Expose
        private String official;

        @SerializedName("date")
        @Expose
        private String date;

        @SerializedName("id")
        @Expose
        private String id;

        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }
        public void setNmae(String name) {
            this.name = name;
        }

        public String getDate() { return date; }
        public void setDate(String date) {
            this.date = date;
        }

        public String getType() {
            return type;
        }
        public void setType(String official) {
            this.type = type;
        }

        public String getOfficial() {
            return official;
        }
        public void setOfficial(String official) {
            this.official = official;
        }

        public String getSite() {
            return site;
        }
        public void setSite(String site) {
            this.site = site;
        }

        public String getKey() {
            return key;
        }
        public void setKey(String key) {
            this.key = key;
        }

        public String getIso6391() {
            return iso6391;
        }
        public void setIso6391(String iso6391) { this.iso6391 = iso6391; }

        public String getIso31661() {
            return iso31661;
        }
        public void setIso31661(String iso31661) { this.iso31661 = iso31661; }



    }
}
