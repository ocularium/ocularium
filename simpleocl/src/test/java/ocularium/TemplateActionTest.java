package ocularium;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IConstraint;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

public class TemplateActionTest {

	@Test
	public void testCompanyHasEnoughEmpolyeesGetConstrainedClasses() throws Exception {
		ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		prjAccessor.open("/Users/marco/dvlp-2016-2/ocularium/docs/omg-ocl-spec/omg-ocl-spec-minimal-company-inv-enough-employees.asta");
		IModel project = prjAccessor.getProject();
		Facade f = new Facade(project);
		List<IClass> actual = f.getConstrainedClasses();
		// TODO: check project.getConstraints(); // gets all project constraints?????
		// TODO: check IConstraint; // attributes, operations?
		
		prjAccessor.close();
		assertEquals(1, actual.size());
		assertEquals("Company", actual.get(0).getName());
		
	}

	@Test
	public void testCompanyHasEnoughEmpolyeesGetConstraints() throws Exception {
		ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		prjAccessor.open("/Users/marco/dvlp-2016-2/ocularium/docs/omg-ocl-spec/omg-ocl-spec-minimal-company-inv-enough-employees.asta");
		IModel project = prjAccessor.getProject();
		Facade f = new Facade(project);
		List<IConstraint> actual = f.getConstraints();
		// TODO: check project.getConstraints(); // gets all project constraints?????
		// TODO: check IConstraint; // attributes, operations?
		
		prjAccessor.close();
		assertEquals(1, actual.size());
		//assertEquals("Company", actual.get(0).getName());
		
	}	
}
