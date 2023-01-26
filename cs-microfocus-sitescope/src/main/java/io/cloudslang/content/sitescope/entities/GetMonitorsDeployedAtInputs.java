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

public class GetMonitorsDeployedAtInputs {
    private String targetServer;
    private String colDelimiter;
    private String rowDelimiter;
    private SiteScopeCommonInputs commonInputs;
    
    private GetMonitorsDeployedAtInputs(String targetServer, String colDelimiter, String rowDelimiter,
                                        SiteScopeCommonInputs commonInputs){
        this.targetServer = targetServer;
        this.colDelimiter = colDelimiter;
        this.rowDelimiter = rowDelimiter;
        this.commonInputs = commonInputs;
    }
    
    @NotNull
    public static GetMonitorsDeployedAtInputsBuilder builder(){
        return new GetMonitorsDeployedAtInputsBuilder();
    }

    @NotNull
    public String getTargetServer() {
        return targetServer;
    }

    @NotNull
    public String getColDelimiter() {
        return colDelimiter;
    }

    @NotNull
    public String getRowDelimiter() {
        return rowDelimiter;
    }

    @NotNull
    public SiteScopeCommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static class GetMonitorsDeployedAtInputsBuilder{
        private String targetServer;
        private String colDelimiter;
        private String rowDelimiter;
        private SiteScopeCommonInputs commonInputs;
        
        public GetMonitorsDeployedAtInputsBuilder(){}
        
        @NotNull
        public GetMonitorsDeployedAtInputs.GetMonitorsDeployedAtInputsBuilder targetServer(@NotNull final String targetServer){
            this.targetServer = targetServer;
            return this;
        }

        @NotNull
        public GetMonitorsDeployedAtInputs.GetMonitorsDeployedAtInputsBuilder colDelimiter(@NotNull final String colDelimiter){
            this.colDelimiter = colDelimiter;
            return this;
        }

        @NotNull
        public GetMonitorsDeployedAtInputs.GetMonitorsDeployedAtInputsBuilder rowDelimiter(@NotNull final String rowDelimiter){
            this.rowDelimiter = rowDelimiter;
            return this;
        }

        @NotNull
        public GetMonitorsDeployedAtInputs.GetMonitorsDeployedAtInputsBuilder commonInputs(@NotNull final SiteScopeCommonInputs commonInputs){
            this.commonInputs = commonInputs;
            return this;
        }

        public GetMonitorsDeployedAtInputs build(){
            return new GetMonitorsDeployedAtInputs(targetServer,colDelimiter,rowDelimiter,commonInputs);
        }
    }
}
