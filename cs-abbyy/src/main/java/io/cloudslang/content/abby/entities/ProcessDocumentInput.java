/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cloudslang.content.abby.entities;

import io.cloudslang.content.abby.constants.ExceptionMsgs;
import io.cloudslang.content.abby.utils.BuilderUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProcessDocumentInput implements AbbyyRequest {
    private LocationId locationId;
    private String applicationId;
    private String password;
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
    private boolean keepAlive;
    private int connectionsMaxPerRoute;
    private int connectionsMaxTotal;
    private String responseCharacterSet;
    private File destinationFile;
    private File sourceFile;
    private List<String> languages;
    private Profile profile;
    private List<TextType> textTypes;
    private ImageSource imageSource;
    private boolean correctOrientation;
    private boolean correctSkew;
    private boolean readBarcodes;
    private List<ExportFormat> exportFormats;
    private boolean writeFormatting;
    private boolean writeRecognitionVariants;
    private WriteTags writeTags;
    private String description;
    private String pdfPassword;


    private ProcessDocumentInput() {
    }


    public LocationId getLocationId() {
        return locationId;
    }


    public String getApplicationId() {
        return applicationId;
    }


    public String getPassword() {
        return password;
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


    public boolean isKeepAlive() {
        return keepAlive;
    }


    public int getConnectionsMaxPerRoute() {
        return connectionsMaxPerRoute;
    }


    public int getConnectionsMaxTotal() {
        return connectionsMaxTotal;
    }


    public String getResponseCharacterSet() {
        return responseCharacterSet;
    }


    public File getDestinationFile() {
        return destinationFile;
    }


    public File getSourceFile() {
        return sourceFile;
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


    public List<ExportFormat> getExportFormats() {
        return exportFormats;
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
        private String keepAlive;
        private String connectionsMaxPerRoute;
        private String connectionsMaxTotal;
        private String responseCharacterSet;
        private String destinationFile;
        private String sourceFile;
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


        public ProcessDocumentInput build() throws Exception {
            ProcessDocumentInput input = new ProcessDocumentInput();

            input.locationId = LocationId.fromString(this.locationId);

            input.applicationId = this.applicationId;

            input.password = this.password;

            input.proxyHost = this.proxyHost;

            input.proxyPort = Short.parseShort(this.proxyPort);

            input.proxyUsername = this.proxyUsername;

            input.proxyPassword = this.proxyPassword;

            input.trustAllRoots = Boolean.parseBoolean(this.trustAllRoots);

            input.x509HostnameVerifier = this.x509HostnameVerifier;

            input.trustKeystore = this.trustKeystore;

            input.trustPassword = this.trustPassword;

            input.connectTimeout = Integer.parseInt(this.connectTimeout);

            input.socketTimeout = Integer.parseInt(this.socketTimeout);

            input.keepAlive = Boolean.parseBoolean(this.keepAlive);

            input.connectionsMaxPerRoute = Integer.parseInt(this.connectionsMaxPerRoute);

            input.connectionsMaxTotal = Integer.parseInt(this.connectionsMaxTotal);

            input.responseCharacterSet = this.responseCharacterSet;

            if(StringUtils.isNotEmpty(this.destinationFile)) {
                input.destinationFile = new File(this.destinationFile);
                if (!input.destinationFile.isDirectory()) {
                    throw new Exception(ExceptionMsgs.DESTINATION_FILE_IS_NOT_DIRECTORY);
                }
            }

            input.sourceFile = new File(this.sourceFile);
            if(!input.sourceFile.isFile()) {
                throw new Exception(ExceptionMsgs.SOURCE_FILE_IS_NOT_FILE);
            }

            String[] languages = this.language.split("[,]");
            input.languages = Arrays.asList(languages);

            input.profile = Profile.fromString(this.profile);

            String[] textTypes = this.textType.split("[,]");
            input.textTypes = new ArrayList<>();
            for (String textType : textTypes) {
                input.textTypes.add(TextType.fromString(textType));
            }

            input.imageSource = ImageSource.fromString(this.imageSource);

            input.correctOrientation = Boolean.parseBoolean(this.correctOrientation);

            input.correctSkew = Boolean.parseBoolean(this.correctSkew);

            String[] exportFormats = this.exportFormat.split("[,]");
            if (exportFormats.length > 3) {
                throw new Exception(ExceptionMsgs.TOO_MANY_EXPORT_FORMATS);
            }
            input.exportFormats = new ArrayList<>();
            for (String exportFormat : exportFormats) {
                input.exportFormats.add(ExportFormat.fromString(exportFormat));
            }

            input.writeFormatting = Boolean.parseBoolean(this.writeFormatting);

            input.writeRecognitionVariants = Boolean.parseBoolean(this.writeRecognitionVariants);

            input.writeTags = WriteTags.fromString(this.writeTags);

            input.readBarcodes = Boolean.parseBoolean(this.readBarcodes);

            input.description = BuilderUtils.buildDescription(this.description);

            input.pdfPassword = StringUtils.defaultString(this.pdfPassword);

            return input;
        }
    }
}
