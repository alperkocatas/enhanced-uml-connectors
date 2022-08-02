package org.ec.transform.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.m2m.qvt.oml.BasicModelExtent;
import org.eclipse.m2m.qvt.oml.ExecutionContextImpl;
import org.eclipse.m2m.qvt.oml.ExecutionDiagnostic;
import org.eclipse.m2m.qvt.oml.ModelExtent;
import org.eclipse.m2m.qvt.oml.TransformationExecutor;
import org.eclipse.m2m.qvt.oml.util.StringBufferLog;
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
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;
import org.eclipse.emf.mwe.core.issues.Issues;

public class E2E3Transformer {
	
	private static final Logger logger = Logger.getLogger(E2E3Transformer.class);

	
	private Map<@NonNull String, @NonNull String> ePackageMappings = new HashMap<>();
	private Map<@NonNull String, @NonNull URI> uriMappings = new HashMap<>();
	private Map<String, Object> configs = new HashMap<>();
	private List<String> ins = new ArrayList<>();
	
	
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
		
		logger.info("Running E1toE3Transformation");
		
		URI inputURI = URI.createFileURI("/Users/akocatas/Dropbox/payprus-workspace/org.ec.transform.qvto/transforms")
				.appendSegment("RoundRobinTransform.qvto");
		new E2E3Transformer().ExecuteTransformation(inputURI);
		
	}	
	
	
	public void ExecuteTransformation(URI transformationURI)
	{
		
		ResourceSet qvtResourceSet = new ResourceSetImpl();
		EPackage.Registry packageRegistry = qvtResourceSet.getPackageRegistry();
		//
		//	Ensure the platform references to used EPackages use the genmodelled EPackage.
		//
		for (Map.Entry<@NonNull String, @NonNull String> entry : ePackageMappings.entrySet()) {
			String filePath = entry.getKey();
			String nsURI = entry.getValue();
			installEPackageMapping(qvtResourceSet, filePath, nsURI);
		}
		//
		//	Ensure that the modeltype resolves to its dynamic EPackage.
		//
		for (Map.Entry<@NonNull String, @NonNull URI> entry : uriMappings.entrySet()) {
			String name = entry.getKey();
			URI modelURI = entry.getValue();
			installURImapping(qvtResourceSet, name, modelURI);
		}
		
		TransformationExecutor transformationExecutor = 
				new TransformationExecutor(transformationURI, packageRegistry);
		
		Diagnostic diag = transformationExecutor.loadTransformation();
		if (diag.getSeverity() != Diagnostic.OK) {
			StringBuilder s = new StringBuilder();
			s.append("Failed to load ");
			s.append(transformationURI);
			for (Diagnostic child : diag.getChildren()) {
				s.append("\n  " + child.getMessage());
			}
			logger.info(s.toString());
			return;
		}
		

		ResourceSet resourceSet = new ResourceSetImpl(); //transformationExecutor.getResourceSet();
		resourceSet.setPackageRegistry(packageRegistry);
		List<@NonNull ModelExtent> modelExtents = new ArrayList<ModelExtent>();
		for (String in : ins) {
			URI inURI = URI.createURI(in, true);
			banner("Loading '" + inURI + "'");
			Resource inResource = resourceSet.getResource(inURI, true);
			if (inResource.getErrors().size() > 0) {
			//	issues.addError(this, "Failed to load", inURI, null, null);
				return;
			}
			modelExtents.add(new BasicModelExtent(inResource.getContents()));
		}

//		if (out != null) {
//			modelExtents.add(new BasicModelExtent());
//		}
		
		
		StringBufferLog qvtoLog = new StringBufferLog();
		try {
			banner("Executing transformation '" + transformationURI + "'");
			ExecutionContextImpl executionContext = new ExecutionContextImpl();
			executionContext.setLog(qvtoLog);
			initializeConfigurationProperties(executionContext);
			//			executionContext.setMonitor();
			ExecutionDiagnostic execDiag = transformationExecutor.execute(executionContext, 
					modelExtents.toArray(new ModelExtent[modelExtents.size()]));
			if (execDiag.getSeverity() != Diagnostic.OK) {
				StringBuilder s = new StringBuilder();
				s.append("Failed to execute ");
				s.append(transformationURI);
				s.append(": ");
				s.append(execDiag.getMessage());
				for (Diagnostic child : execDiag.getChildren()) {
					s.append("\n  " + child.getMessage());
				}
				//issues.addError(this, s.toString(), txURI, null, null);
				banner(s.toString());
				return;
			}
		} catch (Exception e) {
			banner("Failed to launch transformation: " + transformationURI.toString());
			return;
		}

	}

	private void installEPackageMapping(@NonNull ResourceSet resourceSet, @NonNull String sourceURI, @NonNull String nsURI) {
		EPackage.Registry packageRegistry = resourceSet.getPackageRegistry();
		EPackage ePackage = (EPackage)EPackage.Registry.INSTANCE.get(nsURI);
		packageRegistry.put(URI.createPlatformPluginURI(sourceURI, true).toString(), ePackage);
		packageRegistry.put(URI.createPlatformResourceURI(sourceURI, true).toString(), ePackage);
	}	
	
	public void installURImapping(@NonNull ResourceSet resourceSet, @NonNull String sourceURI, @NonNull URI targetURI) {
		EPackage.Registry packageRegistry = resourceSet.getPackageRegistry();
		Resource resource = resourceSet.getResource(targetURI, true);
		EPackage ePackage = (EPackage)resource.getContents().get(0);
		packageRegistry.put(ePackage.getNsURI(), ePackage);
		packageRegistry.put(targetURI.toString(), ePackage);
		packageRegistry.put(sourceURI, ePackage);
	}	

	/**
	 * Clients may override to do any configuration
	 * properties initialization
	 *
	 * @return creates a context to be used by the transformation
	 */
	protected void initializeConfigurationProperties(ExecutionContextImpl context) {
		for (String key : configs.keySet()) {
			Object value = configs.get(key);
			context.setConfigProperty(key, value);
		}
	}
	
	
//	@Override
//	public void checkConfiguration(Issues issues) {
//		if (getUri() == null) {
//			issues.addError(this, "uri not specified.");
//		}
//	}
	
	
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
}
