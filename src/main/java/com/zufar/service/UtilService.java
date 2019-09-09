package com.zufar.service;

import org.apache.logging.log4j.Logger;

public class UtilService {
    
    public static void isObjectNull(Object object, Logger logger, String errorMessage ) {
        if (object == null) {
            final IllegalArgumentException illegalArgumentException = new IllegalArgumentException(errorMessage);
            logger.error(errorMessage, illegalArgumentException);
            throw illegalArgumentException;
        }
    }
}
