package com.software.march.musicplayer.bean;

/**
 * @author Doc.March
 * @version V 1.0
 * @Description MoreFragment item 实体
 * @date 2017/4/23
 */
public class MoreItemBean {

    // 信息标题
    private String title;

    // 图片ID
    private int imageId;

    public MoreItemBean() {

    }

    public MoreItemBean(String title, int imageId) {
        this.title = title;
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "MoreItemBean{" +
                "imageId=" + imageId +
                ", title='" + title + '\'' +
                '}';
    }
}