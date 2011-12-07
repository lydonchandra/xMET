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
package xmet.tools.metadata.editor.views.scv.view;

import xmet.tools.metadata.editor.views.scv.impl.ChoiceItem;
import xmet.tools.metadata.editor.views.scv.impl.Choices;
import xmet.tools.metadata.editor.views.scv.impl.CompositeItem;
import xmet.tools.metadata.editor.views.scv.impl.Group;
import xmet.tools.metadata.editor.views.scv.impl.GroupSubitem;
import xmet.tools.metadata.editor.views.scv.impl.Item;
import xmet.tools.metadata.editor.views.scv.impl.LabeledGroup;
import xmet.tools.metadata.editor.views.scv.impl.Page;
import xmet.tools.metadata.editor.views.scv.impl.PageSubitem;
import xmet.tools.metadata.editor.views.scv.impl.ParentItem;
import xmet.tools.metadata.editor.views.scv.impl.RepeatedGroup;
import xmet.tools.metadata.editor.views.scv.impl.RepeatedPage;
import xmet.tools.metadata.editor.views.scv.impl.Sheet;
import xmet.tools.metadata.editor.views.scv.utils.CodeExecutorUtil;
import xmet.tools.metadata.editor.views.scv.utils.DefaultModelVisitor;
import xmet.tools.metadata.editor.views.scv.utils.SCVUtils;

/**
 * Traverses the model tree and updates controls and page validation stuff.
 * @author Nahid Akbar
 */
public class ValidationUpdateUtil
    extends DefaultModelVisitor {

    /**
     * Instantiates a new validation update util.
     * @param parent the parent
     */
    public ValidationUpdateUtil(
        final ParentItem parent) {
        SCVUtils.accept(
            parent,
            this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitPage(
        final Page page) {
        /* for page...check that all the children are valid */
        if (page.getDC() != null) {
            if (page.getOnValidation() != null) {
                CodeExecutorUtil.executeCode(
                    page.getIc().getSheet(),
                    page,
                    page.getOnValidation());
            }
            boolean valid = true;
            String errorMessage = null;
            for (final PageSubitem iogp : page.getItems()) {
                if (!(iogp instanceof Page)
                    && iogp.getDC() != null) {
                    valid = valid
                        && iogp.getDC().isValid();
                    if (!valid) {
                        errorMessage = iogp.getDC().getValidationErrorMessage();
                        break;
                    }
                }
            }
            page.getDC().setValid(
                valid);
            page.getDC().setValidationErrorMessage(
                errorMessage);
        }
        super.postVisitPage(page);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitChoices(
        final Choices choices) {
        if (choices.getDC() != null) {
            final ChoiceItem selectedItem = choices.getItems().get(
                choices.getIc().getSelected());
            if (selectedItem != null
                && selectedItem.getItem() != null) {
                if ((selectedItem.getItem().getDC() != null)
                    && (choices.getDC() != null)) {
                    choices.getDC().setValid(
                        selectedItem.getItem().getDC().isValid());
                    choices.getDC().setValidationErrorMessage(
                        selectedItem
                            .getItem()
                            .getDC()
                            .getValidationErrorMessage());
                } else {
                    if (selectedItem.getItem().getClass() == Group.class) {
                        choices.getDC().setValid(
                            true);
                        choices.getDC().setValidationErrorMessage(
                            null);
                        final Group carChoiceItem =
                            (Group) selectedItem.getItem();
                        for (final GroupSubitem item : carChoiceItem
                            .getChildren()) {
                            if ((item.getDC() != null)
                                && !item.getDC().isValid()) {
                                choices.getDC().setValid(
                                    item.getDC().isValid());
                                choices.getDC().setValidationErrorMessage(
                                    item.getDC().getValidationErrorMessage());
                                break;
                            }
                        }
                    } else {
                        final GroupSubitem item = selectedItem.getItem();
                        choices.getDC().setValid(
                            true);
                        choices.getDC().setValidationErrorMessage(
                            null);
                        if ((item.getDC() != null)
                            && !item.getDC().isValid()) {
                            choices.getDC().setValid(
                                item.getDC().isValid());
                            choices.getDC().setValidationErrorMessage(
                                item.getDC().getValidationErrorMessage());
                        }
                    }
                }
            }
        }
        super.postVisitChoices(choices);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitRepeatedGroup(
        final RepeatedGroup repeated) {
        if ((repeated.getRIC() != null)
            && (repeated.getDC() != null)) {
            repeated.getDC().setValid(
                true);
            repeated.getDC().setValidationErrorMessage(
                null);
            if (repeated.getRIC().isModelPresent()) {
                for (final Group group : repeated.getRIC().getRepeatedItems()) {
                    if (repeated.getDC().isValid()) {
                        for (final GroupSubitem item : group.getChildren()) {
                            if ((item.getDC() != null)
                                && !item.getDC().isValid()) {
                                repeated.getDC().setValid(
                                    item.getDC().isValid());
                                repeated.getDC().setValidationErrorMessage(
                                    item.getDC().getValidationErrorMessage());
                                break;
                            }
                        }
                    }
                }
            }
        }
        super.postVisitRepeatedGroup(repeated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitRepeatedPageIndex(
        final RepeatedPage repeated,
        final int index) {
        if ((repeated.getRIC() != null)
            && (repeated.getDC() != null)) {
            if (repeated.getRIC().isModelPresent()) {
                final DisplayContext pageDC =
                    repeated.getRIC().getRepeatedItems().get(
                        index).getDC();
                if (pageDC != null) {
                    pageDC.setValid(true);
                    repeated.getDC().setValidationErrorMessage(
                        null);
                    for (final PageSubitem j : getRepeatedPageIndexItems(
                        repeated,
                        index).getItems()) {
                        if ((j.getDC() != null)
                            && !(j instanceof Page)
                            && !j.getDC().isValid()) {
                            pageDC.setValid(j.getDC().isValid());
                            pageDC.setValidationErrorMessage(j
                                .getDC()
                                .getValidationErrorMessage());
                            break;
                        }
                    }
                }
            }
        }
        super.postVisitRepeatedPageIndex(
            repeated,
            index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitRepeatedPage(
        final RepeatedPage repeated) {
        if ((repeated.getRIC() != null)
            && (repeated.getRDC() != null)) {
            /* if (repeated.getRIC().isModelPresent()) { */
            repeated.getDC().setValid(
                true);
            repeated.getDC().setValidationErrorMessage(
                null);
            if (repeated.getRIC().isModelPresent()) {
                for (final Page page : repeated.getRIC().getRepeatedItems()) {
                    if ((page != null)
                        && (page.getDC() != null)
                        && !page.getDC().isValid()) {
                        repeated.getDC().setValid(
                            page.getDC().isValid());
                        repeated.getDC().setValidationErrorMessage(
                            page.getDC().getValidationErrorMessage());
                        break;
                    }
                }
            } else {
                for (final Page page : repeated.getRIC().getRepeatedItems()) {
                    if ((page != null)
                        && (page.getDC() != null)
                        && !page.getDC().isValid()) {
                        page.getDc().setValid(
                            true);
                        page.getDc().setValidationErrorMessage(
                            null);
                        break;
                    }
                }
            }
            // }
        }
        super.postVisitRepeatedPage(repeated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitLabeledGroup(
        final LabeledGroup labeledGroup) {
        if (labeledGroup.getDC() != null) {
            labeledGroup.getDC().setValid(
                true);
            labeledGroup.getDC().setValidationErrorMessage(
                null);
            for (final GroupSubitem item : labeledGroup.getItems()) {
                if ((item.getDC() != null)
                    && !item.getDC().isValid()) {
                    labeledGroup.getDC().setValid(
                        item.getDC().isValid());
                    labeledGroup.getDC().setValidationErrorMessage(
                        item.getDC().getValidationErrorMessage());
                    break;
                }
            }
        }
        super.postVisitLabeledGroup(labeledGroup);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitItem(
        final Item item) {
        String errorMessage = null;
        if (item.getDc() != null) {
            if (item.getValidation() != null) {
                errorMessage = item.getValidation().validateValue(
                    item.getDc().getControl().getValue());
            }
            item.getDc().setValid(
                errorMessage == null);
            item.getDc().setValidationErrorMessage(
                errorMessage);
            item.getDc().updatePanel();
        }
        super.postVisitItem(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitCompositeItem(
        final CompositeItem item) {
        String errorMessage = null;
        if (item.getDc() != null) {
            if (item.getValidation() != null) {
                errorMessage = item.getValidation().validateValue(
                    item.getDc().getControl().getValue());
            }
            item.getDc().setValid(
                errorMessage == null);
            item.getDc().setValidationErrorMessage(
                errorMessage);
            item.getDc().updatePanel();
        }
        super.postVisitCompositeItem(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitGroup(
        final Group group) {
        if (group.getDC() != null) {
            group.getDC().setValid(
                true);
            group.getDC().setValidationErrorMessage(
                null);
            for (final GroupSubitem item : group.getItems()) {
                if (item.getDC() != null
                    && !item.getDC().isValid()) {
                    group.getDC().setValid(
                        false);
                    group.getDC().setValidationErrorMessage(
                        item.getDC().getValidationErrorMessage());
                    break;
                }
            }
        }
        super.postVisitGroup(group);
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitSheet(
        final Sheet sheet) {
        super.preVisitSheet(sheet);
    }
}
