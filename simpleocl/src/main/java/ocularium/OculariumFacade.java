package ocularium;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IConstraint;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IOperation;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.model.IParameter;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

/**
 * 
 * @author marco.mangan@pucrs.br
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
		return classList;
	}

	// http://astah.net/tutorials/plug-ins/plugin_tutorial_en/html/example.html
	private void getAllClasses(INamedElement element, List<IClass> classList)
			throws ClassNotFoundException, ProjectNotFoundException {
		assert project != null;
		
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
	public void exportOCL(Writer output) throws IOException {
		assert project != null;

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
		
		List<IConstraint> actual = getConstraints();
		for (IConstraint iConstraint : actual) {
			output.write("context ");
			IElement[] ces = iConstraint.getConstrainedElement();
			assert ces.length == 1;
			IElement e = ces[0];
			if (e instanceof IAttribute) {
				output.write(e.getOwner().toString());
				output.write("::");
				output.write(e.toString());
			} else if (e instanceof IOperation) {
				IOperation op = (IOperation) e;

				output.write(op.getOwner().toString());
				output.write("::");
				output.write(op.toString());
				output.write("(");
				IParameter[] ps = op.getParameters();

				boolean firstParam = true;

				for (IParameter iParameter : ps) {
					if (!firstParam) {
						output.write(", ");

					}
					String paramName = iParameter.getName().toString();
					String paramType = iParameter.getType().toString();
					output.write(paramName == null ? "" : paramName);
					output.write(paramType == null ? "" : ": " + paramType);

				}

				output.write(")");

				String returnType = op.getReturnType().toString();

				output.write(returnType == null ? "" : ": " + returnType);
			} else {
				output.write(iConstraint.getConstrainedElement()[0].toString());
			}

			output.write("\n");
			String spec = iConstraint.getSpecification();
			if (spec.startsWith("BODYCONDITION:")) {
				spec = spec.substring("BODYCONDITION:".length());
			} else if (spec.startsWith("POSTCONDITION:")) {
				spec = spec.substring("POSTCONDITION:".length());
			}

			output.write(spec);
			output.write("\n");
			output.write("\n");
		}
	}

	public void dumpOCL(Writer output) throws IOException {
		assert output != null;
		assert project != null;
		
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

	public static String getOclProjectPath(ProjectAccessor prjAccessor) throws ProjectNotFoundException {
		assert prjAccessor != null;
		
		return prjAccessor.getProjectPath() + ".ocl";
	}

	// private String getFullName(IClass iClass) {
	// StringBuffer sb = new StringBuffer();
	// IElement owner = iClass.getOwner();
	// while (owner != null && owner instanceof INamedElement &&
	// owner.getOwner() != null) {
	// sb.insert(0, ((INamedElement) owner).getName() + "::");
	// owner = owner.getOwner();
	// }
	// sb.append(iClass.getName());
	// return sb.toString();
	// }
}
