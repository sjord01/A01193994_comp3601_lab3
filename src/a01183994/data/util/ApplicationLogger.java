package a01183994.data.util;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

public class ApplicationLogger {
    private static final String LOG4J_CONFIG_FILENAME = "log4j2.xml";

    static {
        configureLogging();
    }

    private static void configureLogging() {
        ConfigurationSource source;
        try {
            source = new ConfigurationSource(new FileInputStream(LOG4J_CONFIG_FILENAME));
            Configurator.initialize(null, source);
        } catch (IOException e) {
            System.out.println(String.format("Can't find the log4j logging configuration file %s.", LOG4J_CONFIG_FILENAME));
        }
    }

    public static Logger getLogger(Class<?> classToLog) {
        return LogManager.getLogger(classToLog);
    }
}