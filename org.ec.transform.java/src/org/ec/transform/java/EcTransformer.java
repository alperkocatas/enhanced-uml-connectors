package org.ec.transform.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.lang.model.type.PrimitiveType;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.m2m.internal.qvt.oml.evaluator.ThisInstanceResolver;
import org.eclipse.m2m.qvt.oml.BasicModelExtent;
import org.eclipse.m2m.qvt.oml.ExecutionContextImpl;
import org.eclipse.m2m.qvt.oml.ExecutionDiagnostic;
import org.eclipse.m2m.qvt.oml.ModelExtent;
import org.eclipse.m2m.qvt.oml.TransformationExecutor;
import org.eclipse.m2m.qvt.oml.util.Log;
import org.eclipse.m2m.qvt.oml.util.StringBufferLog;
import org.eclipse.m2m.qvt.oml.util.WriterLog;
import org.eclipse.ocl.ecore.impl.PrimitiveTypeImpl;
import org.eclipse.uml2.common.util.UML2Util;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.Lifeline;
import org.eclipse.uml2.uml.LiteralUnlimitedNatural;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.utils.Mapping;

public class EcTransformer {
	
	private static final Logger logger = Logger.getLogger(EcTransformer.class);
	
	private String workspacePath = "/Users/akocatas/Dropbox/payprus-workspace/"; 
	
	private String e1ModelPath = workspacePath + "org.ec.connectors/" + 
			"RoundRobinRequesterQvto/RoundRobinRequester_E1.uml";
	
	private String e2ModelPath = workspacePath + "org.ec.connectors/" + 
			"RoundRobinRequesterQvto/RoundRobinRequester_E2.uml";
	
	private String e3ModelPath = workspacePath + "org.ec.connectors/" + 
			"RoundRobinRequesterQvto/RoundRobinRequester_E3.uml";
	
	private String e1ToE2TxPath = workspacePath + "org.ec.transform.qvto/" + 
			"transforms/E1toE2Transformation.qvto";
	
	private String e2ToAlfTxPath = workspacePath + "org.ec.transform.qvto/" + 
			"transforms/E2ToAlfTransformation.qvto";
	
	private String e2Toe3TxPath = workspacePath + "org.ec.transform.qvto/" + 
			"transforms/E2toE3TransformationTest1.qvto";
	
	private String fUMLOutputDir = workspacePath + "org.ec.connectors/alf_output/";
	
	private ResourceSet resourceSet;
	
	private String ConnectorBehaviorAlfCode;
	
	private String AlfWsPath = "/Users/akocatas/Documents/Alf-Reference-Implementation-master/";
	
	private String ConnectorBehaviorFumlFilePath;
	
	private String ConnectorClsModelPath = null;
	private String OutportMultiplicity = null;
	private String AlfPkgName = null;
	
	public void initializeUMLResources()
	{
		resourceSet = new ResourceSetImpl();
		resourceSet.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().
			put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
		Map uriMap = resourceSet.getURIConverter().getURIMap();
		URI uri = URI.createURI(
				"jar:file:/Users/akocatas/Applications/Papyrus.app/Contents/" + 
				"Eclipse/plugins/org.eclipse.uml2.uml.resources_5.5.0.v20210228-1829.jar!/");
		uriMap.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP), 
				uri.appendSegment("libraries").appendSegment(""));
		uriMap.put(URI.createURI(UMLResource.METAMODELS_PATHMAP), 
				uri.appendSegment("metamodels").appendSegment(""));
		uriMap.put(URI.createURI(UMLResource.PROFILES_PATHMAP), 
				uri.appendSegment("profiles").appendSegment(""));
	}
	
	
	public void runE1ToE2Transformation()
	{
		logger.info("Running E1toE2Transformation");

		URI e1ModelUri = URI.createFileURI(e1ModelPath);
		resourceSet.createResource(e1ModelUri);
		Resource r = resourceSet.getResource(e1ModelUri, true);
		EList<EObject> inObjects = r.getContents();
		for (EObject temp : inObjects) {
			System.out.println(temp.toString());
		}
		
		ModelExtent input = new BasicModelExtent(inObjects);	
		//ModelExtent output = new BasicModelExtent();
		ExecutionContextImpl context = new ExecutionContextImpl();
		context.setConfigProperty("LogIndentLevel", 5);
		
	    OutputStreamWriter outStream = new OutputStreamWriter(System.out);
		E1toE2TransformationLog log = new E1toE2TransformationLog();
		context.setLog(log);
	    
	    
		URI transformationURI = URI.createFileURI(e1ToE2TxPath);
		TransformationExecutor executor = new TransformationExecutor(transformationURI);
		ExecutionDiagnostic result = executor.execute(context, input);
		Diagnostic loadResult = executor.loadTransformation();
		System.out.println(loadResult.getMessage());
		
		
		if (log.CaptureOk)
		{
			this.ConnectorClsModelPath = log.ConnectorClsModelPath;
			this.OutportMultiplicity = log.OutportMultiplicity;
		}
		
		List<EObject> outObjects = input.getContents();
        URI outUri = URI.createURI("file:" + e2ModelPath);
        Resource res = resourceSet.createResource(outUri);
        res.getContents().addAll(outObjects);
        try {
            res.save(Collections.emptyMap());
            logger.info("E1toE2Transformation ok...");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void runE2ToAlfTransformation() throws Exception
	{
		logger.info("Running E2toAlfTransformation");

		URI e1ModelUri = URI.createFileURI(e2ModelPath);
		resourceSet.createResource(e1ModelUri);
		Resource r = resourceSet.getResource(e1ModelUri, true);
		EList<EObject> inObjects = r.getContents();
		for (EObject temp : inObjects) {
			System.out.println(temp.toString());
		}
		
		ModelExtent input = new BasicModelExtent(inObjects);	
		ModelExtent output = new BasicModelExtent();
		ExecutionContextImpl context = new ExecutionContextImpl();		
		context.setConfigProperty("CONNECTOR_CLS_MODEL_PATH", this.ConnectorClsModelPath);
		context.setConfigProperty("OUTPORT_MULTIPLICITY", this.OutportMultiplicity);
		
		E2ToAlfTransformationLog log = new E2ToAlfTransformationLog();
		context.setLog(log);
	    
		URI transformationURI = URI.createFileURI(e2ToAlfTxPath);
		TransformationExecutor executor = new TransformationExecutor(transformationURI);
		ExecutionDiagnostic result = executor.execute(context, input, output);
		Diagnostic loadResult = executor.loadTransformation();
		System.out.println(loadResult.getMessage());
		
		if (log.CaptureOk = true)
		{
			System.out.println("Captured Alf code is: ");
			System.out.println(log.AlfCode);
			ConnectorBehaviorAlfCode = log.AlfCode; 
			
			this.AlfPkgName = log.AlfPkgName;
		}
		else
		{
			throw new Exception("Alf code could not be captured after E2toE3 model transformation");
		}
	}
	
	public void compileAlfCode() throws IOException, InterruptedException
	{
		// Create an alf file in ??		
		String alfPrjPath = AlfWsPath + "dist/alf/";
		String alfPkgName = "PortModel6_RoundRobinConnector";
		String alfFilePath = alfPrjPath + "Models/" + alfPkgName + ".alf";
		
		// Create Alf File:
		FileWriter writer = new FileWriter(alfFilePath);
		writer.write(this.ConnectorBehaviorAlfCode);
		writer.close();
		
		// Compile Alf File using alfc
		String alfcPath = alfPrjPath + "alfc";
		String modelsDirPath = alfPrjPath + "Models";
		String librariesDirPath = alfPrjPath + "Libraries";
				
		ProcessBuilder pb = new ProcessBuilder(alfcPath,
				"-l", librariesDirPath, 
				"-m", modelsDirPath,
				"-u", fUMLOutputDir, 
				"-f", 
				"-v", 
				alfFilePath
				);
		
		pb.directory(new File(alfPrjPath));
		
		Process process = pb.start();
		
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;

		System.out.printf("Output of running %s is:\n", alfcPath);

		while ((line = br.readLine()) != null) {
		  System.out.println(line);
		}


		process.waitFor();
		
		if (process.exitValue() != 0) {
			System.out.printf("Error output of running %s is:\n", alfcPath);
			InputStream eis = process.getErrorStream();
			InputStreamReader eisr = new InputStreamReader(eis);
			BufferedReader ebr = new BufferedReader(eisr);
			
			while ((line = ebr.readLine()) != null) {
				System.out.println(line);
			}
		}
		
		this.ConnectorBehaviorFumlFilePath = fUMLOutputDir + "/" + alfPkgName + ".uml";
	}
	
	/**
	 * reformat the pathmaps so that they can be resolved within the project. 
	 * @throws IOException 
	 */
	public void reformatFUmlConnectorBehaviorFile() throws IOException
	{		
		
		logger.info("Replacing fUML pathmaps");
		String fileContents = new String(
				Files.readAllBytes(Paths.get(ConnectorBehaviorFumlFilePath)));
		
		fileContents = fileContents
				.replaceAll("pathmap://ALF_LIBRARIES/", "../Libraries/uml/");
		
		FileWriter writer = new FileWriter(ConnectorBehaviorFumlFilePath);
		writer.write(fileContents);
		writer.close();
	}
	
	public void runE2toE3Transformation()
	{
		logger.info("Running E2toE3Transformation");

		URI e2ModelUri = URI.createFileURI(e2ModelPath);
		resourceSet.createResource(e2ModelUri);
		Resource r = resourceSet.getResource(e2ModelUri, true);
		EList<EObject> inObjects = r.getContents();
		ModelExtent e2Input = new BasicModelExtent(inObjects);
		
		
		URI fUmlModelUri = URI.createFileURI(ConnectorBehaviorFumlFilePath);
		resourceSet.createResource(fUmlModelUri);
		Resource r2 = resourceSet.getResource(fUmlModelUri, true);
		EList<EObject> inObjects2 = r2.getContents();
		ModelExtent fUmlInput = new BasicModelExtent(inObjects2);
		
		ExecutionContextImpl context = new ExecutionContextImpl();
		context.setConfigProperty("LogIndentLevel", 3);
		context.setConfigProperty("CONNECTOR_CLS_MODEL_PATH", this.ConnectorClsModelPath);
		context.setConfigProperty("OUTPORT_MULTIPLICITY", this.OutportMultiplicity);
		context.setConfigProperty("ALF_PKG_NAME", this.AlfPkgName); 
		
		
	    OutputStreamWriter outStream = new OutputStreamWriter(System.out);
	    Log log = new WriterLog(outStream);
	    context.setLog(log);
		
		URI transformationURI = URI.createFileURI(e2Toe3TxPath);
		TransformationExecutor executor = new TransformationExecutor(transformationURI);
		ExecutionDiagnostic result = executor.execute(context, e2Input, fUmlInput);
		Diagnostic loadResult = executor.loadTransformation();
		System.out.println(loadResult.getMessage());
		
		List<EObject> outObjects = e2Input.getContents();
        URI outUri = URI.createURI("file:" + e3ModelPath);
        Resource res = resourceSet.createResource(outUri);
        res.getContents().addAll(outObjects);
        try {
            res.save(Collections.emptyMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static void main(String[]args)
	{
		// Steps:
		// - Run the E1toE2Transformation first to get E2 model, 
		// - Run the E2ToAlfTransformation using E2 model as input, 
		//   and get the alf code printed as log,
		// - certain parameters should also be printed in the log by this 
		//   or previous transformation
		// - run alfc to compile the alf code into fUML model, 
		// - run E2toE3Transformation to pull back the connector behavior activity 
		//   into the E2 model, to get the E3 model. 
		
		try
		{	
			EcTransformer tx = new EcTransformer();
			tx.initializeUMLResources();
			tx.runE1ToE2Transformation(); 
			tx.runE2ToAlfTransformation();
			tx.compileAlfCode();
			tx.reformatFUmlConnectorBehaviorFile();
			tx.runE2toE3Transformation();
		}
		catch (Exception err)
		{
			System.out.println("Error: " + err.getMessage());
			err.printStackTrace();
		}
	}	
	
	 
	
	//
	// Logging utilities
	//

	protected static void banner(String format, Object... args) {
		System.out.println();
		hrule();

		System.out.printf(format, args);
		if (!format.endsWith("%n")) {
			System.out.println();
		}

		hrule();
		System.out.println();
	}
	
	protected static void hrule() {
		System.out.println("------------------------------------");
	}

	
	class TransformationLog implements Log {

		public boolean CaptureOk = false;
		protected boolean capture = false;
		
        @Override
        public void log(int level, String message, Object param) {
            if (level > 0)
                System.out.println("[" + level + "]: " + message);
            else
                System.out.println(message);
        }

        @Override
        public void log(int level, String message) {
            log(level, message, null);
        }

        @Override
        public void log(String message, Object param) {
            log(0, message, param);
        }

        @Override
        public void log(String message) {
            log(0, message, null);
        }
		
	}
	
	class E1toE2TransformationLog extends TransformationLog {

		public String ConnectorClsModelPath = null;
		public String OutportMultiplicity = null;
		
        @Override
        public void log(int level, String message, Object param) {
        	super.log(level, message, param);
        	
            if (message.contains("CONNECTOR_CLS_MODEL_PATH="))
            {
            	ConnectorClsModelPath = message.replace("CONNECTOR_CLS_MODEL_PATH=", "");
            	ConnectorClsModelPath = ConnectorClsModelPath.trim();
            }
            
            if (message.contains("OUTPORT_MULTIPLICITY="))
            {
            	OutportMultiplicity = message.replace("OUTPORT_MULTIPLICITY=", "");
            	OutportMultiplicity = OutportMultiplicity.trim();
            }
            
            if (ConnectorClsModelPath != null && OutportMultiplicity != null)
				this.CaptureOk = true;
            else
            	this.CaptureOk = false;
        }
    }
	
	class E2ToAlfTransformationLog extends TransformationLog {

		public String AlfCode = "";
		public String AlfPkgName = "";
		
        @Override
        public void log(int level, String message, Object param) {
        	
        	super.log(level, message, param);            
            
        	if (message.contains("<end> Alf Code"))
            {
            	capture = false;
            	CaptureOk = true;
            }
            
            if (capture == true)
            {
            	AlfCode += message + "\n";
            }
            
            if (message.contains("<begin> Alf Code"))
            {
            	capture = true;
            }
            
            if (message.contains("ALF_PKG_NAME="))
            {
            	AlfPkgName = message.replace("ALF_PKG_NAME=", "");
            	AlfPkgName = AlfPkgName.trim();
            }
        }
    }	
}
