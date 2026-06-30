/*
 * Copyright 2022-2024 Open Text
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





package io.cloudslang.content.httpclient.build;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.NameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class URIBuilder {
    private String url;
    private String queryParams;
    private String queryParamsAreURLEncoded = "false";
    private String queryParamsAreFormEncoded = "true";

    public URIBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public URIBuilder setQueryParams(String queryParams) {
        this.queryParams = queryParams;
        return this;
    }

    public URIBuilder setQueryParamsAreURLEncoded(String queryParamsAreURLEncoded) {
        if (!StringUtils.isBlank(queryParamsAreURLEncoded)) {
            this.queryParamsAreURLEncoded = queryParamsAreURLEncoded;
        }
        return this;
    }

    public URIBuilder setQueryParamsAreFormEncoded(String queryParamsAreFormEncoded) {
        if (!StringUtils.isBlank(queryParamsAreFormEncoded)) {
            this.queryParamsAreFormEncoded = queryParamsAreFormEncoded;
        }
        return this;
    }

    public URI buildURI() {
        try {
            //validate as URL
            new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("the value '" + url + "' is not a valid URL", e);
        }
        org.apache.hc.core5.net.URIBuilder uriBuilder;
        try {
            uriBuilder = new org.apache.hc.core5.net.URIBuilder(url);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("the value '" + url + "' is not a valid URI", e);
        }

        boolean bEncodeQueryParams = !Boolean.parseBoolean(queryParamsAreURLEncoded);
        boolean bEncodeQueryParamsAsForm = Boolean.parseBoolean(queryParamsAreFormEncoded);

        if (!StringUtils.isEmpty(queryParams)) {
            if (bEncodeQueryParamsAsForm) {
                List<? extends NameValuePair> params;
                try {
                    params = Utils.urlEncodeMultipleParams(queryParams, bEncodeQueryParams);
                } catch (IllegalArgumentException ie) {
                    throw new IllegalArgumentException(
                            HttpClientInputs.QUERY_PARAMS_ARE_URLENCODED +
                                    " is 'false' but queryParams are not properly encoded. "
                                    + ie.getMessage(), ie);
                }
                // Build form-encoded query string: spaces as '+', other special chars as %XX.
                // We cannot use uriBuilder.addParameter() because newer httpcore5 encodes spaces as %20,
                // and we cannot use setCustomQuery() with pre-encoded strings because it re-encodes '%' to '%25'.
                StringBuilder formQuery = new StringBuilder();
                for (NameValuePair param : params) {
                    if (formQuery.length() > 0) {
                        formQuery.append('&');
                    }
                    try {
                        formQuery.append(URLEncoder.encode(param.getName(), Utils.DEFAULT_CHARACTER_SET));
                        formQuery.append('=');
                        if (param.getValue() != null) {
                            formQuery.append(URLEncoder.encode(param.getValue(), Utils.DEFAULT_CHARACTER_SET));
                        }
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e); // UTF-8 is always supported
                    }
                }
                try {
                    return new URI(uriBuilder.build().toASCIIString() + "?" + formQuery);
                } catch (URISyntaxException e) {
                    throw new IllegalArgumentException("could not build '" + HttpClientInputs.URL
                            + "' for " + url + " and queries " + queryParamsAreURLEncoded, e);
                }
            } else {
                String rawQuery;
                try {
                    rawQuery = Utils.urlEncodeQueryParams(queryParams, bEncodeQueryParams);
                } catch (IllegalArgumentException ie) {
                    throw new IllegalArgumentException(
                            HttpClientInputs.QUERY_PARAMS_ARE_URLENCODED +
                                    " is 'false' but queryParams are not properly encoded. "
                                    + ie.getMessage(), ie);
                }
                // Use the multi-arg URI constructor so that only truly illegal characters (space,
                // non-ASCII, bare %) are percent-encoded, while valid query characters such as
                // '/', '?', ':', '@' are preserved — setCustomQuery() in newer httpcore5 over-encodes these.
                // Then re-parse via toASCIIString() so that uri.toString() also returns the ASCII form
                // (otherwise non-ASCII chars like ü appear as literals in toString() instead of %C3%BC).
                try {
                    URI baseUri = uriBuilder.build();
                    URI withEncodedQuery = new URI(baseUri.getScheme(), baseUri.getAuthority(),
                            baseUri.getPath() != null ? baseUri.getPath() : "",
                            rawQuery, baseUri.getFragment());
                    return new URI(withEncodedQuery.toASCIIString());
                } catch (URISyntaxException e) {
                    throw new IllegalArgumentException("could not build '" + HttpClientInputs.URL
                            + "' for " + url + " and queries " + queryParamsAreURLEncoded, e);
                }
            }
        }

        try {
            return uriBuilder.build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("could not build '" + HttpClientInputs.URL
                    + "' for " + url + " and queries " + queryParamsAreURLEncoded, e);
        }

    }
}