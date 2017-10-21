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
import static org.junit.Assert.assertTrue;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.editor.ModelEditorFactory;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IOperation;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

import ocularium.internal.OculariumFacade;

/**
 * Sanity checking and automated testing for importing opaque constraints.
 * 
 * @author marco.mangan@gmail.com
 *
 */
public class ImportActionTest {

	private static final String WK_C2_EMPTY_FILE = "../specs/royalloyal/mangan-warmer-kleppe-royal-loyal-chapter-2-empty.asta";
	private static final String WK_C2_FILE = "../specs/royalloyal/mangan-warmer-kleppe-royal-loyal-chapter-2.asta";

	@Test
	public void testSanityCheckCreateElements() throws Exception {

		ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		prjAccessor.create();
		IModel project = prjAccessor.getProject();

		TransactionManager.beginTransaction();

		BasicModelEditor basicModelEditor = ModelEditorFactory.getBasicModelEditor();

		IPackage packageA = basicModelEditor.createPackage(project, "omg");

		IClass classA = basicModelEditor.createClass(packageA, "Company");
		classA.setDefinition("Definition of ClassA");
		basicModelEditor.createAttribute(classA, "attribute0", "int");
		basicModelEditor.createOperation(classA, "operation0", "void");

		IClass classB = basicModelEditor.createClass(packageA, "ClassB");

		basicModelEditor.createAssociation(classA, classB, "association name", "classA end", "classB end");

		TransactionManager.endTransaction();

		OculariumFacade f = new OculariumFacade(project);

		List<IClass> actual = f.getConstrainedClasses();
		prjAccessor.close();
		assertEquals(0, actual.size());
	}

	@Test
	public void testSanityCheckFindClassByName() throws Exception {

		ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		prjAccessor.open(WK_C2_FILE, true, false, true);
		INamedElement[] elements = prjAccessor.findElements(IClass.class, "LoyaltyProgram");
		prjAccessor.close();
		assertEquals(1, elements.length);
		assertEquals("LoyaltyProgram", elements[0].getName());

	}

	@Test
	public void testSanityCheckFindOperationByName() throws Exception {

		ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		prjAccessor.open(WK_C2_FILE, true, false, true);
		INamedElement[] elements = prjAccessor.findElements(IOperation.class, "getServices");
		prjAccessor.close();
		assertEquals(2, elements.length);
		assertEquals("getServices", elements[0].getName());
		assertEquals("getServices", elements[1].getName());
	}

	@Test
	public void testSanityCheckFindOperationByNameAsNamed() throws Exception {

		ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		prjAccessor.open(WK_C2_FILE, true, false, true);
		INamedElement[] elements = prjAccessor.findElements(INamedElement.class, "getServices");
		prjAccessor.close();
		assertEquals(2, elements.length);
		assertEquals("getServices", elements[0].getName());
		assertEquals("getServices", elements[1].getName());
	}

	@Test
	public void testSanityCheckFindClassByNameAsNamed() throws Exception {

		ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		prjAccessor.open(WK_C2_FILE, true, false, true);
		INamedElement[] elements = prjAccessor.findElements(INamedElement.class, "LoyaltyProgram");
		prjAccessor.close();
		assertEquals(1, elements.length);
		assertEquals("LoyaltyProgram", elements[0].getName());
	}

	@Test
	public void testCreateInvariant() throws Exception {

		ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		prjAccessor.create();

		IModel project = prjAccessor.getProject();

		TransactionManager.beginTransaction();

		BasicModelEditor basicModelEditor = ModelEditorFactory.getBasicModelEditor();

		IPackage packageA = basicModelEditor.createPackage(project, "omg");

		IClass classA = basicModelEditor.createClass(packageA, "Company");
		basicModelEditor.createAttribute(classA, "numberOfEmployees", "int");

		TransactionManager.endTransaction();

		Reader r = new StringReader("context omg::Company\ninv enoughEmployees:self.numberOfEmployees > 50\n\n");

		OculariumFacade f = new OculariumFacade(project);
		f.importOCL(r);
		List<IClass> actual = f.getConstrainedClasses();
		prjAccessor.close();
		r.close();
		assertEquals(1, actual.size());
		assertEquals("Company", actual.get(0).getName());
		assertTrue(actual.get(0).getConstraints()[0].toString().startsWith("inv enoughEmployees:"));
	}

	@Test
	public void testCreatePre() throws Exception {

		ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		prjAccessor.create();

		IModel project = prjAccessor.getProject();

		TransactionManager.beginTransaction();

		BasicModelEditor basicModelEditor = ModelEditorFactory.getBasicModelEditor();

		IPackage packageA = basicModelEditor.createPackage(project, "omg");

		IClass classA = basicModelEditor.createClass(packageA, "Company");
		basicModelEditor.createAttribute(classA, "numberOfEmployees", "int");

		TransactionManager.endTransaction();

		Reader r = new StringReader("context omg::Company\npre enoughEmployees:self.numberOfEmployees > 50\n\n");

		OculariumFacade f = new OculariumFacade(project);
		f.importOCL(r);
		List<IClass> actual = f.getConstrainedClasses();
		prjAccessor.close();
		r.close();
		assertEquals(1, actual.size());
		assertEquals("Company", actual.get(0).getName());
		assertTrue(actual.get(0).getConstraints()[0].toString().startsWith("PRECONDITION:"));
	}

	@Test
	public void testWkisEmpty() throws Exception {
		ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		prjAccessor.open(WK_C2_EMPTY_FILE, true, false, true);
		IModel project = prjAccessor.getProject();
		OculariumFacade f = new OculariumFacade(project);
		List<IClass> actual = f.getConstrainedClasses();
		prjAccessor.close();

		assertEquals(0, actual.size());
	}

	/**
	 * getServices is a polymorphic operation and its name resolves to two different operations.
	 * Proper implementation must check operation signature in order to select the correct operation.
	 * In the case of more than one signature, parameter type must be checked.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testWkHasPolimorphicElement() throws Exception {
		ProjectAccessor prjAccessor = null;
		Reader r = null;
		try {
			prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
			prjAccessor.open(WK_C2_EMPTY_FILE, true, false, true);
			IModel project = prjAccessor.getProject();
			OculariumFacade f = new OculariumFacade(project);
			r = new StringReader(
					"context ocularium::examples::royalloyal::LoyaltyProgram::getServices(): Set"
					+ "\n body : partners.deliveredServices->asSet()");
			f.importOCL(r);
			List<IClass> actual = f.getConstrainedClasses();
			assertEquals(1, actual.size());
		} catch (Exception e) {
			throw e;
		} finally {
			if (prjAccessor != null)
				prjAccessor.close();
			try {
				if (r != null)
					r.close();
			} catch (Exception e) {
				throw e;
			}
		}
	}

}
