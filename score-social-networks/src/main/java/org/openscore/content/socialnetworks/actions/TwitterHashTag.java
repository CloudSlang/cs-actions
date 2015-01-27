package org.openscore.content.socialnetworks.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import org.openscore.content.socialnetworks.utils.Constants;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vranau on 1/27/2015.
 *
 */
public class TwitterHashTag {

    public static final int MAX_ALLOWED_COUNT = 100;
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    @Action(name = "TwitterHashTag",
            outputs = {
                    @Output(Constants.OutputNames.RETURN_CODE),
                    @Output(Constants.OutputNames.RETURN_RESULT),
                    @Output(Constants.OutputNames.EXCEPTION),
                    @Output(Constants.STDOUT),
                    @Output(Constants.STDERR)
            },
            responses = {
                    @Response(text = Constants.ResponseNames.SUCCESS, field = Constants.OutputNames.RETURN_CODE, value = Constants.ReturnCodes.RETURN_CODE_SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Constants.ResponseNames.FAILURE, field = Constants.OutputNames.RETURN_CODE, value = Constants.ReturnCodes.RETURN_CODE_FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            }
    )
    public Map<String, String> execute(
            @Param(value = Constants.InputNames.HASHTAG, required = true) String hashTag,
            @Param(value = Constants.InputNames.START_DATE) String startDate,
            @Param(value = Constants.InputNames.CONSUMER_KEY_STR, required = true) String consumerKeyStr,
            @Param(value = Constants.InputNames.CONSUMER_SECRET_STR, required = true) String consumerSecretStr,
            @Param(value = Constants.InputNames.ACCESS_TOKEN_STR, required = true) String accessTokenStr,
            @Param(value = Constants.InputNames.ACCESS_TOKEN_SECRET_STR, required = true) String accessTokenSecretStr,
            @Param(value = Constants.InputNames.PROXY_HOST) String proxyHost,
            @Param(value = Constants.InputNames.PROXY_PORT) String proxyPort,
            @Param(value = Constants.InputNames.PROXY_USERNAME) String proxyUsername,
            @Param(value = Constants.InputNames.PROXY_PASSWORD) String proxyPassword)
    {
        Map<String, String> returnResult = new HashMap<>();
        int httpProxyPort = 8080;

        httpProxyPort = Integer.parseInt(proxyPort);

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setHttpProxyHost(proxyHost)
                .setHttpProxyPort(httpProxyPort)
                .setHttpRetryCount(4)
                .setHttpProxyUser(proxyUsername)
                .setHttpProxyPassword(proxyPassword)
                .setOAuthConsumerKey(consumerKeyStr)
                .setOAuthConsumerSecret(consumerSecretStr)
                .setOAuthAccessToken(accessTokenStr)
                .setOAuthAccessTokenSecret(accessTokenSecretStr);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        Query query = new Query(hashTag);
        query.setSince(startDate);

        query.count(MAX_ALLOWED_COUNT);
        QueryResult result = null;

        try {
            StringBuilder allHashTags = new StringBuilder();
            result = twitter.search(query);
            final List<Status> tweets = result.getTweets();
            for (Status status : tweets) {
                final Date createdAt = status.getCreatedAt();

                allHashTags.append("@").append(status.getUser().getScreenName())
                        .append(":")
                        .append(status.getText()).append(System.getProperty("line.separator"));

                allHashTags.append("Created at: ")
                        .append(createdAt).
                        append(LINE_SEPARATOR);
                allHashTags.append("=======================================================")
                        .append("\n");
            }

            if (tweets.size() > 0) {
                returnResult.put(Constants.OutputNames.RETURN_RESULT, allHashTags.toString() );
                returnResult.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_SUCCESS);
            } else {
                returnResult.put(Constants.OutputNames.RETURN_RESULT, "No tweets with " + hashTag + " for the given date!");
                returnResult.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_SUCCESS);
            }

        } catch (Exception e) {
            returnResult.put(Constants.OutputNames.RETURN_RESULT, e.getMessage());
            returnResult.put(Constants.OutputNames.EXCEPTION, e.getMessage());
            returnResult.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_FAILURE);
        }

        return returnResult;
    }
}
