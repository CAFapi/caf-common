package com.hpe.caf.codec;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class DocumentWorkerTask
{
    public Map<String,String> customData;
    
    public DocumentWorkerTask()
    {
    }
}
