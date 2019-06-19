package com.onezero.launcher.launcher.api;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;
import com.onezero.launcher.launcher.BR;

import java.util.List;

public class AppInfoBean {

    /**
     * status : true
     * message : {"total":16,"page_size":10,"pages":2,"page_index":1,"data":[{"id":"1","name":"Hola桌面","package":"com.hola.launcher","version":"3.0.9","path":"1/com.hola.launcher_3.0.9.apk","size":"5359273","build":"30901","md5":"35f02749415cb27983f595eec4f274d2","type":"apk","category":"10","keyword":"","source_type":"1","source":"","download":"","download_total":"","install_ratio":"","score":"","order_value":"0","publish_at":"1541988143","create_at":"1541988143","modify_at":"0","comment":null,"status":"1"},{"id":"4","name":"锤子桌面","package":"com.smartisanos.home","version":"1.5.1","path":"4/com.smartisanos.home_1.5.1.apk","size":"38354968","build":"27","md5":"d5c2e48ee24f5b6773b2b51418e73bfa","type":"apk","category":"10","keyword":"","source_type":"1","source":"","download":"","download_total":"","install_ratio":"","score":"","order_value":"0","publish_at":"1541988145","create_at":"1541988145","modify_at":"0","comment":null,"status":"1"},{"id":"5","name":"Aviate","package":"com.tul.aviate","version":"3.2.12.8","path":"5/com.tul.aviate_3.2.12.8.apk","size":"19448190","build":"20633","md5":"d21c008010e5579f20d09989b816f754","type":"apk","category":"10","keyword":"","source_type":"1","source":"","download":"","download_total":"","install_ratio":"","score":"","order_value":"0","publish_at":"1541988146","create_at":"1541988146","modify_at":"0","comment":null,"status":"1"},{"id":"6","name":"Aris终端桌面","package":"shinado.indi.piping","version":"1.2.60","path":"6/shinado.indi.piping_1.2.60.apk","size":"4448119","build":"1260","md5":"167f8d33847c54117ff493fd04bafeef","type":"apk","category":"10","keyword":"","source_type":"1","source":"","download":"","download_total":"","install_ratio":"","score":"","order_value":"0","publish_at":"1541988146","create_at":"1541988146","modify_at":"0","comment":null,"status":"1"},{"id":"7","name":"SiteDer","package":"org.noear.siteder","version":"1.7.76","path":"7/org.noear.siteder_1.7.76.apk","size":"12104950","build":"76","md5":"5267b8f0c01d1541de05c013d057f697","type":"apk","category":"11","keyword":"","source_type":"1","source":"","download":"","download_total":"","install_ratio":"","score":"","order_value":"0","publish_at":"1541988147","create_at":"1541988147","modify_at":"0","comment":null,"status":"1"},{"id":"8","name":"SmileSoft-智能锁屏","package":"com.SmileIDE.SmartLock","version":"1.3.17","path":"8/com.SmileIDE.SmartLock_1.3.17.apk","size":"1322465","build":"317","md5":"a000bdd44a2b9be1480299a51127e1ad","type":"apk","category":"12","keyword":"","source_type":"1","source":"","download":"","download_total":"","install_ratio":"","score":"","order_value":"0","publish_at":"1541988147","create_at":"1541988147","modify_at":"0","comment":null,"status":"1"},{"id":"10","name":"CM Locker","package":"com.cmcm.locker","version":"4.7.1","path":"10/com.cmcm.locker_4.7.1.apk","size":"9600356","build":"47011623","md5":"5878aa27b3ed822ffde81f2d7f816c4b","type":"apk","category":"12","keyword":"","source_type":"1","source":"","download":"","download_total":"","install_ratio":"","score":"","order_value":"0","publish_at":"1541988148","create_at":"1541988148","modify_at":"0","comment":null,"status":"1"},{"id":"11","name":"氢壁纸视频桌面","package":"com.hydrogen.video.lwp","version":"1.7.6","path":"11/com.hydrogen.video.lwp_1.7.6.apk","size":"4537976","build":"176","md5":"1b2316b16e6591fe2d535904796d1795","type":"apk","category":"12","keyword":"","source_type":"1","source":"","download":"","download_total":"","install_ratio":"","score":"","order_value":"0","publish_at":"1541988148","create_at":"1541988148","modify_at":"0","comment":null,"status":"1"},{"id":"12","name":"GO锁屏","package":"com.jiubang.goscreenlock","version":"3.33","path":"12/com.jiubang.goscreenlock_3.33.apk","size":"5099640","build":"10033","md5":"874aed4c1e1d850b06a4753f7ec97322","type":"apk","category":"12","keyword":"","source_type":"1","source":"","download":"","download_total":"","install_ratio":"","score":"","order_value":"0","publish_at":"1541988148","create_at":"1541988148","modify_at":"0","comment":null,"status":"1"},{"id":"13","name":"MyerSplash","package":"com.juniperphoton.myersplash","version":"3.0.3","path":"13/com.juniperphoton.myersplash_3.0.3.apk","size":"10767450","build":"305","md5":"3016bc357e76be0e7757b4d8291d3f03","type":"apk","category":"12","keyword":"","source_type":"1","source":"","download":"","download_total":"","install_ratio":"","score":"","order_value":"0","publish_at":"1541988149","create_at":"1541988149","modify_at":"0","comment":null,"status":"1"}]}
     */

    private boolean status;
    private MessageBean message;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public MessageBean getMessage() {
        return message;
    }

    public void setMessage(MessageBean message) {
        this.message = message;
    }

    public static class MessageBean {
        /**
         * total : 16
         * page_size : 10
         * pages : 2
         * page_index : 1
         * data : [{"id":"1","name":"Hola桌面","package":"com.hola.launcher","version":"3.0.9","path":"1/com.hola.launcher_3.0.9.apk","size":"5359273","build":"30901","md5":"35f02749415cb27983f595eec4f274d2","type":"apk","category":"10","keyword":"","source_type":"1","source":"","download":"","download_total":"","install_ratio":"","score":"","order_value":"0","publish_at":"1541988143","create_at":"1541988143","modify_at":"0","comment":null,"status":"1"},{"id":"4","name":"锤子桌面","package":"com.smartisanos.home","version":"1.5.1","path":"4/com.smartisanos.home_1.5.1.apk","size":"38354968","build":"27","md5":"d5c2e48ee24f5b6773b2b51418e73bfa","type":"apk","category":"10","keyword":"","source_type":"1","source":"","download":"","download_total":"","install_ratio":"","score":"","order_value":"0","publish_at":"1541988145","create_at":"1541988145","modify_at":"0","comment":null,"status":"1"},{"id":"5","name":"Aviate","package":"com.tul.aviate","version":"3.2.12.8","path":"5/com.tul.aviate_3.2.12.8.apk","size":"19448190","build":"20633","md5":"d21c008010e5579f20d09989b816f754","type":"apk","category":"10","keyword":"","source_type":"1","source":"","download":"","download_total":"","install_ratio":"","score":"","order_value":"0","publish_at":"1541988146","create_at":"1541988146","modify_at":"0","comment":null,"status":"1"},{"id":"6","name":"Aris终端桌面","package":"shinado.indi.piping","version":"1.2.60","path":"6/shinado.indi.piping_1.2.60.apk","size":"4448119","build":"1260","md5":"167f8d33847c54117ff493fd04bafeef","type":"apk","category":"10","keyword":"","source_type":"1","source":"","download":"","download_total":"","install_ratio":"","score":"","order_value":"0","publish_at":"1541988146","create_at":"1541988146","modify_at":"0","comment":null,"status":"1"},{"id":"7","name":"SiteDer","package":"org.noear.siteder","version":"1.7.76","path":"7/org.noear.siteder_1.7.76.apk","size":"12104950","build":"76","md5":"5267b8f0c01d1541de05c013d057f697","type":"apk","category":"11","keyword":"","source_type":"1","source":"","download":"","download_total":"","install_ratio":"","score":"","order_value":"0","publish_at":"1541988147","create_at":"1541988147","modify_at":"0","comment":null,"status":"1"},{"id":"8","name":"SmileSoft-智能锁屏","package":"com.SmileIDE.SmartLock","version":"1.3.17","path":"8/com.SmileIDE.SmartLock_1.3.17.apk","size":"1322465","build":"317","md5":"a000bdd44a2b9be1480299a51127e1ad","type":"apk","category":"12","keyword":"","source_type":"1","source":"","download":"","download_total":"","install_ratio":"","score":"","order_value":"0","publish_at":"1541988147","create_at":"1541988147","modify_at":"0","comment":null,"status":"1"},{"id":"10","name":"CM Locker","package":"com.cmcm.locker","version":"4.7.1","path":"10/com.cmcm.locker_4.7.1.apk","size":"9600356","build":"47011623","md5":"5878aa27b3ed822ffde81f2d7f816c4b","type":"apk","category":"12","keyword":"","source_type":"1","source":"","download":"","download_total":"","install_ratio":"","score":"","order_value":"0","publish_at":"1541988148","create_at":"1541988148","modify_at":"0","comment":null,"status":"1"},{"id":"11","name":"氢壁纸视频桌面","package":"com.hydrogen.video.lwp","version":"1.7.6","path":"11/com.hydrogen.video.lwp_1.7.6.apk","size":"4537976","build":"176","md5":"1b2316b16e6591fe2d535904796d1795","type":"apk","category":"12","keyword":"","source_type":"1","source":"","download":"","download_total":"","install_ratio":"","score":"","order_value":"0","publish_at":"1541988148","create_at":"1541988148","modify_at":"0","comment":null,"status":"1"},{"id":"12","name":"GO锁屏","package":"com.jiubang.goscreenlock","version":"3.33","path":"12/com.jiubang.goscreenlock_3.33.apk","size":"5099640","build":"10033","md5":"874aed4c1e1d850b06a4753f7ec97322","type":"apk","category":"12","keyword":"","source_type":"1","source":"","download":"","download_total":"","install_ratio":"","score":"","order_value":"0","publish_at":"1541988148","create_at":"1541988148","modify_at":"0","comment":null,"status":"1"},{"id":"13","name":"MyerSplash","package":"com.juniperphoton.myersplash","version":"3.0.3","path":"13/com.juniperphoton.myersplash_3.0.3.apk","size":"10767450","build":"305","md5":"3016bc357e76be0e7757b4d8291d3f03","type":"apk","category":"12","keyword":"","source_type":"1","source":"","download":"","download_total":"","install_ratio":"","score":"","order_value":"0","publish_at":"1541988149","create_at":"1541988149","modify_at":"0","comment":null,"status":"1"}]
         */

        private int total;
        private int page_size;
        private int pages;
        private int page_index;
        private List<DataBean> data;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPage_size() {
            return page_size;
        }

        public void setPage_size(int page_size) {
            this.page_size = page_size;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public int getPage_index() {
            return page_index;
        }

        public void setPage_index(int page_index) {
            this.page_index = page_index;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean extends BaseObservable {
            /**
             * id : 1
             * name : Hola桌面
             * package : com.hola.launcher
             * version : 3.0.9
             * path : 1/com.hola.launcher_3.0.9.apk
             * size : 5359273
             * build : 30901
             * md5 : 35f02749415cb27983f595eec4f274d2
             * type : apk
             * category : 10
             * keyword :
             * source_type : 1
             * source :
             * download :
             * download_total :
             * install_ratio :
             * score :
             * order_value : 0
             * publish_at : 1541988143
             * create_at : 1541988143
             * modify_at : 0
             * comment : null
             * status : 1
             */

            private String id;
            private String name;
            @SerializedName("package")
            private String packageX;
            private String version;
            private String path;
            private String size;
            private String build;
            private String md5;
            private String type;
            private String category;
            private String keyword;
            private String source_type;
            private String source;
            private String download;
            private String download_total;
            private String install_ratio;
            private String score;
            private String order_value;
            private String publish_at;
            private String create_at;
            private String modify_at;
            private Object comment;
            private String status;
            private int downloadProgress;

            @Bindable
            public int getDownloadProgress() {
                return downloadProgress;
            }

            public void setDownloadProgress(int downloadProgress) {
                this.downloadProgress = downloadProgress;
//                notifyPropertyChanged(BR.downloadProgress);
            }

            @Bindable
            public String getInstallState() {
                return installState;
            }


            public void setInstallState(String installState) {
                this.installState = installState;
//                notifyPropertyChanged(BR.installState);
            }

            private String installState;

            @Bindable
            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            @Bindable
            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
//                notifyPropertyChanged(BR.name);
            }

            public String getPackageX() {
                return packageX;
            }

            public void setPackageX(String packageX) {
                this.packageX = packageX;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
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

            public String getBuild() {
                return build;
            }

            public void setBuild(String build) {
                this.build = build;
            }

            public String getMd5() {
                return md5;
            }

            public void setMd5(String md5) {
                this.md5 = md5;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getKeyword() {
                return keyword;
            }

            public void setKeyword(String keyword) {
                this.keyword = keyword;
            }

            public String getSource_type() {
                return source_type;
            }

            public void setSource_type(String source_type) {
                this.source_type = source_type;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public String getDownload() {
                return download;
            }

            public void setDownload(String download) {
                this.download = download;
            }

            public String getDownload_total() {
                return download_total;
            }

            public void setDownload_total(String download_total) {
                this.download_total = download_total;
            }

            public String getInstall_ratio() {
                return install_ratio;
            }

            public void setInstall_ratio(String install_ratio) {
                this.install_ratio = install_ratio;
            }

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
            }

            public String getOrder_value() {
                return order_value;
            }

            public void setOrder_value(String order_value) {
                this.order_value = order_value;
            }

            public String getPublish_at() {
                return publish_at;
            }

            public void setPublish_at(String publish_at) {
                this.publish_at = publish_at;
            }

            public String getCreate_at() {
                return create_at;
            }

            public void setCreate_at(String create_at) {
                this.create_at = create_at;
            }

            public String getModify_at() {
                return modify_at;
            }

            public void setModify_at(String modify_at) {
                this.modify_at = modify_at;
            }

            public Object getComment() {
                return comment;
            }

            public void setComment(Object comment) {
                this.comment = comment;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }
}
