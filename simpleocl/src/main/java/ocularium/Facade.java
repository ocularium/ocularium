package ocularium;

import java.util.ArrayList;
import java.util.List;

import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IConstraint;
import com.change_vision.jude.api.inf.model.IModel;
//
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;

//
public class Facade {

	private IModel project;

	public Facade(IModel project) {
		super();
		this.project = project;
	}

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
			if (c.getConstraints().length > 0)
				classList.add(c);
			for (IClass nestedClasses : ((IClass) element).getNestedClasses()) {
				getAllClasses(nestedClasses, classList);
			}
		}
	}

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
			if (c.getConstraints().length > 0)
				for (IConstraint v : ccs) {
					classList.add(v);
				}
			for (IClass nestedClasses : ((IClass) element).getNestedClasses()) {
				getAllConstraints(nestedClasses, classList);
			}
		}
	}	
}
