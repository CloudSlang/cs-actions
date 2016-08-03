package io.cloudslang.content.xml.services;

import java.io.InputStream;
import java.util.Properties;

public final class PropsLoader {

    public static final Properties RESPONSES = PropsLoader.getProps("Responses");
    public static final Properties RESULTS = PropsLoader.getProps("Results");
    public static final Properties RESPONSE_DESCRIPTIONS = PropsLoader.getProps("ResponseDescriptions");
    public static final Properties INPUT_DESCRIPTIONS = PropsLoader.getProps("InputDescriptions");
    public static final Properties NOTES = PropsLoader.getProps("Notes");
    public static final Properties INPUTS = PropsLoader.getProps("Inputs");
    public static final Properties OPERATORS = PropsLoader.getProps("Operators");
    public static final Properties EXCEPTIONS = PropsLoader.getProps("ExceptionMessages");

    private static Properties getProps(String strPropType) {
        Properties props = new Properties();
        InputStream resourceStream = null;
        try {
            resourceStream = PropsLoader.class.getResourceAsStream("/Properties/" + strPropType + ".properties");
            if (resourceStream != null)
                props.load(resourceStream);
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (resourceStream != null) {
                try {
                    resourceStream.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
        return props;
    }

}
