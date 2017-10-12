# Swagger UI

This project is a fork of the [Swagger-UI](https://github.com/swagger-api/swagger-ui) project. 

Provides the base html page for the swagger UI.

## Usage

The web page in this module will work on its own with the location of a swagger.yaml or swagger.json file specified as `var url` in `index.html` i.e. `var url = http://localhost:8080/myswaggerdefinition.yaml;`.

##### Usage in CAF

Add this project and the swagger contract into your own web-service-ui module as a dependencies:


    <dependencies>
        <dependency>
            <groupId>${swagger.contract.groupId}</groupId>
            <artifactId>${swagger.contract.artifactId}</artifactId>
            <version>${swagger.contract.version}</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>com.github.cafapi</groupId>
            <artifactId>swagger-ui</artifactId>
            <version>1.0-SNAPSHOT</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
    


Unpack the dependencies using the maven-dependency-plugin as below. The maven-war-plugin assembles your swagger UI war file with the unpacked swagger UI html page and swagger.yaml. The `${swagger.contract.path}` will be the location from `${project.basedir}` of the swagger.yaml in the contract module.



    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <version>2.10</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>unpack</goal>
                        </goals>
                        <configuration>
                            <artifactItems>
                                <artifactItem>
                                    <groupId>com.github.cafapi</groupId>
                                    <artifactId>swagger-ui</artifactId>
                                    <outputDirectory>${project.build.directory}/swagger-ui</outputDirectory>
                                    <excludes>**/META-INF/**</excludes>
                                </artifactItem>
                                <artifactItem>
                                    <groupId>${swagger.contract.groupId}</groupId>
                                    <artifactId>${swagger.contract.artifactId}</artifactId>
                                    <version>${swagger.contract.version}</version>
                                    <outputDirectory>${project.build.directory}/swagger-contract</outputDirectory>
                                </artifactItem>
                            </artifactItems>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>2.6</version>
                <configuration>
                    <failOnMissingWebXml>false</failOnMissingWebXml>
                    <webResources>
                        <resource>
                            <directory>${project.build.directory}/swagger-ui</directory>
                        </resource>
                        <resource>
                            <directory>${project.build.directory}/swagger-contract${swagger.contract.path}</directory>
                            <targetPath>api-docs</targetPath>
                        </resource>
                    </webResources>
                </configuration>
            </plugin>
        </plugins>
    </build>
    
