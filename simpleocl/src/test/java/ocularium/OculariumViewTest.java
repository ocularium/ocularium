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

import org.junit.Before;
import org.junit.Test;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

import ocularium.views.OclTableModel;
import ocularium.views.OculariumView;

/**
 * Sanity check and automated testing for Ocularium view and table model.
 * 
 * @author marco.mangan@gmail.com
 *
 */
public class OculariumViewTest {
	
	/**
	 * 
	 */
	private static final String COMPANY_FILE = "../specs/omg-ocl-spec/omg-ocl-spec-minimal-company-inv-enough-employees.asta";

	/**
	 * 
	 */
	private OclTableModel m;

	@Before
	public void initModel() {
		OculariumView v = new OculariumView();
		m = v.getModel();
	}

	@Test
	public void testEmptyModelHasZeroRows() throws Exception {
		initModel();
		assertEquals(0, m.getRowCount());
	}

	@Test
	public void testEmptyModelHasTwoColumns() throws Exception {
		initModel();
		assertEquals(2, m.getColumnCount());
	}

	@Test
	public void testModelLoadFromOpenProject() throws Exception {
		initModel();

		ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		prjAccessor.open(COMPANY_FILE, true, false, true);
		assertEquals(1, m.getRowCount());

		prjAccessor.close();
	}

}
