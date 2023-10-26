package com.ctsi.ssdc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Properties specific to ctsi.
 *
 * <p> Properties are configured in the application.yml file. </p>
 */
@ConfigurationProperties(prefix = "ctsi", ignoreUnknownFields = true)
public class CtsiProperties {

    private final Async async = new Async();

    private final Swagger swagger = new Swagger();

    private final Metrics metrics = new Metrics();

    private final CorsConfiguration cors = new CorsConfiguration();

    private final Security security = new Security();
    
    private List<String> corspaths;

	public List<String> getCorspaths() {
		return corspaths;
	}

	public void setCorspaths(List<String> corspaths) {
		this.corspaths = (corspaths != null ? new ArrayList<>(corspaths) : null);
	}

	public Async getAsync() {
        return async;
    }

    public Swagger getSwagger() {
        return swagger;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public CorsConfiguration getCors() {
        return cors;
    }
    

    public Security getSecurity() {
		return security;
	}


	public static class Async {

        private int corePoolSize = CtsiDefaults.Async.CORE_POOL_SIZE;

        private int maxPoolSize = CtsiDefaults.Async.MAX_POOL_SIZE;

        private int queueCapacity = CtsiDefaults.Async.QUEUE_CAPACITY;

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getQueueCapacity() {
            return queueCapacity;
        }

        public void setQueueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }
    }
    
    public static class Swagger {

        private String title = CtsiDefaults.Swagger.TITILE;

        private String description = CtsiDefaults.Swagger.DESCRIPTION;

        private String version = CtsiDefaults.Swagger.VERSION;

        private String termsOfServiceUrl = CtsiDefaults.Swagger.TERMS_OF_SERVICE_URL;

        private String contactName = CtsiDefaults.Swagger.CONTACT_NAME;

        private String contactUrl = CtsiDefaults.Swagger.CONTACT_URL;

        private String contactEmail = CtsiDefaults.Swagger.CONTACT_EMAIL;

        private String license = CtsiDefaults.Swagger.LICENSE;

        private String licenseUrl = CtsiDefaults.Swagger.LICENSE_URL;

        private String defaultIncludePattern = CtsiDefaults.Swagger.DEFAULT_INCLUDE_PATTERN;

        private String host = CtsiDefaults.Swagger.HOST;

        private String[] protocols = CtsiDefaults.Swagger.PROTOCOLS;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getTermsOfServiceUrl() {
            return termsOfServiceUrl;
        }

        public void setTermsOfServiceUrl(String termsOfServiceUrl) {
            this.termsOfServiceUrl = termsOfServiceUrl;
        }

        public String getContactName() {
            return contactName;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
        }

        public String getContactUrl() {
            return contactUrl;
        }

        public void setContactUrl(String contactUrl) {
            this.contactUrl = contactUrl;
        }

        public String getContactEmail() {
            return contactEmail;
        }

        public void setContactEmail(String contactEmail) {
            this.contactEmail = contactEmail;
        }

        public String getLicense() {
            return license;
        }

        public void setLicense(String license) {
            this.license = license;
        }

        public String getLicenseUrl() {
            return licenseUrl;
        }

        public void setLicenseUrl(String licenseUrl) {
            this.licenseUrl = licenseUrl;
        }

        public String getDefaultIncludePattern() {
            return defaultIncludePattern;
        }

        public void setDefaultIncludePattern(String defaultIncludePattern) {
            this.defaultIncludePattern = defaultIncludePattern;
        }

        public String getHost() {
            return host;
        }

        public void setHost(final String host) {
            this.host = host;
        }

        public String[] getProtocols() {
            return protocols;
        }

        public void setProtocols(final String[] protocols) {
            this.protocols = protocols;
        }
    }

    public static class Metrics {

        private final Jmx jmx = new Jmx();

        private final Logs logs = new Logs();

        public Jmx getJmx() {
            return jmx;
        }

         public Logs getLogs() {
            return logs;
        }

        public static class Jmx {

            private boolean enabled = CtsiDefaults.Metrics.Jmx.ENABLED;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }
        }

        public static class Logs {

            private boolean enabled = CtsiDefaults.Metrics.Logs.ENABLED;

            private long reportFrequency = CtsiDefaults.Metrics.Logs.REPORT_FREQUENCY;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public long getReportFrequency() {
                return reportFrequency;
            }

            public void setReportFrequency(long reportFrequency) {
                this.reportFrequency = reportFrequency;
            }
        }
    }

    public static class Security {

        private final ClientAuthorization clientAuthorization = new ClientAuthorization();

        private final Authentication authentication = new Authentication();

        private final RememberMe rememberMe = new RememberMe();

        public ClientAuthorization getClientAuthorization() {
            return clientAuthorization;
        }

        public Authentication getAuthentication() {
            return authentication;
        }

        public RememberMe getRememberMe() {
            return rememberMe;
        }

        public static class ClientAuthorization {

            private String accessTokenUri = CtsiDefaults.Security.ClientAuthorization.ACCESS_TOKEN_URI;

            private String tokenServiceId = CtsiDefaults.Security.ClientAuthorization.TOKEN_SERVICE_ID;

            private String clientId = CtsiDefaults.Security.ClientAuthorization.CLIENT_ID;

            private String clientSecret = CtsiDefaults.Security.ClientAuthorization.CLIENT_SECRET;

            public String getAccessTokenUri() {
                return accessTokenUri;
            }

            public void setAccessTokenUri(String accessTokenUri) {
                this.accessTokenUri = accessTokenUri;
            }

            public String getTokenServiceId() {
                return tokenServiceId;
            }

            public void setTokenServiceId(String tokenServiceId) {
                this.tokenServiceId = tokenServiceId;
            }

            public String getClientId() {
                return clientId;
            }

            public void setClientId(String clientId) {
                this.clientId = clientId;
            }

            public String getClientSecret() {
                return clientSecret;
            }

            public void setClientSecret(String clientSecret) {
                this.clientSecret = clientSecret;
            }
        }

        public static class Authentication {
        	
        	private final Cas cas = new Cas();
        	
        	public Cas getCas() {
        		return cas;
        	}
        	
        	public static class Cas {
        		
        		private String casServerUrlPrefix;

				public String getCasServerUrlPrefix() {
					return casServerUrlPrefix;
				}

				public void setCasServerUrlPrefix(String casServerUrlPrefix) {
					this.casServerUrlPrefix = casServerUrlPrefix;
				}
        		
        	}

            private final Jwt jwt = new Jwt();

            public Jwt getJwt() {
                return jwt;
            }

            public static class Jwt {

                private String secret = CtsiDefaults.Security.Authentication.Jwt.SECRET;

                private long tokenValidityInSeconds = CtsiDefaults.Security.Authentication.Jwt
                    .TOKEN_VALIDITY_IN_SECONDS;

                private long tokenValidityInSecondsForRememberMe = CtsiDefaults.Security.Authentication.Jwt
                    .TOKEN_VALIDITY_IN_SECONDS_FOR_REMEMBERME;

                public String getSecret() {
                    return secret;
                }

                public void setSecret(String secret) {
                    this.secret = secret;
                }

                public long getTokenValidityInSeconds() {
                    return tokenValidityInSeconds;
                }

                public void setTokenValidityInSeconds(long tokenValidityInSeconds) {
                    this.tokenValidityInSeconds = tokenValidityInSeconds;
                }

                public long getTokenValidityInSecondsForRememberMe() {
                    return tokenValidityInSecondsForRememberMe;
                }

                public void setTokenValidityInSecondsForRememberMe(long tokenValidityInSecondsForRememberMe) {
                    this.tokenValidityInSecondsForRememberMe = tokenValidityInSecondsForRememberMe;
                }
            }
        }

        public static class RememberMe {

            private String key = CtsiDefaults.Security.RememberMe.KEY;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }
        }
    }


}
