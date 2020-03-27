# System Requirement Specification Exasol Kinesis Connector

## Introduction 

Exasol Kinesis Connector (short "EKC") is a User Defined Function (UDF) for Exasol that provides the ability to transfer data from [Amazon Kinesis](https://aws.amazon.com/kinesis/) to the Exasol database.

## About This Document

### Target Audience

The target audience are end-users, requirement engineers, software designers and quality assurance. See section ["Stakeholders"](#stakeholders) for more details.

### Goal

The EKC main goal is to provide a UDF for transferring data from Amazon Kinesis directly to the Exasol database. 

### Quality Goals

EKC's main quality goals are:

1. Reliability 
1. Security
1. Usability
1. Performance efficiency

## Stakeholders

### Integrators

Integrators integrate their solution with Exasol. They need to use Exasol as a consumer for the data from Amazon Kinesis.

### Terms and Abbreviations

The following list gives you an overview of terms and abbreviations commonly used in OFT documents.

* Data Consumer: a service retrieving data from Amazon Kinesis shards.
* Data Record: is the unit of data stored in an Amazon Kinesis stream. 
* Shard: an append-only log and a unit of streaming capability of Amazon Kinesis.

## Features

Features are the highest level requirements in this document that describe the main functionality of EKC.

### Transferring Data from Kinesis Streams to Exasol
`feat~transerring-data-from-kinesis-streams-to-exasol~1`

The Exasol Kinesis Connector provides instruments for transferring data directly from [Amazon Kinesis Data Streams](https://aws.amazon.com/kinesis/data-streams/) to the Exasol database.

Needs: req

## Functional Requirements

In this section lists functional requirements from the user's perspective.

### Connect to Kinesis Streams
`req~connect-to-kinesis-streams~1`

EKC connects to Kinesis Streams.

Covers:

* [feat~transerring-data-from-kinesis-streams-to-exasol~1](#transferring-data-from-kinesis-streams-to-exasol)

Needs: dsn

### Import Data to an Exasol Table
`req~transfer-data-to-an-exasol-table~1`

EKC imports data from a Kinesis Stream directly to an Exasol table.

Covers:

* [feat~transerring-data-from-kinesis-streams-to-exasol~1](#transferring-data-from-kinesis-streams-to-exasol)

Needs: dsn

### Import without Loosing Data
`req~transfer-without-loosing-data~1`

EKC imports the data from a Kinesis Stream to an Exasol table without loosing records.

Covers:

* [feat~transerring-data-from-kinesis-streams-to-exasol~1](#transferring-data-from-kinesis-streams-to-exasol)

Needs: dsn

### Import without Duplicates
`req~import-without-duplicates~1`

EKC imports the data from a Kinesis Stream to an Exasol table without duplicates.

Covers:

* [feat~transerring-data-from-kinesis-streams-to-exasol~1](#transferring-data-from-kinesis-streams-to-exasol)

Needs: dsn