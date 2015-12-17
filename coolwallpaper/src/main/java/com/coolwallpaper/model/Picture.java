package com.coolwallpaper.model;

import com.coolwallpaper.model.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "tb_picture".
 */
public class Picture implements java.io.Serializable {

    private Long id;
    private String thumbUrl;
    private String downloadUrl;
    private String fromUrl;
    private Integer width;
    private Integer height;
    private String desc;
    private long paramId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient PictureDao myDao;

    private Param param;
    private Long param__resolvedKey;


    public Picture() {
    }

    public Picture(Long id) {
        this.id = id;
    }

    public Picture(Long id, String thumbUrl, String downloadUrl, String fromUrl, Integer width, Integer height, String desc, long paramId) {
        this.id = id;
        this.thumbUrl = thumbUrl;
        this.downloadUrl = downloadUrl;
        this.fromUrl = fromUrl;
        this.width = width;
        this.height = height;
        this.desc = desc;
        this.paramId = paramId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPictureDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getFromUrl() {
        return fromUrl;
    }

    public void setFromUrl(String fromUrl) {
        this.fromUrl = fromUrl;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getParamId() {
        return paramId;
    }

    public void setParamId(long paramId) {
        this.paramId = paramId;
    }

    /** To-one relationship, resolved on first access. */
    public Param getParam() {
        long __key = this.paramId;
        if (param__resolvedKey == null || !param__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ParamDao targetDao = daoSession.getParamDao();
            Param paramNew = targetDao.load(__key);
            synchronized (this) {
                param = paramNew;
            	param__resolvedKey = __key;
            }
        }
        return param;
    }

    public void setParam(Param param) {
        if (param == null) {
            throw new DaoException("To-one property 'paramId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.param = param;
            paramId = param.getId();
            param__resolvedKey = paramId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}
