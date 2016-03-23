package io.cloudslang.content.vmware.entities;

import io.cloudslang.content.vmware.utils.InputUtils;

/**
 * Created by Mihai Tusa.
 * 3/21/2016.
 */
public class GuestInputs {
    private static final boolean DEFAULT_PLAIN_TEXT = true;

    private static final int DEFAULT_AUTO_LOGON_COUNT = 1;
    private static final int DEFAULT_AUTO_USERS_NUMBER = 1;

    private String rebootOption;
    private String computerName;
    private String computerPassword;
    private String ownerOrganization;
    private String productKey;
    private String domainUsername;
    private String domainPassword;
    private String domain;
    private String workgroup;
    private String licenseDataMode;
    private String dnsServer;

    private boolean deleteAccounts;
    private boolean changeSID;
    private boolean autoLogon;
    private boolean plainText;

    private int autoLogonCount;
    private int autoUsers;

    public GuestInputs(GuestInputsBuilder builder) {
        this.rebootOption = builder.rebootOption;
        this.computerName = builder.computerName;
        this.computerPassword = builder.computerPassword;
        this.ownerOrganization = builder.ownerOrganization;
        this.productKey = builder.productKey;
        this.domainUsername = builder.domainUsername;
        this.domainPassword = builder.domainPassword;
        this.domain = builder.domain;
        this.workgroup = builder.workgroup;
        this.licenseDataMode = builder.licenseDataMode;
        this.dnsServer = builder.dnsServer;
        this.deleteAccounts = builder.deleteAccounts;
        this.changeSID = builder.changeSID;
        this.autoLogon = builder.autoLogon;
        this.plainText = builder.plainText;
        this.autoLogonCount = builder.autoLogonCount;
        this.autoUsers = builder.autoUsers;
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

    public String getOwnerOrganization() {
        return ownerOrganization;
    }

    public String getProductKey() {
        return productKey;
    }

    public String getDomainPassword() {
        return domainPassword;
    }

    public String getDomainUsername() {
        return domainUsername;
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

    public boolean isDeleteAccounts() {
        return deleteAccounts;
    }

    public boolean isChangeSID() {
        return changeSID;
    }

    public boolean isAutoLogon() {
        return autoLogon;
    }

    public boolean isPlainText() {
        return plainText;
    }

    public int getAutoLogonCount() {
        return autoLogonCount;
    }

    public int getAutoUsers() {
        return autoUsers;
    }

    public static class GuestInputsBuilder {
        private String rebootOption;
        private String computerName;
        private String computerPassword;
        private String ownerOrganization;
        private String productKey;
        private String domainUsername;
        private String domainPassword;
        private String domain;
        private String workgroup;
        private String licenseDataMode;
        private String dnsServer;

        private boolean deleteAccounts;
        private boolean changeSID;
        private boolean autoLogon;
        private boolean plainText;

        private int autoLogonCount;
        private int autoUsers;

        public GuestInputs build() {
            return new GuestInputs(this);
        }

        public GuestInputsBuilder withRebootOption(String inputValue) throws Exception {
            rebootOption = RebootOption.getValue(inputValue);
            return this;
        }

        public GuestInputsBuilder withComputerName(String inputValue) throws Exception {
            computerName = inputValue;
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

        public GuestInputsBuilder withLicenseDataMode(String inputValue) throws Exception {
            licenseDataMode = LicenseDataMode.getValue(inputValue);
            return this;
        }

        public GuestInputsBuilder withDnsServer(String inputValue) throws Exception {
            dnsServer = inputValue;
            return this;
        }

        public GuestInputsBuilder withAutoLogon(String inputValue) throws Exception {
            autoLogon = Boolean.parseBoolean(inputValue);
            return this;
        }

        public GuestInputsBuilder withWithPlainText(String inputValue) throws Exception {
            plainText = InputUtils.getBooleanInput(inputValue, DEFAULT_PLAIN_TEXT);
            return this;
        }

        public GuestInputsBuilder withDeleteAccounts(String inputValue) {
            deleteAccounts = Boolean.parseBoolean(inputValue);
            return this;
        }

        public GuestInputsBuilder withChangeSID(String inputValue) {
            changeSID = Boolean.parseBoolean(inputValue);
            return this;
        }

        public GuestInputsBuilder withAutoLogonCount(String inputValue) {
            autoLogonCount = InputUtils.getIntInput(inputValue, DEFAULT_AUTO_LOGON_COUNT);
            return this;
        }

        public GuestInputsBuilder withAutoUsers(String inputValue) {
            autoUsers = InputUtils.getIntInput(inputValue, DEFAULT_AUTO_USERS_NUMBER);
            return this;
        }
    }
}