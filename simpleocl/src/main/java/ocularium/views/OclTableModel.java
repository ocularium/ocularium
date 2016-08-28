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
package ocularium.views;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.change_vision.jude.api.inf.model.IConstraint;

import ocularium.ConstraintFormatter;

/**
 * Table model for constraints.
 * 
 * @author marco.mangan@gmail.com
 *
 */
public class OclTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7278451997120759726L;

	/**
	 * 
	 */
	private static final List<IConstraint> EMPTY_DATA = new ArrayList<IConstraint>();

	/*
	 * 
	 */
	List<IConstraint> data = EMPTY_DATA;

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "Context";
		case 1:
			return "Specification";
		}
		return "@@";
	}
	
	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		IConstraint c = data.get(rowIndex);
		ConstraintFormatter cf = ConstraintFormatter.getConstraintFormatter();
		cf.setConstraint(c);
		switch (columnIndex) {
		case 0:
			return cf.getContext();
		case 1:
			return cf.getSpecification();
		}
		return "##";
	}

	/**
	 * 
	 * @param cs
	 */
	public void setData(List<IConstraint> cs) {
		data = cs;
		fireTableStructureChanged();
	}

	/**
	 * 
	 */
	public void resetData() {
		data = EMPTY_DATA;
	}
}