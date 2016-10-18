package io.cloudslang.content.vmware.connection.impl;

import com.vmware.vim25.*;
import io.cloudslang.content.vmware.connection.Connection;
import io.cloudslang.content.vmware.connection.exceptions.ConnectionException;
import io.cloudslang.content.vmware.entities.ManagedObjectType;

import javax.xml.ws.BindingProvider;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class BasicConnection implements Connection {
    private static final int THIRTY = 30;
    private static final int SIXTY = 60;
    private static final int THOUSAND = 1000;

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
            morRef.setType(ManagedObjectType.SERVICE_INSTANCE.getValue());
            morRef.setValue(ManagedObjectType.SERVICE_INSTANCE.getValue());
            serviceInstanceReference = morRef;
        }
        return serviceInstanceReference;
    }

    public Connection connect(String url, String username, String password, boolean trustEveryone) {
        if (!isConnected()) {
            try {
                makeConnection(url, username, password, trustEveryone);
            } catch (Exception e) {
                Throwable cause = (e.getCause() != null) ? e.getCause() : e;
                throw new BasicConnectionException("failed to connect: " +
                        e.getMessage() + " : " + cause.getMessage(), cause);
            }
        }

        return this;
    }

    public Connection disconnect() {
        if (this.isConnected()) {
            try {
                vimPort.logout(serviceContent.getSessionManager());
            } catch (Exception e) {
                Throwable cause = e.getCause();
                throw new BasicConnectionException("Failed to disconnect properly: " +
                        e.getMessage() + " : " + cause.getMessage(), cause);
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
        return System.currentTimeMillis() < startTime + THIRTY * SIXTY * THOUSAND;
    }

    @SuppressWarnings("rawtypes")
    private void makeConnection(String url, String username, String password, boolean trustEveryone)
            throws RuntimeFaultFaultMsg,
            InvalidLocaleFaultMsg,
            InvalidLoginFaultMsg,
            KeyManagementException,
            NoSuchAlgorithmException {

        vimService = new VimService();
        vimPort = vimService.getVimPort();

        populateContextMap(url, username, password);

        if (Boolean.TRUE.equals(trustEveryone)) {
            DisableSecurity.trustEveryone();
        }

        serviceContent = vimPort.retrieveServiceContent(this.getServiceInstanceReference());
        userSession = vimPort.login(serviceContent.getSessionManager(), username, password, null);
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

        BasicConnectionException(String s, Throwable t) {
            super(s, t);
        }
    }
}
