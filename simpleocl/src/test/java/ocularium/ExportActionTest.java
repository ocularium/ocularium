package ocularium;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.io.FileWriter;

import java.io.Writer;
import java.util.List;

import org.junit.Test;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IConstraint;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

/**
 * 
 * @author marco.mangan@pucrs.br
 *
 */
public class ExportActionTest {
	private static final String WK_C2_FILE = "/Users/marco/dvlp-2016-2/ocularium/specs/royalloyal/mangan-warmer-kleppe-royal-loyal-chapter-2.asta";

	private static final String COMPANY_FILE = "/Users/marco/dvlp-2016-2/ocularium/specs/omg-ocl-spec/omg-ocl-spec-minimal-company-inv-enough-employees.asta";
	private static final String PERSON_SPOUSE_FILE = "/Users/marco/dvlp-2016-2/ocularium/specs/omg-ocl-spec/omg-ocl-spec-minimal-person-has-spouse.asta";
	private static final String PERSON_INCOME_FILE = "/Users/marco/dvlp-2016-2/ocularium/specs/omg-ocl-spec/omg-ocl-spec-minimal-person-income.asta";

	@Test
	public void testCompanyHasEnoughEmployeesGetConstrainedClasses() throws Exception {
		ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		prjAccessor.open(COMPANY_FILE, true, false, true);
		IModel project = prjAccessor.getProject();
		Facade f = new Facade(project);
		List<IClass> actual = f.getConstrainedClasses();
		prjAccessor.close();
		assertEquals(1, actual.size());
		assertEquals("Company", actual.get(0).getName());
	}

	@Test
	public void testPersonHasSpouseGetConstrainedClasses() throws Exception {
		ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		prjAccessor.open(PERSON_SPOUSE_FILE, true, false, true);
		IModel project = prjAccessor.getProject();
		Facade f = new Facade(project);
		List<IClass> actual = f.getConstrainedClasses();
		prjAccessor.close();
		assertEquals(1, actual.size());
		assertEquals("Person", actual.get(0).getName());
	}

	@Test
	public void testPersonIncomeGetConstrainedClasses() throws Exception {
		ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		prjAccessor.open(PERSON_INCOME_FILE, true, false, true);
		IModel project = prjAccessor.getProject();
		Facade f = new Facade(project);
		List<IClass> actual = f.getConstrainedClasses();
		prjAccessor.close();
		assertEquals(1, actual.size());
		assertEquals("Person", actual.get(0).getName());
	}

	@Test
	public void testWkGetConstrainedClasses() throws Exception {
		ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		prjAccessor.open(WK_C2_FILE, true, false, true);
		IModel project = prjAccessor.getProject();
		Facade f = new Facade(project);
		List<IClass> actual = f.getConstrainedClasses();
		prjAccessor.close();
		assertEquals(7, actual.size());
	}

	@Test
	public void testCompanyHasEnoughEmployeesGetConstraints() throws Exception {
		ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		prjAccessor.open(COMPANY_FILE, true, false, true);
		IModel project = prjAccessor.getProject();
		Facade f = new Facade(project);
		List<IConstraint> actual = f.getConstraints();
		prjAccessor.close();
		assertEquals(1, actual.size());
		assertEquals("inv enoughEmployees:self.numberOfEmployees > 50", actual.get(0).toString());
	}

	@Test
	public void testCompanyHasEnoughEmployeesExportOclToString() throws Exception {
		ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		prjAccessor.open(COMPANY_FILE, true, false, true);
		IModel project = prjAccessor.getProject();
		Facade f = new Facade(project);
		Writer actual = new StringWriter();
		f.exportOCL0(actual);
		prjAccessor.close();
		assertEquals("context Company\ninv enoughEmployees:self.numberOfEmployees > 50\n\n", actual.toString());
		actual.close();
	}

	@Test
	public void testCompanyHasEnoughEmployeesExportOclToSameNameFile() throws Exception {
		ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		prjAccessor.open(COMPANY_FILE, true, false, true);
		IModel project = prjAccessor.getProject();
		Facade f = new Facade(project);
		Writer actual = new FileWriter(Facade.getOclProjectPath(prjAccessor));
		f.exportOCL0(actual);
		prjAccessor.close();
		// assertEquals("context Company\ninv
		// enoughEmployees:self.numberOfEmployees > 50\n\n", actual.toString());
		actual.close();
	}
}
