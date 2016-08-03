package io.cloudslang.content.xml.services;

/**
 * Converts to/from XML to another format.
 *
 * @author hasna
 * @since 1.0.135-SNAPSHOT
 */
public interface XmlConverter {
    /**
     * Default name for JSON array items.
     */
    String JSON_ARRAY_ITEM_NAME = "item";
    /**
     * Default name for root JSON object.
     */
    String ROOT_TAG_NAME = "root";

    /**
     * Convert JSON string to XML string (XML document or a list of XML elements)
     *
     * @param json the JSON string
     * @return the XML string
     */
    String convertToXmlString(String json);
}
