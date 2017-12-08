package io.cloudslang.content.utilities.services.osdetector;

import io.cloudslang.content.utilities.entities.OperatingSystemDetails;
import io.cloudslang.content.utilities.entities.OsDetectorInputs;

import java.io.IOException;

/**
 * Created by Tirla Florin-Alin on 28/11/2017.
 **/
public interface OsDetectorService {
    OperatingSystemDetails detect(OsDetectorInputs nmapInputs);
}
