package com.ctsi.ssdc.config;

public interface CtsiDefaults {

    interface Async {
        int CORE_POOL_SIZE = 2;
        int MAX_POOL_SIZE = 50;
        int QUEUE_CAPACITY = 10000;
    }
    
    interface Swagger {
        String TITILE = "Ctsi Application API";
        String DESCRIPTION = "Ctsi API documentation";
        String VERSION = "1.0.0";
        String TERMS_OF_SERVICE_URL = null;
        String CONTACT_NAME = null;
        String CONTACT_URL = null;
        String CONTACT_EMAIL = null;
        String LICENSE = null;
        String LICENSE_URL = null;
        String DEFAULT_INCLUDE_PATTERN = "/api/.*";
        String HOST = null;
        String[] PROTOCOLS = {};
    }

    interface Metrics {

        interface Jmx {

            boolean ENABLED = true;
        }

        interface Logs {

            boolean ENABLED = false;
            long REPORT_FREQUENCY = 60;

        }
    }

    interface Security {

        interface ClientAuthorization {
            String ACCESS_TOKEN_URI = null;
            String TOKEN_SERVICE_ID = null;
            String CLIENT_ID = null;
            String CLIENT_SECRET = null;
        }

        interface Authentication {

            interface Jwt {
                String SECRET = null;
                long TOKEN_VALIDITY_IN_SECONDS = 1800; // 0.5 hour
                long TOKEN_VALIDITY_IN_SECONDS_FOR_REMEMBERME = 2592000; // 30 hours;
            }
        }

        interface RememberMe {

            String KEY = null;
        }
    }

}
