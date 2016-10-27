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
      return this.environment.getProperty("db.connection");
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

}
