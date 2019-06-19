package com.onezero.launcher.launcher.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PreAppInfo {

    /**
     * status : true
     * message : [{"id":"1398","name":"点击精灵","package":"com.chuangdian.ShouDianKe","path":"1398/com.chuangdian.ShouDianKe_1.0.1.apk","size":"2867534"}]
     */

    private boolean status;
    private List<MessageBean> message;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<MessageBean> getMessage() {
        return message;
    }

    public void setMessage(List<MessageBean> message) {
        this.message = message;
    }

    public static class MessageBean {
        /**
         * id : 1398
         * name : 点击精灵
         * package : com.chuangdian.ShouDianKe
         * path : 1398/com.chuangdian.ShouDianKe_1.0.1.apk
         * size : 2867534
         */

        private String id;
        private String name;
        @SerializedName("package")
        private String packageX;
        private String path;
        private String size;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }
    }
}
