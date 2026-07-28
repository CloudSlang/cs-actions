/*
 * Copyright 2021-2024 Open Text
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





package io.cloudslang.content.mail.utils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * User: bancl
 * Date: 4/15/2015
 */
public class HtmlImageNodeVisitor {
    private static final String BASE64 = "base64";

    private Map<String,String> base64ImagesMap = new HashMap<>();

    public HtmlImageNodeVisitor() {
    }

    public void visitDocument(Document document) {
        for (Element img : document.select("img")) {
            String imageValue = img.attr("src");

            if (imageValue.contains(BASE64)) {
                String contentId = getContentId();
                img.attr("src", "cid:" + contentId);
                base64ImagesMap.put(contentId,
                        imageValue.substring(imageValue.indexOf(BASE64) + 7));
            }
        }
    }

    public String getContentId() {
        final Random random = new Random();
        final long nextInt = random.nextInt();
        return Math.abs(random.nextInt(100000)) + "." + (100000000000L + Math.abs(nextInt));
    }

    public Map<String, String> getBase64Images() {
        return base64ImagesMap;
    }
}
