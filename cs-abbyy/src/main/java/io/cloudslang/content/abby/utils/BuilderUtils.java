package io.cloudslang.content.abby.utils;

import org.apache.commons.lang3.StringUtils;

public final class BuilderUtils {
    private BuilderUtils(){
        
    }
    
    public static String buildDescription(String descr) {
        if (StringUtils.isNotEmpty(descr)) {
           return (descr.length() <= 255) ? descr : descr.substring(0, 255);
        } else {
            return StringUtils.EMPTY;
        }
    }
}
