package com.coolwallpaper.utils;

import com.coolwallpaper.bean.PictureResult;
import com.coolwallpaper.model.Picture;

/**
 * 转换工具
 * Created by fuchao on 2015/12/17.
 */
public class ConvertUtil {

    /**
     * PictureResult转换为数据库实体Picture
     *
     * @param result
     * @return
     */
    public static Picture toPicture(PictureResult result) {
        if (result == null) {
            return null;
        }
        Picture picture = new Picture();
        picture.setThumbUrl(result.getThumbUrl());
        picture.setDownloadUrl(result.getDownloadUrl());
        picture.setFromUrl(result.getFromUrl());
        picture.setWidth(result.getWidth());
        picture.setHeight(result.getHeight());
        picture.setDesc(result.getDesc());
        return picture;
    }
}