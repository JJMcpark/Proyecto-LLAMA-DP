package com.dpatrones.proyecto.design.singleton;

import org.springframework.stereotype.Component;

/**
 * PATRÓN SINGLETON
 * Garante que solo exista una instancia de configuración
 */
@Component
public class ConfigService {
    
    private static ConfigService instance;
    
    private final String appName = "EduSocial";
    private final String version = "1.0.0";
    private final int maxPostLength = 5000;
    private final int maxNoteLength = 50000;
    
    private ConfigService() {
        // Constructor privado
    }
    
    public static synchronized ConfigService getInstance() {
        if (instance == null) {
            instance = new ConfigService();
        }
        return instance;
    }
    
    public String getAppName() {
        return appName;
    }
    
    public String getVersion() {
        return version;
    }
    
    public int getMaxPostLength() {
        return maxPostLength;
    }
    
    public int getMaxNoteLength() {
        return maxNoteLength;
    }
    
    public boolean isValidPostLength(String content) {
        return content != null && content.length() <= maxPostLength;
    }
}
