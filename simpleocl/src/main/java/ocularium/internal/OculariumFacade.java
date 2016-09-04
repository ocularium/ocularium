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

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
 * Ocularium Façade and main class.
 * 
 * @author marco.mangan@gmail.com
 *
 */
public class OculariumFacade {

	/**
	 * 
	 */
	private IModel project;

	/**
	 * 
	 * @param project
	 */
	public OculariumFacade(IModel project) {
		super();
		assert project != null;
		this.project = project;
		assert this.project != null;
	}

	/**
	 * 
	 * @return
	 */
	public List<IClass> getConstrainedClasses() {
		assert project != null;

		List<IClass> classList = new ArrayList<IClass>();
		try {
			getAllClasses(project, classList);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProjectNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assert project != null;		
		assert classList != null;

		return classList;
	}

	// http://astah.net/tutorials/plug-ins/plugin_tutorial_en/html/example.html
	private void getAllClasses(INamedElement element, List<IClass> classList)
			throws ClassNotFoundException, ProjectNotFoundException {
		assert project != null;
		assert element != null;
		assert classList != null;

		if (element instanceof IPackage) {
			for (INamedElement ownedNamedElement : ((IPackage) element).getOwnedElements()) {
				getAllClasses(ownedNamedElement, classList);
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
				getAllClasses(nestedClasses, classList);
			}
		}
		
		assert project != null;
		assert element != null;
		assert classList != null;
	}

	/**
	 */
	public List<IConstraint> getConstraints() {
		assert project != null;

		List<IConstraint> classeList = new ArrayList<IConstraint>();
		try {
			getAllConstraints(project, classeList);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProjectNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		assert project != null;		
		assert classeList != null;

		return classeList;
	}

	// http://astah.net/tutorials/plug-ins/plugin_tutorial_en/html/example.html
	private void getAllConstraints(INamedElement element, List<IConstraint> constraintList)
			throws ClassNotFoundException, ProjectNotFoundException {
		assert project != null;
		assert element != null;
		assert constraintList != null;

		if (element instanceof IPackage) {
			for (INamedElement ownedNamedElement : ((IPackage) element).getOwnedElements()) {
				getAllConstraints(ownedNamedElement, constraintList);
			}
		} else if (element instanceof IClass) {
			//
			IClass c = (IClass) element;

			IConstraint[] ccs = c.getConstraints();
			if (ccs.length > 0) {
				for (IConstraint v : ccs) {
					constraintList.add(v);
				}
			}

			for (IAttribute attribute : c.getAttributes()) {
				IConstraint[] acs = attribute.getConstraints();

				if (acs.length > 0) {
					for (IConstraint v : acs) {
						constraintList.add(v);
					}

				}
			}
			for (IOperation operation : c.getOperations()) {

				IConstraint[] ocs = operation.getConstraints();

				if (ocs.length > 0) {
					for (IConstraint v : ocs) {
						constraintList.add(v);
					}
				}
			}

			for (IClass nestedClasses : ((IClass) element).getNestedClasses()) {
				getAllConstraints(nestedClasses, constraintList);
			}
		}
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
	}
	
	private void importOCL0(Reader input) throws Exception {

		
		ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		TransactionManager.beginTransaction();

		BasicModelEditor basicModelEditor = ModelEditorFactory.getBasicModelEditor();

		IClass classA = (IClass)  prjAccessor.findElements(IClass.class, "Company")[0];

		basicModelEditor.createConstraint(classA, "inv enoughEmployees : self.numberOfEmployees > 50");
		

		TransactionManager.endTransaction();		
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

	/**
	 * 
	 * @param output
	 * @throws IOException
	 */
	public void exportOCL0(Writer output) throws IOException {
		assert project != null;
		assert output != null;

		List<IConstraint> allConstraints = getConstraints();
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

		List<IConstraint> actual = getConstraints();
		for (IConstraint iConstraint : actual) {
			output.write("\n             alias1: [" + iConstraint.getAlias1() + "]");
			output.write("\n             alias2: [" + iConstraint.getAlias2() + "]");
			output.write("\n         definition: [" + iConstraint.getDefinition() + "]");
			// name
			// nameSpace
			output.write("\n                 id: [" + iConstraint.getId() + "]");
			output.write("\n               name: [" + iConstraint.getName() + "]");
			output.write("\n      specification: [" + iConstraint.getSpecification() + "]");
			// tagged value
			output.write("\n           type mod: [" + iConstraint.getTypeModifier() + "]");
			//
			// output.write("\n client dep: [" +
			// iConstraint.getClientDependencies().toString()+ "]");
			output.write("\n           comments: [" + Arrays.toString(iConstraint.getComments()) + "]");
			output.write("\nconstrained element: [" + Arrays.toString(iConstraint.getConstrainedElement()) + "]");
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
