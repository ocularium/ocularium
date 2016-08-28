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
package ocularium;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
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
 * @author marco.mangan@gmail.com
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
		OculariumFacade f = new OculariumFacade(project);
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
		OculariumFacade f = new OculariumFacade(project);
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
		OculariumFacade f = new OculariumFacade(project);
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
		OculariumFacade f = new OculariumFacade(project);
		List<IClass> actual = f.getConstrainedClasses();
		prjAccessor.close();
		assertEquals(7, actual.size());
	}

	@Test
	public void testCompanyHasEnoughEmployeesGetConstraints() throws Exception {
		ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		prjAccessor.open(COMPANY_FILE, true, false, true);
		IModel project = prjAccessor.getProject();
		OculariumFacade f = new OculariumFacade(project);
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
		OculariumFacade f = new OculariumFacade(project);
		Writer actual = new StringWriter();
		f.exportOCL0(actual);
		actual.close();
		prjAccessor.close();
		assertEquals("context Company\ninv enoughEmployees:self.numberOfEmployees > 50\n\n", actual.toString());
	}

	@Test
	public void testCompanyHasEnoughEmployeesExportOclToSameNameFile() throws Exception {
		ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		prjAccessor.open(COMPANY_FILE, true, false, true);
		String actual = OculariumFacade.getOclProjectPath(prjAccessor);
		String expected = COMPANY_FILE + ".ocl";
		prjAccessor.close();
		assertEquals(expected, actual);
	}
}
