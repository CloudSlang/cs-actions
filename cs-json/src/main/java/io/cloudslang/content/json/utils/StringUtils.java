/*
 * (c) Copyright 2021 EntIT Software LLC, a Micro Focus company, L.P.
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

import static io.cloudslang.content.constants.OtherValues.EMPTY_STRING;

/**
 * Created by Folea Ilie Cristian on 2/5/2016.
 */
public class StringUtils {
    public static boolean isEmpty(Object val) {
        return (val == null) || (EMPTY_STRING.equals(val));
    }


}
