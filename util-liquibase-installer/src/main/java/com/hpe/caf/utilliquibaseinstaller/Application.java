/*
 * (c) Copyright 2015-2016 Hewlett Packard Enterprise Development LP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hpe.caf.utilliquibaseinstaller;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.logging.LogLevel;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.apache.commons.dbcp2.BasicDataSource;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;
import org.kohsuke.args4j.Option;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by gibsodom on 08/12/2015.
 */
public class Application {
    private final String dbNamePlaceholder = "<dbname>";

    @Option(name = "-fd", usage = "Enables the deletion of existing database for a fresh install.")
    private boolean allowDBDeletion;

    @Option(name = "-log", usage = "Specifies the logging level of the installer")
    private LogLevel logLevel = LogLevel.WARNING;

    @Option(name = "-db.connection", usage = "Specifies the connection string to the database service. e.g. postgresql://localhost:3307/")
    private String connectionString;

    @Option(name = "-db.user", usage = "Specifies the username to access the database.")
    private String username;

    @Option(name = "-db.pass", usage = "Specifies the password to access the database.")
    private String password;

    @Option(name = "-db.name", usage = "Specifies the name of the database to be created or updated.")
    private String dbName;

    private String fullConnectionString;

    private DatabaseProperties properties = loadProperties(DatabaseProperties.class);

    public static void main(String[] args) throws SQLException, LiquibaseException {
        new Application().run(args);
    }

    private void run(String[] args) throws SQLException, LiquibaseException {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            // parse the arguments.
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            // if there's a problem in the command line,
            // you'll get this exception. this will report
            // an error message.
            System.err.println(e.getMessage());
            System.err.println("java Main [options...] arguments...");
            // print the list of available options
            parser.printUsage(System.err);
            System.err.println();

            // print option sample. This is useful some time
            System.err.println("  Example: java -jar database-installer-1.0-SNAPSHOT-jar-with-dependencies.jar " + parser.printExample(ExampleMode.ALL));

            return;
        }
        checkArgs();
        applySchema();
    }

    private void checkArgs() {
        if (properties != null) {
            dbName = dbName != null ? dbName : properties.getDBName();
            connectionString = connectionString != null ? connectionString : properties.getConnectionString();
            fullConnectionString = joinDBConnection(connectionString,dbName);
            username = username != null ? username : properties.getUser();
            password = password != null ? password : properties.getPass();
        } else if (connectionString == null || username == null || password == null) {
            throw new RuntimeException("If no properties specified, either supply a properties file with -DpropertySource or pass the arguments with -Ddb.connection, -Ddb.user, -Ddb.pass");
        }
    }

    private void applySchema() throws SQLException, LiquibaseException {

        boolean dbExists = checkDBExists();

        if (dbExists && allowDBDeletion) {
            System.out.println();
            System.out.println("DB - Exists, and force deletion has been specified for: " + dbName);
            System.out.println();

            BasicDataSource basicDataSourceNoDB = new BasicDataSource();
            basicDataSourceNoDB.setUrl(connectionString);
            basicDataSourceNoDB.setUsername(username);
            basicDataSourceNoDB.setPassword(password);

            try (Connection c = basicDataSourceNoDB.getConnection()) {
                java.sql.Statement statement = c.createStatement();
                statement.executeUpdate("DROP DATABASE " + dbName);
                System.out.println("DELETED database: " + dbName);
                dbExists = false;
            }
        }
        if (!dbExists) {
            System.out.println("about to perform DB installation from scratch.");

            BasicDataSource basicDataSourceNoDB = new BasicDataSource();
            basicDataSourceNoDB.setUrl(connectionString);
            basicDataSourceNoDB.setUsername(username);
            basicDataSourceNoDB.setPassword(password);

            try (java.sql.Connection c = basicDataSourceNoDB.getConnection()) {
                java.sql.Statement statement = c.createStatement();
                statement.executeUpdate("CREATE DATABASE " + dbName);
                System.out.println("Created new database: " + dbName);
            }
        }
        updateDB();
    }

    private void updateDB() throws SQLException, LiquibaseException {
        System.out.println("About to perform DB update.");
        try(BasicDataSource dataSource = new BasicDataSource()) {
            dataSource.setUrl(fullConnectionString);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            try (java.sql.Connection c = dataSource.getConnection()) {
                Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(c));

                // Check that the Database does indeed exist before we try to run the liquibase update.
                Liquibase liquibase = new Liquibase("changelog-master.xml", new ClassLoaderResourceAccessor(), database);
                liquibase.getLog().setLogLevel(logLevel);
                liquibase.update(new Contexts());
                System.out.println("DB update finished.");
            }
        }
    }


    protected static <T> T loadProperties(Class<T> propertiesClass) {
        AnnotationConfigApplicationContext propertiesApplicationContext = new AnnotationConfigApplicationContext();
        propertiesApplicationContext.register(PropertySourcesPlaceholderConfigurer.class);
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(propertiesClass);
        propertiesApplicationContext.registerBeanDefinition(propertiesClass.getSimpleName(), beanDefinition);
        propertiesApplicationContext.refresh();
        return propertiesApplicationContext.getBean(propertiesClass);
    }

    private boolean checkDBExists() throws SQLException {
        try (BasicDataSource dataSource = new BasicDataSource()) {
            dataSource.setUrl(fullConnectionString);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            try (Connection c = dataSource.getConnection()) {
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    private String joinDBConnection(String connectionString, String dbName) {
        if(connectionString!=null && dbName != null){
            if(connectionString.endsWith("/") && !dbName.startsWith("/")) {
                return connectionString + dbName;
            }
            else if(!connectionString.endsWith("/") && dbName.startsWith("/")){
                //Connection string must end with a /
                this.connectionString = connectionString+"/";
                return connectionString+dbName;
            }
            else if(!connectionString.endsWith("/") && !dbName.startsWith("/")){
                //Connection string must end with a /
                this.connectionString = connectionString+"/";
                return connectionString+"/"+dbName;
            }
            else if(connectionString.endsWith("/") && dbName.startsWith("/")){
                int index = connectionString.lastIndexOf("/");
                return connectionString.substring(0,index) + dbName;
            }
        }
        throw new RuntimeException("Must specify both db.connection and db.name");
    }

}
