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
package xmet.tools.metadata.editor.views.scv.designer;

import java.util.ArrayList;

import xmet.tools.metadata.editor.views.scv.model.ChoiceItem;
import xmet.tools.metadata.editor.views.scv.model.Choices;
import xmet.tools.metadata.editor.views.scv.model.CompositeItem;
import xmet.tools.metadata.editor.views.scv.model.DefaultModelPathVisitor;
import xmet.tools.metadata.editor.views.scv.model.Item;
import xmet.tools.metadata.editor.views.scv.model.RepeatedGroup;
import xmet.tools.metadata.editor.views.scv.model.RepeatedPage;
import xmet.tools.metadata.editor.views.scv.model.Sheet;

/**
 * Util extracts xpaths from the model - made for finding xpath conflicts and
 * whatnot.
 * @author Nahid Akbar
 */
public class XPathExtractionUtil
    extends DefaultModelPathVisitor {

    /** The xpaths. */
    private final ArrayList<String> xpaths = new ArrayList<String>();

    /**
     * Instantiates a new x path extraction util.
     * @param sheet the sheet
     */
    public XPathExtractionUtil(
        final Sheet sheet) {
        sheet.accept(this);
    }

    /**
     * Gets the xpaths.
     * @return the xpaths
     */
    public ArrayList<String> getXpaths() {
        return xpaths;
    }

    /**
     * Adds the path.
     * @param base the base
     * @return the string
     */
    private String addPath(
        final String base) {
        String varBase = base;
        if ((varBase != null)
            && (varBase.trim().length() > 0)
            && peekLastXpath() != null) {
            if (varBase.indexOf("$") != -1) {
                varBase = varBase.replaceAll(
                    "\\$",
                    peekLastXpath());
            }
            xpaths.add(varBase);
        }
        return varBase;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitItem(
        final Item item) {
        addPath(item.getXpath());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitRepeatedGroup(
        final RepeatedGroup repeated) {
        super.preVisitRepeatedGroup(repeated);
        addPath(repeated.getBase());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitRepeatedPage(
        final RepeatedPage repeated) {
        super.preVisitRepeatedPage(repeated);
        addPath(repeated.getBase());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitCompositeItem(
        final CompositeItem item) {
        addPath(item.getBase());
        pushLastXpath(item.getBase());
        addPath(item.getRelative());
        popLastXpath();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitChoices(
        final Choices choices) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitChoiceItem(
        final ChoiceItem aChoice,
        final Choices aChoices,
        final int aIndex) {
        addPath(aChoice.getTestXpath());
    }

}
