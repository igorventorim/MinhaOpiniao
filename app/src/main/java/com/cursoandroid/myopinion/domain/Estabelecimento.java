package com.cursoandroid.myopinion.domain;

import android.graphics.Bitmap;
import android.os.Parcelable;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by igor on 16/10/16.
 */

public class Estabelecimento implements Serializable{

    private Bitmap img;
    private String name;
    private String rating;
    private String tipoEstabelecimento;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }


    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }


}
