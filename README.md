<a href="http://cloudslang.io/">
    <img src="https://camo.githubusercontent.com/ece898cfb3a9cc55353e7ab5d9014cc314af0234/687474703a2f2f692e696d6775722e636f6d2f696849353630562e706e67" alt="CloudSlang logo" title="CloudSlang" align="right" height="60"/>
</a>

CloudSlang Actions
==================

[![Join the chat at https://gitter.im/CloudSlang/cs-actions](https://badges.gitter.im/CloudSlang/cs-actions.svg)](https://gitter.im/CloudSlang/cs-actions?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Build Status](https://travis-ci.org/CloudSlang/cs-actions.svg?branch=master)](https://travis-ci.org/CloudSlang/cs-actions)
[![Coverage Status](https://coveralls.io/repos/github/CloudSlang/cs-actions/badge.svg?branch=master)](https://coveralls.io/github/CloudSlang/cs-actions?branch=master)
[![Issue Count](https://codeclimate.com/github/CloudSlang/cs-actions/badges/issue_count.svg)](https://codeclimate.com/github/CloudSlang/cs-actions)
[![Code Climate](https://codeclimate.com/github/CloudSlang/cs-actions/badges/gpa.svg)](https://codeclimate.com/github/CloudSlang/cs-actions)


#### Table of contents


* [Overview](#Overview)
* [Common Integrations](#CommonIntegrations)
* [Open Source Project](#OpenSourceProject)
* [Content Actions Development Best Practices](#BestPractices)


<a name="Overview"/>


#### Overview


###### This repository contains the sources (Java actions) to be used by operations located inside the cloud-slang-content repository.


<a name="CommonIntegrations"/>


#### Common Integrations


| Module | Release | Description |
| ----- | ----- | ----- |
| cs-amazon | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-amazon/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-amazon) | Integration with Amazon AWS |
| cs-azure | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-azure/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-azure) | Integration with Microsoft Azure |
| cs-commons | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-commons/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-commons) | Common constants and utilities (numbers, collections, strings) |
| cs-couchbase | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-couchbase/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-couchbase) | Integration with Couchbase |
| cs-dca | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-dca/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-dca) | Integration with Micro Focus DCA |
| cs-database | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-database/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-database) | Database queries (MSSQL, MySQL, Oracle) |
| cs-date-time | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-date-time/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-date-time) | Date and Time actions |
| cs-dropbox | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-dropbox/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-dropbox) | DropBox actions |
| cs-google | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-google/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-google) | Integration with Google Cloud - Compute |
| cs-http-client | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-http-client/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-http-client) | HTTP REST calls |
| cs-json | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-json/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-json) | JSON parsing and manipulation actions |
| cs-lists |  [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-lists/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-lists) |String list manipulation actions |
| cs-mail | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-mail/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-mail) | Email actions |
| cs-powershell | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-powershell/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-powershell) | PowerShell actions |
| cs-rft | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-rft/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-rft) | Remote File Transfer actions |
| cs-ssh | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-ssh/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-ssh) | SSH actions |
| cs-utilities | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-utilities/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-utilities) | Utilities actions (Default if Empty) |
| cs-vmware | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-vmware/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-vmware) | Integration with VMWare vCenter Server |
| cs-xml | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-xml/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.cloudslang.content/cs-xml) | XML parsing and manipulation actions | 


<a name="OpenSourceProject"/>


#### Open Source Project


| [CloudSlang Project website](http://cloudslang.io/#/) | [CloudSlang Content source code](https://github.com/CloudSlang/cloud-slang-content) | [CloudSlang Documentation](http://cloudslang-docs.readthedocs.io/en/latest/) |


<a name="BestPractices"/>


#### Content Actions Development Best Practices


Things to consider when [developing new cs-actions](https://github.com/CloudSlang/cs-actions/wiki/Best-Practices-%231-maven-checkstyle-plugin-enforcements)


#### Documentation


All documentation is available on the [CloudSlang website](http://www.cloudslang.io/#/docs).


#### Get Involved


Read our Contribution Guide [here](CONTRIBUTING.md).


Contact us at support@cloudslang.io.
