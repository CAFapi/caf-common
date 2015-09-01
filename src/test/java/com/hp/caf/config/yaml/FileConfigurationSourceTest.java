package com.hp.caf.config.yaml;


import com.hp.caf.api.BootstrapConfiguration;
import com.hp.caf.api.Codec;
import com.hp.caf.api.CodecException;
import com.hp.caf.api.ConfigurationException;
import com.hp.caf.api.ConfigurationSource;
import com.hp.caf.api.ServicePath;
import com.hp.caf.cipher.NullCipher;
import com.hp.caf.codec.YamlCodec;
import com.hp.caf.config.file.FileConfigurationSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import javax.naming.InvalidNameException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;


public class FileConfigurationSourceTest
{
    @Rule
    public TemporaryFolder tempDir = new TemporaryFolder();
    private Path temp;
    private final Codec codec = new YamlCodec();
    private ServicePath id;


    @Before
    public void setUp()
            throws IOException, InvalidNameException
    {
        temp = tempDir.newFolder().toPath();
        id = new ServicePath("testApp/testWorker");
    }


    @Test
    public void testGetConfiguration()
            throws IOException, ConfigurationException, CodecException
    {
        TestFileConfig tyc = new TestFileConfig();
        final String comparisonString = "test456";
        tyc.setTestString(comparisonString);
        String name = TestFileConfig.class.getSimpleName() + FileConfigurationSource.FILE_EXTENSION;
        Files.createDirectories(temp.resolve(id.getPath()));
        try (BufferedWriter writer = Files.newBufferedWriter(temp.resolve(id.getPath()).resolve(name), StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
            writer.write(new String(codec.serialise(tyc), StandardCharsets.UTF_8));
        }
        BootstrapConfiguration bc = Mockito.mock(BootstrapConfiguration.class);
        Mockito.when(bc.isConfigurationPresent(FileConfigurationSource.CONFIG_PATH)).thenReturn(true);
        Mockito.when(bc.getConfiguration(FileConfigurationSource.CONFIG_PATH)).thenReturn(temp.toString());
        ConfigurationSource ycp = new FileConfigurationSource(bc, new NullCipher(bc), id, codec);
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
        String name = TestFileConfig.class.getSimpleName() + FileConfigurationSource.FILE_EXTENSION;
        Files.createDirectories(temp.resolve(id.getRoot()));
        try (BufferedWriter writer = Files.newBufferedWriter(temp.resolve(id.getGroup()).resolve(name), StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
            writer.write(new String(codec.serialise(tyc), StandardCharsets.UTF_8));
        }
        BootstrapConfiguration bc = Mockito.mock(BootstrapConfiguration.class);
        Mockito.when(bc.isConfigurationPresent(FileConfigurationSource.CONFIG_PATH)).thenReturn(true);
        Mockito.when(bc.getConfiguration(FileConfigurationSource.CONFIG_PATH)).thenReturn(temp.toString());
        ConfigurationSource ycp = new FileConfigurationSource(bc, new NullCipher(bc), id, codec);
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
        String rootName = RootConfig.class.getSimpleName() + FileConfigurationSource.FILE_EXTENSION;
        String innerName = InnerConfig.class.getSimpleName() + FileConfigurationSource.FILE_EXTENSION;
        Files.createDirectories(temp.resolve(id.getPath()));
        try (BufferedWriter writer = Files.newBufferedWriter(temp.resolve(id.getPath()).resolve(rootName), StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
            writer.write(new String(codec.serialise(rootConfig), StandardCharsets.UTF_8));
        }
        try (BufferedWriter writer = Files.newBufferedWriter(temp.resolve(id.getPath()).resolve(innerName), StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
            writer.write(new String(codec.serialise(innerConfig), StandardCharsets.UTF_8));
        }
        BootstrapConfiguration bc = Mockito.mock(BootstrapConfiguration.class);
        Mockito.when(bc.isConfigurationPresent(FileConfigurationSource.CONFIG_PATH)).thenReturn(true);
        Mockito.when(bc.getConfiguration(FileConfigurationSource.CONFIG_PATH)).thenReturn(temp.toString());
        ConfigurationSource ycp = new FileConfigurationSource(bc, new NullCipher(bc), id, codec);
        RootConfig result = ycp.getConfiguration(RootConfig.class);
        Assert.assertEquals(testInt, result.getInnerConfig().getTestValue());
    }


    @Test(expected = ConfigurationException.class)
    public void testMissingConfiguration()
        throws ConfigurationException
    {
        BootstrapConfiguration bc = Mockito.mock(BootstrapConfiguration.class);
        ConfigurationSource ycp = new FileConfigurationSource(bc, new NullCipher(bc), id, codec);
        ycp.getConfiguration(TestFileConfig.class);
    }


    @Test(expected = ConfigurationException.class)
    public void testInvalidConfiguration()
            throws ConfigurationException, IOException, CodecException
    {
        TestFileConfig tyc = new TestFileConfig();
        tyc.setTestString("");
        String name = TestFileConfig.class.getSimpleName() + FileConfigurationSource.FILE_EXTENSION;
        Files.createDirectories(temp.resolve(id.getPath()));
        try (BufferedWriter writer = Files.newBufferedWriter(temp.resolve(id.getPath()).resolve(name), StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
            writer.write(new String(codec.serialise(tyc), StandardCharsets.UTF_8));
        }
        BootstrapConfiguration bc = Mockito.mock(BootstrapConfiguration.class);
        Mockito.when(bc.isConfigurationPresent(FileConfigurationSource.CONFIG_PATH)).thenReturn(true);
        Mockito.when(bc.getConfiguration(FileConfigurationSource.CONFIG_PATH)).thenReturn(temp.toString());
        ConfigurationSource ycp = new FileConfigurationSource(bc, new NullCipher(bc), id, codec);
        TestFileConfig result = ycp.getConfiguration(TestFileConfig.class);
    }

}
