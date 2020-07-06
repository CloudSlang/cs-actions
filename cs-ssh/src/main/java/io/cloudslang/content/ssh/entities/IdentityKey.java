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



package io.cloudslang.content.ssh.entities;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * User: sacalosb
 * Date: 07.01.2016
 */
public abstract class IdentityKey {
    public static final Charset KEY_ENCODING = Charset.forName("UTF-8");
    protected byte[] passPhrase;

    public byte[] getPassPhrase() {
        return (passPhrase == null) ? null : Arrays.copyOf(passPhrase, passPhrase.length);
    }

    public void setPassPhrase(byte[] passPhrase) {
        this.passPhrase = (passPhrase == null) ? null : Arrays.copyOf(passPhrase, passPhrase.length);
    }

    public void setPassPhrase(String passPhrase) {
        this.passPhrase = (passPhrase == null) ? null : passPhrase.getBytes(KEY_ENCODING);
    }
}
