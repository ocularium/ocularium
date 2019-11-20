/*
 * Copyright (c) 2016, Ocularium. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
 * and associated documentation files (the "Software"), to deal in the Software without restriction, 
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, 
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies 
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE 
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *
 * Please contact Ocularium at http://ocularium.github.io/ocularium if you need additional 
 * information or have any questions.
 */
package ocularium.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.editor.ModelEditorFactory;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IConstraint;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IOperation;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

/**
 * Ocularium Façade.
 * 
 * Methods importOCL() and exportOCL() process OCL constraints and UML elements.
 * 
 * A simple constraint format is adopted, within a subset of OCL, fully
 * qualified context names and single line constraint specifications.
 * 
 * @author marco.mangan@gmail.com
 *
 */
public class OculariumFacade {

	/**
	 * 
	 */
    private static final Logger LOGGER = Logger.getLogger(OculariumFacade.class.getName());
	
	/**
	 * An Astah project containing UML elements and OCL constraints.
	 */
	private IModel project;

	/**
	 * 
	 * Ocularium plugin actions and views use this façade.
	 * 
	 * @param project
	 *            an Astah project containing UML elements and OCL constraints.
	 */
	public OculariumFacade(IModel project) {
		super();
		assert project != null;
		this.project = project;
		assert this.project != null;
	}

	/**
	 * Note: testing method. Returns all classes with any constraint
	 * declaration.
	 * 
	 * @return
	 */
	public List<IClass> getConstrainedClasses() {
		assert project != null;

		List<IClass> classList = new ArrayList<IClass>();
		try {
			getConstrainedClasses0(project, classList);
		} catch (ClassNotFoundException e) {
			LOGGER.log(Level.SEVERE, "ClassNotFoundException occur", e);				
		} catch (ProjectNotFoundException e) {
			LOGGER.log(Level.SEVERE, "ProjectNotFoundException occur", e);		
		}

		assert project != null;
		assert classList != null;

		return classList;
	}

	/**
	 * 
	 * @param element
	 * @param classList
	 * @throws ClassNotFoundException
	 * @throws ProjectNotFoundException
	 */
	private void getConstrainedClasses0(INamedElement element, List<IClass> classList)
			throws ClassNotFoundException, ProjectNotFoundException {
		// http://astah.net/tutorials/plug-ins/plugin_tutorial_en/html/example.html

		assert project != null;
		assert element != null;
		assert classList != null;

		if (element instanceof IPackage) {
			for (INamedElement ownedNamedElement : ((IPackage) element).getOwnedElements()) {
				getConstrainedClasses0(ownedNamedElement, classList);
			}
		} else if (element instanceof IClass) {
			//
			IClass c = (IClass) element;
			boolean constrained = false;
			if (c.getConstraints().length > 0) {
				constrained = true;
			}
			if (!constrained) {
				for (IAttribute attribute : c.getAttributes()) {
					if (attribute.getConstraints().length > 0) {
						constrained = true;
						break;
					}
				}
			}
			if (!constrained) {

				for (IOperation operation : c.getOperations()) {
					if (operation.getConstraints().length > 0) {
						constrained = true;
						break;
					}
				}
			}

			if (constrained) {
				classList.add(c);
			}

			for (IClass nestedClasses : c.getNestedClasses()) {
				getConstrainedClasses0(nestedClasses, classList);
			}
		}

		assert project != null;
		assert element != null;
		assert classList != null;
	}

	/**
	 * Returns all OCL constraints from the model.
	 * 
	 * An OCL constraint starts with one of these OCL keywords: init, inv, def,
	 * derive, pre, post, body.
	 * 
	 * @return
	 */
	public List<IConstraint> getOclConstraints() {
		assert project != null;

		List<IConstraint> classeList = new ArrayList<IConstraint>();
		try {
			getOclConstraints0(project, classeList);
		} catch (ClassNotFoundException e) {
			LOGGER.log(Level.SEVERE, "ClassNotFoundException occur", e);	
		} catch (ProjectNotFoundException e) {
			LOGGER.log(Level.SEVERE, "ProjectNotFoundException occur", e);	
		}

		assert project != null;
		assert classeList != null;

		return classeList;
	}

	/**
	 * 
	 * @param element
	 * @param constraintList
	 * @throws ClassNotFoundException
	 * @throws ProjectNotFoundException
	 */
	private void getOclConstraints0(INamedElement element, List<IConstraint> constraintList)
			throws ClassNotFoundException, ProjectNotFoundException {
		// http://astah.net/tutorials/plug-ins/plugin_tutorial_en/html/example.html
		assert project != null;
		assert element != null;
		assert constraintList != null;

		if (element instanceof IPackage) {
			for (INamedElement ownedNamedElement : ((IPackage) element).getOwnedElements()) {
				getOclConstraints0(ownedNamedElement, constraintList);
			}
		} else if (element instanceof IClass) {
			IClass c = (IClass) element;

			IConstraint[] ccs = c.getConstraints();
			if (ccs.length > 0) {
				for (IConstraint v : ccs) {
					if (hasOclStart(v)) {
						constraintList.add(v);
					}
				}
			}

			for (IAttribute attribute : c.getAttributes()) {
				IConstraint[] acs = attribute.getConstraints();

				if (acs.length > 0) {
					for (IConstraint v : acs) {
						if (hasOclStart(v)) {
							constraintList.add(v);
						}
					}

				}
			}
			for (IOperation operation : c.getOperations()) {

				IConstraint[] ocs = operation.getConstraints();

				if (ocs.length > 0) {
					for (IConstraint v : ocs) {
						if (hasOclStart(v)) {
							constraintList.add(v);
						}
					}
				}
			}

			for (IClass nestedClasses : ((IClass) element).getNestedClasses()) {
				getOclConstraints0(nestedClasses, constraintList);
			}
		}
	}

	/**
	 * @param v
	 * @return
	 */
	public boolean hasOclStart(IConstraint v) {

		ConstraintFormatter cf = ConstraintFormatter.getConstraintFormatter();

		cf.setConstraint(v);
		return cf.isOcl();
	}

	/**
	 * 
	 * @param output
	 * @throws IOException
	 */
	public void importOCL(Reader input) throws Exception {
		assert project != null;
		assert input != null;

		importOCL0(input);

		assert project != null;
		assert input != null;
	}

	/**
	 * 
	 * @param input
	 * @throws Exception
	 */
	private void importOCL0(Reader input) throws Exception {
		assert project != null;
		assert input != null;

		BufferedReader br = new BufferedReader(input);

		String contextLine;
		ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();

		while ((contextLine = br.readLine()) != null) {
			contextLine = contextLine.trim();
			if (contextLine.startsWith("--")) {
				continue;
			}
			if (contextLine.isEmpty()) {
				continue;
			}

			String constraintLine = br.readLine();

			System.out.printf("context:[%s]\n", contextLine);
			String[] elementSplit = contextLine.split("\\s*context\\s+");

			System.out.printf("elementSplit:[%s]\n", Arrays.toString(elementSplit));

			String elementName = elementSplit[1];
			System.out.printf("elementName:[%s]\n", elementName);

			TransactionManager.beginTransaction();

			BasicModelEditor basicModelEditor = ModelEditorFactory.getBasicModelEditor();

			String[] cleanName = elementName.split("\\(");
			
			// The element may be from different types, all interfaces
			// from a common generalized interface named INamedElement
			//
			// If there is a is a parenthesis inside the name
			// it may indicate:
			// IOperation or IMessage.
			// 
			// Otherwise, it may indicate:
			// IAssociation, IAttribute, or IClass


			String[] qualifiedName = cleanName[0].split("::");
			System.out.printf("qualifiedName:[%s]\n", Arrays.toString(qualifiedName));

			// FIXME: getLevel
			INamedElement classA;

			// FIXME: IClass... Should be another type... IOperation
			INamedElement[] elements = prjAccessor.findElements(INamedElement.class,
					qualifiedName[qualifiedName.length - 1]);
			System.out.printf("elements:[%s]\n", Arrays.toString(elements));
			
			assert elements.length > 0; // At least one element was found
			
			classA = elements[0];

			// TODO: Select exact fit based on parameter types
			// 
			//for (INamedElement iNamedElement : elements) {
				
				//classA = elements[0];
				
			//}
			
			
			System.out.println(contextLine);
			System.out.println(constraintLine);

			if (constraintLine.startsWith("pre")) {
				basicModelEditor.createConstraint(classA, "PRECONDITION: " + constraintLine);
			} else {
				basicModelEditor.createConstraint(classA, constraintLine);
			}
			
			TransactionManager.endTransaction();

		}
	}

	/**
	 * 
	 * @param output
	 * @throws IOException
	 */
	public void exportOCL(Writer output) throws IOException {
		assert project != null;
		assert output != null;

		output.write("-- Made with ocularium (http://ocularium.github.io/ocularium)");
		output.write("\n");
		output.write("--");
		output.write("\n");
		output.write("\n");

		exportOCL0(output);
	}

	
	public void exportUse0(Writer output) throws IOException {
		assert project != null;
		assert output != null;

List<IClass> classes = getConstrainedClasses();	
for (IClass iClass : classes) {
	output.write("class ");
 	output.write(iClass.getName());
	output.write("\n");
}

	}
	
	/**
	 * 
	 * @param output
	 * @throws IOException
	 */
	public void exportOCL0(Writer output) throws IOException {
		assert project != null;
		assert output != null;

		List<IConstraint> allConstraints = getOclConstraints();
		ConstraintFormatter cf = ConstraintFormatter.getConstraintFormatter();

		for (IConstraint iConstraint : allConstraints) {
			cf.setConstraint(iConstraint);
			output.write(cf.toString());
			output.write("\n");
		}

	}

	/**
	 * 
	 * @param output
	 * @throws IOException
	 */
	public void dumpOCL(Writer output) throws IOException {
		assert project != null;
		assert output != null;

		List<IConstraint> actual = getOclConstraints();
		for (IConstraint iConstraint : actual) {
			output.write("\n             alias1: [" + iConstraint.getAlias1() + "]");
			output.write("\n             alias2: [" + iConstraint.getAlias2() + "]");
			output.write("\n         definition: [" + iConstraint.getDefinition() + "]");
			output.write("\n                 id: [" + iConstraint.getId() + "]");
			output.write("\n               name: [" + iConstraint.getName() + "]");
			output.write("\n      specification: [" + iConstraint.getSpecification() + "]");
			output.write("\n           type mod: [" + iConstraint.getTypeModifier() + "]");
			output.write("\n           comments: [" + Arrays.toString(iConstraint.getComments()) + "]");
			output.write("\nconstrained element: [" + Arrays.toString(iConstraint.getConstrainedElement()) + "]");
			output.write("\nconstrained element type: [" + iConstraint.getConstrainedElement()[0].getClass().getName()
					+ "]");
			output.write("\n        constraints: [" + Arrays.toString(iConstraint.getConstraints()) + "]");

			output.write("\n");

		}

	}

	/**
	 * 
	 * @param prjAccessor
	 * @return
	 * @throws ProjectNotFoundException
	 */
	public static String getOclProjectPath(ProjectAccessor prjAccessor) throws ProjectNotFoundException {
		assert prjAccessor != null;

		return prjAccessor.getProjectPath() + ".ocl";
	}

}
