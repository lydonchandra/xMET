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
package xmet.tools.metadata.editor.views.scv.utils;

import java.util.Stack;

import xmet.tools.metadata.editor.views.scv.impl.ChoiceItem;
import xmet.tools.metadata.editor.views.scv.impl.Choices;
import xmet.tools.metadata.editor.views.scv.impl.CodeParent;
import xmet.tools.metadata.editor.views.scv.impl.RepeatCode;
import xmet.tools.metadata.editor.views.scv.impl.RepeatedGroup;
import xmet.tools.metadata.editor.views.scv.impl.RepeatedPage;

/**
 * Utility for fixing visiting the uninitialized skeleton model.
 * @author Nahid Akbar
 */
public class DefaultModelSkeletonVisitor
    extends DefaultModelVisitor {

    /** The paths stack. */
    private final Stack<String> pathsStack = new Stack<String>();

    /** The paths stack pushed. */
    private final Stack<Boolean> pathsStackPushed = new Stack<Boolean>();

    /**
     * Instantiates a new default model skeleton visitor.
     */
    public DefaultModelSkeletonVisitor() {
        super();
    }

    /**
     * Instantiates a new default model skeleton visitor.
     * @param lastPath the last path
     */
    public DefaultModelSkeletonVisitor(
        final String lastPath) {
        super(lastPath);
    }

    /**
     * Push last xpath.
     * @param base the base
     * @return the string
     */
    protected String pushLastXpath(
        final String base) {
        String varBase = base;
        if ((varBase != null)
            && (varBase.trim().length() > 0)) {
            if (varBase.indexOf("$") != -1) {
                varBase = varBase.replaceAll(
                    "\\$",
                    pathsStack.peek());
            }
            pathsStack.push(varBase);
            pathsStackPushed.push(true);
        } else {
            pathsStackPushed.push(false);
        }
        return varBase;
    }

    /**
     * Pop last xpath.
     * @return the string
     */
    protected String popLastXpath() {
        if (pathsStackPushed.pop()) {
            return pathsStack.pop();
        } else {
            return null;
        }
    }

    /**
     * Peek last xpath.
     * @return the string
     */
    protected String peekLastXpath() {
        if (!pathsStack.empty()) {
            return pathsStack.peek();
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int preVisitRepeatedPage(
        final RepeatedPage repeated) {
        super.preVisitRepeatedPage(repeated);
        pushLastXpath(repeated.getBase());
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitRepeatedPage(
        final RepeatedPage repeated) {
        popLastXpath();
        super.postVisitRepeatedPage(repeated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int preVisitRepeatedGroup(
        final RepeatedGroup repeated) {
        super.preVisitRepeatedGroup(repeated);
        pushLastXpath(repeated.getBase());
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitRepeatedGroup(
        final RepeatedGroup repeated) {
        popLastXpath();
        super.postVisitRepeatedGroup(repeated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int preVisitRepeatCode(
        final CodeParent item,
        final RepeatCode repeated) {
        super.preVisitRepeatCode(
            item,
            repeated);
        pushLastXpath(repeated.getBase());
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitRepeatCode(
        final CodeParent item,
        final RepeatCode repeated) {
        popLastXpath();
        super.postVisitRepeatCode(
            item,
            repeated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int preVisitChoices(
        final Choices choices) {
        if ((choices.getItems() != null)
            && (choices.getItems().size() > 0)) {
            for (final ChoiceItem ch : choices.getItems()) {
                SCVUtils.accept(
                    ch.getItem(),
                    this);
            }
        }
        return -1;
    }
}
