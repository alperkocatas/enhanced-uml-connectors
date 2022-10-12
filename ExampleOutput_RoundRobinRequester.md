## Example Output from running EcTransformer.java for Round Robin Requester

```
0    INFO  EcTransformer      - Starting...
15   INFO  EcTransformer      - Using e1Model at path: C:\Users\akocatas\Dropbox\payprus-workspace\org.ec.connectors\RoundRobinRequester\RoundRobinRequester_E1.uml
1117 INFO  EcTransformer      - Running E1toE2Transformation
     Starting E1 to E2 Transformation...
        Found a primary enhanced connector: Connector1
        EC_CONFIGURATION=SingleRequesterMultipleProvider
       >> begin: Processing Connector1 as Primary Enhanced Connector..
          Connector configuration is: SingleRequesterMultipleProvider
          CONNECTOR_CLS_MODEL_PATH=EnhancedConnectorTestPkg::ECls::Connector1_ConnectorCls
          Checking if the connector is an assembly connector...
          Creating the connector class ports
          Creating request port class and its ports
          Create Request Port object inside the connector class
          Creating connector betweeen request port object and providing port of connector class
          Creating the connector object...
          Creating connectors between connector object and connected parts...
          Processing secondary enhanced connector: Connector2
          OUTPORT_MULTIPLICITY=2
          Setting multiplicity and return type of the reply buffer
       Done
     Done
4624 INFO  EcTransformer      - E1 to E2 transformation load result: OK
4656 INFO  EcTransformer      - E1 to E2 transformation ran successfully
4656 INFO  EcTransformer      - ----------------------------------------------------------------------------
4656 INFO  EcTransformer      - 
4656 INFO  EcTransformer      - Running E2toAlfTransformation
Starting E2 to Alf Transformation...
   <begin> Alf Code: 
private import Alf::Library::PrimitiveBehaviors::IntegerFunctions::ToString;
private import Alf::Library::PrimitiveBehaviors::IntegerFunctions::*;

package RoundRobinRequester_E1 {
    public abstract class XIfc
    {
        public abstract xOp(in arg:Integer) : Integer;
    }
    
    public class Connector1_ConnectorCls
    {
        public InPort : RequestPortCls[1..1] ordered nonunique;
        public OutPort : XIfc[2..2] ordered nonunique;
        public Index : Integer[1..1] = 1;
        ReplyBuffer : Integer[2..2];
        
        public xOp(in arg:Integer) : Integer
        {
            Integer result = this.OutPort[this.Index].xOp(arg);
            
            this.Index = this.Index + 1;
            
            this.Index = this.Index % 2;   
            
            return result;
        }

        @Create
        public Connector1_ConnectorCls() 
        {
            Integer i = 1;            
            while (i <= 2)             
            {            
                this.InPort[i] = new RequestPortCls(this);            
                i = i + 1;            
            }            

        }

        public class RequestPortCls
        {
            public ownerConnector : Connector1_ConnectorCls[1..1];
            
            @Create
            public RequestPortCls(in c:Connector1_ConnectorCls) 
            {
                this.ownerConnector  = c;                

            }

            public xOp(in arg:Integer) : Integer
            {
                // Let connector object process the request                
                return this.ownerConnector.xOp(arg);                

            }

        }
        
    }
    
}

   <end> Alf Code
Done
4937 INFO  EcTransformer      - E2 to Alf transformation load result: OK
4937 INFO  EcTransformer      - Alf code is captured successfully
4937 INFO  EcTransformer      - E2 to Alf transformation ran successfully
4937 INFO  EcTransformer      - ----------------------------------------------------------------------------
4937 INFO  EcTransformer      - 
4937 INFO  EcTransformer      - Creating alf file: C:\Users\akocatas\Dropbox\payprus-workspace\\AlfRefImp-1.1.0k\dist/alf/Models/RoundRobinRequester_E1.alf
4937 INFO  EcTransformer      - Compiling alf file using alfc
6187 INFO  EcTransformer      - alfc stdout: Alf Reference Implementation v1.1.0k
6204 INFO  EcTransformer      - alfc stdout: Loaded C:\Users\akocatas\Dropbox\payprus-workspace\\AlfRefImp-1.1.0k\dist/alf/Libraries/resources/error-messages.txt
10646 INFO  EcTransformer      - alfc stdout: Loaded file:/C:/Users/akocatas/Dropbox/payprus-workspace//AlfRefImp-1.1.0k/dist/alf/Libraries/uml/Standard.profile.uml
10794 INFO  EcTransformer      - alfc stdout: Loaded file:/C:/Users/akocatas/Dropbox/payprus-workspace//AlfRefImp-1.1.0k/dist/alf/Libraries/uml/fUML.library.uml
12439 INFO  EcTransformer      - alfc stdout: Loaded file:/C:/Users/akocatas/Dropbox/payprus-workspace//AlfRefImp-1.1.0k/dist/alf/Libraries/uml/Alf.library.uml
12641 INFO  EcTransformer      - alfc stdout: Parsed C:\Users\akocatas\Dropbox\payprus-workspace\\AlfRefImp-1.1.0k\dist/alf/Models/RoundRobinRequester_E1.alf
13579 INFO  EcTransformer      - alfc stdout: Mapped successfully.
13745 INFO  EcTransformer      - alfc stdout: Saved to file:/C:\Users\akocatas\Dropbox\payprus-workspace\org.ec.connectors\alf_output/RoundRobinRequester_E1.uml
13949 INFO  EcTransformer      - Alf code compiled successfully
13949 INFO  EcTransformer      - ----------------------------------------------------------------------------
13949 INFO  EcTransformer      - 
13949 INFO  EcTransformer      - Replacing fUML pathmaps
13964 INFO  EcTransformer      - ----------------------------------------------------------------------------
13964 INFO  EcTransformer      - 
13964 INFO  EcTransformer      - Running E2toE3Transformation
   Starting E2 to E3 transformation
     Locating e2 model elements...
     Done
     Locating fUML model elements
     Done
     Fixing unresolved references of connector behavior activity EnhancedConnectorTestPkg::ECls::Connector1_ConnectorCls::Connector1_ConnectorCls$initialization$1
        Found: StructuralFeatureAction: Write(Index)
        Updated structuralFeature field of StructuralFeatureAction Write(Index)
        Found StructuralFeatureAction object: Write(Index).object
        Updating object.type field of StructuralFeatureAction Write(Index) as Connector1_ConnectorCls
        Found StructuralFeatureAction result: Model::RoundRobinRequester_E1::Connector1_ConnectorCls
        Updating result.type field of StructuralFeatureAction Write(Index) as Connector1_ConnectorCls
        Found: ExpansionReqion structuredNodeInput type: Model::RoundRobinRequester_E1::Connector1_ConnectorCls
        Updating type field of structured node input: Iterate(Write(Connector1_ConnectorCls$initializationFlag$1)).input(Fork(ReadSelf.result)) as Connector1_ConnectorCls
        Updating inputPin.type field of StructuralFeatureAction Read(Connector1_ConnectorCls$initializationFlag$1) as Connector1_ConnectorCls
        Updating inputPin.type field of StructuralFeatureAction Write(Connector1_ConnectorCls$initializationFlag$1) as Connector1_ConnectorCls
        Updating outputPin.type field of StructuralFeatureAction Write(Connector1_ConnectorCls$initializationFlag$1) as Connector1_ConnectorCls
        Found: ReadSelfAction result.type: Model::RoundRobinRequester_E1::Connector1_ConnectorCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Replaced 10 references
     Done
     Fixing unresolved references of connector behavior activity EnhancedConnectorTestPkg::ECls::Connector1_ConnectorCls::xOp$method$1
        Found: StructuralFeatureAction: Write(Index)
        Updated structuralFeature field of StructuralFeatureAction Write(Index)
        Found StructuralFeatureAction object: Write(Index).object
        Updating object.type field of StructuralFeatureAction Write(Index) as Connector1_ConnectorCls
        Found StructuralFeatureAction result: Model::RoundRobinRequester_E1::Connector1_ConnectorCls
        Updating result.type field of StructuralFeatureAction Write(Index) as Connector1_ConnectorCls
        Found: StructuralFeatureAction: Read(Index)
        Updated structuralFeature field of StructuralFeatureAction Read(Index)
        Found StructuralFeatureAction object: Read(Index).object
        Updating object.type field of StructuralFeatureAction Read(Index) as Connector1_ConnectorCls
        Found: StructuralFeatureAction: Read(Index)
        Updated structuralFeature field of StructuralFeatureAction Read(Index)
        Found StructuralFeatureAction object: Read(Index).object
        Updating object.type field of StructuralFeatureAction Read(Index) as Connector1_ConnectorCls
        Found: ReadSelfAction result.type: Model::RoundRobinRequester_E1::Connector1_ConnectorCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: ReadSelfAction result.type: Model::RoundRobinRequester_E1::Connector1_ConnectorCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: StructuralFeatureAction: Clear(Index)
        Updated structuralFeature field of StructuralFeatureAction Clear(Index)
        Found StructuralFeatureAction object: Clear(Index).object
        Updating object.type field of StructuralFeatureAction Clear(Index) as Connector1_ConnectorCls
        Found StructuralFeatureAction result: Model::RoundRobinRequester_E1::Connector1_ConnectorCls
        Updating result.type field of StructuralFeatureAction Clear(Index) as Connector1_ConnectorCls
        Found: StructuralFeatureAction: Read(Index)
        Updated structuralFeature field of StructuralFeatureAction Read(Index)
        Found StructuralFeatureAction object: Read(Index).object
        Updating object.type field of StructuralFeatureAction Read(Index) as Connector1_ConnectorCls
        Found: ReadSelfAction result.type: Model::RoundRobinRequester_E1::Connector1_ConnectorCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: StructuralFeatureAction: Read(OutPort)
        Updated structuralFeature field of StructuralFeatureAction Read(OutPort)
        Found StructuralFeatureAction object: Read(OutPort).object
        Updating object.type field of StructuralFeatureAction Read(OutPort) as Connector1_ConnectorCls
        Found StructuralFeatureAction result: Model::RoundRobinRequester_E1::XIfc
        Updating result.type field of StructuralFeatureAction Read(OutPort) as XIfc
        Found: StructuralFeatureAction: Write(Index)
        Updated structuralFeature field of StructuralFeatureAction Write(Index)
        Found StructuralFeatureAction object: Write(Index).object
        Updating object.type field of StructuralFeatureAction Write(Index) as Connector1_ConnectorCls
        Found StructuralFeatureAction result: Model::RoundRobinRequester_E1::Connector1_ConnectorCls
        Updating result.type field of StructuralFeatureAction Write(Index) as Connector1_ConnectorCls
        Found: ReadSelfAction result.type: Model::RoundRobinRequester_E1::Connector1_ConnectorCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: CallOperation operation: Model::RoundRobinRequester_E1::XIfc::xOp
        Updating operation field of CallOperationAction: Call(xOp)
        Found: CallOperation target type: Model::RoundRobinRequester_E1::XIfc
        Updating target.type field of CallOperationAction: Call(xOp) as XIfc
        Found: StructuralFeatureAction: Clear(Index)
        Updated structuralFeature field of StructuralFeatureAction Clear(Index)
        Found StructuralFeatureAction object: Clear(Index).object
        Updating object.type field of StructuralFeatureAction Clear(Index) as Connector1_ConnectorCls
        Found StructuralFeatureAction result: Model::RoundRobinRequester_E1::Connector1_ConnectorCls
        Updating result.type field of StructuralFeatureAction Clear(Index) as Connector1_ConnectorCls
        Found: ReadSelfAction result.type: Model::RoundRobinRequester_E1::Connector1_ConnectorCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: ReadSelfAction result.type: Model::RoundRobinRequester_E1::Connector1_ConnectorCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Replaced 43 references
     Done
     Fixing unresolved references of connector behavior activity EnhancedConnectorTestPkg::ECls::Connector1_ConnectorCls::Connector1_ConnectorCls$method$1
        Found Parameter type: 
        Updating type field of parameter: 
        Found: ActivityParameterNode type: Model::RoundRobinRequester_E1::Connector1_ConnectorCls
        Updating type field of ActivityParameterNode: Return as Connector1_ConnectorCls
        Found: ReadSelfAction result.type: Model::RoundRobinRequester_E1::Connector1_ConnectorCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: ReadSelfAction result.type: Model::RoundRobinRequester_E1::Connector1_ConnectorCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: CreateObjectAction: classifier: RequestPortCls
        Updating classifier field of CreateObjectAction Create(RequestPortCls) as RequestPortCls
        Found: CreateObjectAction: result type: RequestPortCls
        Updating classifier field of CreateObjectAction Create(RequestPortCls) as RequestPortCls
        Found: StructuralFeatureAction: Write(InPort)
        Updated structuralFeature field of StructuralFeatureAction Write(InPort)
        Found StructuralFeatureAction object: Write(InPort).object
        Updating object.type field of StructuralFeatureAction Write(InPort) as Connector1_ConnectorCls
        Found StructuralFeatureAction result: Model::RoundRobinRequester_E1::Connector1_ConnectorCls
        Updating result.type field of StructuralFeatureAction Write(InPort) as Connector1_ConnectorCls
        Updating inputPin.type field of StructuralFeatureAction Write(InPort) as RequestPortCls
        Found: StructuralFeatureAction: Remove(InPort)
        Updated structuralFeature field of StructuralFeatureAction Remove(InPort)
        Found StructuralFeatureAction object: Remove(InPort).object
        Updating object.type field of StructuralFeatureAction Remove(InPort) as Connector1_ConnectorCls
        Found StructuralFeatureAction result: Model::RoundRobinRequester_E1::Connector1_ConnectorCls
        Updating result.type field of StructuralFeatureAction Remove(InPort) as Connector1_ConnectorCls
        Updating inputPin.type field of StructuralFeatureAction Read(Connector1_ConnectorCls$initializationFlag$1) as Connector1_ConnectorCls
        Found: CallOperation target type: Model::RoundRobinRequester_E1::Connector1_ConnectorCls
        Updating target.type field of CallOperationAction: Call(Connector1_ConnectorCls$initialization$1) as Connector1_ConnectorCls
        Found: CallOperation operation: Model::RoundRobinRequester_E1::Connector1_ConnectorCls::RequestPortCls::RequestPortCls
        Updating operation field of CallOperationAction: Call(RequestPortCls)
        Found: CallOperation target type: Model::RoundRobinRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updating target.type field of CallOperationAction: Call(RequestPortCls) as RequestPortCls
        Found: CallOperationAction argument type: Model::RoundRobinRequester_E1::Connector1_ConnectorCls
        Updating argument.type field of CallOperationAction: Call(RequestPortCls) as Connector1_ConnectorCls
        Found CallOperationAction result tyupe: Model::RoundRobinRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updating result.type field of CallOperationAction: Call(RequestPortCls) as RequestPortCls
        Found: ReadSelfAction result.type: Model::RoundRobinRequester_E1::Connector1_ConnectorCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Replaced 25 references
     Done
     Fixing unresolved references of connector behavior activity EnhancedConnectorTestPkg::ECls::Connector1_ConnectorCls::destroy$method$1
        Replaced 0 references
     Done
     Fixing unresolved references of connector behavior activity EnhancedConnectorTestPkg::ECls::Connector1_ConnectorCls::RequestPortCls::RequestPortCls$initialization$1
        Found: ReadSelfAction result.type: Model::RoundRobinRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Updating inputPin.type field of StructuralFeatureAction Write(RequestPortCls$initializationFlag$1) as RequestPortCls
        Updating outputPin.type field of StructuralFeatureAction Write(RequestPortCls$initializationFlag$1) as RequestPortCls
        Updating inputPin.type field of StructuralFeatureAction Read(RequestPortCls$initializationFlag$1) as RequestPortCls
        Found: ExpansionReqion structuredNodeInput type: Model::RoundRobinRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updating type field of structured node input: Iterate(Write(RequestPortCls$initializationFlag$1)).input(Fork(ReadSelf.result)) as RequestPortCls
        Replaced 6 references
     Done
     Fixing unresolved references of connector behavior activity EnhancedConnectorTestPkg::ECls::Connector1_ConnectorCls::RequestPortCls::RequestPortCls$method$1
        Found: StructuralFeatureAction: Clear(ownerConnector)
        Updated structuralFeature field of StructuralFeatureAction Clear(ownerConnector)
        Found StructuralFeatureAction object: Clear(ownerConnector).object
        Updating object.type field of StructuralFeatureAction Clear(ownerConnector) as RequestPortCls
        Found StructuralFeatureAction result: Model::RoundRobinRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updating result.type field of StructuralFeatureAction Clear(ownerConnector) as RequestPortCls
        Found: ActivityParameterNode type: Model::RoundRobinRequester_E1::Connector1_ConnectorCls
        Updating type field of ActivityParameterNode: Input(c) as Connector1_ConnectorCls
        Found Parameter type: 
        Updating type field of parameter: 
        Found: StructuredActivityNode structuredNodeInput type: Model::RoundRobinRequester_E1::Connector1_ConnectorCls
        Updating type field of structured node input: WriteAll(RequestPortCls::ownerConnector).input(RequestPortCls::ownerConnector) as Connector1_ConnectorCls
        Found: ActivityParameterNode type: Model::RoundRobinRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updating type field of ActivityParameterNode: Return as RequestPortCls
        Found: ReadSelfAction result.type: Model::RoundRobinRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: ReadSelfAction result.type: Model::RoundRobinRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: CallOperation target type: Model::RoundRobinRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updating target.type field of CallOperationAction: Call(RequestPortCls$initialization$1) as RequestPortCls
        Updating inputPin.type field of StructuralFeatureAction Read(RequestPortCls$initializationFlag$1) as RequestPortCls
        Found Parameter type: c
        Updating type field of parameter: c
        Found: StructuralFeatureAction: Write(ownerConnector)
        Updated structuralFeature field of StructuralFeatureAction Write(ownerConnector)
        Found StructuralFeatureAction object: Write(ownerConnector).object
        Updating object.type field of StructuralFeatureAction Write(ownerConnector) as RequestPortCls
        Found StructuralFeatureAction result: Model::RoundRobinRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updating result.type field of StructuralFeatureAction Write(ownerConnector) as RequestPortCls
        Updating inputPin.type field of StructuralFeatureAction Write(ownerConnector) as Connector1_ConnectorCls
        Replaced 20 references
     Done
     Fixing unresolved references of connector behavior activity EnhancedConnectorTestPkg::ECls::Connector1_ConnectorCls::RequestPortCls::xOp$method$1
        Found: CallOperation operation: Model::RoundRobinRequester_E1::Connector1_ConnectorCls::xOp
        Updating operation field of CallOperationAction: Call(xOp)
        Found: CallOperation target type: Model::RoundRobinRequester_E1::Connector1_ConnectorCls
        Updating target.type field of CallOperationAction: Call(xOp) as Connector1_ConnectorCls
        Found: StructuralFeatureAction: Read(ownerConnector)
        Updated structuralFeature field of StructuralFeatureAction Read(ownerConnector)
        Found StructuralFeatureAction object: Read(ownerConnector).object
        Updating object.type field of StructuralFeatureAction Read(ownerConnector) as RequestPortCls
        Found StructuralFeatureAction result: Model::RoundRobinRequester_E1::Connector1_ConnectorCls
        Updating result.type field of StructuralFeatureAction Read(ownerConnector) as Connector1_ConnectorCls
        Found: ReadSelfAction result.type: Model::RoundRobinRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Replaced 8 references
     Done
     Fixing unresolved references of connector behavior activity EnhancedConnectorTestPkg::ECls::Connector1_ConnectorCls::RequestPortCls::destroy$method$1
        Replaced 0 references
     Done
   Done
16600 INFO  EcTransformer      - OK
16725 INFO  EcTransformer      - E2toE3Transformation executed successfully
16725 INFO  EcTransformer      - ----------------------------------------------------------------------------
16725 INFO  EcTransformer      - 
16725 INFO  EcTransformer      - Process completed successfully
```
