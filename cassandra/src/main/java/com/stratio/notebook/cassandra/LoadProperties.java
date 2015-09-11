package com.stratio.notebook.cassandra;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created by afidalgo on 11/09/15.
 */
public class LoadProperties {


    public static void load() {
        try {



            Properties prop = new Properties();
            prop.load(new FileInputStream("./resources/application.properties"));
        }catch(Exception e){
              e.printStackTrace();
        }


    }

}
