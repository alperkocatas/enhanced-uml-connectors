## Example Output from running EcTransformer.java for Less Frequent Sender

```
0    INFO  EcTransformer      - Starting...
16   INFO  EcTransformer      - Using e1Model at path: C:\Users\akocatas\Dropbox\payprus-workspace\org.ec.connectors\LessFrequentRequester\LessFrequentRequester_E1.uml
1090 INFO  EcTransformer      - Running E1toE2Transformation
     Starting E1 to E2 Transformation...
        Found a primary enhanced connector: Connector1
        EC_CONFIGURATION=MultipleRequesterSingleProvider
       >> begin: Processing Connector1 as Primary Enhanced Connector..
          Connector configuration is: MultipleRequesterSingleProvider
          CONNECTOR_CLS_MODEL_PATH=EnhancedConnectorTestPkg::ECls::Connector1_ConnectorCls
          Checking if the connector is an assembly connector...
          Creating the connector class ports
          Creating request port class and its ports
          Create Request Port object inside the connector class
          Creating connector betweeen request port object and providing port of connector class
          Creating the connector object...
          Creating connectors between connector object and connected parts...
          INPORT_MULTIPLICITY=1
       Done
     Done
4560 INFO  EcTransformer      - E1 to E2 transformation load result: OK
4591 INFO  EcTransformer      - E1 to E2 transformation ran successfully
4591 INFO  EcTransformer      - ----------------------------------------------------------------------------
4591 INFO  EcTransformer      - 
4591 INFO  EcTransformer      - Running E2toAlfTransformation
Starting E2 to Alf Transformation...
   <begin> Alf Code: 
private import Alf::Library::PrimitiveBehaviors::IntegerFunctions::ToString;
private import Alf::Library::PrimitiveBehaviors::IntegerFunctions::*;

package LessFrequentRequester_E1 {
    public abstract class XIfc
    {
        public abstract xOp(in arg:Integer) : Integer;
    }
    
    public class Connector1_ConnectorCls
    {
        public InPort : RequestPortCls[1..1] ordered nonunique;
        public OutPort : XIfc[1..1] ordered nonunique;
        public MaxRequestCount : Integer[1..1] = 10;
        public DefaultReply : Integer[1..1] = 0;
        
        public xOp(in arg:Integer) : Integer
        {
            Integer returnValue = this.DefaultReply;
            
            
            
            if (this.InPort[1].RequestBuffer.size() == this.MaxRequestCount)
            
            {
            
                // Merge requests:
            
                Integer sum = 0;
            
                while (this.InPort[1].RequestBuffer.size() != 0)
            
                {
            
                    Integer request = this.InPort[1].RequestBuffer.removeFirst();
            
                    if (request != null)
            
                    {
            
                        sum = sum + request;
            
                    }
            
                }
            
                
            
                returnValue = sum;
            
            }
            
            
            
            return returnValue;
        }

        @Create
        public Connector1_ConnectorCls() 
        {
            Integer i = 1;            
            while (i <= 1)             
            {            
                this.InPort[i] = new RequestPortCls(this);            
                i = i + 1;            
            }            

        }

        public class RequestPortCls
        {
            public RequestBuffer : Queue<Integer>[1..1];
            public ownerConnector : Connector1_ConnectorCls[1..1];
            
            @Create
            public RequestPortCls(in c:Connector1_ConnectorCls) 
            {
                this.ownerConnector  = c;                
                this.RequestBuffer = new Queue<Integer>();                

            }

            public xOp(in arg:Integer) : Integer
            {
                // Store incoming request in request buffer                
                this.RequestBuffer.add(arg);                
                
                // Let connector object process the request                
                return this.ownerConnector.xOp(arg);                

            }

        }
        
    }
    
}

   <end> Alf Code
Done
4826 INFO  EcTransformer      - E2 to Alf transformation load result: OK
4826 INFO  EcTransformer      - Alf code is captured successfully
4826 INFO  EcTransformer      - E2 to Alf transformation ran successfully
4826 INFO  EcTransformer      - ----------------------------------------------------------------------------
4826 INFO  EcTransformer      - 
4826 INFO  EcTransformer      - Creating alf file: C:\Users\akocatas\Dropbox\payprus-workspace\\AlfRefImp-1.1.0k\dist/alf/Models/LessFrequentRequester_E1.alf
4826 INFO  EcTransformer      - Compiling alf file using alfc
6185 INFO  EcTransformer      - alfc stdout: Alf Reference Implementation v1.1.0k
6187 INFO  EcTransformer      - alfc stdout: Loaded C:\Users\akocatas\Dropbox\payprus-workspace\\AlfRefImp-1.1.0k\dist/alf/Libraries/resources/error-messages.txt
10343 INFO  EcTransformer      - alfc stdout: Loaded file:/C:/Users/akocatas/Dropbox/payprus-workspace//AlfRefImp-1.1.0k/dist/alf/Libraries/uml/Standard.profile.uml
10489 INFO  EcTransformer      - alfc stdout: Loaded file:/C:/Users/akocatas/Dropbox/payprus-workspace//AlfRefImp-1.1.0k/dist/alf/Libraries/uml/fUML.library.uml
12223 INFO  EcTransformer      - alfc stdout: Loaded file:/C:/Users/akocatas/Dropbox/payprus-workspace//AlfRefImp-1.1.0k/dist/alf/Libraries/uml/Alf.library.uml
12430 INFO  EcTransformer      - alfc stdout: Parsed C:\Users\akocatas\Dropbox\payprus-workspace\\AlfRefImp-1.1.0k\dist/alf/Models/LessFrequentRequester_E1.alf
13821 INFO  EcTransformer      - alfc stdout: Mapped successfully.
14125 INFO  EcTransformer      - alfc stdout: Saved to file:/C:\Users\akocatas\Dropbox\payprus-workspace\org.ec.connectors\alf_output/LessFrequentRequester_E1.uml
14468 INFO  EcTransformer      - Alf code compiled successfully
14468 INFO  EcTransformer      - ----------------------------------------------------------------------------
14468 INFO  EcTransformer      - 
14468 INFO  EcTransformer      - Replacing fUML pathmaps
14484 INFO  EcTransformer      - ----------------------------------------------------------------------------
14484 INFO  EcTransformer      - 
14484 INFO  EcTransformer      - Running E2toE3Transformation
   Starting E2 to E3 transformation
     Locating e2 model elements...
     Done
     Locating fUML model elements
     Done
     Fixing unresolved references of connector behavior activity EnhancedConnectorTestPkg::ECls::Connector1_ConnectorCls::Connector1_ConnectorCls$initialization$1
        Found: StructuralFeatureAction: Write(MaxRequestCount)
        Updated structuralFeature field of StructuralFeatureAction Write(MaxRequestCount)
        Found StructuralFeatureAction object: Write(MaxRequestCount).object
        Updating object.type field of StructuralFeatureAction Write(MaxRequestCount) as Connector1_ConnectorCls
        Found StructuralFeatureAction result: Model::LessFrequentRequester_E1::Connector1_ConnectorCls
        Updating result.type field of StructuralFeatureAction Write(MaxRequestCount) as Connector1_ConnectorCls
        Updating inputPin.type field of StructuralFeatureAction Write(Connector1_ConnectorCls$initializationFlag$1) as Connector1_ConnectorCls
        Updating outputPin.type field of StructuralFeatureAction Write(Connector1_ConnectorCls$initializationFlag$1) as Connector1_ConnectorCls
        Found: ExpansionReqion structuredNodeInput type: Model::LessFrequentRequester_E1::Connector1_ConnectorCls
        Updating type field of structured node input: Iterate(Write(Connector1_ConnectorCls$initializationFlag$1)).input(Fork(ReadSelf.result)) as Connector1_ConnectorCls
        Found: ReadSelfAction result.type: Model::LessFrequentRequester_E1::Connector1_ConnectorCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Updating inputPin.type field of StructuralFeatureAction Read(Connector1_ConnectorCls$initializationFlag$1) as Connector1_ConnectorCls
        Found: StructuralFeatureAction: Write(DefaultReply)
        Updated structuralFeature field of StructuralFeatureAction Write(DefaultReply)
        Found StructuralFeatureAction object: Write(DefaultReply).object
        Updating object.type field of StructuralFeatureAction Write(DefaultReply) as Connector1_ConnectorCls
        Found StructuralFeatureAction result: Model::LessFrequentRequester_E1::Connector1_ConnectorCls
        Updating result.type field of StructuralFeatureAction Write(DefaultReply) as Connector1_ConnectorCls
        Replaced 14 references
     Done
     Fixing unresolved references of connector behavior activity EnhancedConnectorTestPkg::ECls::Connector1_ConnectorCls::xOp$method$1
        Found: ReadSelfAction result.type: Model::LessFrequentRequester_E1::Connector1_ConnectorCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: ReadSelfAction result.type: Model::LessFrequentRequester_E1::Connector1_ConnectorCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: ReadSelfAction result.type: Model::LessFrequentRequester_E1::Connector1_ConnectorCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: StructuralFeatureAction: Read(RequestBuffer)
        Updated structuralFeature field of StructuralFeatureAction Read(RequestBuffer)
        Found StructuralFeatureAction object: Read(RequestBuffer).object
        Updating object.type field of StructuralFeatureAction Read(RequestBuffer) as RequestPortCls
        Found: ReadSelfAction result.type: Model::LessFrequentRequester_E1::Connector1_ConnectorCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: StructuralFeatureAction: Read(InPort)
        Updated structuralFeature field of StructuralFeatureAction Read(InPort)
        Found StructuralFeatureAction object: Read(InPort).object
        Updating object.type field of StructuralFeatureAction Read(InPort) as Connector1_ConnectorCls
        Found StructuralFeatureAction result: Model::LessFrequentRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updating result.type field of StructuralFeatureAction Read(InPort) as RequestPortCls
        Found: StructuralFeatureAction: Read(InPort)
        Updated structuralFeature field of StructuralFeatureAction Read(InPort)
        Found StructuralFeatureAction object: Read(InPort).object
        Updating object.type field of StructuralFeatureAction Read(InPort) as Connector1_ConnectorCls
        Found StructuralFeatureAction result: Model::LessFrequentRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updating result.type field of StructuralFeatureAction Read(InPort) as RequestPortCls
        Found: StructuralFeatureAction: Read(InPort)
        Updated structuralFeature field of StructuralFeatureAction Read(InPort)
        Found StructuralFeatureAction object: Read(InPort).object
        Updating object.type field of StructuralFeatureAction Read(InPort) as Connector1_ConnectorCls
        Found StructuralFeatureAction result: Model::LessFrequentRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updating result.type field of StructuralFeatureAction Read(InPort) as RequestPortCls
        Found: StructuralFeatureAction: Read(DefaultReply)
        Updated structuralFeature field of StructuralFeatureAction Read(DefaultReply)
        Found StructuralFeatureAction object: Read(DefaultReply).object
        Updating object.type field of StructuralFeatureAction Read(DefaultReply) as Connector1_ConnectorCls
        Found: ReadSelfAction result.type: Model::LessFrequentRequester_E1::Connector1_ConnectorCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: StructuralFeatureAction: Read(RequestBuffer)
        Updated structuralFeature field of StructuralFeatureAction Read(RequestBuffer)
        Found StructuralFeatureAction object: Read(RequestBuffer).object
        Updating object.type field of StructuralFeatureAction Read(RequestBuffer) as RequestPortCls
        Found: StructuralFeatureAction: Read(RequestBuffer)
        Updated structuralFeature field of StructuralFeatureAction Read(RequestBuffer)
        Found StructuralFeatureAction object: Read(RequestBuffer).object
        Updating object.type field of StructuralFeatureAction Read(RequestBuffer) as RequestPortCls
        Found: StructuralFeatureAction: Read(MaxRequestCount)
        Updated structuralFeature field of StructuralFeatureAction Read(MaxRequestCount)
        Found StructuralFeatureAction object: Read(MaxRequestCount).object
        Updating object.type field of StructuralFeatureAction Read(MaxRequestCount) as Connector1_ConnectorCls
        Replaced 37 references
     Done
     Fixing unresolved references of connector behavior activity EnhancedConnectorTestPkg::ECls::Connector1_ConnectorCls::Connector1_ConnectorCls$method$1
        Updating inputPin.type field of StructuralFeatureAction Read(Connector1_ConnectorCls$initializationFlag$1) as Connector1_ConnectorCls
        Found: ReadSelfAction result.type: Model::LessFrequentRequester_E1::Connector1_ConnectorCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: StructuralFeatureAction: Write(InPort)
        Updated structuralFeature field of StructuralFeatureAction Write(InPort)
        Found StructuralFeatureAction object: Write(InPort).object
        Updating object.type field of StructuralFeatureAction Write(InPort) as Connector1_ConnectorCls
        Found StructuralFeatureAction result: Model::LessFrequentRequester_E1::Connector1_ConnectorCls
        Updating result.type field of StructuralFeatureAction Write(InPort) as Connector1_ConnectorCls
        Updating inputPin.type field of StructuralFeatureAction Write(InPort) as RequestPortCls
        Found Parameter type: 
        Updating type field of parameter: 
        Found: ReadSelfAction result.type: Model::LessFrequentRequester_E1::Connector1_ConnectorCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: CallOperation operation: Model::LessFrequentRequester_E1::Connector1_ConnectorCls::RequestPortCls::RequestPortCls
        Updating operation field of CallOperationAction: Call(RequestPortCls)
        Found: CallOperation target type: Model::LessFrequentRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updating target.type field of CallOperationAction: Call(RequestPortCls) as RequestPortCls
        Found: CallOperationAction argument type: Model::LessFrequentRequester_E1::Connector1_ConnectorCls
        Updating argument.type field of CallOperationAction: Call(RequestPortCls) as Connector1_ConnectorCls
        Found CallOperationAction result tyupe: Model::LessFrequentRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updating result.type field of CallOperationAction: Call(RequestPortCls) as RequestPortCls
        Found: CallOperation target type: Model::LessFrequentRequester_E1::Connector1_ConnectorCls
        Updating target.type field of CallOperationAction: Call(Connector1_ConnectorCls$initialization$1) as Connector1_ConnectorCls
        Found: ReadSelfAction result.type: Model::LessFrequentRequester_E1::Connector1_ConnectorCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: StructuralFeatureAction: Remove(InPort)
        Updated structuralFeature field of StructuralFeatureAction Remove(InPort)
        Found StructuralFeatureAction object: Remove(InPort).object
        Updating object.type field of StructuralFeatureAction Remove(InPort) as Connector1_ConnectorCls
        Found StructuralFeatureAction result: Model::LessFrequentRequester_E1::Connector1_ConnectorCls
        Updating result.type field of StructuralFeatureAction Remove(InPort) as Connector1_ConnectorCls
        Found: ActivityParameterNode type: Model::LessFrequentRequester_E1::Connector1_ConnectorCls
        Updating type field of ActivityParameterNode: Return as Connector1_ConnectorCls
        Found: CreateObjectAction: classifier: RequestPortCls
        Updating classifier field of CreateObjectAction Create(RequestPortCls) as RequestPortCls
        Found: CreateObjectAction: result type: RequestPortCls
        Updating classifier field of CreateObjectAction Create(RequestPortCls) as RequestPortCls
        Replaced 25 references
     Done
     Fixing unresolved references of connector behavior activity EnhancedConnectorTestPkg::ECls::Connector1_ConnectorCls::destroy$method$1
        Replaced 0 references
     Done
     Fixing unresolved references of connector behavior activity EnhancedConnectorTestPkg::ECls::Connector1_ConnectorCls::RequestPortCls::RequestPortCls$initialization$1
        Updating inputPin.type field of StructuralFeatureAction Write(RequestPortCls$initializationFlag$1) as RequestPortCls
        Updating outputPin.type field of StructuralFeatureAction Write(RequestPortCls$initializationFlag$1) as RequestPortCls
        Found: ExpansionReqion structuredNodeInput type: Model::LessFrequentRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updating type field of structured node input: Iterate(Write(RequestPortCls$initializationFlag$1)).input(Fork(ReadSelf.result)) as RequestPortCls
        Updating inputPin.type field of StructuralFeatureAction Read(RequestPortCls$initializationFlag$1) as RequestPortCls
        Found: ReadSelfAction result.type: Model::LessFrequentRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Replaced 6 references
     Done
     Fixing unresolved references of connector behavior activity EnhancedConnectorTestPkg::ECls::Connector1_ConnectorCls::RequestPortCls::RequestPortCls$method$1
        Found: StructuredActivityNode structuredNodeInput type: Model::LessFrequentRequester_E1::Connector1_ConnectorCls
        Updating type field of structured node input: WriteAll(RequestPortCls::ownerConnector).input(RequestPortCls::ownerConnector) as Connector1_ConnectorCls
        Found: ReadSelfAction result.type: Model::LessFrequentRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: CallOperation target type: Model::LessFrequentRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updating target.type field of CallOperationAction: Call(RequestPortCls$initialization$1) as RequestPortCls
        Updating inputPin.type field of StructuralFeatureAction Read(RequestPortCls$initializationFlag$1) as RequestPortCls
        Found Parameter type: 
        Updating type field of parameter: 
        Found: StructuralFeatureAction: Clear(RequestBuffer)
        Updated structuralFeature field of StructuralFeatureAction Clear(RequestBuffer)
        Found StructuralFeatureAction object: Clear(RequestBuffer).object
        Updating object.type field of StructuralFeatureAction Clear(RequestBuffer) as RequestPortCls
        Found StructuralFeatureAction result: Model::LessFrequentRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updating result.type field of StructuralFeatureAction Clear(RequestBuffer) as RequestPortCls
        Found: StructuralFeatureAction: Write(ownerConnector)
        Updated structuralFeature field of StructuralFeatureAction Write(ownerConnector)
        Found StructuralFeatureAction object: Write(ownerConnector).object
        Updating object.type field of StructuralFeatureAction Write(ownerConnector) as RequestPortCls
        Found StructuralFeatureAction result: Model::LessFrequentRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updating result.type field of StructuralFeatureAction Write(ownerConnector) as RequestPortCls
        Updating inputPin.type field of StructuralFeatureAction Write(ownerConnector) as Connector1_ConnectorCls
        Found: StructuralFeatureAction: Clear(ownerConnector)
        Updated structuralFeature field of StructuralFeatureAction Clear(ownerConnector)
        Found StructuralFeatureAction object: Clear(ownerConnector).object
        Updating object.type field of StructuralFeatureAction Clear(ownerConnector) as RequestPortCls
        Found StructuralFeatureAction result: Model::LessFrequentRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updating result.type field of StructuralFeatureAction Clear(ownerConnector) as RequestPortCls
        Found: StructuralFeatureAction: Write(RequestBuffer)
        Updated structuralFeature field of StructuralFeatureAction Write(RequestBuffer)
        Found StructuralFeatureAction object: Write(RequestBuffer).object
        Updating object.type field of StructuralFeatureAction Write(RequestBuffer) as RequestPortCls
        Found StructuralFeatureAction result: Model::LessFrequentRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updating result.type field of StructuralFeatureAction Write(RequestBuffer) as RequestPortCls
        Found: ActivityParameterNode type: Model::LessFrequentRequester_E1::Connector1_ConnectorCls
        Updating type field of ActivityParameterNode: Input(c) as Connector1_ConnectorCls
        Found: ActivityParameterNode type: Model::LessFrequentRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updating type field of ActivityParameterNode: Return as RequestPortCls
        Found: ReadSelfAction result.type: Model::LessFrequentRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found Parameter type: c
        Updating type field of parameter: c
        Found: ReadSelfAction result.type: Model::LessFrequentRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Replaced 30 references
     Done
     Fixing unresolved references of connector behavior activity EnhancedConnectorTestPkg::ECls::Connector1_ConnectorCls::RequestPortCls::xOp$method$1
        Found: ReadSelfAction result.type: Model::LessFrequentRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: StructuralFeatureAction: Read(RequestBuffer)
        Updated structuralFeature field of StructuralFeatureAction Read(RequestBuffer)
        Found StructuralFeatureAction object: Read(RequestBuffer).object
        Updating object.type field of StructuralFeatureAction Read(RequestBuffer) as RequestPortCls
        Found: ReadSelfAction result.type: Model::LessFrequentRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: CallOperation operation: Model::LessFrequentRequester_E1::Connector1_ConnectorCls::xOp
        Updating operation field of CallOperationAction: Call(xOp)
        Found: CallOperation target type: Model::LessFrequentRequester_E1::Connector1_ConnectorCls
        Updating target.type field of CallOperationAction: Call(xOp) as Connector1_ConnectorCls
        Found: StructuralFeatureAction: Read(ownerConnector)
        Updated structuralFeature field of StructuralFeatureAction Read(ownerConnector)
        Found StructuralFeatureAction object: Read(ownerConnector).object
        Updating object.type field of StructuralFeatureAction Read(ownerConnector) as RequestPortCls
        Found StructuralFeatureAction result: Model::LessFrequentRequester_E1::Connector1_ConnectorCls
        Updating result.type field of StructuralFeatureAction Read(ownerConnector) as Connector1_ConnectorCls
        Replaced 13 references
     Done
     Fixing unresolved references of connector behavior activity EnhancedConnectorTestPkg::ECls::Connector1_ConnectorCls::RequestPortCls::destroy$method$1
        Replaced 0 references
     Done
   Done
17360 INFO  EcTransformer      - OK
17563 INFO  EcTransformer      - E2toE3Transformation executed successfully
17563 INFO  EcTransformer      - ----------------------------------------------------------------------------
17563 INFO  EcTransformer      - 
17563 INFO  EcTransformer      - Process completed successfully

```