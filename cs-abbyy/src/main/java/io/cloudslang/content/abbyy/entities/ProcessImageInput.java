/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.abbyy.entities;

import io.cloudslang.content.abbyy.constants.InputNames;
import io.cloudslang.content.abbyy.http.AbbyyRequest;
import io.cloudslang.content.abbyy.utils.InputParser;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProcessImageInput implements AbbyyRequest {
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
    private Boolean readBarcodes;
    private List<ExportFormat> exportFormats;
    private boolean writeFormatting;
    private boolean writeRecognitionVariants;
    private WriteTags writeTags;
    private String description;
    private String pdfPassword;


    private ProcessImageInput() {
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


    public Boolean isReadBarcodes() {
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
        private String destinationFolder;
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


        public Builder destinationFolder(String destinationFile) {
            this.destinationFolder = destinationFile;
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


        public ProcessImageInput build() throws Exception {
            ProcessImageInput input = new ProcessImageInput();

            input.locationId = InputParser.parseEnum(this.locationId, LocationId.class, InputNames.LOCATION_ID);

            input.applicationId = this.applicationId;

            input.password = this.password;

            input.proxyHost = this.proxyHost;

            input.proxyPort = InputParser.parseProxyPort(this.proxyPort);

            input.proxyUsername = this.proxyUsername;

            input.proxyPassword = this.proxyPassword;

            input.trustAllRoots = InputParser.parseBoolean(this.trustAllRoots, HttpClientInputs.TRUST_ALL_ROOTS);

            input.x509HostnameVerifier = this.x509HostnameVerifier;

            input.trustKeystore = this.trustKeystore;

            input.trustPassword = this.trustPassword;

            input.connectTimeout = InputParser.parseNonNegativeInt(this.connectTimeout, HttpClientInputs.CONNECT_TIMEOUT);

            input.socketTimeout = InputParser.parseNonNegativeInt(this.socketTimeout, HttpClientInputs.SOCKET_TIMEOUT);

            input.keepAlive = InputParser.parseBoolean(this.keepAlive, HttpClientInputs.KEEP_ALIVE);

            input.connectionsMaxPerRoute = InputParser.parseNonNegativeInt(this.connectionsMaxPerRoute, HttpClientInputs.CONNECTIONS_MAX_PER_ROUTE);

            input.connectionsMaxTotal = InputParser.parseNonNegativeInt(this.connectionsMaxTotal, HttpClientInputs.CONNECTIONS_MAX_TOTAL);

            input.responseCharacterSet = this.responseCharacterSet;

            if(StringUtils.isNotEmpty(this.destinationFolder)) {
                input.destinationFile = new File(this.destinationFolder);
            }

            input.sourceFile = new File(this.sourceFile);

            String[] languages = this.language.split("[,]");
            input.languages = Arrays.asList(languages);

            input.profile = InputParser.parseEnum(this.profile, Profile.class, InputNames.PROFILE);

            String[] textTypes = this.textType.split("[,]");
            input.textTypes = new ArrayList<>();
            for (String textType : textTypes) {
                input.textTypes.add(InputParser.parseEnum(textType, TextType.class, InputNames.TEXT_TYPE));
            }

            input.imageSource = InputParser.parseEnum(this.imageSource, ImageSource.class, InputNames.IMAGE_SOURCE);

            input.correctOrientation = InputParser.parseBoolean(this.correctOrientation, InputNames.CORRECT_ORIENTATION);

            input.correctSkew = InputParser.parseBoolean(this.correctSkew, InputNames.CORRECT_SKEW);

            String[] exportFormats = this.exportFormat.split("[,]");
            input.exportFormats = new ArrayList<>();
            for (String exportFormat : exportFormats) {
                input.exportFormats.add(InputParser.parseEnum(exportFormat, ExportFormat.class, InputNames.EXPORT_FORMAT));
            }

            input.writeFormatting = InputParser.parseBoolean(this.writeFormatting, InputNames.WRITE_FORMATTING);

            input.writeRecognitionVariants = InputParser.parseBoolean(this.writeRecognitionVariants, InputNames.WRITE_RECOGNITION_VARIANTS);

            input.writeTags = InputParser.parseEnum(this.writeTags, WriteTags.class, InputNames.WRITE_TAGS);

            if(StringUtils.isNotEmpty(this.readBarcodes)) {
                input.readBarcodes = InputParser.parseBoolean(this.readBarcodes, InputNames.READ_BARCODES);
            }

            input.description = InputParser.parseDescription(this.description);

            input.pdfPassword = StringUtils.defaultString(this.pdfPassword);

            return input;
        }
    }
}
