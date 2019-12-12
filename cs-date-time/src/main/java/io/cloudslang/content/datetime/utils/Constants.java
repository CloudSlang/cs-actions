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



package io.cloudslang.content.datetime.utils;

/**
 * Created by stcu on 22.04.2016.
 */
public class Constants {
    public static class ErrorMessages {
        public static final String DATE_NULL_OR_EMPTY = "Date is either Null or Empty";
    }

    public static class Miscellaneous {
        public static final String GMT = "GMT";
        public static final String UNIX = "unix"; //seconds since January 1, 1970
        public static final String MILLISECONDS = "S"; //milliseconds since January 1, 1970; DateTimeFormat doesn't support it

        public static final int THOUSAND_MULTIPLIER = 1000;
    }
}