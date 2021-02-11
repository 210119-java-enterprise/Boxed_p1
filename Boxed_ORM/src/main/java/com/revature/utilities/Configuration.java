package com.revature.utilities;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Wezley Singleton
 */
public class Configuration {
    //Attributes -------------------------------------------------
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;
    private List<Metamodel<Class<?>>> metamodelList;

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
