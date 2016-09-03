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
package ocularium.apps;

import java.io.FileWriter;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

import ocularium.internal.OculariumFacade;

/**
 * Simple exploratory test application to check out export results from W&K R&L, Second Edition, Chapter 2.
 * 
 * @author marco.mangan@gmail.com
 *
 */
public class ExportWKRLC2App {
	private static final String WK_C2_FILE = "../specs/royalloyal/mangan-warmer-kleppe-royal-loyal-chapter-2.asta";

	public static void main(String[] args) throws Exception {
		

		System.out.println("Opening project...");

		ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		prjAccessor.open(WK_C2_FILE, true, false, true);
		
		System.out.println("Creating file...");
		FileWriter fw = new FileWriter(OculariumFacade.getOclProjectPath(prjAccessor));
        System.out.println(prjAccessor.getProjectPath());

		IModel project = prjAccessor.getProject();
		System.out.println("Running facade...");

		OculariumFacade f = new OculariumFacade(project);
		System.out.println("Exporting constraints...");
		
		f.exportOCL(fw);
		prjAccessor.close();
		fw.close();
		System.out.println("Done.");


	}



}
