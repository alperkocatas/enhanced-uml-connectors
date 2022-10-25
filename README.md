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
- git configuration management system
- Eclipse Papyrus 2021-09 or higher. The tools were developed using Eclipse Papyrus version 2021-09, but they were also tested with the latest version of Eclipse Papyrus (Version 2022-03 as of October 2022). Therefore, a version later than 2021-09 should work. 
- QVT Operational SDK 3.10.3 or higher. 
- Java 11.0.13 or higher. 
- The tools were tested in Windows 10, MacOS Monterey and Ubuntu Linux. 

## Installation of Execution Environment
- Clone this repository in a folder on your workstation using the command: 
```
    git clone https://github.com/alperkocatas/enhanced-uml-connectors.git
```
- **Linux and MacOS only**: On MacOS or Linux, cd into directory AlfRefImp-1.1.0k/dist/alf and Use "chmod +x alfc" command to grant execute permissions for the alfc Alf compiler. 
- Install Java 11.0.13 or higher if not already installed. 
- Download and install Eclipse Papyrus from https://www.eclipse.org/papyrus/download.html
- Start Eclipse Papyrus and choose the root directory of this repository as your workspace 
- Install QVT Operational SDK plugin: 
  - Choose *Help->Install new software*, 
  - Select the default update site for the version (e.g. Eclipse Repository - https://download.eclipse.org/releases/2022-03/ for version 2022-03). 
  - Enter *QVT* to filter field, 
  - Select *QVT Operational SDK* from the list, 
  - Click Finish. 
  - Papyrus will request a restart when installation completes. Confirm to restart Papyrus
- Import projects into workspace: 
  - Select File->Import. Then select "Existing projects into workspace". 
  - Select the root directory of this repository in the field "Select root directory". 
  - Select all the projects and click Finish. 
- Run the transformations:
  - Expand the org.ec.transforma.java project, 
  - Right click on the launch configuration file "EcTransformer_RoundRobinRequester.launch", choose "Run As -> EcTransformer_RoundRobinRequester". 
  - This will run all the model transformations for the Round Robin Requester enhanced connector. You can run the transformations for the other example connectors by right clicking on them.

## Run configurations for EcTransformer.java
There are four run configurations under the directory /org.ec.transform.java which can be used to transform each of the example connectors presented in the article: 
- **EcTransformer_RoundRobinRequester.launch:** Run configuration for transforming the Round Robin Requester Connector E1 Model. [Sample output](https://github.com/alperkocatas/enhanced-uml-connectors/blob/main/ExampleOutput_RoundRobinRequester.md).
- **EcTransformer_MultipleDestinationSender.launch:** Run configuration for transforming the Multiple Destination Sender Connector E1 Model.  [Sample output](https://github.com/alperkocatas/enhanced-uml-connectors/blob/main/ExampleOutput_MultipleDestinationSender.md).
- **EcTransformer_LessFrequentSender.launch:** Run configuration for transforming the Less Frequent Sender Connector E1 Model. [Sample output](https://github.com/alperkocatas/enhanced-uml-connectors/blob/main/ExampleOutput_LessFrequentSender.md).
- **EcTransformer_RequestBarrier.launch:** Run configuration for transforming the Request Barrier Connector E1 Model. [Sample output](https://github.com/alperkocatas/enhanced-uml-connectors/blob/main/ExampleOutput_RequestBarrier.md).

Output E3 models are saved under the same directory as the E1 input models. These directories are: 

## Detailed information for org.ec.connectors project
- Directory *RoundRobinRequester* contains the E1 model files for the *Round Robin Requester* connector. 
- When the *Papyrus perspective* is used by selecting *Window-> Perspective -> Open Perspective -> Other -> Papyrus*, Papyrus groups the model files as: "di, notation and uml", under *RoundRobinRequester_E1* model file. 
- You can double click on the *RoundRobinRequester_E1* model file open it. You can then click on the *Composite Structure Diagram* link under the *Notation Views* section in the *Welcome* tab for the model file to view the structure diagram for the connector. 
- Under the *RoundRobinRequester* directory, the UML model file *RoundRobinRequester_E1.uml* is the E1 level input UML model file. The files *RoundRobinRequester_E1_E2.uml* and *RoundRobinRequester_E1_E3.uml* are the outputs of the E1toE2 and E2toE3 transformations. You can view the UML model files by right clicking on them and then choosing *"Open with -> UML Model Editor"*. 
- Directories *MultiDestRequester, LessFrequentSender* and *RequestBarrier* are for the other example connectors, and they are organized similarly. 
- Directory *alf_output* is the directory where fUML model output of the alfc compiler is stored. 


## Command-line arguments for EcTransformer.java: 
To use the run configurations, please update the values of the following command line arguments in run-configurations with the specific installation path on your workstation: 
- ***-wsDir:*** Directory of the repository. This is provided as "${project_loc}/.." in the run configurations to unify the definition across platforms. 
- ***-eclipsePluginsDir:*** directory of the "plugins" directory under Eclipse Papyrus installation. This is provided as "${eclipse_home}plugins" in the run configurations to unify the definition across platforms. 
- ***-e1Model:*** A choice of the example connectors defined in the study. Valid options are: *RoundRobinRequester, MultiDestRequester, RequestBarrier, LessFrequentSender.*
- ***-e1ModelPath:*** If -e1Model option is not provided, this option can be used to provide the path of a specific E1 model file as input. Note that the path should be relative to the -wsDir argument. 


## Troubleshooting
- We use the *project_loc* variable to get the location of the E1 models. If eclipse complains that *project_loc* is not set, please select the org.ec.transform.java project from the Project Explorer in eclipse. When no project or resource is selected, this environment variable is not set. 

Please e-mail me at: alperkocatas@gmail.com, tolga.kocatas@metu.edu.tr for further problems. 
