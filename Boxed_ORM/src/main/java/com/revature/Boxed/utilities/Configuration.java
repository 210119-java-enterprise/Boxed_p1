package com.revature.Boxed.utilities;

import com.revature.Boxed.model.model.Metamodel;

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
    private String loginCredentialFields;

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
                Metamodel meta = Metamodel.of((Class) Class.forName(s));
                metamodelList.add(meta);
                if (meta.isLogOnCredentials()){
                    System.out.println("Log on credentials class: " + meta.getClassName());
                    loginCredentialFields = meta.getEntityName() + meta.getCredentialFields();
                }

            }catch (ClassNotFoundException e){
                System.out.println("Entity Class listed in Configuration File not Found");
            }
        }

        System.out.println("Login credentials class name: " + loginCredentialFields);
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

    public String getLoginCredEntityName() {
        return loginCredentialFields;
    }


    //Other --------------------------------------------------------
    public Metamodel<Class<?>> getMatchingMetamodel(Class<?> model){
        for (Metamodel<Class<?>> mm:metamodelList) {
            if(mm.getClassName().equals(model.getSimpleName())){
                return mm;
            }
        }
        return null;
    }

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
    public Class<?> getMatchingClass(String entityName){
        for (Metamodel<Class<?>> metamodel: metamodelList) {
            //System.out.println("metamodel Names: |" + metamodel.getEntityName() + "| - |" + entityName + "|");
            if (metamodel.getEntityName().equals(entityName)){
                //System.out.println("found");
                return metamodel.getClazz();
            }
        }
        return null;
    }
}
