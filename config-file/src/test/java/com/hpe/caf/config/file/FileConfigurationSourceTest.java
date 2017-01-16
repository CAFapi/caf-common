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

package com.hpe.caf.config.file;


import com.hpe.caf.api.BootstrapConfiguration;
import com.hpe.caf.api.Codec;
import com.hpe.caf.api.CodecException;
import com.hpe.caf.api.ConfigurationException;
import com.hpe.caf.api.ConfigurationSource;
import com.hpe.caf.cipher.NullCipher;
import com.hpe.caf.codec.YamlCodec;
import com.hpe.caf.naming.ServicePath;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.mockito.Mockito;

import javax.naming.InvalidNameException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;


public class FileConfigurationSourceTest
{
    public File tempDir;
    private Path temp;
    private final Codec codec = new YamlCodec();
    private final String groupName = "testApp";
    private final String appId = "testWorker";
    private ServicePath id;


    @BeforeMethod
    public void setUp()
            throws IOException, InvalidNameException
    {
        tempDir = new File("temp");
        tempDir.mkdir();
        temp = tempDir.toPath();
        id = new ServicePath(groupName + "/" + appId);
    }
    
    @AfterMethod
    public void tearDown()
    {
        deleteDir(tempDir);
    }
    
    private void deleteDir(File file)
    {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }    

    @Test
    public void testGetConfiguration()
            throws IOException, ConfigurationException, CodecException
    {
        TestFileConfig tyc = new TestFileConfig();
        final String comparisonString = "test456";
        tyc.setTestString(comparisonString);
        String name = "cfg_" + groupName + "_" + appId + "_" + TestFileConfig.class.getSimpleName();
        try (BufferedWriter writer = Files.newBufferedWriter(temp.resolve(name), StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
            writer.write(new String(codec.serialise(tyc), StandardCharsets.UTF_8));
        }
        BootstrapConfiguration bc = Mockito.mock(BootstrapConfiguration.class);
        Mockito.when(bc.isConfigurationPresent(FileConfigurationSource.CONFIG_PATH)).thenReturn(true);
        Mockito.when(bc.getConfiguration(FileConfigurationSource.CONFIG_PATH)).thenReturn(temp.toString());
        ConfigurationSource ycp = new FileConfigurationSource(bc, new NullCipher(), id, codec);
        TestFileConfig result = ycp.getConfiguration(TestFileConfig.class);
        Assert.assertEquals(tyc.getTestString(), result.getTestString());
    }


    @Test
    public void testGetGroupConfiguration()
        throws IOException, ConfigurationException, CodecException
    {
        TestFileConfig tyc = new TestFileConfig();
        final String comparisonString = "test456";
        tyc.setTestString(comparisonString);
        String name = "cfg_" + groupName + "_" + TestFileConfig.class.getSimpleName();
        try (BufferedWriter writer = Files.newBufferedWriter(temp.resolve(name), StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
            writer.write(new String(codec.serialise(tyc), StandardCharsets.UTF_8));
        }
        BootstrapConfiguration bc = Mockito.mock(BootstrapConfiguration.class);
        Mockito.when(bc.isConfigurationPresent(FileConfigurationSource.CONFIG_PATH)).thenReturn(true);
        Mockito.when(bc.getConfiguration(FileConfigurationSource.CONFIG_PATH)).thenReturn(temp.toString());
        ConfigurationSource ycp = new FileConfigurationSource(bc, new NullCipher(), id, codec);
        TestFileConfig result = ycp.getConfiguration(TestFileConfig.class);
        Assert.assertEquals(tyc.getTestString(), result.getTestString());
    }


    @Test
    public void testGetRecursiveConfiguration()
        throws IOException, ConfigurationException, CodecException
    {
        RootConfig rootConfig = new RootConfig();
        InnerConfig innerConfig = new InnerConfig();
        final int testInt = 90;
        innerConfig.setTestValue(testInt);
        String rootName = "cfg_" + groupName + "_" + appId + "_" + RootConfig.class.getSimpleName();
        String innerName = "cfg_" + groupName + "_" + appId + "_" + InnerConfig.class.getSimpleName();
        try (BufferedWriter writer = Files.newBufferedWriter(temp.resolve(rootName), StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
            writer.write(new String(codec.serialise(rootConfig), StandardCharsets.UTF_8));
        }
        try (BufferedWriter writer = Files.newBufferedWriter(temp.resolve(innerName), StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
            writer.write(new String(codec.serialise(innerConfig), StandardCharsets.UTF_8));
        }
        BootstrapConfiguration bc = Mockito.mock(BootstrapConfiguration.class);
        Mockito.when(bc.isConfigurationPresent(FileConfigurationSource.CONFIG_PATH)).thenReturn(true);
        Mockito.when(bc.getConfiguration(FileConfigurationSource.CONFIG_PATH)).thenReturn(temp.toString());
        ConfigurationSource ycp = new FileConfigurationSource(bc, new NullCipher(), id, codec);
        RootConfig result = ycp.getConfiguration(RootConfig.class);
        Assert.assertEquals(testInt, result.getInnerConfig().getTestValue());
    }


    @Test(expectedExceptions = ConfigurationException.class)
    public void testMissingConfiguration()
        throws ConfigurationException
    {
        BootstrapConfiguration bc = Mockito.mock(BootstrapConfiguration.class);
        ConfigurationSource ycp = new FileConfigurationSource(bc, new NullCipher(), id, codec);
        ycp.getConfiguration(TestFileConfig.class);
    }


    @Test(expectedExceptions = ConfigurationException.class)
    public void testInvalidConfiguration()
            throws ConfigurationException, IOException, CodecException
    {
        TestFileConfig tyc = new TestFileConfig();
        tyc.setTestString("");
        String name = "cfg_" + groupName + "_" + appId + "_" + TestFileConfig.class.getSimpleName();
        try (BufferedWriter writer = Files.newBufferedWriter(temp.resolve(name), StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
            writer.write(new String(codec.serialise(tyc), StandardCharsets.UTF_8));
        }
        BootstrapConfiguration bc = Mockito.mock(BootstrapConfiguration.class);
        Mockito.when(bc.isConfigurationPresent(FileConfigurationSource.CONFIG_PATH)).thenReturn(true);
        Mockito.when(bc.getConfiguration(FileConfigurationSource.CONFIG_PATH)).thenReturn(temp.toString());
        ConfigurationSource ycp = new FileConfigurationSource(bc, new NullCipher(), id, codec);
        TestFileConfig result = ycp.getConfiguration(TestFileConfig.class);
    }

}
