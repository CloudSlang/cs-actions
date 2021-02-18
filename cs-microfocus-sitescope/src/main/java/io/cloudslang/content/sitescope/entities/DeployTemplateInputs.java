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
package io.cloudslang.content.sitescope.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class DeployTemplateInputs {
    private String pathToTemplate;
    private String pathToTargetGroup;
    private String connectToServer;
    private String testRemotes;
    private String customParameters;
    private String delimiter;
    private SiteScopeCommonInputs commonInputs;

    private DeployTemplateInputs(String pathToTemplate,String pathToTargetGroup, String connectToServer,
                                 String testRemotes,String customParameters, String delimiter, SiteScopeCommonInputs commonInputs){
        this.pathToTemplate = pathToTemplate;
        this.pathToTargetGroup = pathToTargetGroup;
        this.connectToServer = connectToServer;
        this.testRemotes = testRemotes;
        this.customParameters = customParameters;
        this.delimiter = delimiter;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static DeployTemplateInputsBuilder builder(){
        return new DeployTemplateInputsBuilder();
    }

    @NotNull
    public String getPathToTemplate() {
        return pathToTemplate;
    }

    @NotNull
    public String getPathToTargetGroup() {
        return pathToTargetGroup;
    }

    @NotNull
    public String getConnectToServer() {
        return connectToServer;
    }

    @NotNull
    public String getTestRemotes() {
        return testRemotes;
    }

    @NotNull
    public String getCustomParameters() {
        return customParameters;
    }

    @NotNull
    public String getDelimiter() {
        return delimiter;
    }

    @NotNull
    public SiteScopeCommonInputs getCommonInputs() {
        return commonInputs;
    }


    public static class DeployTemplateInputsBuilder{
        private String pathToTemplate = EMPTY;
        private String pathToTargetGroup = EMPTY;
        private String connectToServer = EMPTY;
        private String testRemotes = EMPTY;
        private String customParameters = EMPTY;
        private String delimiter = EMPTY;
        private SiteScopeCommonInputs commonInputs;
        
        public DeployTemplateInputsBuilder(){
            
        }
        
        @NotNull
        public DeployTemplateInputs.DeployTemplateInputsBuilder pathToTemplate(@NotNull final String pathToTemplate){
            this.pathToTemplate = pathToTemplate;
            return this;
        }

        @NotNull
        public DeployTemplateInputs.DeployTemplateInputsBuilder pathToTargetGroup(@NotNull final String pathToTargetGroup){
            this.pathToTargetGroup = pathToTargetGroup;
            return this;
        }

        @NotNull
        public DeployTemplateInputs.DeployTemplateInputsBuilder connectToServer(@NotNull final String connectToServer){
            this.connectToServer = connectToServer;
            return this;
        }

        @NotNull
        public DeployTemplateInputs.DeployTemplateInputsBuilder testRemotes(@NotNull final String testRemotes){
            this.testRemotes = testRemotes;
            return this;
        }

        @NotNull
        public DeployTemplateInputs.DeployTemplateInputsBuilder customParameters(@NotNull final String customParameters){
            this.customParameters = customParameters;
            return this;
        }

        @NotNull
        public DeployTemplateInputs.DeployTemplateInputsBuilder delimiter(@NotNull final String delimiter){
            this.delimiter = delimiter;
            return this;
        }

        @NotNull
        public DeployTemplateInputs.DeployTemplateInputsBuilder commonInputs(@NotNull final SiteScopeCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public DeployTemplateInputs build(){
            return new DeployTemplateInputs(pathToTemplate,pathToTargetGroup,connectToServer,testRemotes,customParameters,delimiter,commonInputs);
        }
    }
}
