package org.ec.transform.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.m2m.qvt.oml.BasicModelExtent;
import org.eclipse.m2m.qvt.oml.ExecutionContextImpl;
import org.eclipse.m2m.qvt.oml.ExecutionDiagnostic;
import org.eclipse.m2m.qvt.oml.ModelExtent;
import org.eclipse.m2m.qvt.oml.TransformationExecutor;
import org.eclipse.m2m.qvt.oml.util.Log;
import org.eclipse.m2m.qvt.oml.util.WriterLog;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
 
/**
 * Runs the enhanced connector model transformation workflow which consists of QVTo transformations
 * and compilation of the Alf code into fUML using the alfc compiler. 
 * 
 * @author Alper Tolga Kocatas
 * 
 */ 
public class EcTransformer {
	
	private static final Logger logger = Logger.getLogger(EcTransformer.class);
	
	/**
	 * Path of E1toE2 Qvto transformation 
	 */
	private String e1ToE2TxPath = "org.ec.transform.qvto" + File.separator +  
			"transforms" + File.separator + "E1toE2Transformation.qvto";
	
	/**
	 * Path of E2toAlf Qvto transformation 
	 */
	private String e2ToAlfTxPath = "org.ec.transform.qvto" + File.separator +  
			"transforms"+File.separator+"E2ToAlfTransformation.qvto";
	
	/**
	 * Path of E2toE3 Qvto transformation 
	 */
	private String e2Toe3TxPath = "org.ec.transform.qvto" + File.separator +  
			"transforms" + File.separator + "E2toE3Transformation.qvto";
	
	/**
	 * Path of directory where intermediate fUML models are stored.  
	 */
	private String fUMLOutputDir = "org.ec.connectors" + File.separator + "alf_output";	
	
	/**
	 * Path of Alf reference implementation Example: 
	 *    C:\Users\akocatas\Documents\AlfRefImp-1.1.0k 
	 */
	private String AlfWsPath;
	
	/**
	 * Path of plugins directory under eclipse installation. For example: 
	 *      D:/Apps/papyrus-2021-09-5.2.0-win64/Papyrus/plugins
	 */
	private String EclipsePluginsDir;
	
	/**
	 * Worspace directory. This is the path to the directory where the repository resides. 
	 * Example: C:\Users\akocatas\payprus-workspace 
	 */
	private String wsDir = "";
	
	
	/**
	 * Input E1 model kind. Following e1 models are provided as preset models. Switching
	 * between these input models are performed using the -e1Model argument. 
	 * 
	 * If you want to use a custom e1 model file, you should use the -e1ModelPath argument
	 */
	private enum E1ModelKind {
		RoundRobinRequester, MultiDestRequester, RequestBarrier, LessFrequentSender};
	
	
	/**
	 * Intermediate variables to store data in between transformations
	 */
	private String e1ModelPath;
	private String e2ModelPath;
	private String e3ModelPath;
	private ResourceSet resourceSet;
	private String ConnectorBehaviorAlfCode;
	private String ConnectorBehaviorFumlFilePath;
	private String ConnectorClsModelPath = null;
	private String InOutportMultiplicity = null;
	private String AlfPkgName = null;
	private String EcConfiguration = null;
	
	private enum PlatformKind {Windows, Other};
	private PlatformKind platform;
	
	
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
			logger.info("Starting...");
			EcTransformer tx = new EcTransformer();
			tx.CalculateFilePaths(args);
			tx.initializeUMLResources();
			tx.runE1ToE2Transformation(); 
			tx.runE2ToAlfTransformation();
			tx.compileAlfCode();
			tx.reformatFUmlConnectorBehaviorFile();
			tx.runE2toE3Transformation();
			logger.info("Enhanced connector transformation completed successfully");
		}
		catch (Exception err)
		{
			System.out.println("Error: " + err.getMessage());
			err.printStackTrace();
			
			System.out.println("Enhanced connector transformation completed with error(s)");
		}
	}	
	
	private String getCmdLineArg(String [] args, String argname)
	{
		String argVal = null;
		for (int i = 0; i < args.length; i++)
		{
			if (args[i].equals(argname))
			{
				if (i < args.length - 1)
					argVal = args[i+1];
			}
		}
		
		return argVal;
	}
	
	
	private void CalculateFilePaths(String [] args) throws Exception
	{
		if (File.separator.equals("\\"))
			this.platform = PlatformKind.Windows;
		else 
			this.platform = PlatformKind.Other;
		
		wsDir = "";
		String wsDirArg = getCmdLineArg(args, "-wsDir");
		if (wsDirArg != null)
		{
			wsDir = wsDirArg;
		}
		else
		{
			throw new Exception("Argument -wsDir is mandatory");
		}
		
		wsDir = (new File(wsDir).getAbsolutePath());

		if (!wsDir.endsWith(File.separator))
			wsDir += File.separator;
		
		logger.debug("wsDir is: " + wsDir);
		
		
		e1ModelPath = wsDir + "org.ec.connectors" + File.separator;
		String e1ModelArg = getCmdLineArg(args, "-e1Model");
		if (e1ModelArg != null)
		{
			if (e1ModelArg.equals(E1ModelKind.RoundRobinRequester.toString()))
			{
				e1ModelPath += "RoundRobinRequester" + File.separator + "RoundRobinRequester_E1.uml"; 
			}
			else if (e1ModelArg.equals(E1ModelKind.MultiDestRequester.toString()))
			{
				e1ModelPath += "MultiDestRequester" + File.separator + "MultiDestRequester_E1.uml"; 
			}
			else if (e1ModelArg.equals(E1ModelKind.RequestBarrier.toString()))
			{
				e1ModelPath += "RequestBarrier" + File.separator + "RequestBarrier_E1.uml"; 
			}
			else if (e1ModelArg.equals(E1ModelKind.LessFrequentSender.toString()))
			{
				e1ModelPath += "LessFrequentRequester" + File.separator + "LessFrequentRequester_E1.uml"; 
			}
			else 
			{
				throw new Exception("Unknown E1 model type: " + e1ModelArg + 
						". Please use the -e1ModelPath argument to specify a custom path" + 
						" relative to the workspace dir. e.g. " + 
						"org.ec.connectors/RoundRobinRequester/RoundRobinRequester_E1.uml");
			}
		}
		else
		{
			String e1ModelPathArg = getCmdLineArg(args, "-e1ModelPath");
			if (e1ModelPathArg != null)
			{
				e1ModelPath = wsDir + File.separator + e1ModelPathArg; 
			}
		}
		
		logger.info("Using e1Model at path: " + e1ModelPath);
		
		AlfWsPath = wsDir + (wsDir.endsWith(File.separator)?"":File.separator) + "AlfRefImp-1.1.0k" + File.separator;
				
		e1ToE2TxPath = wsDir + e1ToE2TxPath; 
		
		e2ToAlfTxPath = wsDir + e2ToAlfTxPath;
		
		e2Toe3TxPath = wsDir + e2Toe3TxPath;
		
		fUMLOutputDir = wsDir + fUMLOutputDir;	
		
		File e1File = new File(e1ModelPath);
		
		// Calculate model dir
		String e1ModelDir = getDirectory(e1File);
		
		String e1FileName = e1File.getName();
		
		e2ModelPath = e1ModelDir + File.separator + e1FileName.replace(".uml", "_E2.uml");
		
		e3ModelPath = e1ModelDir +  File.separator + e1FileName.replace(".uml", "_E3.uml");
		
		AlfPkgName = e1FileName.replace(".uml", "");
		
		EclipsePluginsDir = getCmdLineArg(args, "-eclipsePluginsDir");
		if (EclipsePluginsDir == null)
		{
			throw new Exception("Argument -eclipsePluginsDir is mandatory");
		}

		logger.debug("EclipsePluginsDir: " + EclipsePluginsDir);
	}
	
	/**
	 * return the directory of the file
	 * @param filePath
	 * @return
	 */
	public static String getDirectory(File file)
	{
		if (file.exists())
		{
			String parent = file.getParent();
			
			if ((new File(parent)).isDirectory())
			{
				return parent;
			}
		}
		
		return null;
	}
	
	public void initializeUMLResources() throws Exception
	{
		resourceSet = new ResourceSetImpl();
		resourceSet.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().
			put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
		Map uriMap = resourceSet.getURIConverter().getURIMap();
		
		URI uri;
		String prefix = "jar:file:";
		if (!EclipsePluginsDir.startsWith(File.separator))
			prefix += File.separator;
		
		uri = URI.createURI(prefix + EclipsePluginsDir +
				"/org.eclipse.uml2.uml.resources_5.5.0.v20210228-1829.jar!/");
		
		uriMap.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP), 
				uri.appendSegment("libraries").appendSegment(""));
		uriMap.put(URI.createURI(UMLResource.METAMODELS_PATHMAP), 
				uri.appendSegment("metamodels").appendSegment(""));
		uriMap.put(URI.createURI(UMLResource.PROFILES_PATHMAP), 
				uri.appendSegment("profiles").appendSegment(""));
	}
	
	
	public void runE1ToE2Transformation() throws Exception
	{
		logger.info("Running E1toE2Transformation");

		URI e1ModelUri = URI.createFileURI(e1ModelPath);
		resourceSet.createResource(e1ModelUri);
		Resource r = resourceSet.getResource(e1ModelUri, true);
		EList<EObject> inObjects = r.getContents();
		
		ModelExtent input = new BasicModelExtent(inObjects);	
		ExecutionContextImpl context = new ExecutionContextImpl();
		context.setConfigProperty("LogIndentLevel", 5);
		context.setConfigProperty("LogLevel", 0);
		
	    OutputStreamWriter outStream = new OutputStreamWriter(System.out);
		E1toE2TransformationLog log = new E1toE2TransformationLog();
		context.setLog(log);
	    
		URI transformationURI = URI.createFileURI(e1ToE2TxPath);
		TransformationExecutor executor = new TransformationExecutor(transformationURI);
		ExecutionDiagnostic result = executor.execute(context, input);
		Diagnostic loadResult = executor.loadTransformation();
		logger.info("E1 to E2 transformation load result: " + loadResult.getMessage());
		
		if (loadResult.getSeverity() != Diagnostic.OK)
		{
			throw new Exception("Error occurred while loading E1 to E2 transformation");
		}
		
		if (log.CaptureOk)
		{
			this.ConnectorClsModelPath = log.ConnectorClsModelPath;
			this.InOutportMultiplicity = log.InOutportMultiplicity;
			this.EcConfiguration = log.EcConfiguration;
		}
		else
		{
			throw new Exception("Results of E1 to E2 transformation could not be captured");
		}
		
		List<EObject> outObjects = input.getContents();
        URI outUri = URI.createFileURI(e2ModelPath);
        Resource res = resourceSet.createResource(outUri);
        res.getContents().addAll(outObjects);
        try {
            res.save(Collections.emptyMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        logger.info("E1 to E2 transformation ran successfully");
		logger.info("E2 model is saved to: " + e2ModelPath);
        printLogSeparator();
	}
	
	public void runE2ToAlfTransformation() throws Exception
	{
		logger.info("Running E2toAlfTransformation");

		URI e1ModelUri = URI.createFileURI(e2ModelPath);
		resourceSet.createResource(e1ModelUri);
		Resource r = resourceSet.getResource(e1ModelUri, true);
		EList<EObject> inObjects = r.getContents();

		ModelExtent input = new BasicModelExtent(inObjects);	
		ModelExtent output = new BasicModelExtent();
		ExecutionContextImpl context = new ExecutionContextImpl();		
		context.setConfigProperty("CONNECTOR_CLS_MODEL_PATH", this.ConnectorClsModelPath);
		context.setConfigProperty("OUTPORT_MULTIPLICITY", this.InOutportMultiplicity);
		context.setConfigProperty("ALF_PKG_NAME", this.AlfPkgName);
		context.setConfigProperty("EC_CONFIGURATION", this.EcConfiguration);
		context.setConfigProperty("LogLevel", 0);
		
		
		E2ToAlfTransformationLog log = new E2ToAlfTransformationLog();
		context.setLog(log);
	    
		URI transformationURI = URI.createFileURI(e2ToAlfTxPath);
		TransformationExecutor executor = new TransformationExecutor(transformationURI);
		ExecutionDiagnostic result = executor.execute(context, input, output);
		Diagnostic loadResult = executor.loadTransformation();
		logger.info("E2 to Alf transformation load result: " + loadResult.getMessage());

		if (loadResult.getSeverity() != Diagnostic.OK)
		{
			throw new Exception("Error occurred while loading E1 to E2 transformation");
		}
		
		if (log.CaptureOk = true)
		{
			logger.info("Alf code is captured successfully");
			ConnectorBehaviorAlfCode = log.AlfCode; 
		}
		else
		{
			throw new Exception("Alf code could not be captured after E2toE3 model transformation");
		}
		
		logger.info("E2 to Alf transformation ran successfully");
		printLogSeparator();
	}
	
	public void compileAlfCode() throws IOException, InterruptedException, Exception
	{
		// Create an alf file in ??		
		String alfPrjPath = AlfWsPath + "dist/alf/";
		String alfFilePath = alfPrjPath + "Models/" + AlfPkgName + ".alf";

		logger.info("Creating alf file: " + alfFilePath);
		
		// Create Alf File:
		FileWriter writer = new FileWriter(alfFilePath);
		writer.write(this.ConnectorBehaviorAlfCode);
		writer.close();
		
		logger.info("Compiling alf file using alfc");
		
		// Compile Alf File using alfc
		String alfc = "alfc";
		if (this.platform == PlatformKind.Windows)
			alfc += ".bat";
		String alfcPath = alfPrjPath + alfc;
		String modelsDirPath = alfPrjPath + "Models";
		String librariesDirPath = alfPrjPath + "Libraries";
		
		ProcessBuilder pb = new ProcessBuilder(alfcPath,
				"-l", librariesDirPath, 
				"-m", modelsDirPath,
				"-u", "file:/" + fUMLOutputDir, 
				"-f", 
				"-v", 
				alfFilePath
				);
		
		pb.directory(new File(alfPrjPath).getAbsoluteFile());
		
		Process process = pb.start();
		
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;

		boolean alfCompilationIsSuccessful = false;
		while ((line = br.readLine()) != null) {
		  logger.debug("alfc stdout: " + line);
		  
		  if (line.contains("Mapped successfully."))
		  {
			  alfCompilationIsSuccessful = true;
		  }
		}

		process.waitFor();
		
		// Alfc does not exit with error code if the code doest not compile
		// so this is just left as a defensive code. 
		if (process.exitValue() != 0) {
			InputStream eis = process.getErrorStream();
			InputStreamReader eisr = new InputStreamReader(eis);
			BufferedReader ebr = new BufferedReader(eisr);
			
			while ((line = ebr.readLine()) != null) {
				logger.error("alfc stderr: " + line);
			}	
			
			throw new Exception("Error occurred while compiling Alf file");
		}
		
		// Actual alf compilation result is determined here. 
		if (alfCompilationIsSuccessful == false)
		{
			throw new Exception("Error occurred while compiling Alf file");
		}
		
		logger.info("Alf code compiled successfully");
		
		this.ConnectorBehaviorFumlFilePath = fUMLOutputDir + File.separator + AlfPkgName + ".uml";
		printLogSeparator();
	}
	
	/**
	 * reformat the pathmaps so that they can be resolved within the project. 
	 * @throws IOException 
	 */
	public void reformatFUmlConnectorBehaviorFile() throws IOException
	{		
		logger.debug("Replacing fUML pathmaps");
		String fileContents = new String(
				Files.readAllBytes(Paths.get(ConnectorBehaviorFumlFilePath)));
		
		fileContents = fileContents
				.replaceAll("pathmap://ALF_LIBRARIES/", "../Libraries/uml/");
		
		FileWriter writer = new FileWriter(ConnectorBehaviorFumlFilePath);
		writer.write(fileContents);
		writer.close();
	}
	
	
	public void runE2toE3Transformation() throws Exception
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
		context.setConfigProperty("LogLevel", 0);
		context.setConfigProperty("CONNECTOR_CLS_MODEL_PATH", this.ConnectorClsModelPath);
		context.setConfigProperty("OUTPORT_MULTIPLICITY", this.InOutportMultiplicity);
		context.setConfigProperty("ALF_PKG_NAME", this.AlfPkgName); 
		
		
	    OutputStreamWriter outStream = new OutputStreamWriter(System.out);
	    Log log = new WriterLog(outStream);
	    context.setLog(log);
		
		URI transformationURI = URI.createFileURI(e2Toe3TxPath);
		TransformationExecutor executor = new TransformationExecutor(transformationURI);
		ExecutionDiagnostic result = executor.execute(context, e2Input, fUmlInput);
		Diagnostic loadResult = executor.loadTransformation();
		logger.info(loadResult.getMessage());
		
		if (loadResult.getSeverity() != Diagnostic.OK)
		{
			throw new Exception("Error occurred while loading E2 to E3 transformation");
		}
		
		List<EObject> outObjects = e2Input.getContents();
        URI outUri = URI.createFileURI(e3ModelPath);
        Resource res = resourceSet.createResource(outUri);
        res.getContents().addAll(outObjects);
        try {
            res.save(Collections.emptyMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        logger.info("E2toE3Transformation executed successfully");
		logger.info("E3 model is saved to: " + e3ModelPath);
        printLogSeparator();
	}

	public void printLogSeparator()
	{
		logger.info("----------------------------------------------------------------------------");
		logger.info("");
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
		public String InOutportMultiplicity = null;
		public String EcConfiguration = null;
		
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
            	InOutportMultiplicity = message.replace("OUTPORT_MULTIPLICITY=", "");
            	InOutportMultiplicity = InOutportMultiplicity.trim();
            }
            
            if (message.contains("INPORT_MULTIPLICITY="))
            {
            	InOutportMultiplicity = message.replace("INPORT_MULTIPLICITY=", "");
            	InOutportMultiplicity = InOutportMultiplicity.trim();
            }
            
            if (message.contains("EC_CONFIGURATION="))
            {
            	EcConfiguration = message.replace("EC_CONFIGURATION=", "");
            	EcConfiguration = EcConfiguration.trim();
            }
            
            if (ConnectorClsModelPath != null && 
            	InOutportMultiplicity != null &&
            	EcConfiguration != null)
            {
				this.CaptureOk = true;
            }
            else
            {
            	this.CaptureOk = false;
            }
        }
    }
	
	class E2ToAlfTransformationLog extends TransformationLog {

		public String AlfCode = "";
		
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
        }
    }	
}
