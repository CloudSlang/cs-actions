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
                base64ImagesMap.put(contentId,
                        imageValue.substring(imageValue.indexOf("base64") + 7, imageValue.length()));
            }
        }
    }

    public String getContentId() {
        Random random = new Random();
        return Math.abs(random.nextInt(100000)) + "." + (100000000000L + Math.abs(random.nextInt()));
    }

    public Map<String, String> getBase64Images() {
        return base64ImagesMap;
    }
}
