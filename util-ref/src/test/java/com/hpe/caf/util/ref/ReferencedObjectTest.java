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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ReferencedObjectTest
{
    @Test
    public void testAcquireWithObject()
        throws DataSourceException
    {
        String test = "test123";
        ReferencedObject<String> testRef = ReferencedObject.getWrappedObject(String.class, test);
        DataSource source = Mockito.mock(DataSource.class);
        Assert.assertEquals(test, testRef.acquire(source));
        Mockito.verify(source, Mockito.times(0)).getObject(Mockito.any(), Mockito.any());
        Assert.assertNull(testRef.getReference());
    }

    @Test
    public void testAcquireWithReference()
        throws DataSourceException
    {
        String test = "test123";
        String ref = "ref123";
        ReferencedObject<String> testRef = ReferencedObject.getReferencedObject(String.class, ref);
        DataSource source = Mockito.mock(DataSource.class);
        Assert.assertNotNull(testRef.getReference());
        Mockito.when(source.getObject(ref, String.class)).thenReturn(test);
        Assert.assertEquals(test, testRef.acquire(source));
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testMissingObjectAndReference()
        throws DataSourceException
    {
        ReferencedObject<String> test = new ReferencedObject<>();
        test.acquire(Mockito.mock(DataSource.class));
    }

    @Test
    public void testSerialisation()
        throws DataSourceException, IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String test = "test123";
        ReferencedObject<String> testRef = ReferencedObject.getWrappedObject(String.class, test);
        DummyMessage dummy = new DummyMessage();
        dummy.setDummyObject(testRef);
        byte[] ser = mapper.writeValueAsBytes(dummy);
        DummyMessage res = mapper.readValue(ser, DummyMessage.class);
        DataSource source = Mockito.mock(DataSource.class);
        Assert.assertEquals(test, res.getDummyObject().acquire(source));
    }
}
