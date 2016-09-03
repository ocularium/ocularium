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
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

import ocularium.internal.OculariumFacade;

/**
 * Sanity checking and automated testing for exporting opaque constraints.
 * 
 * @author marco.mangan@gmail.com
 *
 */
public class ImportActionTest {

	@Test
	public void testCreateElements() throws Exception {

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

		// // Abort transaction
		// TransactionManager.abortTransaction();

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

		// // Abort transaction
		// TransactionManager.abortTransaction();

	}

}
