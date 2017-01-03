/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.mail.utils;

import org.htmlparser.Tag;
import org.htmlparser.visitors.NodeVisitor;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * User: bancl
 * Date: 4/15/2015
 */
public class HtmlImageNodeVisitor extends NodeVisitor {
    private Map<String,String> base64ImagesMap = new HashMap<>();

    public HtmlImageNodeVisitor() {
    }

    public void visitTag(Tag tag) {
        if (tag.getRawTagName().equalsIgnoreCase("img")) {
            String imageValue = tag.getAttribute("src");

            if (imageValue.contains("base64")) {
                String contentId = getContentId();
                tag.setAttribute("src", "cid:" + contentId);
                base64ImagesMap.put(contentId, imageValue.substring(imageValue.indexOf("base64") + 7, imageValue.length()));
            }
        }
    }

    public String getContentId() {
        return new Random(System.currentTimeMillis()).nextInt(100000) + "." + System.currentTimeMillis();
    }

    public Map<String, String> getBase64Images() {
        return base64ImagesMap;
    }
}
