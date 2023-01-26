package com.example.lmantamqrcode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Post {

    private final String qrCode;
    private final double latitude;
    private final double longitude;

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private final List<Data> data = null;

    public Post(String qrCode, double latitude, double longitude) {
        this.qrCode = qrCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<Data> getData() {
        return data;
    }



    public class Data{

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("nama")
        @Expose
        private String nama;
        @SerializedName("serial")
        @Expose
        private String serial;
        @SerializedName("berat")
        @Expose
        private String berat;
        @SerializedName("tahun")
        @Expose
        private String tahun;
        @SerializedName("kd_unik")
        @Expose
        private String kdUnik;
        @SerializedName("kd_skip")
        @Expose
        private Integer kdSkip;
        @SerializedName("no_qrcode")
        @Expose
        private String noQrcode;
        @SerializedName("path")
        @Expose
        private String path;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getSerial() {
            return serial;
        }

        public void setSerial(String serial) {
            this.serial = serial;
        }

        public String getBerat() {
            return berat;
        }

        public void setBerat(String berat) {
            this.berat = berat;
        }

        public String getTahun() {
            return tahun;
        }

        public void setTahun(String tahun) {
            this.tahun = tahun;
        }

        public String getKdUnik() {
            return kdUnik;
        }

        public void setKdUnik(String kdUnik) {
            this.kdUnik = kdUnik;
        }

        public Integer getKdSkip() {
            return kdSkip;
        }

        public void setKdSkip(Integer kdSkip) {
            this.kdSkip = kdSkip;
        }

        public String getNoQrcode() {
            return noQrcode;
        }

        public void setNoQrcode(String noQrcode) {
            this.noQrcode = noQrcode;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
