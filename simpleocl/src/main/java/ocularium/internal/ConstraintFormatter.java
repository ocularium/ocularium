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
package ocularium.internal;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IConstraint;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IOperation;
import com.change_vision.jude.api.inf.model.IParameter;

/**
 * A constraint formatter.
 * 
 * This formatter is similar to NumberFormat and DateFormat from Java API.
 * 
 * @author marco.mangan@gmail.com
 *
 */
public class ConstraintFormatter {

	/**
	 * 
	 */
	private static final Logger LOGGER = Logger.getLogger(ConstraintFormatter.class.getName());

	/**
	 * 
	 */
	private static final ConstraintFormatter DEFAULT_FORMATTER = new ConstraintFormatter();

	/**
	 * 
	 * @return
	 */
	public static ConstraintFormatter getConstraintFormatter() {
		return DEFAULT_FORMATTER;
	}

	/**
	 * 
	 */
	private IConstraint iConstraint;

	@Override
	public String toString() {
		assert iConstraint != null;
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("context ");
			sb.append(getContext());
			sb.append("\n");
			sb.append(getSpecification());
			sb.append("\n");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Exception occur", e);
		}
		assert iConstraint != null;
		return sb.toString();

	}

	/**
	 * 
	 * @param iConstraint
	 */
	public void setConstraint(IConstraint iConstraint) {
		assert iConstraint != null;
		this.iConstraint = iConstraint;
		assert this.iConstraint != null;

	}

	/**
	 * 
	 * @param iClass
	 * @return
	 */
	private static String getFullName(IClass iClass) {
		assert iClass != null;

		StringBuilder sb = new StringBuilder();
		IElement owner = iClass.getOwner();
		while (owner != null && owner instanceof INamedElement && owner.getOwner() != null) {
			sb.insert(0, ((INamedElement) owner).getName() + "::");
			owner = owner.getOwner();
		}
		sb.append(iClass.getName());
		return sb.toString();
	}

	/**
	 * 
	 */
	public StringBuilder getContext(IAttribute e) {
		StringBuilder sb = new StringBuilder();

		IElement owner = e.getOwner();
		if (owner instanceof IClass) {
			IClass cl = (IClass) owner;
			sb.append(getFullName(cl));
		} else {
			sb.append(owner.toString());
		}
		sb.append("::");
		sb.append(e.toString());
		return sb;
	}

	/**
	 * 
	 */
	public StringBuilder getContext(IOperation op) {
		StringBuilder sb = new StringBuilder();

		IElement owner = op.getOwner();
		if (owner instanceof IClass) {
			IClass cl = (IClass) owner;
			sb.append(getFullName(cl));
		} else {
			sb.append(owner.toString());
		}
		sb.append("::");
		sb.append(op.toString());
		sb.append("(");
		IParameter[] ps = op.getParameters();

		boolean firstParam = true;

		for (IParameter iParameter : ps) {
			if (!firstParam) {
				sb.append(", ");
			}
			firstParam = false;
			String paramName = iParameter.getName();
			String paramType = iParameter.getType().toString();
			sb.append(paramName == null ? "" : paramName);
			sb.append(paramType == null ? "" : ": " + paramType);

		}
		sb.append(")");

		String returnType = op.getReturnType().toString();

		sb.append(returnType == null ? "" : ": " + returnType);
		return sb;
	}

	/**
	 * 
	 */
	public StringBuilder getContext(IClass cl) {
		StringBuilder sb = new StringBuilder();

		sb.append(getFullName(cl));
		return sb;
	}

	/**
	 * 
	 */
	public StringBuilder getContext(IElement e) {
		StringBuilder sb = new StringBuilder();
		sb.append(e.toString());
		return sb;
	}

	/**
	 * 
	 * @return
	 */
	public String getContext() {
		assert iConstraint != null;

		StringBuilder sb = new StringBuilder();

		IElement[] ces = iConstraint.getConstrainedElement();
		assert ces.length == 1;
		IElement e = ces[0];
		sb.append(getContext(e));

		return sb.toString();
	}

	/**
	 * 
	 * @return
	 */
	public String getSpecification() {
		assert iConstraint != null;

		StringBuilder sb = new StringBuilder();
		String spec = iConstraint.getSpecification();
		if (spec.startsWith("BODYCONDITION:")) {
			spec = spec.substring("BODYCONDITION:".length());
		} else if (spec.startsWith("POSTCONDITION:")) {
			spec = spec.substring("POSTCONDITION:".length());
		} else if (spec.startsWith("PRECONDITION:")) {
			spec = spec.substring("PRECONDITION:".length());
		}

		sb.append(spec);
		return sb.toString();
	}

	/**
	 * 
	 * @return
	 */
	public boolean isOcl() {
		assert iConstraint != null;

		String[] starts = { "inv", "def", "init", "derive", "pre", "post", "body" };
		String spec = getSpecification().trim().toLowerCase();
		for (String s : starts) {
			if (spec.startsWith(s))
				return true;
		}
		return false;
	}

}
