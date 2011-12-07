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

import xmet.tools.metadata.editor.views.scv.impl.AlertUserCode;
import xmet.tools.metadata.editor.views.scv.impl.ChoiceItem;
import xmet.tools.metadata.editor.views.scv.impl.Choices;
import xmet.tools.metadata.editor.views.scv.impl.Code;
import xmet.tools.metadata.editor.views.scv.impl.CodeBlock;
import xmet.tools.metadata.editor.views.scv.impl.CodeParent;
import xmet.tools.metadata.editor.views.scv.impl.CompositeItem;
import xmet.tools.metadata.editor.views.scv.impl.ExecItemCodeCode;
import xmet.tools.metadata.editor.views.scv.impl.GetPathValueCode;
import xmet.tools.metadata.editor.views.scv.impl.Group;
import xmet.tools.metadata.editor.views.scv.impl.IfCode;
import xmet.tools.metadata.editor.views.scv.impl.Item;
import xmet.tools.metadata.editor.views.scv.impl.LabeledGroup;
import xmet.tools.metadata.editor.views.scv.impl.Page;
import xmet.tools.metadata.editor.views.scv.impl.RepeatCode;
import xmet.tools.metadata.editor.views.scv.impl.RepeatedGroup;
import xmet.tools.metadata.editor.views.scv.impl.RepeatedItem;
import xmet.tools.metadata.editor.views.scv.impl.RepeatedPage;
import xmet.tools.metadata.editor.views.scv.impl.SetItemMandatoryCode;
import xmet.tools.metadata.editor.views.scv.impl.SetItemValidCode;
import xmet.tools.metadata.editor.views.scv.impl.SetItemValueCode;
import xmet.tools.metadata.editor.views.scv.impl.SetItemVisibleCode;
import xmet.tools.metadata.editor.views.scv.impl.SetPathValueCode;
import xmet.tools.metadata.editor.views.scv.impl.Sheet;

/**
 * Default Implementation of Model Visitor all other classes are to extend from.
 * This should automatically takes care of xpath issues to do with repeated
 * entities. If not sure, always be sure to include a super.visit...(...) call
 * in extending visitor implementations. Note that preVisit... method should be
 * called before your extending code and postVisit after. See DataLoaderUtil for
 * an example.
 * @author Nahid Akbar
 */
public class DefaultModelVisitor
    implements
    SCVModelVisitor {

    /** The last path stack. */
    private final Stack<String> lastPathStack = new Stack<String>();

    /** The pushed path. */
    private final Stack<Boolean> pushedPath = new Stack<Boolean>();

    /**
     * The last path. Note: this is to be maintained as the last path. I.e. push
     * it into stack before this is set to something else and pop from stack
     * once it exits the context of the last lastPath. See below for examples
     */
    private String lastPath = null;

    /**
     * Gets the last path.
     * @return the last path
     */
    protected String getLastPath() {
        return lastPath;
    }

    /**
     * Instantiates a new default model visitor.
     */
    public DefaultModelVisitor() {
        super();
    }

    /**
     * Instantiates a new default model visitor.
     * @param aLastPath the last path
     */
    public DefaultModelVisitor(
        final String aLastPath) {
        super();
        this.setLastPath(aLastPath);
    }

    /* == Sheet Implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitSheet(
        final Sheet sheet) {
        if (getLastPath() != null
            && sheet.getIc() != null) {
            sheet.getIc().setNodeXpath(
                getLastPath());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitSheet(
        final Sheet sheet) {

    }

    /* == Page Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitPage(
        final Page page) {
        if (getLastPath() != null
            && page.getIc() != null) {
            page.getIc().setNodeXpath(
                getLastPath());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitPage(
        final Page page) {

    }

    /* == Items implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitItem(
        final Item item) {
        if (item.getIc() != null) {
            item.getIc().hardTraceSubstitutableXpath(
                getLastPath(),
                item.getXpath());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitItem(
        final Item item) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitCompositeItem(
        final CompositeItem item) {
        if (item.getIc() != null) {
            item.getIc().hardTraceSubstitutableXpath(
                getLastPath(),
                item.getBase());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitCompositeItem(
        final CompositeItem item) {

    }

    /* == Group Implementaton == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitGroup(
        final Group group) {
        if (getLastPath() != null
            && group.getIc() != null) {
            group.getIc().setNodeXpath(
                getLastPath());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitGroup(
        final Group group) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitLabeledGroup(
        final LabeledGroup labeledGroup) {
        if (getLastPath() != null
            && labeledGroup.getIc() != null) {
            labeledGroup.getIc().setNodeXpath(
                getLastPath());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitLabeledGroup(
        final LabeledGroup labeledGroup) {

    }

    /* == Repeated implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public int preVisitRepeatedGroup(
        final RepeatedGroup repeated) {
        return preVisitRepeatedItem(repeated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitRepeatedGroupIndex(
        final RepeatedGroup repeated,
        final int index) {
        preVisitRepeatedItemIndex(
            repeated,
            index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitRepeatedGroupIndex(
        final RepeatedGroup repeated,
        final int index) {
        postVisitRepeatedItemIndex(
            repeated,
            index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Group getRepeatedGroupIndexItems(
        final RepeatedGroup group,
        final int index) {
        return getRepeatedItemIndexItem(
            group,
            index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitRepeatedGroup(
        final RepeatedGroup repeated) {
        postVisitRepeatedItem(repeated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int preVisitRepeatCode(
        final CodeParent item,
        final RepeatCode repeated) {
        return preVisitRepeatedItem(repeated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitRepeatCodeIndex(
        final CodeParent item,
        final RepeatCode repeated,
        final int index) {
        preVisitRepeatedItemIndex(
            repeated,
            index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitRepeatCodeIndex(
        final CodeParent item,
        final RepeatCode repeated,
        final int index) {
        postVisitRepeatedItemIndex(
            repeated,
            index);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Code getRepeatedCodeIndexItem(
        final CodeParent codeItem,
        final RepeatCode src,
        final int index) {
        return getRepeatedItemIndexItem(
            src,
            index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitRepeatCode(
        final CodeParent item,
        final RepeatCode repeated) {
        postVisitRepeatedItem(repeated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int preVisitRepeatedPage(
        final RepeatedPage repeated) {
        return preVisitRepeatedItem(repeated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitRepeatedPageIndex(
        final RepeatedPage repeated,
        final int index) {
        preVisitRepeatedItemIndex(
            repeated,
            index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page getRepeatedPageIndexItems(
        final RepeatedPage repeated,
        final int index) {
        return getRepeatedItemIndexItem(
            repeated,
            index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitRepeatedPageIndex(
        final RepeatedPage repeated,
        final int index) {
        postVisitRepeatedItemIndex(
            repeated,
            index);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitRepeatedPage(
        final RepeatedPage repeated) {
        postVisitRepeatedItem(repeated);
    }

    /**
     * Pre visit repeated item.
     * @param <E> the element type
     * @param repeated the repeated
     * @return the int
     */
    public <E> int preVisitRepeatedItem(
        final RepeatedItem<E> repeated) {
        pushedPath.push(repeated.getRIC() != null);
        if (pushedPath.peek()) {
            lastPathStack.push(repeated.getRIC().hardTraceSubstitutableXpath(
                getLastPath(),
                repeated.getBase()));
            return repeated.getRIC().getRepeatedItemsCount();
        }
        return 0;
    }

    /**
     * Pre visit repeated item index.
     * @param <E> the element type
     * @param repeated the repeated
     * @param index the index
     */
    public <E> void preVisitRepeatedItemIndex(
        final RepeatedItem<E> repeated,
        final int index) {
        pushedPath.push(repeated.getRIC() != null);
        if (pushedPath.peek()) {
            lastPathStack.push(getLastPath());
            setLastPath(repeated.getRIC().getIndexPath(
                index));
        }

    }

    /**
     * Post visit repeated item index.
     * @param <E> the element type
     * @param repeated the repeated
     * @param index the index
     */
    public <E> void postVisitRepeatedItemIndex(
        final RepeatedItem<E> repeated,
        final int index) {
        if (pushedPath.pop()) {
            setLastPath(lastPathStack.pop());
        }
    }

    /**
     * Gets the repeated item index item.
     * @param <E> the element type
     * @param src the src
     * @param i the i
     * @return the repeated item index item
     */
    public <E> E getRepeatedItemIndexItem(
        final RepeatedItem<E> src,
        final int i) {
        if ((src.getRIC() != null)
            && ((src.getRIC().getRepeatedItems() != null) && (src
                .getRIC()
                .getRepeatedItems()
                .size() > i))) {
            return src.getRIC().getRepeatedItems().get(
                i);
        }
        return src.getItem();
    }

    /**
     * Post visit repeated item.
     * @param <E> the element type
     * @param repeated the repeated
     */
    public <E> void postVisitRepeatedItem(
        final RepeatedItem<E> repeated) {
        if (pushedPath.pop()) {
            lastPathStack.pop();
        }
    }

    /* == Choices Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public int preVisitChoices(
        final Choices choices) {
        if (getLastPath() != null
            && choices.getIc() != null) {
            choices.getIc().setNodeXpath(
                getLastPath());
        }
        if ((choices.getItems() != null)
            && (choices.getItems().size() > 0)) {
            for (final ChoiceItem ch : choices.getItems()) {
                if (ch.getIc() != null) {
                    ch.getIc().softRetraceXpath(
                        getLastPath(),
                        ch.getTestXpath());
                }
            }
        }
        if (choices.getIc() != null) {
            return choices.getIc().updateSelected();
        }
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitSelectedChoiceItem(
        final ChoiceItem choice,
        final Choices choices,
        final int index) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitSelectedChoiceItem(
        final ChoiceItem choice,
        final Choices choices,
        final int index) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitChoices(
        final Choices choices) {

    }

    /* == Code Implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean preVisitIfCode(
        final CodeParent item,
        final IfCode ifcode) {
        if (ifcode.getIc() != null) {
            ifcode.getIc().softTraceSubstitutableXpath(
                getLastPath(),
                ifcode.getBase());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitIfCode(
        final CodeParent item,
        final IfCode ifcode) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitSetPathValueCode(
        final CodeParent item,
        final SetPathValueCode setPathValue) {
        if (setPathValue.getIc() != null) {
            setPathValue.getIc().hardTraceSubstitutableXpath(
                getLastPath(),
                setPathValue.getDest());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitGetPathValueCode(
        final CodeParent item,
        final GetPathValueCode getPathValue) {
        if (getPathValue.getIc() != null) {
            getPathValue.getIc().hardTraceSubstitutableXpath(
                getLastPath(),
                getPathValue.getSrc());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitCodeBlock(
        final CodeParent item,
        final CodeBlock codeBlock) {
        if (getLastPath() != null
            && codeBlock.getIc() != null) {
            codeBlock.getIc().setNodeXpath(
                getLastPath());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitCodeBlock(
        final CodeParent item,
        final CodeBlock codeBlock) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitSetItemVisibleCode(
        final CodeParent item,
        final SetItemVisibleCode makeVisible) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitExecItemCodeCode(
        final CodeParent item,
        final ExecItemCodeCode execItemCode) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitSetItemMandatoryCode(
        final CodeParent item,
        final SetItemMandatoryCode makeMamdatory) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitSetItemValueCode(
        final CodeParent item,
        final SetItemValueCode setItemValue) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitSetItemValidCode(
        final CodeParent item,
        final SetItemValidCode setValid) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitAlertUserCode(
        final CodeParent item,
        final AlertUserCode alertUser) {

    }

    /**
     * Sets the last path.
     * @param aLastPath the new last path
     */
    public void setLastPath(
        final String aLastPath) {
        this.lastPath = aLastPath;
    }

}
