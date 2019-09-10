package com.zufar.service;

import org.apache.logging.log4j.Logger;

public class UtilService {
    
    public static Boolean isObjectNull(Object object, Logger logger, String errorMessage ) {
        if (object == null) {
            final NullPointerException nullPointerException = new NullPointerException(errorMessage);
            logger.error(errorMessage, nullPointerException);
            throw nullPointerException;
        }
        return true;
    }
}
