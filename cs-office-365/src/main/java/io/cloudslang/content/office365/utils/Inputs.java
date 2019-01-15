/*
 * (c) Copyright 2019 Micro Focus, L.P.
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
package io.cloudslang.content.office365.utils;

import io.cloudslang.content.constants.InputNames;

public final class Inputs extends InputNames {
    public static class CommonInputs {
        public static final String PROXY_HOST = "proxyHost";
        public static final String PROXY_PORT = "proxyPort";
        public static final String PROXY_USERNAME = "proxyUsername";
        public static final String PROXY_PASSWORD = "proxyPassword";
    }

    public static class AuthorizationInputs {
        public static final String LOGIN_TYPE = "loginType";
        public static final String CLIENT_ID = "clientId";
        public static final String CLIENT_SECRET = "clientSecret";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String LOGIN_AUTHORITY = "loginAuthority";
        public static final String RESOURCE = "resource";
    }

    public static class EmailInputs {
        public static final String AUTH_TOKEN = "authToken";
        public static final String USER_PRINCIPAL_NAME = "userPrincipalName";
        public static final String USER_ID = "userId";
        public static final String MESSAGE_ID = "messageId";
        public static final String FOLDER_ID = "folderId";
        public static final String O_DATA_QUERY = "oDataQuery";
    }

    public static class CreateMessage {
        public static final String BCC_RECIPIENTS = "bccRecipients";
        public static final String CATEGORIES = "categories";
        public static final String CC_RECIPIENTS = "ccRecipients";
        public static final String FROM = "from";
        public static final String IMPORTANCE ="importance";
        public static final String INFERENCE_CLASSIFICATION = "inferenceClassification";
        public static final String INTERNET_MESSAGE_ID = "internetMessageId";
        public static final String IS_READ ="isRead";
        public static final String REPLY_TO = "replyTo";
        public static final String SENDER = "sender";
        public static final String TO_RECIPIENTS = "toRecipients";
        public static final String BODY = "body";
        public static final String IS_DELIVERY_RECEIPT_REQUESTED = "isDeliveryReceiptRequested";
        public static final String IS_READ_RECEIPT_REQUESTED = "isReadReceiptRequested";
        public static final String SUBJECT = "subject";
    }

    public static class CreateUser {
        public static final String ACCOUNT_ENABLED = "accountEnabled";
        public static final String DISPLAY_NAME = "displayName";
        public static final String ON_PREMISES_IMMUTABLE_ID = "onPremisesImmutableId";
        public static final String MAIL_NICKNAME = "mailNickname";
        public static final String FORCE_CHANGE_PASSWORD = "forceChangePasswordNextSignIn";
    }

    public static class GetUser {
        public static final String USER_PRINCIPAL_NAME_OUTPUT = "userPrincipalName";
        public static final String ID_OUTPUT = "id";
    }
}

