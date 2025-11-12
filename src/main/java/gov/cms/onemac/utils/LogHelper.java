package gov.cms.onemac.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LogHelper {
        private static final Logger logger = LogManager.getLogger(LogHelper.class);

        public static Logger getLogger() {
            return logger;
        }
}
