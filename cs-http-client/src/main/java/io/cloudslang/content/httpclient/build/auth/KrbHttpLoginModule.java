/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.httpclient.build.auth;

import com.sun.security.auth.module.Krb5LoginModule;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 9/23/14
 */
public class KrbHttpLoginModule extends Krb5LoginModule {
    static String USR = "javax.security.auth.login.name";
    static String PAS = "javax.security.auth.login.password";

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        super.initialize(subject, callbackHandler, sharedState, options);    //To change body of overridden methods use File | Settings | File Templates.
        Map<String, Object> myss = (Map<String, Object>) sharedState;
        myss.put(USR, System.getProperty(USR));
        myss.put(PAS, System.getProperty(PAS).toCharArray());
        System.clearProperty(USR);
        System.clearProperty(PAS);
    }
}
