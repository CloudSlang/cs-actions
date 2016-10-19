package io.cloudslang.content.jclouds.execute.queries;

import io.cloudslang.content.httpclient.CSHttpClient;
import io.cloudslang.content.httpclient.HttpClientInputs;
import io.cloudslang.content.jclouds.entities.aws.AuthorizationHeader;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.entities.inputs.ElasticIpInputs;
import io.cloudslang.content.jclouds.entities.inputs.IamInputs;
import io.cloudslang.content.jclouds.entities.inputs.ImageInputs;
import io.cloudslang.content.jclouds.entities.inputs.InputsWrapper;
import io.cloudslang.content.jclouds.entities.inputs.InstanceInputs;
import io.cloudslang.content.jclouds.entities.inputs.NetworkInputs;
import io.cloudslang.content.jclouds.entities.inputs.VolumeInputs;
import io.cloudslang.content.jclouds.services.AmazonSignatureService;
import io.cloudslang.content.jclouds.services.impl.MockingHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.anyMapOf;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by Mihai Tusa.
 * 9/7/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CSHttpClient.class, AmazonSignatureService.class, QueryApiExecutor.class})
public class QueryApiExecutorTest {
    private static final String HEADERS = "Accept:text/plain\r\n Content-Type:application/json";

    private QueryApiExecutor toTest;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private CSHttpClient csHttpClientMock;

    @Mock
    private AmazonSignatureService amazonSignatureServiceMock;

    @Mock
    private AuthorizationHeader authorizationHeaderMock;

    @Spy
    private QueryApiExecutor queryApiExecutorSpy = new QueryApiExecutor();

    @Before
    public void init() throws Exception {
        toTest = new QueryApiExecutor();
        addCommonMocksForQueryApi();
    }

    @After
    public void tearDown() {
        toTest = null;
        queryApiExecutorSpy = null;
    }

    @Test
    public void testAddLaunchPermissionsToImage() throws Exception {
        toTest.execute(getCommonInputs("ModifyImageAttribute", HEADERS, ""), getAddLaunchPermissionsToImageInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("ModifyImageAttribute")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testAllocateAddress() throws Exception {
        toTest.execute(getCommonInputs("AllocateAddress", HEADERS, ""), getCustomInputs(), getElasticIpInputs(),
                getNetworkInputs(true));

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("AllocateAddress")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testAssociateAddress() throws Exception {
        toTest.execute(getCommonInputs("AssociateAddress", HEADERS, ""), getCustomInputs(), getElasticIpInputs(),
                getNetworkInputs(true));

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("AssociateAddress")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testAttachNetworkInterface() throws Exception {
        toTest.execute(getCommonInputs("DeleteNetworkInterface", HEADERS, ""), getCustomInputs(), getNetworkInputs(true));

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DeleteNetworkInterface")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testAttachVolume() throws Exception {
        toTest.execute(getCommonInputs("AttachVolume", HEADERS, ""), getVolumeCustomInputs(), getVolumeInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("AttachVolume")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testCreateNetworkInterface() throws Exception {
        toTest.execute(getCommonInputs("CreateNetworkInterface", HEADERS, ""), getCustomInputs(), getElasticIpInputs(),
                getIamInputs(), getNetworkInputs(false));

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("CreateNetworkInterface")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testCreateImage() throws Exception {
        toTest.execute(getCommonInputs("CreateImage", HEADERS, ""), getCreateImageInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("CreateImage")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testCreateSnapshot() throws Exception {
        toTest.execute(getCommonInputs("CreateSnapshot", HEADERS, ""), getVolumeCustomInputs(), getVolumeInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("CreateSnapshot")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testCreateTags() throws Exception {
        toTest.execute(getCommonInputs("CreateTags", HEADERS, ""), getCustomInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("CreateTags")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testCreateVolume() throws Exception {
        toTest.execute(getCommonInputs("CreateVolume", HEADERS, ""), getCustomInputs(), getVolumeInputs(),
                getNetworkInputs(false));

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("CreateVolume")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testDeleteNetworkInterface() throws Exception {
        toTest.execute(getCommonInputs("AttachNetworkInterface", HEADERS, ""), getCustomInputs(), getNetworkInputs(true));

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("AttachNetworkInterface")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testDeleteSnapshot() throws Exception {
        toTest.execute(getCommonInputs("DeleteSnapshot", HEADERS, ""), getVolumeInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DeleteSnapshot")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testDeleteVolume() throws Exception {
        toTest.execute(getCommonInputs("DeleteVolume", HEADERS, ""), getVolumeCustomInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DeleteVolume")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testDeregisterImage() throws Exception {
        toTest.execute(getCommonInputs("DeregisterImage", HEADERS, ""), getImageInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DeregisterImage")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testDescribeImagesOnImage() throws Exception {
        toTest.execute(getCommonInputs("DescribeImages", HEADERS, ""), getDescribeImagesInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DescribeImages")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testDescribeRegions() throws Exception {
        toTest.execute(getCommonInputs("DescribeRegions", HEADERS, ""), getCustomInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DescribeRegions")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testDetachNetworkInterface() throws Exception {
        toTest.execute(getCommonInputs("DetachNetworkInterface", HEADERS, ""), getCustomInputs(), getNetworkInputs(false));

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DetachNetworkInterface")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testDetachVolume() throws Exception {
        toTest.execute(getCommonInputs("DetachVolume", HEADERS, ""), getVolumeCustomInputs(), getVolumeInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DetachVolume")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testDisassociateAddress() throws Exception {
        toTest.execute(getCommonInputs("DisassociateAddress", HEADERS, ""), getCustomInputs(), getElasticIpInputs(),
                getNetworkInputs(false));

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DisassociateAddress")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testReleaseAddress() throws Exception {
        toTest.execute(getCommonInputs("ReleaseAddress", HEADERS, ""), getCustomInputs(), getElasticIpInputs(),
                getNetworkInputs(false));

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("ReleaseAddress")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testStartInstances() throws Exception {
        toTest.execute(getCommonInputs("StartInstances", HEADERS, ""), getStartInstancesInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("StartInstances")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testStopInstances() throws Exception {
        toTest.execute(getCommonInputs("StopInstances", HEADERS, ""), getInstanceCustomInputs(), getStopInstancesInstanceInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("StopInstances")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testRebootInstances() throws Exception {
        toTest.execute(getCommonInputs("RebootInstances", HEADERS, ""), getInstanceCustomInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("RebootInstances")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testResetLaunchPermissionOnImage() throws Exception {
        toTest.execute(getCommonInputs("ResetImageAttribute", HEADERS, ""), getResetLaunchPermissionOnImageInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("ResetImageAttribute")));
        runCommonVerifiersForQueryApi();
    }

    private CustomInputs getResetLaunchPermissionOnImageInputs() {
        return new CustomInputs.Builder()
                .withAttribute("launchPermission")
                .withImageId("ami-abcd1234")
                .build();
    }

    @Test
    public void testGetLaunchPermissionForImage() throws Exception {
        toTest.execute(getCommonInputs("DescribeImageAttribute", HEADERS, ""), getLaunchPermissionForImageInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DescribeImageAttribute")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testTerminateInstances() throws Exception {
        toTest.execute(getCommonInputs("TerminateInstances", HEADERS, ""), getInstanceCustomInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("TerminateInstances")));
        runCommonVerifiersForQueryApi();
    }

    private CustomInputs getLaunchPermissionForImageInputs() {
        return new CustomInputs.Builder()
                .withAttribute("launchPermission")
                .withImageId("ami-abcd1234")
                .build();
    }

    @Test
    public void testExecuteWithException() throws Exception {
        MockingHelper.setExpectedExceptions(exception, RuntimeException.class, "Unsupported Query API.");

        toTest.execute(getCommonInputs("", "", ""), getCustomInputs(), getVolumeInputs(), getNetworkInputs(false));

        verify(amazonSignatureServiceMock, never()).signRequestHeaders(any(InputsWrapper.class),
                anyMapOf(String.class, String.class), anyMapOf(String.class, String.class));
        verify(csHttpClientMock, never()).execute(any(HttpClientInputs.class));
    }

    private void addCommonMocksForQueryApi() throws Exception {
        whenNew(AmazonSignatureService.class).withNoArguments().thenReturn(amazonSignatureServiceMock);
        when(amazonSignatureServiceMock.signRequestHeaders(any(InputsWrapper.class),
                anyMapOf(String.class, String.class), anyMapOf(String.class, String.class)))
                .thenReturn(authorizationHeaderMock);
        doNothing().when(queryApiExecutorSpy).setQueryApiHeaders(any(InputsWrapper.class),
                anyMapOf(String.class, String.class), anyMapOf(String.class, String.class));
        whenNew(CSHttpClient.class).withNoArguments().thenReturn(csHttpClientMock);
        when(csHttpClientMock.execute(any(HttpClientInputs.class))).thenReturn(null);
    }

    private void runCommonVerifiersForQueryApi() throws Exception {
        verifyNew(AmazonSignatureService.class).withNoArguments();
        verifyNew(CSHttpClient.class).withNoArguments();
        verify(csHttpClientMock, times(1)).execute(any(HttpClientInputs.class));
        verifyNoMoreInteractions(amazonSignatureServiceMock);
        verifyNoMoreInteractions(csHttpClientMock);
    }

    private CommonInputs getCommonInputs(String action, String headersString, String queryParamsString)
            throws MalformedURLException {
        return new CommonInputs.Builder()
                .withAction(action)
                .withHeaders(headersString)
                .withQueryParams(queryParamsString)
                .withVersion("2016-04-01")
                .withDelimiter(",")
                .build();
    }

    private CustomInputs getImageInputs() {
        return new CustomInputs.Builder()
                .withImageId("ami-abcd1234")
                .build();
    }

    private CustomInputs getCustomInputs() throws Exception {
        return new CustomInputs.Builder()
                .withAllocationId("eipalloc-abcdef12")
                .withAssociationId("eipassoc-abcdef12")
                .withAttachmentId("eni-attach-12345678")
                .withAvailabilityZone("us-east-1d")
                .withDomain("")
                .withInstanceId("i-abcdef12")
                .withVolumeType("")
                .withSubnetId("subnet-abcdef12")
                .withResourceIdsString("i-abcdef12,i-12345678")
                .withKeyTagsString("Name,webserver,stack,scope")
                .withValueTagsString("Tagged from API call,Not relevant,Testing,For testing purposes")
                .withRegionsString("us-east-1,eu-central-1")
                .build();
    }

    private VolumeInputs getVolumeInputs() {
        return new VolumeInputs.Builder()
                .withSnapshotId("snap-id")
                .withDeviceName("device-name")
                .withDescription("some-desc")
                .withSize("10")
                .withIops("")
                .build();
    }

    private ElasticIpInputs getElasticIpInputs() {
        return new ElasticIpInputs.Builder()
                .withAllowReassociation("tRuE")
                .withPublicIp("52.0.0.2")
                .withPrivateIpAddress("10.0.0.129")
                .withPrivateIpAddressesString("10.0.0.130,10.0.0.131,10.0.0.132")
                .build();
    }

    private IamInputs getIamInputs() {
        return new IamInputs.Builder()
                .withSecurityGroupIdsString("sg-12345678,sg-abcdef12")
                .build();
    }

    private NetworkInputs getNetworkInputs(boolean withNetworkInterfaceId) throws Exception {
        if (withNetworkInterfaceId) {
            return new NetworkInputs.Builder()
                    .withDeviceIndex("25")
                    .withNetworkInterfaceDescription("anything in here")
                    .withNetworkInterfaceId("eni-12345678")
                    .withNetworkInterfaceSubnetId("subnet-abcdef12")
                    .withSecondaryPrivateIpAddressCount("3")
                    .build();
        }
        return new NetworkInputs.Builder()
                .withDeviceIndex("25")
                .withNetworkInterfaceDescription("anything in here")
                .withNetworkInterfacePrivateIpAddress("10.0.0.129")
                .withNetworkInterfacePublicIp("52.0.0.2")
                .withNetworkInterfaceSubnetId("subnet-abcdef12")
                .withSecondaryPrivateIpAddressCount("3")
                .build();
    }

    private Map<String, String> getHeadersMap() {
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("accept", "text/plain");
        headersMap.put("content-type", "application/json");

        return headersMap;
    }

    private Map<String, String> getQueryParamsMap(String action) {
        Map<String, String> queryParamsMap = new HashMap<>();
        queryParamsMap.put("Action", action);
        queryParamsMap.put("Version", "2016-04-01");

        switch (action) {
            case "AllocateAddress":
                queryParamsMap.put("Domain", "standard");
                break;
            case "AssociateAddress":
                queryParamsMap.put("AllocationId", "eipalloc-abcdef12");
                queryParamsMap.put("AllowReassociation", "true");
                queryParamsMap.put("NetworkInterfaceId", "eni-12345678");
                queryParamsMap.put("PrivateIpAddress", "10.0.0.129");
                queryParamsMap.put("PublicIp", "52.0.0.2");
                break;
            case "AttachNetworkInterface":
                queryParamsMap.put("InstanceId", "i-abcdef12");
                queryParamsMap.put("NetworkInterfaceId", "eni-12345678");
                queryParamsMap.put("DeviceIndex", "25");
                break;
            case "AttachVolume":
                queryParamsMap.put("VolumeId", "v-12345678");
                queryParamsMap.put("InstanceId", "i-12345678");
                queryParamsMap.put("Device", "device-name");
                break;
            case "CreateNetworkInterface":
                queryParamsMap.put("SubnetId", "subnet-abcdef12");
                queryParamsMap.put("Description", "anything in here");
                queryParamsMap.put("PrivateIpAddresses.1.Primary", "true");
                queryParamsMap.put("PrivateIpAddresses.1.PrivateIpAddress", "10.0.0.129");
                queryParamsMap.put("PrivateIpAddresses.2.Primary", "false");
                queryParamsMap.put("PrivateIpAddresses.2.PrivateIpAddress", "10.0.0.130");
                queryParamsMap.put("PrivateIpAddresses.3.Primary", "false");
                queryParamsMap.put("PrivateIpAddresses.3.PrivateIpAddress", "10.0.0.131");
                queryParamsMap.put("PrivateIpAddresses.4.Primary", "false");
                queryParamsMap.put("PrivateIpAddresses.4.PrivateIpAddress", "10.0.0.132");
                queryParamsMap.put("SecurityGroupId.1", "sg-12345678");
                queryParamsMap.put("SecurityGroupId.2", "sg-abcdef12");
                break;
            case "CreateImage":
                queryParamsMap.put("Description", "Some description");
                queryParamsMap.put("InstanceId", "i-b0e2ad1b");
                queryParamsMap.put("Name", "img-name");
                queryParamsMap.put("NoReboot", "true");
                break;
            case "CreateTags":
                queryParamsMap.put("ResourceId.1", "i-abcdef12");
                queryParamsMap.put("ResourceId.2", "i-12345678");
                queryParamsMap.put("Tag.1.Key", "Name");
                queryParamsMap.put("Tag.1.Value", "Tagged from API call");
                queryParamsMap.put("Tag.2.Key", "webserver");
                queryParamsMap.put("Tag.2.Value", "");
                queryParamsMap.put("Tag.3.Key", "stack");
                queryParamsMap.put("Tag.3.Value", "Testing");
                queryParamsMap.put("Tag.4.Key", "scope");
                queryParamsMap.put("Tag.4.Value", "For testing purposes");
                break;
            case "CreateSnapshot":
                queryParamsMap.put("Description", "some-desc");
                queryParamsMap.put("VolumeId", "v-12345678");
                break;
            case "CreateVolume":
                queryParamsMap.put("VolumeType", "standard");
                queryParamsMap.put("Size", "10");
                queryParamsMap.put("SnapshotId", "snap-id");
                queryParamsMap.put("AvailabilityZone", "us-east-1d");
                break;
            case "DeleteNetworkInterface":
                queryParamsMap.put("NetworkInterfaceId", "eni-12345678");
                break;
            case "DeleteSnapshot":
                queryParamsMap.put("SnapshotId", "snap-id");
                break;
            case "DeleteVolume":
                queryParamsMap.put("VolumeId", "v-12345678");
                break;
            case "DeregisterImage":
                queryParamsMap.put("ImageId", "ami-abcd1234");
                break;
            case "DetachNetworkInterface":
                queryParamsMap.put("AttachmentId", "eni-attach-12345678");
                break;
            case "DetachVolume":
                queryParamsMap.put("InstanceId", "i-12345678");
                queryParamsMap.put("Device", "device-name");
                queryParamsMap.put("VolumeId", "v-12345678");
                break;
            case "DisassociateAddress":
                queryParamsMap.put("AssociationId", "eipassoc-abcdef12");
                queryParamsMap.put("PublicIp", "52.0.0.2");
                break;
            case "DescribeImageAttribute":
                queryParamsMap.put("ImageId", "ami-abcd1234");
                queryParamsMap.put("Attribute", "launchPermission");
                break;
            case "DescribeImages":
                queryParamsMap.put("Filter.2.Value", "true");
                queryParamsMap.put("Filter.27.Name", "name");
                queryParamsMap.put("Filter.21.Name", "tag-value");
                queryParamsMap.put("Filter.13.Name", "product-code");
                queryParamsMap.put("Filter.7.Name", "hypervisor");
                queryParamsMap.put("Filter.28.Value", "available");
                queryParamsMap.put("Filter.9.Value", "kernel-id");
                queryParamsMap.put("Filter.18.Name", "state-reason-code");
                queryParamsMap.put("Filter.6.Name", "block-device-mapping.volume-type");
                queryParamsMap.put("Filter.11.Value", "o-id");
                queryParamsMap.put("Filter.21.Value", "my-value-tags");
                queryParamsMap.put("Filter.6.Value", "gp2");
                queryParamsMap.put("Filter.22.Name", "virtualization-type");
                queryParamsMap.put("Filter.26.Name", "manifest-location");
                queryParamsMap.put("Filter.14.Name", "product-code.type");
                queryParamsMap.put("Filter.19.Value", "r-message");
                queryParamsMap.put("Filter.3.Value", "/dev/sdh");
                queryParamsMap.put("Filter.24.Value", "machine");
                queryParamsMap.put("Filter.1.Value", "i386");
                queryParamsMap.put("Filter.12.Value", "windows");
                queryParamsMap.put("Filter.27.Value", "img-name");
                queryParamsMap.put("Filter.20.Value", "my-key-tags");
                queryParamsMap.put("Filter.18.Value", "r-code");
                queryParamsMap.put("Filter.7.Value", "ovm");
                queryParamsMap.put("Filter.19.Name", "state-reason-message");
                queryParamsMap.put("Filter.4.Name", "block-device-mapping.snapshot-id");
                queryParamsMap.put("Filter.1.Name", "architecture");
                queryParamsMap.put("Filter.16.Name", "root-device-name");
                queryParamsMap.put("Filter.15.Value", "id-ram");
                queryParamsMap.put("Filter.24.Name", "image-type");
                queryParamsMap.put("Filter.10.Name", "owner-alias");
                queryParamsMap.put("Filter.23.Value", "some-desc");
                queryParamsMap.put("Filter.4.Value", "my-id");
                queryParamsMap.put("Filter.13.Value", "p-code");
                queryParamsMap.put("Owner.2", "o_id1");
                queryParamsMap.put("Owner.1", "o_id2");
                queryParamsMap.put("Filter.3.Name", "block-device-mapping.device-name");
                queryParamsMap.put("Filter.15.Name", "ramdisk-id");
                queryParamsMap.put("Filter.25.Name", "is-public");
                queryParamsMap.put("Filter.26.Value", "manif-location");
                queryParamsMap.put("Filter.16.Value", "/dev/sda1");
                queryParamsMap.put("Filter.11.Name", "owner-id");
                queryParamsMap.put("ExecutableBy.1", "my-id");
                queryParamsMap.put("Filter.9.Name", "kernel-id");
                queryParamsMap.put("Filter.8.Value", "ami-abcd1234");
                queryParamsMap.put("Filter.14.Value", "devpay");
                queryParamsMap.put("Filter.8.Name", "image-id");
                queryParamsMap.put("Filter.23.Name", "description");
                queryParamsMap.put("Filter.20.Name", "tag-key");
                queryParamsMap.put("Filter.2.Name", "block-device-mapping.delete-on-termination");
                queryParamsMap.put("Filter.17.Name", "root-device-type");
                queryParamsMap.put("Filter.22.Value", "paravirtual");
                queryParamsMap.put("ImageId.2", "i_id1");
                queryParamsMap.put("Filter.12.Name", "platform");
                queryParamsMap.put("Filter.5.Name", "block-device-mapping.volume-size");
                queryParamsMap.put("Filter.17.Value", "available");
                queryParamsMap.put("ImageId.1", "i_id2");
                queryParamsMap.put("Filter.28.Name", "state");
                queryParamsMap.put("Filter.5.Value", "25.0");
                queryParamsMap.put("Filter.25.Value", "true");
                queryParamsMap.put("Filter.10.Value", "amazon");
                break;
            case "DescribeRegions":
                queryParamsMap.put("RegionName.1", "us-east-1");
                queryParamsMap.put("RegionName.2", "eu-central-1");
                break;
            case "ModifyImageAttribute":
                queryParamsMap.put("Attribute", "launchPermission");
                queryParamsMap.put("OperationType", "add");
                queryParamsMap.put("ImageId", "ami-abcd1234");
                queryParamsMap.put("UserId.2", "1");
                queryParamsMap.put("UserId.1", "2");
                queryParamsMap.put("UserGroup.2", "g1");
                queryParamsMap.put("UserGroup.1", "g2");
                break;
            case "RebootInstances":
                queryParamsMap.put("InstanceId.1", "i-12345678");
                break;
            case "ReleaseAddress":
                queryParamsMap.put("AllocationId", "eipalloc-abcdef12");
                queryParamsMap.put("PublicIp", "52.0.0.2");
                break;
            case "ResetImageAttribute":
                queryParamsMap.put("Attribute", "launchPermission");
                queryParamsMap.put("ImageId", "ami-abcd1234");
                break;
            case "StartInstances":
                queryParamsMap.put("InstanceId.1", "i-12345678");
                break;
            case "StopInstances":
                queryParamsMap.put("InstanceId.1", "i-12345678");
                queryParamsMap.put("Force", "true");
                break;
            case "TerminateInstances":
                queryParamsMap.put("InstanceId.1", "i-12345678");
                break;
            default:
                throw new RuntimeException("You forgot to setup queryParamsMap naughty developer!");
        }

        return queryParamsMap;
    }

    private CustomInputs getVolumeCustomInputs() {
        return new CustomInputs.Builder()
                .withInstanceId("i-12345678")
                .withVolumeId("v-12345678")
                .build();
    }

    private ImageInputs getAddLaunchPermissionsToImageInputs() {
        CustomInputs customInputs = new CustomInputs.Builder()
                .withAttribute("launchPermission")
                .withOperationType("add")
                .withImageId("ami-abcd1234")
                .build();
        return new ImageInputs.Builder()
                .withCustomInputs(customInputs)
                .withUserIdsString("1,2")
                .withUserGroupsString("g1,g2")
                .build();
    }

    private ImageInputs getCreateImageInputs() {
        CustomInputs customInputs = new CustomInputs.Builder()
                .withInstanceId("i-b0e2ad1b")
                .build();
        return new ImageInputs.Builder()
                .withCustomInputs(customInputs)
                .withImageName("img-name")
                .withDescription("Some description")
                .withImageNoReboot("true")
                .build();
    }

    private ImageInputs getDescribeImagesInputs() throws Exception {
        CustomInputs customInputs = new CustomInputs.Builder()
                .withIdentityId("my-id")
                .withArchitecture("i386")
                .withDeleteOnTermination("true")
                .withBlockMappingDeviceName("/dev/sdh")
                .withBlockDeviceMappingSnapshotId("my-id")
                .withVolumeSize("25")
                .withVolumeType("gp2")
                .withHypervisor("ovm")
                .withImageId("ami-abcd1234")
                .withKernelId("kernel-id")
                .withOwnerAlias("amazon")
                .withOwnerId("o-id")
                .withPlatform("windows")
                .withProductCode("p-code")
                .withProductCodeType("devpay")
                .withRamdiskId("id-ram")
                .withRootDeviceName("/dev/sda1")
                .withRootDeviceType("available")
                .withStateReasonCode("r-code")
                .withStateReasonMessage("r-message")
                .withKeyTagsString("my-key-tags")
                .withValueTagsString("my-value-tags")
                .withVirtualizationType("paravirtual")
                .build();
        return new ImageInputs.Builder()
                .withCustomInputs(customInputs)
                .withDescription("some-desc")
                .withImageIdsString("i_id1,i_id2")
                .withOwnersString("o_id1,o_id2")
                .withType("machine")
                .withIsPublic("true")
                .withManifestLocation("manif-location")
                .withImageName("img-name")
                .withState("available")
                .build();
    }

    private InstanceInputs getStopInstancesInstanceInputs() {
        return new InstanceInputs.Builder().withForceStop("true").build();
    }

    private CustomInputs getInstanceCustomInputs() {
        return new CustomInputs.Builder().withInstanceId("i-12345678").build();
    }

    private CustomInputs getStartInstancesInputs() {
        return new CustomInputs.Builder().withInstanceId("i-12345678").build();
    }
}