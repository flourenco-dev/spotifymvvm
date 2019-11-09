package com.fabiolourenco.spotifymvvm.data;

public class RepositoryFactory {

    private static DataRepository dataRepository = null;

    public static DataRepository getDataRepository() {
        if (dataRepository == null) {
            dataRepository = new AppDataRepository();
        }

        return dataRepository;
    }
}
