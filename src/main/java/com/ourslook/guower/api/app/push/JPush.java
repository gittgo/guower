package com.ourslook.guower.api.app.push;

/**
 * @author wang cheng wei
 * @date 2019-8-31 10:38
 */
public class JPush {
        private String appKey;

        private String masterSecret;

        public String getAppKey() {
            return appKey;
        }

        public void setAppKey(String appKey) {
            this.appKey = appKey;
        }

        public String getMasterSecret() {
            return masterSecret;
        }

        public void setMasterSecret(String masterSecret) {
            this.masterSecret = masterSecret;
        }
}

