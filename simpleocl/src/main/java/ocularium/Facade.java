package ocularium;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

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
 * 
 * @author marco.mangan@pucrs.br
 *
 */
public class Facade {

	private IModel project;

	/**
	 * 
	 * @param project
	 */
	public Facade(IModel project) {
		super();
		this.project = project;
	}

	/**
	 * 
	 * @return
	 */
	public List<IClass> getConstrainedClasses() {
		List<IClass> classeList = new ArrayList<IClass>();
		try {
			getAllClasses(project, classeList);
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
	private void getAllClasses(INamedElement element, List<IClass> classList)
			throws ClassNotFoundException, ProjectNotFoundException {
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
	private void getAllConstraints(INamedElement element, List<IConstraint> classList)
			throws ClassNotFoundException, ProjectNotFoundException {
		if (element instanceof IPackage) {
			for (INamedElement ownedNamedElement : ((IPackage) element).getOwnedElements()) {
				getAllConstraints(ownedNamedElement, classList);
			}
		} else if (element instanceof IClass) {
			//
			IClass c = (IClass) element;
			
			IConstraint[] ccs = c.getConstraints();
			if (ccs.length > 0) {
				for (IConstraint v : ccs) {
					classList.add(v);
				}
			}
			

			for (IAttribute attribute : c.getAttributes()) {
				IConstraint[] acs = attribute.getConstraints();

				if (acs.length > 0) {
					for (IConstraint v : acs) {
						classList.add(v);
					}
		
				}
			}
			for (IOperation operation : c.getOperations()) {
				
				IConstraint[] ocs = operation.getConstraints();

			
				if (ocs.length > 0) {
					for (IConstraint v : ocs) {
						classList.add(v);
					}
				}
			}
			
			for (IClass nestedClasses : ((IClass) element).getNestedClasses()) {
				getAllConstraints(nestedClasses, classList);
			}
		}
	}

	/**
	 * 
	 * @param output
	 * @throws IOException
	 */
	public void exportOCL(Writer output) throws IOException {
		List<IConstraint> actual = getConstraints();
		for (IConstraint iConstraint : actual) {
			// "alias1 " +iConstraint.getAlias1()
			// + "alias2 " +iConstraint.getAlias2()
			// + "definition " +iConstraint.getDefinition()
			// + "id " +iConstraint.getId()
			// +
			// "name " +iConstraint.getName()
			// + "type mod " +iConstraint.getTypeModifier()
			output.write("context " + iConstraint.getConstrainedElement()[0].toString() + "\n"
					+ iConstraint.getSpecification() + "\n" + "\n");
		}

	}
	
	public static String getOclProjectPath(ProjectAccessor prjAccessor) throws ProjectNotFoundException {
		return prjAccessor.getProjectPath() + ".ocl";
	}
}
