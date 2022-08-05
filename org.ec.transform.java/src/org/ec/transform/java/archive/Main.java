package org.ec.transform.java.archive;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
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

public class Main {
	
	public static void main(String[]args)
	{
		banner("Starting transformation");
		
		URI inputURI = URI.createFileURI("/Users/akocatas/Dropbox/payprus-workspace/org.ec.connectors/e2_e3_transformation_test")
				.appendSegment("RoundRobinRequester_M2_test_3")
				.appendFileExtension(UMLResource.FILE_EXTENSION);
		
		EList<EObject> contents = load(inputURI);
		
		EObject o = contents.get(0);
		
		if (o instanceof Model)
		{
			Model m = (Model)o;
			read(m);
		}
		
	}	
	
	protected static void read(Model model)
	{
		EList<PackageableElement> elements = model.getPackagedElements();
		
		// Ok. here, we can do a master loop where we can read through all the model.. 
		// In fact, we already have the model with us now. we can read a list of packaged
		// elements such as classes, properties, associations, etc. 
		
		// now, our job is to find out an "enhanced connector" inside this model, and first, replace
		// it with a class which denotes the connector, and an object of this class. This class should be connected
		// using ports.. or is it? 
		
		// god.. I am too tired.. I just feel like I must not go to work to do anything at all.. why is it like this? 
		// 
		// it is really hard to do anything other than work in the week days. 
		
		int a = 0;
		
		for (PackageableElement e : elements)
		{
			String name = e.getName();
			
			if (e instanceof Interaction)
			{
				banner("Yep, found one!");
				
				Interaction i = (Interaction) e;
				
				Lifeline lifeline = i.getLifeline("con");
				
				if (lifeline == null)
				{
					banner("Opps.. ");
				}
				
			}
		}
		
	}
	
	
	// Checks if there are exactly three lifelines: req, con and prv. 
	protected static void checkLifelines()
	{
		
	}
	
	
	protected static EList<EObject> load(URI uri)
	{
		ResourceSet resourceSet = new ResourceSetImpl();
		
		// Initialize registrations of resource factories, library models,
		// profiles, Ecore metadata, and other dependencies required for
		// serializing and working with UML resources. This is only necessary in
		// applications that are not hosted in the Eclipse platform run-time, in
		// which case these registrations are discovered automatically from
		// Eclipse extension points.
		UMLResourcesUtil.init(resourceSet);
		// UMLResourcesUtil.initLocalRegistries(resourceSet);
		
		
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("uml", new UMLResourceFactoryImpl());

		
		Resource resource = resourceSet.createResource(uri);
		Map<Object, Object> optionsMap = new HashMap<>();
		optionsMap.put("OPTION_RESOLVE", true);
		
		try
		{
			resource.load(new FileInputStream(uri.path()), null);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		EList<EObject> contents =  resource.getContents();
		
		return contents;
		
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
