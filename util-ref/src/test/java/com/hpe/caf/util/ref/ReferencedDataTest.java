/*
 * Copyright 2015-2017 EntIT Software LLC, a Micro Focus company.
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
package com.hpe.caf.util.ref;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.internal.junit.ArrayAsserts;

public class ReferencedDataTest
{
    @Test
    public void testAcquireWithData()
        throws DataSourceException, IOException
    {
        byte[] test = "test123".getBytes(StandardCharsets.UTF_8);
        ReferencedData testDat = ReferencedData.getWrappedData(test);
        DataSource source = Mockito.mock(DataSource.class);
        byte[] buf = new byte[test.length];
        InputStream str = testDat.acquire(source);
        int nRead = str.read(buf, 0, buf.length);
        Assert.assertEquals(test.length, nRead);
        ArrayAsserts.assertArrayEquals(test, buf);
        Mockito.verify(source, Mockito.times(0)).getStream(Mockito.any());
        Assert.assertNull(testDat.getReference());
    }

    @Test
    public void testAcquireWithReference()
        throws DataSourceException, IOException
    {
        byte[] test = "test123".getBytes(StandardCharsets.UTF_8);
        String ref = "ref123";
        ReferencedData testDat = ReferencedData.getReferencedData(ref);
        DataSource source = Mockito.mock(DataSource.class);
        Assert.assertNotNull(testDat.getReference());
        InputStream byteStr = new ByteArrayInputStream(test);
        Mockito.when(source.getStream(ref)).thenReturn(byteStr);
        byte[] buf = new byte[test.length];
        InputStream str = testDat.acquire(source);
        int nRead = str.read(buf, 0, buf.length);
        Assert.assertEquals(test.length, nRead);
        ArrayAsserts.assertArrayEquals(test, buf);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testMissingDataAndReference()
        throws DataSourceException
    {
        ReferencedData test = new ReferencedData();
        test.acquire(Mockito.mock(DataSource.class));
    }
}
