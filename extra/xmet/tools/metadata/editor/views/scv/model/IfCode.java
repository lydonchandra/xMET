/******************************************************************************
 * xMET - eXtensible Metadata Editing Tool<br>
 * <br>
 * Copyright (C) 2010-2011 - Office Of Spatial Data Management<br>
 * <br>
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.<br>
 * <br>
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.<br>
 * <br>
 * For a copy of the GNU General Public License, see http://www.gnu.org/licenses
 ******************************************************************************/
package xmet.tools.metadata.editor.views.scv.model;

import n.io.CS;
import n.io.CSC;

/**
 * Code for conditional stuff.
 * @author Nahid Akbar
 */
/**
 * @author Nahid Akbar
 */
@CSC("if")
public class IfCode
    extends Code {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /* == Properties == */
    /** base xpath. use "." to refer to the current item */
    @CS
    private String base = "#";

    /** The condition expression. */
    @CS
    private Condition condition = Condition.None;

    /** value to compare condition with. */
    @CS
    private String expression;

    /** code to execute if condition is satisfied. */
    @CSC
    private Code code;

    /** The else code. */
    @CSC
    private Code elseCode;

    /**
     * Gets the serialversionuid.
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /**
     * Gets the base xpath.
     * @return the base xpath
     */
    public String getBase() {
        return base;
    }

    /**
     * Gets the condition expression.
     * @return the condition expression
     */
    public Condition getCondition() {
        return condition;
    }

    /**
     * Gets the value to compare condition with.
     * @return the value to compare condition with
     */
    public String getExpression() {
        return expression;
    }

    /**
     * Gets the code to execute if condition is satisfied.
     * @return the code to execute if condition is satisfied
     */
    public Code getCode() {
        return code;
    }

    /**
     * Gets the else code.
     * @return the else code
     */
    public Code getElseCode() {
        return elseCode;
    }

    /**
     * Sets the base xpath.
     * @param aBase the new base xpath
     */
    public void setBase(
        final String aBase) {
        base = aBase;
    }

    /**
     * Sets the condition expression.
     * @param aCondition the new condition expression
     */
    public void setCondition(
        final Condition aCondition) {
        condition = aCondition;
    }

    /**
     * Sets the value to compare condition with.
     * @param aExpression the new value to compare condition with
     */
    public void setExpression(
        final String aExpression) {
        expression = aExpression;
    }

    /**
     * Sets the code to execute if condition is satisfied.
     * @param aCode the new code to execute if condition is satisfied
     */
    public void setCode(
        final Code aCode) {
        code = aCode;
    }

    /**
     * Sets the else code.
     * @param aElseCode the new else code
     */
    public void setElseCode(
        final Code aElseCode) {
        elseCode = aElseCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(
        final ModelVisitor visitor) {
        visitor.preVisitIfCode(this);

        if (this.code != null) {
            visitor.preVisitIfThenCode(code);
            code.accept(visitor);
            visitor.postVisitIfThenCode(code);
        } else if (this.elseCode != null) {
            visitor.preVisitIfElseCode(elseCode);
            elseCode.accept(visitor);
            visitor.postVisitIfElseCode(elseCode);
        }
        visitor.postVisitIfCode(this);
    }

}
