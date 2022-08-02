package org.ec.transform.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

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
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.utils.Mapping;

public class E2E3Transformer3 {
	
	private static final Logger logger = Logger.getLogger(E2E3Transformer3.class);

//	// Static initializer for archiveSchemes.
//	static
//	{
//		Set<String> set = new HashSet<String>();
//		String propertyValue =
//		System.getProperty("org.eclipse.emf.common.util.URI.archiveSchemes ");
//	
//		if (propertyValue == null)
//		{
//			set.add(URI.SCHEME_JAR);
//			set.add(SCHEME_ZIP);
//			set.add(SCHEME_ARCHIVE);
//		}
//		else
//		{
//			for (StringTokenizer t = new StringTokenizer(propertyValue);
//			t.hasMoreTokens(); )
//			{
//				set.add(t.nextToken().toLowerCase());
//			}
//		}
//	
//		archiveSchemes = Collections.unmodifiableSet(set);
//	}
	
	
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
		
//		URI inputURI = URI.createFileURI("/Users/akocatas/Dropbox/payprus-workspace/org.ec.transform.qvto/transforms")
//				.appendSegment("RoundRobinTransform.qvto");

		QVToTransformationExecutor te = new QVToTransformationExecutor();
		
		te.setUri("file:/Users/akocatas/Dropbox/payprus-workspace/org.ec.transform.qvto/transforms/RoundRobinTransform.qvto");
		te.addIn("file:/Users/akocatas/Dropbox/payprus-workspace/org.ec.connectors/RoundRobinRequesterQvto/RoundRobinRequester_E1.uml");
		te.setOut("file:/Users/akocatas/Dropbox/payprus-workspace/org.ec.connectors/RoundRobinRequesterQvto/RoundRobinRequester_E2_java.uml");
		
		//te.adduriMapping(new Mapping(null, null))
		
		//UMLPackage.eNS_URI
		
//		modeltype uml "strict" uses uml('http://www.eclipse.org/uml2/5.0.0/UML');

		
		te.preInvoke();
		
		te.invoke(null);
		
		te.postInvoke();
		
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
}
