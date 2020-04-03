package io.cloudslang.content.abby.entities;

import java.util.List;

public class ProcessImageInput {
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
    private List<String> languages;
    private Profile profile;
    private List<TextType> textTypes;
    private ImageSource imageSource;
    private boolean correctOrientation;
    private boolean correctSkew;
    private boolean readBarcodes;
    private ExportFormat exportFormat;
    private boolean writeFormatting;
    private boolean writeRecognitionVariants;
    private WriteTags writeTags;
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


    public List<String> getLanguages() {
        return languages;
    }


    public Profile getProfile() {
        return profile;
    }


    public List<TextType> getTextTypes() {
        return textTypes;
    }


    public ImageSource getImageSource() {
        return imageSource;
    }


    public boolean isCorrectOrientation() {
        return correctOrientation;
    }


    public boolean isCorrectSkew() {
        return correctSkew;
    }


    public boolean isReadBarcodes() {
        return readBarcodes;
    }


    public ExportFormat getExportFormat() {
        return exportFormat;
    }


    public boolean isWriteFormatting() {
        return writeFormatting;
    }


    public boolean isWriteRecognitionVariants() {
        return writeRecognitionVariants;
    }


    public WriteTags getWriteTags() {
        return writeTags;
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
        private String language;
        private String profile;
        private String textType;
        private String imageSource;
        private String correctOrientation;
        private String correctSkew;
        private String readBarcodes;
        private String exportFormat;
        private String writeFormatting;
        private String writeRecognitionVariants;
        private String writeTags;
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


        public Builder language(String language) {
            this.language = language;
            return this;
        }


        public Builder profile(String profile) {
            this.profile = profile;
            return this;
        }


        public Builder textType(String textType) {
            this.textType = textType;
            return this;
        }


        public Builder imageSource(String imageSource) {
            this.imageSource = imageSource;
            return this;
        }


        public Builder correctOrientation(String correctOrientation) {
            this.correctOrientation = correctOrientation;
            return this;
        }


        public Builder correctSkew(String correctSkew) {
            this.correctSkew = correctSkew;
            return this;
        }


        public Builder readBarcodes(String readBarcodes) {
            this.readBarcodes = readBarcodes;
            return this;
        }


        public Builder exportFormat(String exportFormat) {
            this.exportFormat = exportFormat;
            return this;
        }


        public Builder writeFormatting(String writeFormatting) {
            this.writeFormatting = writeFormatting;
            return this;
        }


        public Builder writeRecognitionVariants(String writeRecognitionVariants) {
            this.writeRecognitionVariants = writeRecognitionVariants;
            return this;
        }


        public Builder writeTags(String writeTags) {
            this.writeTags = writeTags;
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


        public ProcessImageInput build() throws Exception {
            //TODO
            return null;
        }
    }
}
