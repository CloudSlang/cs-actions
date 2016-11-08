package io.cloudslang.content.amazon.entities.aws;

/**
 * Created by TusaM
 * 11/4/2016.
 */
public enum AvailabilityZoneState {
    AVAILABLE,
    INFORMATION,
    IMPAIRED,
    UNAVAILABLE;

    public static String getValue(String input) throws RuntimeException {
        for (AvailabilityZoneState member : AvailabilityZoneState.values()) {
            if (member.name().equalsIgnoreCase(input)) {
                return member.name().toLowerCase();
            }
        }

        throw new RuntimeException("Invalid architecture value: [" + input + "]. Valid values: available, information, impaired, unavailable.");
    }
}