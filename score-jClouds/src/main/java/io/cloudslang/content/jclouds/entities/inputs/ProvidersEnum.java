package io.cloudslang.content.jclouds.entities.inputs;

/**
 * Created by persdana on 7/14/2015.
 */
public enum ProvidersEnum {
    OPENSTACK, AMAZON, OTHER;

    private static String provider;

    public static ProvidersEnum getProvider(String provider) {
        ProvidersEnum.provider = provider;
        try {
            return ProvidersEnum.valueOf(provider.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return OTHER;
        }
    }

    public static String getProviderStr() {
        return provider;
    }
}
