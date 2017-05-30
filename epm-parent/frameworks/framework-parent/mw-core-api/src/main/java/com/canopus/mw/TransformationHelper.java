package com.canopus.mw;

import org.apache.log4j.*;
import org.modelmapper.*;

public class TransformationHelper
{
    private static final Logger logger;
    
    public static <S, D> void createTypeMap(final ModelMapper modelMapper, final Class<S> source, final Class<D> destination) {
        createTypeMapSafe(modelMapper, (Class<Object>)source, (Class<Object>)destination);
        createTypeMapSafe(modelMapper, destination, source);
    }
    
    public static <S, D> void createTypeMapSafe(final ModelMapper modelMapper, final Class<S> source, final Class<D> destination) {
        try {
            modelMapper.createTypeMap((Class)source, (Class)destination);
        }
        catch (Exception ex) {
            TransformationHelper.logger.debug((Object)"Failed to register type map", (Throwable)ex);
        }
    }
    
    static {
        logger = Logger.getLogger((Class)TransformationHelper.class);
    }
}
