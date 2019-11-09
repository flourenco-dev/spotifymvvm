package com.fabiolourenco.spotifymvvm.data.api;

public class ApiHelperFactory {

    private static ApiHelper apiHelper = null;

    public static ApiHelper getApiHelper() {
        if (apiHelper == null) {
            apiHelper = new AppApiHelper();
        }

        return apiHelper;
    }
}
