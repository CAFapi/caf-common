package com.hpe.caf.utilliquibaseinstaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

/**
 * Created by gibsodom on 08/12/2015.
 */
@Configuration
@PropertySources({
        @PropertySource(value = "classpath:database.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "file:${DATABASE_CONFIG}/database.properties", ignoreResourceNotFound = true)
})
public class DatabaseProperties {

    private final String dbNamePlaceholder = "<dbname>";

    @Autowired
    protected Environment environment;

    public String getConnectionString(){
        String connectionString = this.environment.getProperty("db.connection");
        if(connectionString == null || connectionString.isEmpty()){
            return connectionString;
        }
//        else if (!connectionString.contains(dbNamePlaceholder)) {
//            return connectionString;
//        }
//        return connectionString.replace(dbNamePlaceholder,getDBName());
        return connectionString;
    }

    public String getUser(){
        return this.environment.getProperty("db.user");
    }

    public String getPass(){
        return this.environment.getProperty("db.pass");
    }

    public String getDBName(){
        return this.environment.getProperty("db.name");
    }

    public String getBaseConnectionString(){
        // this can be used to get the connection string, without the DB name present, in order to create
        // the DB.
        String baseConnectionString = this.environment.getProperty("db.connection");
        if (baseConnectionString == null || baseConnectionString.isEmpty()){
            return baseConnectionString;
        }
        // otherwise, read up to this point, and chop off the rest.
        return parseConnection(baseConnectionString);
    }

    public String parseConnection(String originalString){
//        check for presence of the <dbname> place holder.
        if ( !originalString.contains(dbNamePlaceholder)) {
            throw new RuntimeException("Unable to create a new DB without db.connection containing the <dbname> placeholder.");
        }

        int index = originalString.lastIndexOf(dbNamePlaceholder);

        return originalString.substring(0, index);
    }

}
