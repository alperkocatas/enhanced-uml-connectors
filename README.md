# Enhanced Uml Connectors

This repository contains the related model and source files for the study **"Enhancing UML Connectors with Precise Behavioral Specifications"**, submitted to **IEEE Access**. 

Enhanced UML Connectors​ are UML connectors with additional associated behaviors. Associating behaviors with connectors solve the following problems: First, as a result of the coordination logic being pushed outside of the connected components, designs of the components are simplified. Since the components have more focused responsibilities, their reusability increases. Second, the behaviors associated with connectors help resolve ambiguities when n-ary connectors are used to coordinate more than two entities. Finally, a specific connector behavior can be reused in different cases. 

Action Language for Foundational UML (ALF) has been used to specify connector behaviors. A series of model transformations are developed using QVT Operational Mappings Language (QVTo). The model transformations transform UML models including connector behaviors in terms of ALF specifications into UML models which include fUML activities as connector behavior specifications. Resulting UML models are platform-independent models which can be transformed into code using model-to-text transformation methods, or into another model for different purposes. 


## Directory Descriptions: 
- **AlfRefImp-1.1.0k:** A copy of the Alf reference implementation. 
- **org.ec.connectors:** E1 level UML Model files for example connectors presented in the article: 
  - *Round Robin Requester:* RoundRobinRequester/RoundRobinRequester_E1.uml
  - *Multiple Destination Sender:* MultiDestRequester/MultiDestRequester_E1.uml
  - *Less Frequent Sender:* LessFrequentRequester/LessFrequentRequester_E1.uml
  - *Request Barrier:* RequestBarrier/RequestBarrier_E1.uml
- **org.ec.transform.qvto:** QVTo transformations source files for: 
  - *E1toE2:* Model transformation rules from E1 model into E2 model
  - *E2toAlf:* Model transformation rules for generating Alf code from E2 model
  - *E2toE3:* Model transformation rules from E3 model into E3 model
- **org.ec.profile:** The enhanced connector profile (Papyrus profile project)
- **org.ec.transform.java:** Java project which contains the EcTransformer. This application automatically runs the model transformations in required order. Input is an E1 level model. Output is an E3 level model saved under org.ec.connectors in the same directory as the input E1 models. 

## Prerequisites
- Eclipse Papyrus version 2021-09 (4.21.0) or higher, with QVT Operational SDK 3.10.3.v20210309-1855 installed. 
- Java 11.0.13 or higher

## Installation of Execution Environment
- Clone this repository in a folder on your workstation,
- Install Java 11.0.13 or higher if not already installed, 
- Install Eclipse Papyrus and make sure that QVT Operational SDK plugin is also included. If not, use Help->Install new software and use the update site: https://download.eclipse.org/releases/2021-03/ to download and install the QVT Operational SDK plugin, 
- Open Eclipse Papyrus and choose the root directory of this repository as your workspace. 
- Use the run-configurations provided below to run the model transformations for the example ehanced connectors. 

## Run configurations for EcTransformer.java
There are four run configurations under the directory /org.ec.transform.java which can be used to transform each of the example connectors presented in the article: 
- **EcTransformer_RoundRobinRequester.launch:** Run configuration for transforming the Round Robin Requester Connector E1 Model. [Click here for sample output](https://github.com/alperkocatas/enhanced-uml-connectors/blob/main/ExampleOutput_RoundRobinRequester.md).
- **EcTransformer_MultipleDestinationSender.launch:** Run configuration for transforming the Multiple Destination Sender Connector E1 Model.  [Click here for sample output](https://github.com/alperkocatas/enhanced-uml-connectors/blob/main/ExampleOutput_MultipleDestinationSender.md).
- **EcTransformer_LessFrequentSender.launch:** Run configuration for transforming the Less Frequent Sender Connector E1 Model. [Click here for sample output](https://github.com/alperkocatas/enhanced-uml-connectors/blob/main/ExampleOutput_LessFrequentSender.md).
- **EcTransformer_RequestBarrier.launch:** Run configuration for transforming the Request Barrier Connector E1 Model. [Click here for sample output](https://github.com/alperkocatas/enhanced-uml-connectors/blob/main/ExampleOutput_RequestBarrier.md).

Output E3 models are saved under the same directory as the E1 input models. These directories are: 


## Command-line arguments for EcTransformer.java: 
To use the run configurations, please update the values of the following command line arguments in run-configurations with the specific installation path on your workstation: 
- ***-wsDir:*** Directory of the repository. 
- ***-eclipsePluginsDir:*** directory of the "plugins" directory under Eclipse Papyrus installation. 
- ***-e1Model:*** A choice of the example connectors defined in the study. Valid options are: *RoundRobinRequester, MultiDestRequester, RequestBarrier, LessFrequentSender.*
- ***-e1ModelPath:*** If -e1Model option is not provided, this option can be used to provide the path of a specific E1 model file as input. Note that the path should be relative to the -wsDir argument. 
