/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.entities;

import io.cloudslang.content.vmware.constants.Constants;

import static io.cloudslang.content.vmware.utils.InputUtils.getBooleanInput;
import static io.cloudslang.content.vmware.utils.InputUtils.getIntInput;
import static java.lang.Boolean.parseBoolean;

/**
 * Created by Mihai Tusa.
 * 3/21/2016.
 */
public class GuestInputs {
    private String rebootOption;
    private String computerName;
    private String computerPassword;
    private String ownerName;
    private String ownerOrganization;
    private String productKey;
    private String domainUsername;
    private String domainPassword;
    private String domain;
    private String workgroup;
    private String licenseDataMode;
    private String dnsServer;
    private String ipAddress;
    private String subnetMask;
    private String defaultGateway;
    private String macAddress;

    private boolean deleteAccounts;
    private boolean changeSID;
    private boolean autoLogon;
    private boolean hwClockUTC;

    private int autoLogonCount;
    private int autoUsers;
    private int timeZone;

    private GuestInputs(GuestInputsBuilder builder) {
        this.rebootOption = builder.rebootOption;
        this.computerName = builder.computerName;
        this.computerPassword = builder.computerPassword;
        this.ownerName = builder.ownerName;
        this.ownerOrganization = builder.ownerOrganization;
        this.productKey = builder.productKey;
        this.domainUsername = builder.domainUsername;
        this.domainPassword = builder.domainPassword;
        this.domain = builder.domain;
        this.workgroup = builder.workgroup;
        this.licenseDataMode = builder.licenseDataMode;
        this.dnsServer = builder.dnsServer;
        this.ipAddress = builder.ipAddress;
        this.subnetMask = builder.subnetMask;
        this.defaultGateway = builder.defaultGateway;
        this.macAddress = builder.macAddress;

        this.deleteAccounts = builder.deleteAccounts;
        this.changeSID = builder.changeSID;
        this.autoLogon = builder.autoLogon;
        this.hwClockUTC = builder.hwClockUTC;

        this.autoLogonCount = builder.autoLogonCount;
        this.autoUsers = builder.autoUsers;
        this.timeZone = builder.timeZone;
    }

    public String getRebootOption() {
        return rebootOption;
    }

    public String getComputerName() {
        return computerName;
    }

    public String getComputerPassword() {
        return computerPassword;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerOrganization() {
        return ownerOrganization;
    }

    public String getProductKey() {
        return productKey;
    }

    public String getDomainUsername() {
        return domainUsername;
    }

    public String getDomainPassword() {
        return domainPassword;
    }

    public String getDomain() {
        return domain;
    }

    public String getWorkgroup() {
        return workgroup;
    }

    public String getLicenseDataMode() {
        return licenseDataMode;
    }

    public String getDnsServer() {
        return dnsServer;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getSubnetMask() {
        return subnetMask;
    }

    public String getDefaultGateway() {
        return defaultGateway;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public boolean isDeleteAccounts() {
        return deleteAccounts;
    }

    public boolean isChangeSID() {
        return changeSID;
    }

    public boolean isAutoLogon() {
        return autoLogon;
    }

    public boolean isHwClockUTC() {
        return hwClockUTC;
    }

    public int getAutoLogonCount() {
        return autoLogonCount;
    }

    public int getAutoUsers() {
        return autoUsers;
    }

    public int getTimeZone() {
        return timeZone;
    }

    public static class GuestInputsBuilder {
        private String rebootOption;
        private String computerName;
        private String ownerName;
        private String computerPassword;
        private String ownerOrganization;
        private String productKey;
        private String domainUsername;
        private String domainPassword;
        private String domain;
        private String workgroup;
        private String licenseDataMode;
        private String dnsServer;
        private String ipAddress;
        private String subnetMask;
        private String defaultGateway;
        private String macAddress;

        private boolean deleteAccounts;
        private boolean changeSID;
        private boolean autoLogon;
        private boolean hwClockUTC;

        private int autoLogonCount;
        private int autoUsers;
        private int timeZone;

        public GuestInputs build() {
            return new GuestInputs(this);
        }

        public GuestInputsBuilder withRebootOption(String inputValue) {
            rebootOption = inputValue;
            return this;
        }

        public GuestInputsBuilder withComputerName(String inputValue) throws Exception {
            computerName = inputValue;
            return this;
        }

        public GuestInputsBuilder withOwnerName(String inputValue) throws Exception {
            ownerName = inputValue;
            return this;
        }

        public GuestInputsBuilder withComputerPassword(String inputValue) throws Exception {
            computerPassword = inputValue;
            return this;
        }

        public GuestInputsBuilder withOwnerOrganization(String inputValue) throws Exception {
            ownerOrganization = inputValue;
            return this;
        }

        public GuestInputsBuilder withProductKey(String inputValue) throws Exception {
            productKey = inputValue;
            return this;
        }

        public GuestInputsBuilder withDomainUsername(String inputValue) throws Exception {
            domainUsername = inputValue;
            return this;
        }

        public GuestInputsBuilder withDomainPassword(String inputValue) throws Exception {
            domainPassword = inputValue;
            return this;
        }

        public GuestInputsBuilder withDomain(String inputValue) throws Exception {
            domain = inputValue;
            return this;
        }

        public GuestInputsBuilder withWorkgroup(String inputValue) throws Exception {
            workgroup = inputValue;
            return this;
        }

        public GuestInputsBuilder withLicenseDataMode(String inputValue) {
            licenseDataMode = inputValue;
            return this;
        }

        public GuestInputsBuilder withDnsServer(String inputValue) throws Exception {
            dnsServer = inputValue;
            return this;
        }

        public GuestInputsBuilder withIpAddress(String inputValue) throws Exception {
            ipAddress = inputValue;
            return this;
        }

        public GuestInputsBuilder withSubnetMask(String inputValue) throws Exception {
            subnetMask = inputValue;
            return this;
        }

        public GuestInputsBuilder withDefaultGateway(String inputValue) throws Exception {
            defaultGateway = inputValue;
            return this;
        }

        public GuestInputsBuilder withMacAddress(String inputValue) throws Exception {
            macAddress = inputValue;
            return this;
        }

        public GuestInputsBuilder withAutoLogon(String inputValue) throws Exception {
            autoLogon = parseBoolean(inputValue);
            return this;
        }

        public GuestInputsBuilder withHwClockUTC(String inputValue) throws Exception {
            hwClockUTC = getBooleanInput(inputValue, true);
            return this;
        }

        public GuestInputsBuilder withDeleteAccounts(String inputValue) {
            deleteAccounts = parseBoolean(inputValue);
            return this;
        }

        public GuestInputsBuilder withChangeSID(String inputValue) {
            changeSID = getBooleanInput(inputValue, true);
            return this;
        }

        public GuestInputsBuilder withAutoLogonCount(String inputValue) {
            autoLogonCount = getIntInput(inputValue, Constants.DEFAULT_AUTO_LOGON_COUNT);
            return this;
        }

        public GuestInputsBuilder withAutoUsers(String inputValue) {
            autoUsers = getIntInput(inputValue, Constants.DEFAULT_AUTO_USERS_NUMBER);
            return this;
        }

        public GuestInputsBuilder withTimeZone(String inputValue) {
            timeZone = getIntInput(inputValue, Constants.DEFAULT_TIME_ZONE);
            return this;
        }
    }
}
