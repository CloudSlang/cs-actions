cloud-slang-content/virtualization
=============

This repository includes CloudSlang flows and operations needed to work with VMware.

#### Getting started:

###### Pre-Requisite: vim25.jar

1. Go to (https://my.vmware.com/web/vmware) and register.
2. Go to (https://my.vmware.com/group/vmware/get-download?downloadGroup=MNGMTSDK600) and download the `VMware-vSphere-SDK-6.0.0-2561048.zip`.
3. Locate the vim25.jar in `../VMware-vSphere-SDK-6.0.0-2561048/SDK/vsphere-ws/java/JAXWS/lib`
4. Copy the vim25.jar into the CloudSlang CLI folder under `/cslang/lib`.

###### How to use certificates for authentication?

1. Go to (https://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.dsg.doc_50%2Fsdk_java_development.4.3.html) to see how to import a certificate into Java Keystore.
2. Go to (https://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.dsg.doc_50%2Fsdk_sg_server_certificate_Appendix.6.4.html) to see how to obtain a valid vCenter certificate.


###### How to add the vim25.jar and build VMware Java actions?

1. Clone the repository at (https://github.com/CloudSlang/score-actions).
2. Add the VMware proprietary dependency, the `vim25.jar`, using (https://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html) as a guide or just copy the jar file into you local Maven repository.
	Note: We assume that your local Maven repository default path will be: `${env.USERPROFILE}\.m2\repository\` for Windows OSes or: `${env.HOME}/.m2/repository/` for Linux based OSes. In those cases the dependecy management from the POM file for the VMware vim25.jar will look like this:  `<vmware_path>${env.USERPROFILE}\.m2\repository\com\vmware\vim25\1.0\</vmware_path>` for Windows OSes and `<vmware_path>${env.HOME}/.m2/repository/com/vmware/vim25/1.0/</vmware_path>` for Linux based OSes.
	Default paths can be customized using the `vmware_path` system property.
3. Build the VMware actions locally using Maven. In the `''vmware` folder in your repository directory `<repository_path>/''vmware` run the `mvn clean install` command.

#### Documentation :

All documentation is available on the [CloudSlang website](http://www.cloudslang.io/#/docs).

#### Get Involved

Read our contributing guide [here](CONTRIBUTING.md).

Contact us [here](mailto:support@cloudslang.io).