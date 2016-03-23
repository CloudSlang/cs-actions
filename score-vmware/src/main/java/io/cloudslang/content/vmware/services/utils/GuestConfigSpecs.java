package io.cloudslang.content.vmware.services.utils;

import com.vmware.vim25.*;
import io.cloudslang.content.vmware.constants.ErrorMessages;
import io.cloudslang.content.vmware.entities.GuestInputs;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by Mihai Tusa.
 * 3/21/2016.
 */
public class GuestConfigSpecs {
    public CustomizationSpec getCustomizationSpec(GuestInputs guestInputs) {
        CustomizationSpec customizationSpec = new CustomizationSpec();

        CustomizationSysprep customizationSysprep = getCustomizationSysprep(guestInputs);
        customizationSpec.setIdentity(customizationSysprep);

        CustomizationWinOptions winOptions = getCustomizationWinOptions(guestInputs);
        customizationSpec.setOptions(winOptions);

        customizationSpec.setGlobalIPSettings(new CustomizationGlobalIPSettings());

        setAdapter(guestInputs, customizationSpec);

        return customizationSpec;
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

        CustomizationSysprepRebootOption rebootOption = CustomizationSysprepRebootOption.fromValue(guestInputs.getRebootOption());
        winOptions.setReboot(rebootOption);

        return winOptions;
    }

    private void setAdapter(GuestInputs guestInputs, CustomizationSpec customizationSpec) {
        List<CustomizationAdapterMapping> adaptersList = customizationSpec.getNicSettingMap();
        CustomizationAdapterMapping adapterMapping = getCustomizationAdapterMapping(guestInputs);
        adaptersList.add(adapterMapping);
    }

    private CustomizationAdapterMapping getCustomizationAdapterMapping(GuestInputs guestInputs) {
        CustomizationAdapterMapping adapterMapping = new CustomizationAdapterMapping();

        CustomizationIPSettings ipSettings = new CustomizationIPSettings();
        ipSettings.setSubnetMask(guestInputs.getSubnetMask());

        CustomizationFixedIp fixedIp = new CustomizationFixedIp();
        fixedIp.setIpAddress(guestInputs.getIpAddress());

        ipSettings.setIp(fixedIp);

        if (StringUtils.isNotBlank(guestInputs.getDefaultGateway())) {
            List<String> gatewaysList = ipSettings.getGateway();
            gatewaysList.add(guestInputs.getDefaultGateway());
        }

        if (StringUtils.isNotBlank(guestInputs.getDnsServer())) {
            List<String> dnsServersList = ipSettings.getDnsServerList();
            dnsServersList.add(guestInputs.getDnsServer());
        }

        adapterMapping.setAdapter(ipSettings);
        adapterMapping.setMacAddress("");

        return adapterMapping;
    }

    private CustomizationSysprep getPopulatedCustomizationSysprep(GuestInputs guestInputs) {
        CustomizationSysprep customizationSysprep = new CustomizationSysprep();

        CustomizationGuiUnattended guiUnattended = getGuiUnattended(guestInputs);
        customizationSysprep.setGuiUnattended(guiUnattended);

        CustomizationIdentification identification = getCustomizationIdentification(guestInputs);
        customizationSysprep.setIdentification(identification);

        CustomizationLicenseFilePrintData licenseFilePrintData = getCustomizationLicenseFilePrintData(guestInputs);
        customizationSysprep.setLicenseFilePrintData(licenseFilePrintData);

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
        licenseFilePrintData.setAutoUsers(guestInputs.getAutoUsers());

        CustomizationLicenseDataMode licenseDataMode = CustomizationLicenseDataMode.fromValue(guestInputs.getLicenseDataMode());
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