# Liquibase Database Installer

The Liquibase Database Installer is a generic  java application that will create and update databases using the supplied Liquibase change log.

## Consuming the Installer
To make use of the installer use the following steps:

1. Create a new Java project.
2. Add com.hpe.caf.util-liquibase-installer as a dependency.
3. Create a Liquibase change log called changelog.xml and place this in the resources folder of the project.
4. Build the project with the mainClass as:
    
    `com.hpe.caf.utilliquibaseinstaller.Application`

This will produce a .jar specific to your project.
 
Note: The installer comes prepackaged only with Postgresql and h2 drivers. If you need to use another database driver you must add it as a dependency to your own project.

## Example POM
Below is an example project's POM showing how to build using the Liquibase installer. 
<groupId>com.hpe.caf</groupId>
<artifactId>boilerplate-db</artifactId>
<version>1.0-SNAPSHOT</version>

    <dependencies>
        <dependency>
            <groupId>com.hpe.caf.util</groupId>
            <artifactId>util-liquibase-installer</artifactId>
            <version>1.0</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <artifactId>maven-assembly-plugin</artifactId>
                <configuration>
                    <archive>
                        <manifest>
                            <mainClass>com.hpe.caf.utilliquibaseinstaller.Application</mainClass>
                        </manifest>
                    </archive>
                    <descriptorRefs>
                        <descriptorRef>jar-with-dependencies</descriptorRef>
                    </descriptorRefs>
                </configuration>
                <executions>
                    <execution>
                        <id>make-assembly</id> <!-- this is used for inheritance merges -->
                        <phase>package</phase> <!-- bind to the packaging phase -->
                        <goals>
                            <goal>single</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
    
## Running the Installer

The installer is run via command line. The following arguments can be specified:

*   db.user  :  Specifies the username to access the database.
*   db.pass  :  Specifies the password to access the database.
*   db.connection  : Specifies the jbdc connection string to the database service. This does not include the database name.  e.g. jdbc:postgresql:/localhost:3307/
*   db.name  :  Specifies the name of the database to be created or updated.
*   fd  :  Enables the deletion of the existing database for a fresh install, rather than updating the database.
*   log : Specifies the logging level of the installer. Valid options are: [debug, info, warning, severe, off]

You can specify these options in a database.properties file. This can be specified with the system property `-DDATABASE_CONFIG=<DIRECTORY OF PROPERTY FILE>`   
The command to run the jar will be in the following format:  

    java -jar [system properties] installer.jar [arguemnts]  

For example:   

    java -jar -DDATABASE_CONFIG=C:\var myInstaller.jar -fd -db.user Admin  
    
Note that passing a property as a command line argument will override the properties file.  