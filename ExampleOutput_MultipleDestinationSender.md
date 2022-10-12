## Example Output from running EcTransformer.jave for Multiple Destination Sender

```
0    INFO  EcTransformer      - Starting...
0    INFO  EcTransformer      - Using e1Model at path: C:\Users\akocatas\Dropbox\payprus-workspace\org.ec.connectors\MultiDestRequester\MultiDestRequester_E1.uml
1103 INFO  EcTransformer      - Running E1toE2Transformation
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
4542 INFO  EcTransformer      - E1 to E2 transformation load result: OK
4573 INFO  EcTransformer      - E1 to E2 transformation ran successfully
4573 INFO  EcTransformer      - ----------------------------------------------------------------------------
4573 INFO  EcTransformer      - 
4573 INFO  EcTransformer      - Running E2toAlfTransformation
Starting E2 to Alf Transformation...
   <begin> Alf Code: 
private import Alf::Library::PrimitiveBehaviors::IntegerFunctions::ToString;
private import Alf::Library::PrimitiveBehaviors::IntegerFunctions::*;

package MultiDestRequester_E1 {
    public abstract class XIfc
    {
        public abstract xOp(in arg:Integer) : Integer;
    }
    
    public class Connector1_ConnectorCls
    {
        public InPort : RequestPortCls[1..1] ordered nonunique;
        public OutPort : XIfc[2..2] ordered nonunique;
        ReplyBuffer : Integer[2..2] ordered nonunique;
        
        public xOp(in arg:Integer) : Integer
        {
            Integer i = 1; 
            
            for (ifc in this.OutPort)  
            
            {
            
                this.ReplyBuffer[i] = ifc.xOp(arg);
            
                i++; 
            
            }
            
            
            
            // Reply merge strategy:         
            
            i = 1;
            
            Integer sum = 0;
            
            while (i<=2)
            
            {
            
                Integer intval = this.ReplyBuffer[i];
            
                
            
                if (intval != null)
            
                {
            
                    sum = sum + intval;
            
                     
            
                    // Invalidate value in the single copy buffer
            
                    this.ReplyBuffer[i] = null;        
            
                }
            
                i++;
            
            }
            
            
            
            return sum;
            

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
4855 INFO  EcTransformer      - E2 to Alf transformation load result: OK
4855 INFO  EcTransformer      - Alf code is captured successfully
4855 INFO  EcTransformer      - E2 to Alf transformation ran successfully
4855 INFO  EcTransformer      - ----------------------------------------------------------------------------
4855 INFO  EcTransformer      - 
4855 INFO  EcTransformer      - Creating alf file: C:\Users\akocatas\Dropbox\payprus-workspace\\AlfRefImp-1.1.0k\dist/alf/Models/MultiDestRequester_E1.alf
4855 INFO  EcTransformer      - Compiling alf file using alfc
6151 INFO  EcTransformer      - alfc stdout: Alf Reference Implementation v1.1.0k
6155 INFO  EcTransformer      - alfc stdout: Loaded C:\Users\akocatas\Dropbox\payprus-workspace\\AlfRefImp-1.1.0k\dist/alf/Libraries/resources/error-messages.txt
10377 INFO  EcTransformer      - alfc stdout: Loaded file:/C:/Users/akocatas/Dropbox/payprus-workspace//AlfRefImp-1.1.0k/dist/alf/Libraries/uml/Standard.profile.uml
10506 INFO  EcTransformer      - alfc stdout: Loaded file:/C:/Users/akocatas/Dropbox/payprus-workspace//AlfRefImp-1.1.0k/dist/alf/Libraries/uml/fUML.library.uml
12233 INFO  EcTransformer      - alfc stdout: Loaded file:/C:/Users/akocatas/Dropbox/payprus-workspace//AlfRefImp-1.1.0k/dist/alf/Libraries/uml/Alf.library.uml
12408 INFO  EcTransformer      - alfc stdout: Parsed C:\Users\akocatas\Dropbox\payprus-workspace\\AlfRefImp-1.1.0k\dist/alf/Models/MultiDestRequester_E1.alf
13486 INFO  EcTransformer      - alfc stdout: Mapped successfully.
13686 INFO  EcTransformer      - alfc stdout: Saved to file:/C:\Users\akocatas\Dropbox\payprus-workspace\org.ec.connectors\alf_output/MultiDestRequester_E1.uml
13983 INFO  EcTransformer      - Alf code compiled successfully
13983 INFO  EcTransformer      - ----------------------------------------------------------------------------
13983 INFO  EcTransformer      - 
13983 INFO  EcTransformer      - Replacing fUML pathmaps
13999 INFO  EcTransformer      - ----------------------------------------------------------------------------
13999 INFO  EcTransformer      - 
13999 INFO  EcTransformer      - Running E2toE3Transformation
   Starting E2 to E3 transformation
     Locating e2 model elements...
     Done
     Locating fUML model elements
     Done
     Fixing unresolved references of connector behavior activity EnhancedConnectorTestPkg::ECls::Connector1_ConnectorCls::Connector1_ConnectorCls$initialization$1
        Updating inputPin.type field of StructuralFeatureAction Write(Connector1_ConnectorCls$initializationFlag$1) as Connector1_ConnectorCls
        Updating outputPin.type field of StructuralFeatureAction Write(Connector1_ConnectorCls$initializationFlag$1) as Connector1_ConnectorCls
        Found: ExpansionReqion structuredNodeInput type: Model::MultiDestRequester_E1::Connector1_ConnectorCls
        Updating type field of structured node input: Iterate(Write(Connector1_ConnectorCls$initializationFlag$1)).input(Fork(ReadSelf.result)) as Connector1_ConnectorCls
        Found: ReadSelfAction result.type: Model::MultiDestRequester_E1::Connector1_ConnectorCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Updating inputPin.type field of StructuralFeatureAction Read(Connector1_ConnectorCls$initializationFlag$1) as Connector1_ConnectorCls
        Replaced 6 references
     Done
     Fixing unresolved references of connector behavior activity EnhancedConnectorTestPkg::ECls::Connector1_ConnectorCls::xOp$method$1
        Found: ReadSelfAction result.type: Model::MultiDestRequester_E1::Connector1_ConnectorCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: StructuralFeatureAction: Read(OutPort)
        Updated structuralFeature field of StructuralFeatureAction Read(OutPort)
        Found StructuralFeatureAction object: Read(OutPort).object
        Updating object.type field of StructuralFeatureAction Read(OutPort) as Connector1_ConnectorCls
        Found StructuralFeatureAction result: Model::MultiDestRequester_E1::XIfc
        Updating result.type field of StructuralFeatureAction Read(OutPort) as XIfc
        Found: StructuralFeatureAction: Remove(ReplyBuffer)
        Updated structuralFeature field of StructuralFeatureAction Remove(ReplyBuffer)
        Found StructuralFeatureAction object: Remove(ReplyBuffer).object
        Updating object.type field of StructuralFeatureAction Remove(ReplyBuffer) as Connector1_ConnectorCls
        Found StructuralFeatureAction result: Model::MultiDestRequester_E1::Connector1_ConnectorCls
        Updating result.type field of StructuralFeatureAction Remove(ReplyBuffer) as Connector1_ConnectorCls
        Found: StructuralFeatureAction: Remove(ReplyBuffer)
        Updated structuralFeature field of StructuralFeatureAction Remove(ReplyBuffer)
        Found StructuralFeatureAction object: Remove(ReplyBuffer).object
        Updating object.type field of StructuralFeatureAction Remove(ReplyBuffer) as Connector1_ConnectorCls
        Found StructuralFeatureAction result: Model::MultiDestRequester_E1::Connector1_ConnectorCls
        Updating result.type field of StructuralFeatureAction Remove(ReplyBuffer) as Connector1_ConnectorCls
        Found: StructuralFeatureAction: Read(ReplyBuffer)
        Updated structuralFeature field of StructuralFeatureAction Read(ReplyBuffer)
        Found StructuralFeatureAction object: Read(ReplyBuffer).object
        Updating object.type field of StructuralFeatureAction Read(ReplyBuffer) as Connector1_ConnectorCls
        Found: ReadSelfAction result.type: Model::MultiDestRequester_E1::Connector1_ConnectorCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: ReadSelfAction result.type: Model::MultiDestRequester_E1::Connector1_ConnectorCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: CallOperation operation: Model::MultiDestRequester_E1::XIfc::xOp
        Updating operation field of CallOperationAction: Call(xOp)
        Found: CallOperation target type: Model::MultiDestRequester_E1::XIfc
        Updating target.type field of CallOperationAction: Call(xOp) as XIfc
        Found: ReadSelfAction result.type: Model::MultiDestRequester_E1::Connector1_ConnectorCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: StructuredActivityNode structuredNodeInput type: Model::MultiDestRequester_E1::XIfc
        Updating type field of structured node input: Passthru(ForStatement@15cf663c.loopVariable(ifc)).input as XIfc
        Found: StructuredActivityNode structuredNodeOutput type: Model::MultiDestRequester_E1::XIfc
        Updating type field of structured node output: Passthru(ForStatement@15cf663c.loopVariable(ifc)).output as XIfc
        Found: StructuralFeatureAction: Write(ReplyBuffer)
        Updated structuralFeature field of StructuralFeatureAction Write(ReplyBuffer)
        Found StructuralFeatureAction object: Write(ReplyBuffer).object
        Updating object.type field of StructuralFeatureAction Write(ReplyBuffer) as Connector1_ConnectorCls
        Found StructuralFeatureAction result: Model::MultiDestRequester_E1::Connector1_ConnectorCls
        Updating result.type field of StructuralFeatureAction Write(ReplyBuffer) as Connector1_ConnectorCls
        Found: StructuredActivityNode structuredNodeInput type: Model::MultiDestRequester_E1::XIfc
        Updating type field of structured node input: ForStatement@15cf663c.loopVariableInput(ifc) as XIfc
        Found: StructuredActivityNode structuredNodeOutput type: Model::MultiDestRequester_E1::XIfc
        Updating type field of structured node output: ForStatement@15cf663c.result(ifc) as XIfc
        Found loop variable type: ForStatement@15cf663c.loopVariable(ifc)
        Updating type field of loop variable ForStatement@15cf663c.loopVariable(ifc) as XIfc
        Replaced 34 references
     Done
     Fixing unresolved references of connector behavior activity EnhancedConnectorTestPkg::ECls::Connector1_ConnectorCls::Connector1_ConnectorCls$method$1
        Found: StructuralFeatureAction: Write(InPort)
        Updated structuralFeature field of StructuralFeatureAction Write(InPort)
        Found StructuralFeatureAction object: Write(InPort).object
        Updating object.type field of StructuralFeatureAction Write(InPort) as Connector1_ConnectorCls
        Found StructuralFeatureAction result: Model::MultiDestRequester_E1::Connector1_ConnectorCls
        Updating result.type field of StructuralFeatureAction Write(InPort) as Connector1_ConnectorCls
        Updating inputPin.type field of StructuralFeatureAction Write(InPort) as RequestPortCls
        Found: CreateObjectAction: classifier: RequestPortCls
        Updating classifier field of CreateObjectAction Create(RequestPortCls) as RequestPortCls
        Found: CreateObjectAction: result type: RequestPortCls
        Updating classifier field of CreateObjectAction Create(RequestPortCls) as RequestPortCls
        Found: CallOperation operation: Model::MultiDestRequester_E1::Connector1_ConnectorCls::RequestPortCls::RequestPortCls
        Updating operation field of CallOperationAction: Call(RequestPortCls)
        Found: CallOperation target type: Model::MultiDestRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updating target.type field of CallOperationAction: Call(RequestPortCls) as RequestPortCls
        Found: CallOperationAction argument type: Model::MultiDestRequester_E1::Connector1_ConnectorCls
        Updating argument.type field of CallOperationAction: Call(RequestPortCls) as Connector1_ConnectorCls
        Found CallOperationAction result tyupe: Model::MultiDestRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updating result.type field of CallOperationAction: Call(RequestPortCls) as RequestPortCls
        Found: CallOperation target type: Model::MultiDestRequester_E1::Connector1_ConnectorCls
        Updating target.type field of CallOperationAction: Call(Connector1_ConnectorCls$initialization$1) as Connector1_ConnectorCls
        Found: ReadSelfAction result.type: Model::MultiDestRequester_E1::Connector1_ConnectorCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found Parameter type: 
        Updating type field of parameter: 
        Updating inputPin.type field of StructuralFeatureAction Read(Connector1_ConnectorCls$initializationFlag$1) as Connector1_ConnectorCls
        Found: ReadSelfAction result.type: Model::MultiDestRequester_E1::Connector1_ConnectorCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: ReadSelfAction result.type: Model::MultiDestRequester_E1::Connector1_ConnectorCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: StructuralFeatureAction: Remove(InPort)
        Updated structuralFeature field of StructuralFeatureAction Remove(InPort)
        Found StructuralFeatureAction object: Remove(InPort).object
        Updating object.type field of StructuralFeatureAction Remove(InPort) as Connector1_ConnectorCls
        Found StructuralFeatureAction result: Model::MultiDestRequester_E1::Connector1_ConnectorCls
        Updating result.type field of StructuralFeatureAction Remove(InPort) as Connector1_ConnectorCls
        Found: ActivityParameterNode type: Model::MultiDestRequester_E1::Connector1_ConnectorCls
        Updating type field of ActivityParameterNode: Return as Connector1_ConnectorCls
        Replaced 25 references
     Done
     Fixing unresolved references of connector behavior activity EnhancedConnectorTestPkg::ECls::Connector1_ConnectorCls::destroy$method$1
        Replaced 0 references
     Done
     Fixing unresolved references of connector behavior activity EnhancedConnectorTestPkg::ECls::Connector1_ConnectorCls::RequestPortCls::RequestPortCls$initialization$1
        Found: ReadSelfAction result.type: Model::MultiDestRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Updating inputPin.type field of StructuralFeatureAction Read(RequestPortCls$initializationFlag$1) as RequestPortCls
        Updating inputPin.type field of StructuralFeatureAction Write(RequestPortCls$initializationFlag$1) as RequestPortCls
        Updating outputPin.type field of StructuralFeatureAction Write(RequestPortCls$initializationFlag$1) as RequestPortCls
        Found: ExpansionReqion structuredNodeInput type: Model::MultiDestRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updating type field of structured node input: Iterate(Write(RequestPortCls$initializationFlag$1)).input(Fork(ReadSelf.result)) as RequestPortCls
        Replaced 6 references
     Done
     Fixing unresolved references of connector behavior activity EnhancedConnectorTestPkg::ECls::Connector1_ConnectorCls::RequestPortCls::RequestPortCls$method$1
        Found: ReadSelfAction result.type: Model::MultiDestRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: ActivityParameterNode type: Model::MultiDestRequester_E1::Connector1_ConnectorCls
        Updating type field of ActivityParameterNode: Input(c) as Connector1_ConnectorCls
        Found: StructuralFeatureAction: Write(ownerConnector)
        Updated structuralFeature field of StructuralFeatureAction Write(ownerConnector)
        Found StructuralFeatureAction object: Write(ownerConnector).object
        Updating object.type field of StructuralFeatureAction Write(ownerConnector) as RequestPortCls
        Found StructuralFeatureAction result: Model::MultiDestRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updating result.type field of StructuralFeatureAction Write(ownerConnector) as RequestPortCls
        Updating inputPin.type field of StructuralFeatureAction Write(ownerConnector) as Connector1_ConnectorCls
        Updating inputPin.type field of StructuralFeatureAction Read(RequestPortCls$initializationFlag$1) as RequestPortCls
        Found: StructuredActivityNode structuredNodeInput type: Model::MultiDestRequester_E1::Connector1_ConnectorCls
        Updating type field of structured node input: WriteAll(RequestPortCls::ownerConnector).input(RequestPortCls::ownerConnector) as Connector1_ConnectorCls
        Found: ActivityParameterNode type: Model::MultiDestRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updating type field of ActivityParameterNode: Return as RequestPortCls
        Found Parameter type: c
        Updating type field of parameter: c
        Found: StructuralFeatureAction: Clear(ownerConnector)
        Updated structuralFeature field of StructuralFeatureAction Clear(ownerConnector)
        Found StructuralFeatureAction object: Clear(ownerConnector).object
        Updating object.type field of StructuralFeatureAction Clear(ownerConnector) as RequestPortCls
        Found StructuralFeatureAction result: Model::MultiDestRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updating result.type field of StructuralFeatureAction Clear(ownerConnector) as RequestPortCls
        Found: ReadSelfAction result.type: Model::MultiDestRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found Parameter type: 
        Updating type field of parameter: 
        Found: CallOperation target type: Model::MultiDestRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updating target.type field of CallOperationAction: Call(RequestPortCls$initialization$1) as RequestPortCls
        Replaced 20 references
     Done
     Fixing unresolved references of connector behavior activity EnhancedConnectorTestPkg::ECls::Connector1_ConnectorCls::RequestPortCls::xOp$method$1
        Found: CallOperation operation: Model::MultiDestRequester_E1::Connector1_ConnectorCls::xOp
        Updating operation field of CallOperationAction: Call(xOp)
        Found: CallOperation target type: Model::MultiDestRequester_E1::Connector1_ConnectorCls
        Updating target.type field of CallOperationAction: Call(xOp) as Connector1_ConnectorCls
        Found: ReadSelfAction result.type: Model::MultiDestRequester_E1::Connector1_ConnectorCls::RequestPortCls
        Updated result.type field of ReadSelfAction: ReadSelf
        Found: StructuralFeatureAction: Read(ownerConnector)
        Updated structuralFeature field of StructuralFeatureAction Read(ownerConnector)
        Found StructuralFeatureAction object: Read(ownerConnector).object
        Updating object.type field of StructuralFeatureAction Read(ownerConnector) as RequestPortCls
        Found StructuralFeatureAction result: Model::MultiDestRequester_E1::Connector1_ConnectorCls
        Updating result.type field of StructuralFeatureAction Read(ownerConnector) as Connector1_ConnectorCls
        Replaced 8 references
     Done
     Fixing unresolved references of connector behavior activity EnhancedConnectorTestPkg::ECls::Connector1_ConnectorCls::RequestPortCls::destroy$method$1
        Replaced 0 references
     Done
   Done
16928 INFO  EcTransformer      - OK
17021 INFO  EcTransformer      - E2toE3Transformation executed successfully
17021 INFO  EcTransformer      - ----------------------------------------------------------------------------
17021 INFO  EcTransformer      - 
17021 INFO  EcTransformer      - Process completed successfully
```
