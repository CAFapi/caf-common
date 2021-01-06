/*
 * Copyright 2015-2021 Micro Focus or one of its affiliates.
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
public class DatabaseProperties
{
    private final String dbNamePlaceholder = "<dbname>";

    @Autowired
    protected Environment environment;

    public String getConnectionString()
    {
        return this.environment.getProperty("db.connection");
    }

    public String getUser()
    {
        return this.environment.getProperty("db.user");
    }

    public String getPass()
    {
        return this.environment.getProperty("db.pass");
    }

    public String getDBName()
    {
        return this.environment.getProperty("db.name");
    }
}
