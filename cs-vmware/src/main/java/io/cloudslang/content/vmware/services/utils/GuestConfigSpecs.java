/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.services.utils;

import com.vmware.vim25.CustomizationAdapterMapping;
import com.vmware.vim25.CustomizationDhcpIpGenerator;
import com.vmware.vim25.CustomizationFixedIp;
import com.vmware.vim25.CustomizationFixedName;
import com.vmware.vim25.CustomizationGlobalIPSettings;
import com.vmware.vim25.CustomizationGuiUnattended;
import com.vmware.vim25.CustomizationIPSettings;
import com.vmware.vim25.CustomizationIdentification;
import com.vmware.vim25.CustomizationLicenseDataMode;
import com.vmware.vim25.CustomizationLicenseFilePrintData;
import com.vmware.vim25.CustomizationLinuxPrep;
import com.vmware.vim25.CustomizationPassword;
import com.vmware.vim25.CustomizationSpec;
import com.vmware.vim25.CustomizationSysprep;
import com.vmware.vim25.CustomizationSysprepRebootOption;
import com.vmware.vim25.CustomizationUserData;
import com.vmware.vim25.CustomizationWinOptions;
import io.cloudslang.content.vmware.constants.ErrorMessages;
import io.cloudslang.content.vmware.entities.GuestInputs;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by Mihai Tusa.
 * 3/21/2016.
 */
public class GuestConfigSpecs {
    private static final String PER_SERVER = "perServer";

    public CustomizationSpec getWinCustomizationSpec(GuestInputs guestInputs) {
        CustomizationSpec customizationSpec = new CustomizationSpec();

        CustomizationSysprep customizationSysprep = getCustomizationSysprep(guestInputs);
        customizationSpec.setIdentity(customizationSysprep);

        CustomizationWinOptions winOptions = getCustomizationWinOptions(guestInputs);
        customizationSpec.setOptions(winOptions);

        customizationSpec.setGlobalIPSettings(new CustomizationGlobalIPSettings());

        setAdapter(guestInputs, customizationSpec);

        return customizationSpec;
    }

    public CustomizationSpec getLinuxCustomizationSpec(GuestInputs guestInputs) {
        CustomizationSpec customizationSpec = new CustomizationSpec();

        CustomizationLinuxPrep customizationLinuxPrep = getCustomizationLinuxPrep(guestInputs);
        customizationSpec.setIdentity(customizationLinuxPrep);

        CustomizationGlobalIPSettings customizationGlobalIPSettings = new CustomizationGlobalIPSettings();
        customizationSpec.setGlobalIPSettings(customizationGlobalIPSettings);

        setAdapter(guestInputs, customizationSpec);

        return customizationSpec;
    }

    private CustomizationLinuxPrep getCustomizationLinuxPrep(GuestInputs guestInputs) {
        CustomizationLinuxPrep customizationLinuxPrep = new CustomizationLinuxPrep();
        customizationLinuxPrep.setDomain(guestInputs.getDomain());
        customizationLinuxPrep.setTimeZone(String.valueOf(guestInputs.getTimeZone()));

        CustomizationFixedName computerName = new CustomizationFixedName();
        computerName.setName(guestInputs.getComputerName());
        customizationLinuxPrep.setHostName(computerName);

        if (guestInputs.isHwClockUTC()) {
            customizationLinuxPrep.setHwClockUTC(true);
        } else {
            customizationLinuxPrep.setHwClockUTC(false);
        }

        return customizationLinuxPrep;
    }

    private CustomizationSysprep getCustomizationSysprep(GuestInputs guestInputs) {
        CustomizationSysprep customizationSysprep = getPopulatedCustomizationSysprep(guestInputs);

        CustomizationUserData userData = getCustomizationUserData(guestInputs);
        customizationSysprep.setUserData(userData);

        return customizationSysprep;
    }


    private CustomizationWinOptions getCustomizationWinOptions(GuestInputs guestInputs) {
        CustomizationWinOptions winOptions = new CustomizationWinOptions();
        winOptions.setDeleteAccounts(guestInputs.isDeleteAccounts());
        winOptions.setChangeSID(guestInputs.isChangeSID());

        if (StringUtils.isNotBlank(guestInputs.getRebootOption())) {
            CustomizationSysprepRebootOption rebootOption = CustomizationSysprepRebootOption
                    .fromValue(guestInputs.getRebootOption());
            winOptions.setReboot(rebootOption);
        }

        return winOptions;
    }

    private void setAdapter(GuestInputs guestInputs, CustomizationSpec customizationSpec) {
        CustomizationAdapterMapping adapterMapping = getCustomizationAdapterMapping(guestInputs);
        List<CustomizationAdapterMapping> adaptersList = customizationSpec.getNicSettingMap();
        adaptersList.add(adapterMapping);
    }

    private CustomizationAdapterMapping getCustomizationAdapterMapping(GuestInputs guestInputs) {
        CustomizationAdapterMapping adapterMapping = new CustomizationAdapterMapping();

        CustomizationIPSettings ipSettings;
        if (StringUtils.isNotBlank(guestInputs.getIpAddress())) {
            ipSettings = getFixedIpSettings(guestInputs);
        } else {
            ipSettings = new CustomizationIPSettings();
            ipSettings.setIp(new CustomizationDhcpIpGenerator());
        }
        adapterMapping.setAdapter(ipSettings);

        if (StringUtils.isNotBlank(guestInputs.getMacAddress())) {
            adapterMapping.setMacAddress(guestInputs.getMacAddress());
        }

        return adapterMapping;
    }

    private CustomizationIPSettings getFixedIpSettings(GuestInputs guestInputs) {
        CustomizationIPSettings ipSettings = new CustomizationIPSettings();

        CustomizationFixedIp fixedIp = new CustomizationFixedIp();
        fixedIp.setIpAddress(guestInputs.getIpAddress());

        ipSettings.setIp(fixedIp);
        ipSettings.setSubnetMask(guestInputs.getSubnetMask());

        if (StringUtils.isNotBlank(guestInputs.getDefaultGateway())) {
            List<String> gatewaysList = ipSettings.getGateway();
            gatewaysList.add(guestInputs.getDefaultGateway());
        }

        if (StringUtils.isNotBlank(guestInputs.getDnsServer())) {
            List<String> dnsServersList = ipSettings.getDnsServerList();
            dnsServersList.add(guestInputs.getDnsServer());
        }

        return ipSettings;
    }

    private CustomizationSysprep getPopulatedCustomizationSysprep(GuestInputs guestInputs) {
        CustomizationSysprep customizationSysprep = new CustomizationSysprep();

        CustomizationGuiUnattended guiUnattended = getGuiUnattended(guestInputs);
        customizationSysprep.setGuiUnattended(guiUnattended);

        CustomizationIdentification identification = getCustomizationIdentification(guestInputs);
        customizationSysprep.setIdentification(identification);

        if (StringUtils.isNotBlank(guestInputs.getLicenseDataMode())) {
            CustomizationLicenseFilePrintData licenseFilePrintData = getCustomizationLicenseFilePrintData(guestInputs);
            customizationSysprep.setLicenseFilePrintData(licenseFilePrintData);
        }

        return customizationSysprep;
    }

    private CustomizationUserData getCustomizationUserData(GuestInputs guestInputs) {
        CustomizationUserData userData = new CustomizationUserData();
        userData.setFullName(guestInputs.getOwnerName());
        userData.setOrgName(guestInputs.getOwnerOrganization());
        userData.setProductId(guestInputs.getProductKey());

        CustomizationFixedName computerName = new CustomizationFixedName();
        computerName.setName(guestInputs.getComputerName());
        userData.setComputerName(computerName);

        return userData;
    }

    private CustomizationGuiUnattended getGuiUnattended(GuestInputs guestInputs) {
        CustomizationGuiUnattended guiUnattended = new CustomizationGuiUnattended();
        guiUnattended.setAutoLogon(guestInputs.isAutoLogon());
        guiUnattended.setAutoLogonCount(guestInputs.getAutoLogonCount());
        guiUnattended.setTimeZone(guestInputs.getTimeZone());

        CustomizationPassword password = getCustomizationPassword(guestInputs.getComputerPassword());
        guiUnattended.setPassword(password);

        return guiUnattended;
    }

    private CustomizationIdentification getCustomizationIdentification(GuestInputs guestInputs) {
        CustomizationIdentification identification = new CustomizationIdentification();

        if (StringUtils.isNotBlank(guestInputs.getDomain()) && StringUtils.isNotBlank(guestInputs.getWorkgroup())) {
            throw new RuntimeException(ErrorMessages.DOMAIN_AND_WORKGROUP_BOTH_PRESENT);
        }

        if (StringUtils.isNotBlank(guestInputs.getDomain())) {
            identification.setDomainAdmin(guestInputs.getDomainUsername());
            identification.setJoinDomain(guestInputs.getDomain());

            CustomizationPassword customPassword = getCustomizationPassword(guestInputs.getDomainPassword());
            identification.setDomainAdminPassword(customPassword);
        } else {
            identification.setJoinWorkgroup(guestInputs.getWorkgroup());
        }

        return identification;
    }

    private CustomizationLicenseFilePrintData getCustomizationLicenseFilePrintData(GuestInputs guestInputs) {
        CustomizationLicenseFilePrintData licenseFilePrintData = new CustomizationLicenseFilePrintData();
        if (PER_SERVER.equals(guestInputs.getLicenseDataMode())) {
            licenseFilePrintData.setAutoUsers(guestInputs.getAutoUsers());
        }

        CustomizationLicenseDataMode licenseDataMode = CustomizationLicenseDataMode
                .fromValue(guestInputs.getLicenseDataMode());
        licenseFilePrintData.setAutoMode(licenseDataMode);

        return licenseFilePrintData;
    }

    private CustomizationPassword getCustomizationPassword(String value) {
        CustomizationPassword password = new CustomizationPassword();

        if (StringUtils.isNotBlank(value)) {
            password.setPlainText(true);
            password.setValue(value);
        } else {
            password.setValue(null);
        }

        return password;
    }
}
