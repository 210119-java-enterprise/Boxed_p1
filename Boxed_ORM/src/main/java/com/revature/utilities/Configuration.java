package com.revature.utilities;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author Wezley Singleton
 */
public class Configuration {
    //Attributes -------------------------------------------------
    private Properties props = new Properties();
    private String dbUrl, dbUsername, dbPassword, dbSchema;
    private List<Class> preloadedEntities;
    private List<Metamodel<Class<?>>> metamodelList;

    public Configuration(String configLocation) {
        try{
            props.load(new FileReader(configLocation));
        }catch(IOException e){
            e.printStackTrace();
        }
        dbUrl = props.getProperty("url");
        dbUsername = props.getProperty("admin-usr");
        dbPassword = props.getProperty("admin-pw");
        dbSchema = props.getProperty("current-schema");

        metamodelList = new LinkedList<>();
        //retrieve preloaded Entities
        String CSV = props.getProperty("entity-location");
        String[] annotatedEntityLocations= CSV.split(",");
        for (String s : annotatedEntityLocations){
            try {
                metamodelList.add(Metamodel.of((Class) Class.forName(s)));
            }catch (ClassNotFoundException e){
                System.out.println("Entity Class listed in Configuration File not Found");
            }
        }


    }

    //Getters and Setters -------------------------------------------
    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getDbSchema() {
        return dbSchema;
    }

    //Other --------------------------------------------------------
    public Configuration addAnnotatedClass(Class annotatedClass) {
        if (metamodelList == null) {
            metamodelList = new LinkedList<>();
        }

        metamodelList.add(Metamodel.of(annotatedClass));

        return this;
    }

    public List<Metamodel<Class<?>>> getMetamodels() {
        return (metamodelList == null) ? Collections.emptyList() : metamodelList;
    }
}
