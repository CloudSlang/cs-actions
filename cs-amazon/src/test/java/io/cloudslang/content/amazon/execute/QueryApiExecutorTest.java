/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.execute;

import io.cloudslang.content.amazon.entities.aws.AuthorizationHeader;
import io.cloudslang.content.amazon.entities.aws.VolumeFilter;
import io.cloudslang.content.amazon.entities.inputs.*;
import io.cloudslang.content.amazon.factory.ParamsMapBuilder;
import io.cloudslang.content.amazon.services.AmazonSignatureService;
import io.cloudslang.content.amazon.utils.InputsUtil;
import io.cloudslang.content.amazon.utils.MockingHelper;
import io.cloudslang.content.httpclient.CSHttpClient;
import io.cloudslang.content.httpclient.HttpClientInputs;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.COMMA_DELIMITER;
import static io.cloudslang.content.amazon.factory.helpers.FilterUtils.processTagFilter;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by Mihai Tusa.
 * 9/7/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CSHttpClient.class, AmazonSignatureService.class, QueryApiExecutor.class, ParamsMapBuilder.class, InputsUtil.class})
public class QueryApiExecutorTest {
    private static final String HEADERS = "Accept:text/plain\r\n Content-Type:application/json";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private CSHttpClient csHttpClientMock;

    @Mock
    private AmazonSignatureService amazonSignatureServiceMock;

    @Mock
    private AuthorizationHeader authorizationHeaderMock;

    @Mock
    private InputsUtil inputsUtilMock;

    private QueryApiExecutor toTest;

    @Before
    public void init() throws Exception {
        toTest = new QueryApiExecutor();
        addCommonMocksForQueryApi();
    }

    @After
    public void tearDown() {
        toTest = null;
    }

    @Test
    public void testAddLaunchPermissionsToImage() throws Exception {
        toTest.execute(getCommonInputs("ModifyImageAttribute", HEADERS), getAddLaunchPermissionsToImageInputs(), getAddLaunchPermissionsToImageCustomInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("ModifyImageAttribute")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testAllocateAddress() throws Exception {
        toTest.execute(getCommonInputs("AllocateAddress", HEADERS), getCustomInputs(), getElasticIpInputs(),
                getNetworkInputs(true));

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("AllocateAddress")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testAssociateAddress() throws Exception {
        toTest.execute(getCommonInputs("AssociateAddress", HEADERS), getCustomInputs(), getElasticIpInputs(),
                getNetworkInputs(true));

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("AssociateAddress")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testAttachNetworkInterface() throws Exception {
        toTest.execute(getCommonInputs("DeleteNetworkInterface", HEADERS), getCustomInputs(), getNetworkInputs(true));

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DeleteNetworkInterface")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testAttachVolume() throws Exception {
        toTest.execute(getCommonInputs("AttachVolume", HEADERS), getVolumeCustomInputs(), getVolumeInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("AttachVolume")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testCreateLoadBalancer() throws Exception {
        toTest.execute(getCommonInputsForLoadBalancers("CreateLoadBalancer", HEADERS), getCustomInputs(), getIamInputs(),
                getLoadBalancerInputs(), getNetworkInputsForLoadBalancers());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("CreateLoadBalancer")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testDeleteLoadBalancer() throws Exception {
        toTest.execute(getCommonInputsForLoadBalancers("DeleteLoadBalancer", HEADERS), getLoadBalancerInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DeleteLoadBalancer")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testCreateNetworkInterface() throws Exception {
        toTest.execute(getCommonInputs("CreateNetworkInterface", HEADERS), getCustomInputs(), getElasticIpInputs(),
                getIamInputs(), getNetworkInputs(false));

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("CreateNetworkInterface")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testCreateImage() throws Exception {
        toTest.execute(getCommonInputs("CreateImage", HEADERS), getCreateImageInputs(), getCreateImageCustomInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("CreateImage")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testCreateSnapshot() throws Exception {
        toTest.execute(getCommonInputs("CreateSnapshot", HEADERS), getVolumeCustomInputs(), getVolumeInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("CreateSnapshot")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testCreateSubnet() throws Exception {
        toTest.execute(getCommonInputs("CreateSubnet", HEADERS), getCustomInputs(), getNetworkInputs(false));

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("CreateSubnet")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testCreateTags() throws Exception {
        toTest.execute(getCommonInputs("CreateTags", HEADERS), getCustomInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("CreateTags")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testCreateVolume() throws Exception {
        toTest.execute(getCommonInputs("CreateVolume", HEADERS), getCustomInputs(), getVolumeInputs(),
                getNetworkInputs(false));

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("CreateVolume")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testDeleteNetworkInterface() throws Exception {
        toTest.execute(getCommonInputs("AttachNetworkInterface", HEADERS), getCustomInputs(), getNetworkInputs(true));

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("AttachNetworkInterface")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testDeleteSnapshot() throws Exception {
        toTest.execute(getCommonInputs("DeleteSnapshot", HEADERS), getVolumeInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DeleteSnapshot")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testDeleteSubnet() throws Exception {
        toTest.execute(getCommonInputs("DeleteSubnet", HEADERS), getCustomInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DeleteSubnet")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testDeleteVolume() throws Exception {
        toTest.execute(getCommonInputs("DeleteVolume", HEADERS), getVolumeCustomInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DeleteVolume")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testDeregisterImage() throws Exception {
        toTest.execute(getCommonInputs("DeregisterImage", HEADERS), getImageCustomInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DeregisterImage")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testDescribeAvailabilityZones() throws Exception {
        toTest.execute(getCommonInputs("DescribeAvailabilityZones", HEADERS), getCustomInputsForDescribeAvailabilityZones());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DescribeAvailabilityZones")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testDescribeImages() throws Exception {
        toTest.execute(getCommonInputs("DescribeImages", HEADERS), getDescribeImagesInputs(), getDescribeImagesCustomInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DescribeImages")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testDescribeVolumes() throws Exception {
        final VolumeInputs volumeInputs = new VolumeInputs.Builder()
                .withVolumeIdsString("1,2,3")
                .withMaxResults("10")
                .withNextToken("token")
                .build();
        final FilterInputs.Builder filterInputsBuilder = new FilterInputs.Builder()
                .withDelimiter(COMMA_DELIMITER)
                .withNewFilter(VolumeFilter.STATUS, "in-use,available")
                .withNewFilter(VolumeFilter.SIZE, "50");
        processTagFilter("TEST=testTag", COMMA_DELIMITER, filterInputsBuilder);
        toTest.execute(getCommonInputs("DescribeVolumes", HEADERS), volumeInputs, filterInputsBuilder.build());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DescribeVolumes")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testDescribeInstances() throws Exception {
        toTest.execute(getCommonInputs("DescribeInstances", HEADERS), getDescribeInstancesInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DescribeInstances")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testDescribeInstanceWithFailureAffinity() throws Exception {
        MockingHelper.setExpectedExceptions(exception, RuntimeException.class, "Invalid affinity value: [WRONG_VALUE]. Valid values: default, host.");
        InstanceInputs instanceInputs = new InstanceInputs.Builder()
                .withFilterNamesString("affinity")
                .withFilterValuesString("WRONG_VALUE")
                .build();
        toTest.execute(getCommonInputs("DescribeInstances", HEADERS), instanceInputs);
    }

    @Test
    public void testDescribeInstanceWithFailureArchitecture() throws Exception {
        MockingHelper.setExpectedExceptions(exception, RuntimeException.class, "Invalid architecture value: [WRONG_VALUE]. Valid values: i386, x86_64.");

        InstanceInputs instanceInputs = new InstanceInputs.Builder()
                .withFilterNamesString("architecture")
                .withFilterValuesString("i386|WRONG_VALUE|x86_64")
                .build();

        toTest.execute(getCommonInputs("DescribeInstances", HEADERS), instanceInputs);
    }

    @Test
    public void testDescribeInstancesWithSuccessAvailabilityZone() throws Exception {
        InstanceInputs instanceInputs = new InstanceInputs.Builder()
                .withFilterNamesString("availability-zone")
                .withFilterValuesString("new_value")
                .withMaxResults("5")
                .build();
        toTest.execute(getCommonInputs("DescribeInstances", HEADERS), instanceInputs);
        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DescribeInstancesSuccess")));
    }

    @Test
    public void testDescribeRegions() throws Exception {
        toTest.execute(getCommonInputs("DescribeRegions", HEADERS), getCustomInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DescribeRegions")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testDetachNetworkInterface() throws Exception {
        toTest.execute(getCommonInputs("DetachNetworkInterface", HEADERS), getCustomInputs(), getNetworkInputs(false));

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DetachNetworkInterface")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testDetachVolume() throws Exception {
        toTest.execute(getCommonInputs("DetachVolume", HEADERS), getVolumeCustomInputs(), getVolumeInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DetachVolume")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testDisassociateAddress() throws Exception {
        toTest.execute(getCommonInputs("DisassociateAddress", HEADERS), getCustomInputs(), getElasticIpInputs(),
                getNetworkInputs(false));

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DisassociateAddress")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testModifyInstanceAttribute() throws Exception {
        toTest.execute(getCommonInputs("ModifyInstanceAttribute", HEADERS), getCustomInputs(),
                getModifyInstanceAttributeEbsinputs(), getModifyInstanceAttributeIamInputs(), getModifyInstanceAttributeInstanceInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("ModifyInstanceAttribute")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testReleaseAddress() throws Exception {
        toTest.execute(getCommonInputs("ReleaseAddress", HEADERS), getCustomInputs(), getElasticIpInputs(),
                getNetworkInputs(false));

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("ReleaseAddress")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testStartInstances() throws Exception {
        toTest.execute(getCommonInputs("StartInstances", HEADERS), getRebootStartStopTerminateInstancesInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("StartInstances")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testStopInstances() throws Exception {
        toTest.execute(getCommonInputs("StopInstances", HEADERS), getRebootStartStopTerminateInstancesInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("StopInstances")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testRebootInstances() throws Exception {
        toTest.execute(getCommonInputs("RebootInstances", HEADERS), getRebootStartStopTerminateInstancesInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("RebootInstances")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testRunInstances() throws Exception {
        toTest.execute(getCommonInputs("RunInstances", HEADERS), getRunInstancesCustomInputs(),
                getRunInstancesEbsInputs(), getRunInstancesElasticIpInputs(), getRunInstancesIamInputs(),
                getRunInstancesInstanceInputs(), getRunInstancesNetworkInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("RunInstances")));
        runCommonVerifiersForQueryApi();
    }

    private InstanceInputs getRunInstancesInstanceInputs() {
        return new InstanceInputs.Builder()
                .withAffinity("affinity")
                .withClientToken("token")
                .withDisableApiTermination("false")
                .withInstanceInitiatedShutdownBehavior("terminate")
                .withMaxCount("1")
                .withMinCount("2")
                .withMonitoring("monitor")
                .withPlacementGroupName("placement")
                .withTenancy("default")
                .withUserData("user_data")
                .build();
    }

    private NetworkInputs getRunInstancesNetworkInputs() {
        return new NetworkInputs.Builder()
                .withNetworkInterfacesAssociatePublicIpAddressesString("false,true")
                .withNetworkInterfaceDeleteOnTermination("true,false")
                .withNetworkInterfaceDescription("description1,description2")
                .withNetworkInterfaceDeviceIndex("0,1")
                .withNetworkInterfaceId("nid1,nid2")
                .withSecondaryPrivateIpAddressCount("3,4")
                .build();
    }

    private IamInputs getRunInstancesIamInputs() {
        return new IamInputs.Builder()
                .withIamInstanceProfileArn("profile_arn")
                .withIamInstanceProfileName("profile_name")
                .withKeyPairName("pair_name")
                .withSecurityGroupIdsString("gid1,gid2")
                .withSecurityGroupNamesString("group_names")
                .build();
    }

    private ElasticIpInputs getRunInstancesElasticIpInputs() {
        return new ElasticIpInputs.Builder()
                .withPrivateIpAddress("")
                .withPrivateIpAddressesString("1.1.1.1|2.2.2.2,3.3.3.3")
                .build();
    }

    private EbsInputs getRunInstancesEbsInputs() {
        return new EbsInputs.Builder()
                .withBlockDeviceMappingDeviceNamesString("key1")
                .withBlockDeviceMappingVirtualNamesString("value1")
                .withDeleteOnTerminationsString("true")
                .withEbsOptimized("optimized")
                .withEncryptedString("enc")
                .withIopsString("iops")
                .withSnapshotIdsString("s_ids")
                .withVolumeSizesString("4")
                .withVolumeTypesString("gp2")
                .build();
    }

    private CustomInputs getRunInstancesCustomInputs() {
        return new CustomInputs.Builder()
                .withAvailabilityZone("eu-east-1")
                .withHostId("host_id")
                .withImageId("image_id")
                .withInstanceType("t2.micro")
                .withKernelId("kernel")
                .withRamdiskId("ramdisk")
                .withSubnetId("subnet1,subnet2")
                .build();
    }

    @Test
    public void testResetLaunchPermissionOnImage() throws Exception {
        toTest.execute(getCommonInputs("ResetImageAttribute", HEADERS), getResetLaunchPermissionOnImageInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("ResetImageAttribute")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testGetLaunchPermissionForImage() throws Exception {
        toTest.execute(getCommonInputs("DescribeImageAttribute", HEADERS), getLaunchPermissionForImageInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DescribeImageAttribute")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testTerminateInstances() throws Exception {
        toTest.execute(getCommonInputs("TerminateInstances", HEADERS), getRebootStartStopTerminateInstancesInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("TerminateInstances")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testExecuteWithException() throws Exception {
        MockingHelper.setExpectedExceptions(exception, RuntimeException.class, "Unsupported Query API.");

        toTest.execute(getCommonInputs("", ""), getCustomInputs(), getVolumeInputs(), getNetworkInputs(false));

        verify(amazonSignatureServiceMock, never()).signRequestHeaders(any(InputsWrapper.class),
                anyMapOf(String.class, String.class), anyMapOf(String.class, String.class));
        verify(csHttpClientMock, never()).execute(any(HttpClientInputs.class));
    }

    @Test
    public void testGetBucket() throws Exception {
        toTest.execute(getStorageCommonInputs(), getStorageInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getS3HeadersMap()),
                eq(getS3QueryParamsmap("GET Bucket")));
        runCommonVerifiersForQueryApi();
    }

    private void addCommonMocksForQueryApi() throws Exception {
        whenNew(AmazonSignatureService.class).withNoArguments().thenReturn(amazonSignatureServiceMock);
        when(amazonSignatureServiceMock
                .signRequestHeaders(any(InputsWrapper.class), anyMapOf(String.class, String.class), anyMapOf(String.class, String.class)))
                .thenReturn(authorizationHeaderMock);
        when(authorizationHeaderMock.getAuthorizationHeader()).thenReturn("");
        when(authorizationHeaderMock.getSignature()).thenReturn("");
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

    private StorageInputs getStorageInputs() {
        return new StorageInputs.Builder()
                .withBucketName("testBucket")
                .withContinuationToken("1ueGcxLPRx1Tr/XYExHnhbYLgveDs2J/wm36Hy4vbOwM=")
                .withEncodingType("url")
                .withFetchOwner("tRuE")
                .withMaxKeys("100")
                .withPrefix("E")
                .withStartAfter("ExampleGuide.pdf")
                .build();
    }

    private EbsInputs getModifyInstanceAttributeEbsinputs() {
        return new EbsInputs.Builder()
                .withEbsOptimized("true")
                .withBlockDeviceMappingDeviceNamesString("/dev/sdh1,xvdh2,/dev/sdh3,xvdh4")
                .withBlockDeviceMappingVirtualNamesString("Permanent,ephemeral0,Not relevant,ephemeral1")
                .withDeleteOnTerminationsString("false,true,true,true")
                .withVolumeIdsString("vol-12345678,vol-abcdef12,vol-456789ab,vol-89abcdef")
                .build();
    }

    private IamInputs getModifyInstanceAttributeIamInputs() {
        return new IamInputs.Builder().withSecurityGroupIdsString("sg-012345678,sg-12345678a,sg-23456789ab,sg-78abcdef").build();
    }

    private InstanceInputs getModifyInstanceAttributeInstanceInputs() {
        return new InstanceInputs.Builder()
                .withAttribute("userData")
                .withAttributeValue("c3Vwb3NlIHRvIGJlIGJhc2U2NCBlbmNvZGVk")
                .withDisableApiTermination("TruE")
                .withEnaSupport("tRUe")
                .withInstanceInitiatedShutdownBehavior("terminate")
                .withKernel("aki-8f9dcae6")
                .withRamdisk("testing purposes")
                .withSourceDestinationCheck("tRuE")
                .withSriovNetSupport("simple")
                .build();
    }

    private CustomInputs getLaunchPermissionForImageInputs() {
        return new CustomInputs.Builder()
                .withAttribute("launchPermission")
                .withImageId("ami-abcd1234")
                .build();
    }

    private CustomInputs getResetLaunchPermissionOnImageInputs() {
        return new CustomInputs.Builder()
                .withAttribute("launchPermission")
                .withImageId("ami-abcd1234")
                .build();
    }

    private CommonInputs getCommonInputs(String action, String headersString) {
        return new CommonInputs.Builder()
                .withAction(action)
                .withHeaders(headersString)
                .withQueryParams("")
                .withApiService("ec2")
                .withVersion("2016-04-01")
                .withDelimiter(",")
                .build();
    }

    private CommonInputs getCommonInputsForLoadBalancers(String action, String headersString) {
        return new CommonInputs.Builder()
                .withAction(action)
                .withHeaders(headersString)
                .withQueryParams("")
                .withApiService("elasticloadbalancing")
                .withVersion("2016-04-01")
                .withDelimiter(",")
                .build();
    }

    private CommonInputs getStorageCommonInputs() {
        return new CommonInputs.Builder()
                .withAction("GET Bucket")
                .withHeaders("")
                .withQueryParams("")
                .withApiService("s3")
                .withDelimiter("/")
                .build();
    }

    private NetworkInputs getNetworkInputsForLoadBalancers() {
        return new NetworkInputs.Builder()
                .withSubnetIdsString("subnet-abcdef12,subnet-12345678")
                .build();
    }

    private CustomInputs getImageCustomInputs() {
        return new CustomInputs.Builder()
                .withImageId("ami-abcd1234")
                .build();
    }

    private CustomInputs getCustomInputs() {
        return new CustomInputs.Builder()
                .withAllocationId("eipalloc-abcdef12")
                .withAssociationId("eipassoc-abcdef12")
                .withAttachmentId("eni-attach-12345678")
                .withAvailabilityZone("us-east-1d")
                .withDomain("")
                .withInstanceId("i-abcdef12")
                .withInstanceType("m4.large")
                .withVolumeType("")
                .withSubnetId("subnet-abcdef12")
                .withResourceIdsString("i-abcdef12,i-12345678")
                .withKeyTagsString("Name,webserver,stack,scope")
                .withValueTagsString("Tagged from API call,Not relevant,Testing,For testing purposes")
                .withRegionsString("us-east-1,eu-central-1")
                .withVpcId("vpc-1a2b3c4d")
                .build();
    }

    private CustomInputs getCustomInputsForDescribeAvailabilityZones() {
        return new CustomInputs.Builder()
                .withAvailabilityZonesString("us-east-1d,eu-central-1a")
                .withKeyFiltersString("state")
                .withValueFiltersString("available")
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

    private NetworkInputs getNetworkInputs(boolean withNetworkInterfaceId) {
        if (withNetworkInterfaceId) {
            return new NetworkInputs.Builder()
                    .withDeviceIndex("25")
                    .withNetworkInterfaceDescription("anything in here")
                    .withNetworkInterfaceId("eni-12345678")
                    .withSecondaryPrivateIpAddressCount("3")
                    .build();
        }
        return new NetworkInputs.Builder()
                .withDeviceIndex("25")
                .withNetworkInterfaceDescription("anything in here")
                .withNetworkInterfacePrivateIpAddress("10.0.0.129")
                .withSecondaryPrivateIpAddressCount("3")
                .withCidrBlock("10.0.1.0/24")
                .build();
    }

    private Map<String, String> getHeadersMap() {
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("accept", "text/plain");
        headersMap.put("content-type", "application/json");

        return headersMap;
    }

    private Map<String, String> getS3HeadersMap() {
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "text/plain");
        headersMap.put("Host", "testBucket.s3.amazonaws.com");

        return headersMap;
    }

    private LoadBalancerInputs getLoadBalancerInputs() {
        return new LoadBalancerInputs.Builder()
                .withLoadBalancerName("testLB")
                .withScheme("internal")
                .withLoadBalancerArn("arn:aws:elasticloadbalancing:us-west-2:123456789012:loadbalancer/app/my-load-balancer/50dc6c495c0c9188")
                .build();
    }

    private Map<String, String> getS3QueryParamsmap(String action) {
        Map<String, String> s3QueryParamsMap = new HashMap<>();
        switch (action) {
            case "GET Bucket":
                s3QueryParamsMap.put("list-type", "2");
                s3QueryParamsMap.put("continuation-token", "1ueGcxLPRx1Tr/XYExHnhbYLgveDs2J/wm36Hy4vbOwM=");
                s3QueryParamsMap.put("delimiter", "/");
                s3QueryParamsMap.put("encoding-type", "url");
                s3QueryParamsMap.put("fetch-owner", "true");
                s3QueryParamsMap.put("max-keys", "100");
                s3QueryParamsMap.put("prefix", "E");
                s3QueryParamsMap.put("start-after", "ExampleGuide.pdf");
                break;
            default:
                throw new RuntimeException("You forgot to setup s3QueryParamsMap naughty developer!");
        }

        return s3QueryParamsMap;
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
            case "CreateLoadBalancer":
                queryParamsMap.put("Name", "testLB");
                queryParamsMap.put("Scheme", "internal");
                queryParamsMap.put("SecurityGroups.member.1", "sg-12345678");
                queryParamsMap.put("SecurityGroups.member.2", "sg-abcdef12");
                queryParamsMap.put("Subnets.member.1", "subnet-abcdef12");
                queryParamsMap.put("Subnets.member.2", "subnet-12345678");
                queryParamsMap.put("Tags.member.1.Key", "Name");
                queryParamsMap.put("Tags.member.1.Value", "Tagged from API call");
                queryParamsMap.put("Tags.member.2.Key", "webserver");
                queryParamsMap.put("Tags.member.2.Value", "Not relevant");
                queryParamsMap.put("Tags.member.3.Key", "stack");
                queryParamsMap.put("Tags.member.3.Value", "Testing");
                queryParamsMap.put("Tags.member.4.Key", "scope");
                queryParamsMap.put("Tags.member.4.Value", "For testing purposes");
                break;
            case "DeleteLoadBalancer":
                queryParamsMap.put("LoadBalancerArn", "arn:aws:elasticloadbalancing:us-west-2:123456789012:loadbalancer/app/my-load-balancer/50dc6c495c0c9188");
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
            case "CreateSubnet":
                queryParamsMap.put("VpcId", "vpc-1a2b3c4d");
                queryParamsMap.put("CidrBlock", "10.0.1.0/24");
                queryParamsMap.put("AvailabilityZone", "us-east-1d");
                break;
            case "CreateVolume":
                queryParamsMap.put("VolumeType", "standard");
                queryParamsMap.put("Size", "10");
                queryParamsMap.put("SnapshotId", "snap-id");
                queryParamsMap.put("AvailabilityZone", "us-east-1d");
                break;
            case "DescribeVolumes":
                queryParamsMap.put("Filter.1.Name", "status");
                queryParamsMap.put("Filter.1.Value.1", "in-use");
                queryParamsMap.put("Filter.1.Value.2", "available");
                queryParamsMap.put("Filter.2.Name", "size");
                queryParamsMap.put("Filter.2.Value.1", "50");
                queryParamsMap.put("Filter.3.Name", "tag:TEST");
                queryParamsMap.put("Filter.3.Value.1", "testTag");
                queryParamsMap.put("VolumeId.1", "1");
                queryParamsMap.put("VolumeId.2", "2");
                queryParamsMap.put("VolumeId.3", "3");
                queryParamsMap.put("MaxResults", "10");
                queryParamsMap.put("NextToken", "token");
                break;
            case "DeleteNetworkInterface":
                queryParamsMap.put("NetworkInterfaceId", "eni-12345678");
                break;
            case "DeleteSnapshot":
                queryParamsMap.put("SnapshotId", "snap-id");
                break;
            case "DeleteSubnet":
                queryParamsMap.put("SubnetId", "subnet-abcdef12");
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
            case "DescribeAvailabilityZones":
                queryParamsMap.put("Filter.1.Name", "state");
                queryParamsMap.put("Filter.1.Value", "available");
                queryParamsMap.put("ZoneName.1", "us-east-1d");
                queryParamsMap.put("ZoneName.2", "eu-central-1a");
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
                queryParamsMap.put("Owner.1", "o_id1");
                queryParamsMap.put("Owner.2", "o_id2");
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
                queryParamsMap.put("ImageId.1", "i_id1");
                queryParamsMap.put("Filter.12.Name", "platform");
                queryParamsMap.put("Filter.5.Name", "block-device-mapping.volume-size");
                queryParamsMap.put("Filter.17.Value", "available");
                queryParamsMap.put("ImageId.2", "i_id2");
                queryParamsMap.put("Filter.28.Name", "state");
                queryParamsMap.put("Filter.5.Value", "25.0");
                queryParamsMap.put("Filter.25.Value", "true");
                queryParamsMap.put("Filter.10.Value", "amazon");
                break;
            case "DescribeInstances":
                queryParamsMap.put("NextToken", "token");
                queryParamsMap.put("MaxResults", "10");
                queryParamsMap.put("InstanceId.1", "instance1");
                queryParamsMap.put("InstanceId.2", "instance2");
                queryParamsMap.put("InstanceId.3", "instance3");
                queryParamsMap.put("Filter.1.Name", "architecture");
                queryParamsMap.put("Filter.1.Value.1", "i386");
                queryParamsMap.put("Filter.1.Value.2", "x86_64");
                queryParamsMap.put("Filter.2.Name", "affinity");
                queryParamsMap.put("Filter.2.Value.1", "default");
                queryParamsMap.put("Filter.2.Value.2", "host");
                queryParamsMap.put("Filter.3.Name", "owner-id");
                queryParamsMap.put("Filter.3.Value.1", "o-id");
                break;
            case "DescribeInstancesSuccess":
                queryParamsMap.put("Action", "DescribeInstances");
                queryParamsMap.put("MaxResults", "5");
                queryParamsMap.put("Filter.1.Name", "availability-zone");
                queryParamsMap.put("Filter.1.Value.1", "new_value");
                break;
            case "DescribeRegions":
                queryParamsMap.put("RegionName.1", "us-east-1");
                queryParamsMap.put("RegionName.2", "eu-central-1");
                break;
            case "ModifyImageAttribute":
                queryParamsMap.put("Attribute", "launchPermission");
                queryParamsMap.put("OperationType", "add");
                queryParamsMap.put("ImageId", "ami-abcd1234");
                queryParamsMap.put("UserId.1", "1");
                queryParamsMap.put("UserId.2", "2");
                queryParamsMap.put("UserGroup.1", "g1");
                queryParamsMap.put("UserGroup.2", "g2");
                break;
            case "ModifyInstanceAttribute":
                queryParamsMap.put("InstanceId", "i-abcdef12");
                queryParamsMap.put("Attribute", "userData");
                queryParamsMap.put("GroupId.1", "sg-012345678");
                queryParamsMap.put("GroupId.2", "sg-12345678a");
                queryParamsMap.put("GroupId.3", "sg-23456789ab");
                queryParamsMap.put("GroupId.4", "sg-78abcdef");
                queryParamsMap.put("BlockDeviceMapping.1.DeviceName", "/dev/sdh1");
                queryParamsMap.put("BlockDeviceMapping.2.DeviceName", "xvdh2");
                queryParamsMap.put("BlockDeviceMapping.3.DeviceName", "/dev/sdh3");
                queryParamsMap.put("BlockDeviceMapping.4.DeviceName", "xvdh4");
                queryParamsMap.put("BlockDeviceMapping.1.Ebs.DeleteOnTermination", "false");
                queryParamsMap.put("BlockDeviceMapping.1.Ebs.VolumeId", "vol-12345678");
                queryParamsMap.put("BlockDeviceMapping.2.Ebs.VolumeId", "vol-abcdef12");
                queryParamsMap.put("BlockDeviceMapping.3.Ebs.VolumeId", "vol-456789ab");
                queryParamsMap.put("BlockDeviceMapping.4.Ebs.VolumeId", "vol-89abcdef");
                queryParamsMap.put("BlockDeviceMapping.1.VirtualName", "Permanent");
                queryParamsMap.put("BlockDeviceMapping.2.VirtualName", "ephemeral0");
                queryParamsMap.put("BlockDeviceMapping.4.VirtualName", "ephemeral1");
                queryParamsMap.put("Value", "c3Vwb3NlIHRvIGJlIGJhc2U2NCBlbmNvZGVk");
                queryParamsMap.put("DisableApiTermination.Value", "true");
                queryParamsMap.put("EbsOptimized.Value", "true");
                queryParamsMap.put("EnaSupport.Value", "true");
                queryParamsMap.put("InstanceInitiatedShutdownBehavior.Value", "terminate");
                queryParamsMap.put("InstanceType.Value", "m4.large");
                queryParamsMap.put("Kernel.Value", "aki-8f9dcae6");
                queryParamsMap.put("Ramdisk.Value", "testing purposes");
                queryParamsMap.put("SourceDestCheck.Value", "true");
                queryParamsMap.put("SriovNetSupport.Value", "simple");
                break;
            case "RebootInstances":
                queryParamsMap.put("InstanceId.1", "i-12345678");
                break;
            case "RunInstances":
                queryParamsMap.put("BlockDeviceMapping.1.Ebs.SnapshotId", "s_ids");
                queryParamsMap.put("Placement.HostId", "host_id");
                queryParamsMap.put("Placement.Tenancy", "default");
                queryParamsMap.put("NetworkInterface.1.SecurityGroupId.1", "gid1");
                queryParamsMap.put("MaxCount", "1");
                queryParamsMap.put("BlockDeviceMapping.1.VirtualName", "value1");
                queryParamsMap.put("NetworkInterface.1.AssociatePublicIpAddress", "false");
                queryParamsMap.put("NetworkInterface.2.PrivateIpAddresses.1.Primary", "true");
                queryParamsMap.put("NetworkInterface.2.DeleteOnTermination", "false");
                queryParamsMap.put("NetworkInterface.1.SecondaryPrivateIpAddressCount", "3");
                queryParamsMap.put("IamInstanceProfile.Name", "profile_name");
                queryParamsMap.put("NetworkInterface.2.SubnetId", "subnet2");
                queryParamsMap.put("ImageId", "image_id");
                queryParamsMap.put("NetworkInterface.2.SecurityGroupId.1", "gid2");
                queryParamsMap.put("NetworkInterface.1.PrivateIpAddresses.1.PrivateIpAddress", "1.1.1.1");
                queryParamsMap.put("NetworkInterface.1.DeleteOnTermination", "true");
                queryParamsMap.put("NetworkInterface.2.NetworkInterfaceId", "nid2");
                queryParamsMap.put("NetworkInterface.1.NetworkInterfaceId", "nid1");
                queryParamsMap.put("BlockDeviceMapping.1.DeviceName", "key1");
                queryParamsMap.put("ClientToken", "token");
                queryParamsMap.put("BlockDeviceMapping.1.Ebs.VolumeType", "gp2");
                queryParamsMap.put("NetworkInterface.1.PrivateIpAddresses.1.Primary", "true");
                queryParamsMap.put("MinCount", "2");
                queryParamsMap.put("NetworkInterface.2.Description", "description2");
                queryParamsMap.put("NetworkInterface.2.DeviceIndex", "1");
                queryParamsMap.put("Placement.AvailabilityZone", "eu-east-1");
                queryParamsMap.put("NetworkInterface.2.AssociatePublicIpAddress", "true");
                queryParamsMap.put("NetworkInterface.2.SecondaryPrivateIpAddressCount", "4");
                queryParamsMap.put("NetworkInterface.1.Description", "description1");
                queryParamsMap.put("UserData", "user_data");
                queryParamsMap.put("NetworkInterface.1.DeviceIndex", "0");
                queryParamsMap.put("KernelId", "kernel");
                queryParamsMap.put("EbsOptimized", "false");
                queryParamsMap.put("NetworkInterface.1.SubnetId", "subnet1");
                queryParamsMap.put("Placement.GroupName", "placement");
                queryParamsMap.put("Placement.Affinity", "affinity");
                queryParamsMap.put("Monitoring.Enabled", "false");
                queryParamsMap.put("NetworkInterface.2.PrivateIpAddresses.1.PrivateIpAddress", "3.3.3.3");
                queryParamsMap.put("InstanceType", "t2.micro");
                queryParamsMap.put("IamInstanceProfile.Arn", "profile_arn");
                queryParamsMap.put("BlockDeviceMapping.1.Ebs.VolumeSize", "4");
                queryParamsMap.put("InstanceInitiatedShutdownBehavior", "terminate");
                queryParamsMap.put("NetworkInterface.1.PrivateIpAddresses.2.Primary", "false");
                queryParamsMap.put("NetworkInterface.1.PrivateIpAddresses.2.PrivateIpAddress", "2.2.2.2");
                queryParamsMap.put("DisableApiTermination", "false");
                queryParamsMap.put("KeyName", "pair_name");
                queryParamsMap.put("RamdiskId", "ramdisk");
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

    private CustomInputs getAddLaunchPermissionsToImageCustomInputs() {
        return new CustomInputs.Builder()
                .withAttribute("launchPermission")
                .withOperationType("add")
                .withImageId("ami-abcd1234")
                .build();
    }

    private ImageInputs getAddLaunchPermissionsToImageInputs() {
        return new ImageInputs.Builder()
                .withUserIdsString("1,2")
                .withUserGroupsString("g1,g2")
                .build();
    }

    private CustomInputs getCreateImageCustomInputs() {
        return new CustomInputs.Builder()
                .withInstanceId("i-b0e2ad1b")
                .build();
    }

    private ImageInputs getCreateImageInputs() {
        return new ImageInputs.Builder()
                .withImageName("img-name")
                .withDescription("Some description")
                .withImageNoReboot("true")
                .build();
    }

    private InstanceInputs getRebootStartStopTerminateInstancesInputs() {
        return new InstanceInputs.Builder().withForceStop("true").withInstanceIdsString("i-12345678").build();
    }

    private CustomInputs getDescribeImagesCustomInputs() {
        return new CustomInputs.Builder()
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
    }

    private ImageInputs getDescribeImagesInputs() {
        return new ImageInputs.Builder()
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

    private InstanceInputs getDescribeInstancesInputs() {
        return new InstanceInputs.Builder()
                .withFilterNamesString("architecture,affinity,owner-id")
                .withFilterValuesString("i386||x86_64,|default|host,||o-id")
                .withInstanceIdsString("instance1,instance2,instance3")
                .withMaxResults("10")
                .withNextToken("token")
                .build();
    }
}
