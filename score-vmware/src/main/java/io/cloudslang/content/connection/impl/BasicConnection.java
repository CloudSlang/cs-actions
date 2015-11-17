package io.cloudslang.content.connection.impl;

import com.vmware.vim25.*;
import io.cloudslang.content.connection.Connection;
import io.cloudslang.content.connection.exceptions.ConnectionException;
import io.cloudslang.content.constants.Constants;

import javax.xml.ws.BindingProvider;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 10/19/2015.
 */
public class BasicConnection implements Connection {
    private VimService vimService;
    private VimPortType vimPort;
    private ServiceContent serviceContent;
    private UserSession userSession;
    private ManagedObjectReference serviceInstanceReference;

    public VimPortType getVimPort() {
        return vimPort;
    }

    public ServiceContent getServiceContent() {
        return serviceContent;
    }

    public ManagedObjectReference getServiceInstanceReference() {
        if (serviceInstanceReference == null) {
            ManagedObjectReference morRef = new ManagedObjectReference();
            morRef.setType(Constants.SERVICE_INSTANCE);
            morRef.setValue(Constants.SERVICE_INSTANCE);
            serviceInstanceReference = morRef;
        }
        return serviceInstanceReference;
    }

    public Connection connect(String url, String username, String password, boolean x509HostnameVerifier) {
        if (!isConnected()) {
            try {
                makeConnection(url, username, password, x509HostnameVerifier);
            } catch (Exception e) {
                Throwable cause = (e.getCause() != null) ? e.getCause() : e;
                throw new BasicConnectionException("failed to connect: "
                        + e.getMessage()
                        + Constants.BLANK_SPACE + Constants.COLON + Constants.BLANK_SPACE
                        + cause.getMessage(), cause);
            }
        }

        return this;
    }

    @SuppressWarnings("rawtypes")
    private void makeConnection(String url, String username, String password, boolean x509HostnameVerifier)
            throws RuntimeFaultFaultMsg,
            InvalidLocaleFaultMsg,
            InvalidLoginFaultMsg,
            KeyManagementException,
            NoSuchAlgorithmException {

        vimService = new VimService();
        vimPort = vimService.getVimPort();

        populateContextMap(url, username, password);

        if (Boolean.FALSE.equals(x509HostnameVerifier)) {
            DisableSecurity.trustEveryone();
        }

        serviceContent = vimPort.retrieveServiceContent(this.getServiceInstanceReference());
        userSession = vimPort.login(serviceContent.getSessionManager(), username, password, null);
    }

    public Connection disconnect() {
        if (this.isConnected()) {
            try {
                vimPort.logout(serviceContent.getSessionManager());
            } catch (Exception e) {
                Throwable cause = e.getCause();
                throw new BasicConnectionException("failed to disconnect properly: "
                        + e.getMessage()
                        + Constants.BLANK_SPACE + Constants.COLON + Constants.BLANK_SPACE
                        + cause.getMessage(), cause);
            } finally {
                userSession = null;
                serviceContent = null;
                vimPort = null;
                vimService = null;
            }
        }

        return this;
    }

    public boolean isConnected() {
        if (userSession == null) {
            return false;
        }
        long startTime = userSession.getLastActiveTime().toGregorianCalendar().getTime().getTime();

        // verifying the equivalent of 30 minutes
        return System.currentTimeMillis() < startTime + Constants.THIRTY * Constants.SIXTY * Constants.THOUSAND_MULTIPLIER;
    }

    private void populateContextMap(String url, String username, String password) {
        Map<String, Object> context = ((BindingProvider) vimPort).getRequestContext();
        context.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        context.put(BindingProvider.USERNAME_PROPERTY, username);
        context.put(BindingProvider.PASSWORD_PROPERTY, password);
        context.put(BindingProvider.SESSION_MAINTAIN_PROPERTY, Boolean.TRUE);
    }

    private class BasicConnectionException extends ConnectionException {
        private static final long serialVersionUID = 1L;

        public BasicConnectionException(String s, Throwable t) {
            super(s, t);
        }
    }
}
