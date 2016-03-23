package io.cloudslang.content.vmware.services.utils;

import com.vmware.vim25.*;
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

        CustomizationGlobalIPSettings globalIPSettings = getCustomizationGlobalIPSettings(guestInputs);
        customizationSpec.setGlobalIPSettings(globalIPSettings);

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

        CustomizationSysprepRebootOption rebootOption = CustomizationSysprepRebootOption
                .fromValue(guestInputs.getRebootOption());
        winOptions.setReboot(rebootOption);

        return winOptions;
    }

    private CustomizationGlobalIPSettings getCustomizationGlobalIPSettings(GuestInputs guestInputs) {
        CustomizationGlobalIPSettings globalIPSettings = new CustomizationGlobalIPSettings();
        List<String> dnsServerList = globalIPSettings.getDnsServerList();
        dnsServerList.add(guestInputs.getDnsServer());

        return globalIPSettings;
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
        userData.setFullName(guestInputs.getComputerName());
        userData.setOrgName(guestInputs.getOwnerOrganization());
        userData.setProductId(guestInputs.getProductKey());

        return userData;
    }

    private CustomizationGuiUnattended getGuiUnattended(GuestInputs guestInputs) {
        CustomizationGuiUnattended guiUnattended = new CustomizationGuiUnattended();
        guiUnattended.setAutoLogon(guestInputs.isAutoLogon());
        guiUnattended.setAutoLogonCount(guestInputs.getAutoLogonCount());

        CustomizationPassword password = getCustomizationPassword(guestInputs.isPlainText(),
                guestInputs.getComputerPassword());
        guiUnattended.setPassword(password);

        return guiUnattended;
    }

    private CustomizationIdentification getCustomizationIdentification(GuestInputs guestInputs) {
        CustomizationIdentification identification = new CustomizationIdentification();
        identification.setDomainAdmin(guestInputs.getDomainUsername());
        identification.setJoinDomain(guestInputs.getDomain());
        identification.setJoinWorkgroup(guestInputs.getWorkgroup());

        CustomizationPassword password = getCustomizationPassword(guestInputs.isPlainText(),
                guestInputs.getDomainPassword());
        identification.setDomainAdminPassword(password);


        return identification;
    }

    private CustomizationLicenseFilePrintData getCustomizationLicenseFilePrintData(GuestInputs guestInputs) {
        CustomizationLicenseFilePrintData licenseFilePrintData = new CustomizationLicenseFilePrintData();
        licenseFilePrintData.setAutoUsers(guestInputs.getAutoUsers());

        CustomizationLicenseDataMode licenseDataMode = CustomizationLicenseDataMode
                .fromValue(guestInputs.getLicenseDataMode());
        licenseFilePrintData.setAutoMode(licenseDataMode);

        return licenseFilePrintData;
    }

    private CustomizationPassword getCustomizationPassword(boolean plainText, String value) {
        CustomizationPassword password = new CustomizationPassword();
        password.setPlainText(plainText);
        password.setValue(value);

        return password;
    }
}