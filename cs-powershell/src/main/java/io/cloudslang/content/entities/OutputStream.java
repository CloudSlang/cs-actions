/*
 * Copyright 2019-2024 Open Text
 * This program and the accompanying materials
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




package io.cloudslang.content.entities;

/**
 * Created by giloan on 6/7/2016.
 */
public enum OutputStream {
    STDOUT("stdout"),
    STDERR("stderr");

    private String value;

    OutputStream(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
