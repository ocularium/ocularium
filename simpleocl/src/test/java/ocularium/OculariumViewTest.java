package ocularium;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

import ocularium.views.OclTableModel;
import ocularium.views.OculariumView;

public class OculariumViewTest {
	private static final String COMPANY_FILE = "/Users/marco/dvlp-2016-2/ocularium/specs/omg-ocl-spec/omg-ocl-spec-minimal-company-inv-enough-employees.asta";

	@Test
	public void testEmptyModelHasZeroRows() throws Exception {
		OculariumView v = new OculariumView();
		OclTableModel m = v.getModel();
		assertEquals(0, m.getRowCount());
	}

	@Test
	public void testEmptyModelHasTwoColumns() throws Exception {
		OculariumView v = new OculariumView();
		OclTableModel m = v.getModel();
		assertEquals(2, m.getColumnCount());
	}

	@Test
	public void testModelLoadFromOpenProject() throws Exception {
		OculariumView v = new OculariumView();
		OclTableModel m = v.getModel();

		ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		prjAccessor.open(COMPANY_FILE, true, false, true);
		assertEquals(1, m.getRowCount());

		prjAccessor.close();
	}

}
