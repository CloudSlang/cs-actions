package io.cloudslang.content.abby.entities;

import java.util.List;

public class ProcessTextFieldInput {
    private String locationId;
    private String applicationId;
    private String password;
    private int timeToWait;
    private int numberOfRetries;
    private String proxyHost;
    private short proxyPort;
    private String proxyUsername;
    private String proxyPassword;
    private boolean trustAllRoots;
    private String x509HostnameVerifier;
    private String trustKeystore;
    private String trustPassword;
    private int connectTimeout;
    private int socketTimeout;
    private boolean useCookies;
    private boolean keepAlive;
    private int connectionsMaxPerRoute;
    private int connectionsMaxTotal;
    private String headers;
    private String responseCharacterSet;
    private String destinationFile;
    private String sourceFile;
    private String chunkedRequestEntity;
    private Region region;
    private List<String> languages;
    private String letterSet;
    private String regExp;
    private TextType textType;
    private boolean oneTextLine;
    private boolean oneWordPerTextLine;
    private MarkingType markingType;
    private int placeholdersCount;
    private WritingStyle writingStyle;
    private String description;
    private String pdfPassword;


    public String getLocationId() {
        return locationId;
    }


    public String getApplicationId() {
        return applicationId;
    }


    public String getPassword() {
        return password;
    }


    public int getTimeToWait() {
        return timeToWait;
    }


    public int getNumberOfRetries() {
        return numberOfRetries;
    }


    public String getProxyHost() {
        return proxyHost;
    }


    public short getProxyPort() {
        return proxyPort;
    }


    public String getProxyUsername() {
        return proxyUsername;
    }


    public String getProxyPassword() {
        return proxyPassword;
    }


    public boolean isTrustAllRoots() {
        return trustAllRoots;
    }


    public String getX509HostnameVerifier() {
        return x509HostnameVerifier;
    }


    public String getTrustKeystore() {
        return trustKeystore;
    }


    public String getTrustPassword() {
        return trustPassword;
    }


    public int getConnectTimeout() {
        return connectTimeout;
    }


    public int getSocketTimeout() {
        return socketTimeout;
    }


    public boolean isUseCookies() {
        return useCookies;
    }


    public boolean isKeepAlive() {
        return keepAlive;
    }


    public int getConnectionsMaxPerRoute() {
        return connectionsMaxPerRoute;
    }


    public int getConnectionsMaxTotal() {
        return connectionsMaxTotal;
    }


    public String getHeaders() {
        return headers;
    }


    public String getResponseCharacterSet() {
        return responseCharacterSet;
    }


    public String getDestinationFile() {
        return destinationFile;
    }


    public String getSourceFile() {
        return sourceFile;
    }


    public String getChunkedRequestEntity() {
        return chunkedRequestEntity;
    }


    public Region getRegion() {
        return region;
    }


    public List<String> getLanguages() {
        return languages;
    }


    public String getLetterSet() {
        return letterSet;
    }


    public String getRegExp() {
        return regExp;
    }


    public TextType getTextType() {
        return textType;
    }


    public boolean isOneTextLine() {
        return oneTextLine;
    }


    public boolean isOneWordPerTextLine() {
        return oneWordPerTextLine;
    }


    public MarkingType getMarkingType() {
        return markingType;
    }


    public int getPlaceholdersCount() {
        return placeholdersCount;
    }


    public WritingStyle getWritingStyle() {
        return writingStyle;
    }


    public String getDescription() {
        return description;
    }


    public String getPdfPassword() {
        return pdfPassword;
    }


    public static class Builder {
        private String locationId;
        private String applicationId;
        private String password;
        private String timeToWait;
        private String numberOfRetries;
        private String proxyHost;
        private String proxyPort;
        private String proxyUsername;
        private String proxyPassword;
        private String trustAllRoots;
        private String x509HostnameVerifier;
        private String trustKeystore;
        private String trustPassword;
        private String connectTimeout;
        private String socketTimeout;
        private String useCookies;
        private String keepAlive;
        private String connectionsMaxPerRoute;
        private String connectionsMaxTotal;
        private String headers;
        private String responseCharacterSet;
        private String destinationFile;
        private String sourceFile;
        private String chunkedRequestEntity;
        private String region;
        private String language;
        private String letterSet;
        private String regExp;
        private String textType;
        private String oneTextLine;
        private String oneWordPerTextLine;
        private String markingType;
        private String placeholdersCount;
        private String writingStyle;
        private String description;
        private String pdfPassword;


        public Builder locationId(String locationId) {
            this.locationId = locationId;
            return this;
        }


        public Builder applicationId(String applicationId) {
            this.applicationId = applicationId;
            return this;
        }


        public Builder password(String password) {
            this.password = password;
            return this;
        }


        public Builder timeToWait(String timeToWait) {
            this.timeToWait = timeToWait;
            return this;
        }


        public Builder numberOfRetries(String numberOfRetries) {
            this.numberOfRetries = numberOfRetries;
            return this;
        }


        public Builder proxyHost(String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }


        public Builder proxyPort(String proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }


        public Builder proxyUsername(String proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }


        public Builder proxyPassword(String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }


        public Builder trustAllRoots(String trustAllRoots) {
            this.trustAllRoots = trustAllRoots;
            return this;
        }


        public Builder x509HostnameVerifier(String x509HostnameVerifier) {
            this.x509HostnameVerifier = x509HostnameVerifier;
            return this;
        }


        public Builder trustKeystore(String trustKeystore) {
            this.trustKeystore = trustKeystore;
            return this;
        }


        public Builder trustPassword(String trustPassword) {
            this.trustPassword = trustPassword;
            return this;
        }


        public Builder connectTimeout(String connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }


        public Builder socketTimeout(String socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }


        public Builder useCookies(String useCookies) {
            this.useCookies = useCookies;
            return this;
        }


        public Builder keepAlive(String keepAlive) {
            this.keepAlive = keepAlive;
            return this;
        }


        public Builder connectionsMaxPerRoute(String connectionsMaxPerRoute) {
            this.connectionsMaxPerRoute = connectionsMaxPerRoute;
            return this;
        }


        public Builder connectionsMaxTotal(String connectionsMaxTotal) {
            this.connectionsMaxTotal = connectionsMaxTotal;
            return this;
        }


        public Builder headers(String headers) {
            this.headers = headers;
            return this;
        }


        public Builder responseCharacterSet(String responseCharacterSet) {
            this.responseCharacterSet = responseCharacterSet;
            return this;
        }


        public Builder destinationFile(String destinationFile) {
            this.destinationFile = destinationFile;
            return this;
        }


        public Builder sourceFile(String sourceFile) {
            this.sourceFile = sourceFile;
            return this;
        }


        public Builder chunkedRequestEntity(String chunkedRequestEntity) {
            this.chunkedRequestEntity = chunkedRequestEntity;
            return this;
        }


        public Builder region(String region) {
            this.region = region;
            return this;
        }


        public Builder language(String language) {
            this.language = language;
            return this;
        }


        public Builder letterSet(String letterSet) {
            this.letterSet = letterSet;
            return this;
        }


        public Builder regExp(String regExp) {
            this.regExp = regExp;
            return this;
        }


        public Builder textType(String textType) {
            this.textType = textType;
            return this;
        }


        public Builder oneTextLine(String oneTextLine) {
            this.oneTextLine = oneTextLine;
            return this;
        }


        public Builder oneWordPerTextLine(String oneWordPerTextLine) {
            this.oneWordPerTextLine = oneWordPerTextLine;
            return this;
        }


        public Builder markingType(String markingType) {
            this.markingType = markingType;
            return this;
        }


        public Builder placeholdersCount(String placeholdersCount) {
            this.placeholdersCount = placeholdersCount;
            return this;
        }


        public Builder writingStyle(String writingStyle) {
            this.writingStyle = writingStyle;
            return this;
        }


        public Builder description(String description) {
            this.description = description;
            return this;
        }


        public Builder pdfPassword(String pdfPassword) {
            this.pdfPassword = pdfPassword;
            return this;
        }


        public ProcessTextFieldInput build() throws Exception {
            //TODO
            return null;
        }
    }
}