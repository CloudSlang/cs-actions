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


package io.cloudslang.content.json.utils;

/**
 * Created by vranau
 * Date 2/17/2015.
 */
public enum ActionsEnum {
    get(Constants.EditJsonOperations.GET_ACTION, false),
    insert(Constants.EditJsonOperations.INSERT_ACTION, true),
    add(Constants.EditJsonOperations.ADD_ACTION, true),
    update(Constants.EditJsonOperations.UPDATE_ACTION, true),
    delete(Constants.EditJsonOperations.DELETE_ACTION, false);
    private final Boolean needValue;
    private final String value;


    ActionsEnum(String value, Boolean needValue) {
        this.value = value;
        this.needValue = needValue;
    }


    public String getValue() {
        return value;
    }


    public Boolean getNeedValue() {
        return needValue;
    }
}
