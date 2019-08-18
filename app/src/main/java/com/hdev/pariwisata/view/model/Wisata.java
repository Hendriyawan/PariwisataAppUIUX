package com.hdev.pariwisata.view.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Wisata implements Parcelable {
    public static final Parcelable.Creator<Wisata> CREATOR = new Parcelable.Creator<Wisata>() {
        @Override
        public Wisata createFromParcel(Parcel source) {
            return new Wisata(source);
        }

        @Override
        public Wisata[] newArray(int size) {
            return new Wisata[size];
        }
    };
    private String nama;
    private String foto;
    private String deskripsi;

    public Wisata() {
    }

    protected Wisata(Parcel in) {
        this.nama = in.readString();
        this.foto = in.readString();
        this.deskripsi = in.readString();
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nama);
        dest.writeString(this.foto);
        dest.writeString(this.deskripsi);
    }
}
