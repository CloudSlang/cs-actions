package io.cloudslang.content.alibaba.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;

public class ClientUtil {
    public static IAcsClient getClient(final String regionId, final String accessKey, final String accessKeySecret) {
        final DefaultProfile profile = DefaultProfile.getProfile(
                regionId,
                accessKey,
                accessKeySecret);
        return new DefaultAcsClient(profile);
    }
}
