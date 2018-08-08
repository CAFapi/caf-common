/*
 * Copyright 2015-2018 Micro Focus or one of its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
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

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnector
{

    public void applySchema(final String connectionString, final String username, final String password,
                             final String dbName, final boolean allowDBDeletion) throws SQLException, LiquibaseException
    {

        final String fullConnectionString = joinDBConnection(connectionString, dbName);
        boolean dbExists = checkDBExists(fullConnectionString, username, password);

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

            try (Connection c = basicDataSourceNoDB.getConnection()) {
                java.sql.Statement statement = c.createStatement();
                statement.executeUpdate("CREATE DATABASE " + dbName);
                System.out.println("Created new database: " + dbName);
            }
        }
        updateDB(fullConnectionString, username, password);
    }

    /**
     * Checks connection, retrieves appropriate changelog and performs database update.
     *
     * @throws SQLException
     * @throws LiquibaseException
     */
    private void updateDB(final String fullConnectionString, final String username,
                          final String password) throws SQLException, LiquibaseException
    {
        System.out.println("About to perform DB update.");
        try (BasicDataSource dataSource = new BasicDataSource()) {
            dataSource.setUrl(fullConnectionString);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            try (Connection c = dataSource.getConnection()) {
                Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(c));

                // Check that the Database does indeed exist before we try to run the liquibase update.
                Liquibase liquibase = null;
                ClassLoaderResourceAccessor accessor = new ClassLoaderResourceAccessor();
                try {
                    if (accessor.getResourcesAsStream("changelog-master.xml") != null) {
                        liquibase = new Liquibase("changelog-master.xml", new ClassLoaderResourceAccessor(), database);
                    } else if (accessor.getResourcesAsStream("changelog.xml") != null) {
                        liquibase = new Liquibase("changelog.xml", new ClassLoaderResourceAccessor(), database);
                    } else {
                        String errorMessage = "No liquibase changelog-master.xml or changelog.xml could be located";
                        Logger.getLogger(DatabaseConnector.class.getName()).log(Level.SEVERE, errorMessage, this);
                        throw new RuntimeException(errorMessage);
                    }
                } catch (final IOException ioe) {
                    Logger.getLogger(DatabaseConnector.class.getName()).log(Level.SEVERE, ioe.getMessage(), ioe);
                }

                liquibase.getLog().setLogLevel(LogLevel.INFO);
                liquibase.update(new Contexts());
                System.out.println("DB update finished.");
            }
        }
    }

    private boolean checkDBExists(final String fullConnectionString, final String username,
                                  final String password) throws SQLException
    {
        try (BasicDataSource dataSource = new BasicDataSource()) {
            dataSource.setUrl(fullConnectionString);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            try (Connection c = dataSource.getConnection()) {
                return true;
            } catch (final Exception e) {
                return false;
            }
        }
    }

    private String joinDBConnection(final String connectionString, final String dbName)
    {
        if (connectionString != null && dbName != null) {
            if (connectionString.endsWith("/") && !dbName.startsWith("/")) {
                return connectionString + dbName;
            } else if (!connectionString.endsWith("/") && dbName.startsWith("/")) {
                //Connection string must end with a /
                return connectionString + "/" + dbName;
            } else if (!connectionString.endsWith("/") && !dbName.startsWith("/")) {
                //Connection string must end with a /
                return connectionString + "//" + dbName;
            } else if (connectionString.endsWith("/") && dbName.startsWith("/")) {
                int index = connectionString.lastIndexOf("/");
                return connectionString.substring(0, index) + dbName;
            }
        }
        throw new RuntimeException("Must specify both db.connection and db.name");
    }
}
