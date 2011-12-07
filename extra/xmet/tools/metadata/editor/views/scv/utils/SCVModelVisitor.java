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
import xmet.tools.metadata.editor.views.scv.impl.RepeatedPage;
import xmet.tools.metadata.editor.views.scv.impl.SetItemMandatoryCode;
import xmet.tools.metadata.editor.views.scv.impl.SetItemValidCode;
import xmet.tools.metadata.editor.views.scv.impl.SetItemValueCode;
import xmet.tools.metadata.editor.views.scv.impl.SetItemVisibleCode;
import xmet.tools.metadata.editor.views.scv.impl.SetPathValueCode;
import xmet.tools.metadata.editor.views.scv.impl.Sheet;

/**
 * The visitor interface for the SCV model Do not implement it directly - extend
 * DefaultModelVisitor Why? - So the model can be extended without mucking up
 * your model implementation the "post" prefixed methods are called after
 * structure and "pre" prefixed ones before if these prefixes are absent ... it
 * most likely does not have any child structure rule for adding new methods: if
 * it has children, add a postVisit... and preVisit... method - add dummy
 * implementation in DefaultModelVisitor.java rule for converting plain visit...
 * to pre/postVisit leave the visit method as a preVisit... method. in addition,
 * add preVisit... and postVisit... methods as above. annotate the normal method
 * as depreciated
 * @author Nahid Akbar
 */
public interface SCVModelVisitor {

    /**
     * Pre visit sheet.
     * @param sheet the sheet
     */
    void preVisitSheet(
        Sheet sheet);

    /**
     * Post visit sheet.
     * @param sheet the sheet
     */
    void postVisitSheet(
        Sheet sheet);

    /**
     * Pre visit page.
     * @param page the page
     */
    void preVisitPage(
        Page page);

    /**
     * Post visit page.
     * @param page the page
     */
    void postVisitPage(
        Page page);

    /**
     * Pre visit group.
     * @param group the group
     */
    void preVisitGroup(
        Group group);

    /**
     * Post visit group.
     * @param group the group
     */
    void postVisitGroup(
        Group group);

    /**
     * Pre visit repeated group.
     * @param repeated the repeated
     * @return the int
     */
    int preVisitRepeatedGroup(
        RepeatedGroup repeated);

    /**
     * Gets the repeated group index items.
     * @param group the group
     * @param index the index
     * @return the repeated group index items
     */
    Group getRepeatedGroupIndexItems(
        RepeatedGroup group,
        int index);

    /**
     * Pre visit repeated group index.
     * @param repeated the repeated
     * @param index the index
     */
    void preVisitRepeatedGroupIndex(
        RepeatedGroup repeated,
        int index);

    /**
     * Post visit repeated group index.
     * @param repeated the repeated
     * @param index the index
     */
    void postVisitRepeatedGroupIndex(
        RepeatedGroup repeated,
        int index);

    /**
     * Post visit repeated group.
     * @param repeated the repeated
     */
    void postVisitRepeatedGroup(
        RepeatedGroup repeated);

    /**
     * Pre visit labeled group.
     * @param labeledGroup the labeled group
     */
    void preVisitLabeledGroup(
        LabeledGroup labeledGroup);

    /**
     * Post visit labeled group.
     * @param labeledGroup the labeled group
     */
    void postVisitLabeledGroup(
        LabeledGroup labeledGroup);

    /**
     * Pre visit item.
     * @param item the item
     */
    void preVisitItem(
        Item item);

    /**
     * Post visit item.
     * @param item the item
     */
    void postVisitItem(
        Item item);

    /**
     * Pre visit composite item.
     * @param item the item
     */
    void preVisitCompositeItem(
        CompositeItem item);

    /**
     * Post visit composite item.
     * @param item the item
     */
    void postVisitCompositeItem(
        CompositeItem item);

    /**
     * Pre visit code block.
     * @param item the item
     * @param codeBlock the code block
     */
    void preVisitCodeBlock(
        CodeParent item,
        CodeBlock codeBlock);

    /**
     * Post visit code block.
     * @param item the item
     * @param codeBlock the code block
     */
    void postVisitCodeBlock(
        CodeParent item,
        CodeBlock codeBlock);

    /* return true if to explore if block contents...false for else block */
    /* contents */
    /**
     * Pre visit if code.
     * @param item the item
     * @param ifcode the ifcode
     * @return true, if successful
     */
    boolean preVisitIfCode(
        CodeParent item,
        IfCode ifcode);

    /**
     * Post visit if code.
     * @param item the item
     * @param ifcode the ifcode
     */
    void postVisitIfCode(
        CodeParent item,
        IfCode ifcode);

    /**
     * Visit make visible.
     * @param item the item
     * @param makeVisible the make visible
     */
    void visitSetItemVisibleCode(
        CodeParent item,
        SetItemVisibleCode makeVisible);

    /**
     * Visit set item mandatory code.
     * @param item the item
     * @param makeMamdatory the make mamdatory
     */
    void visitSetItemMandatoryCode(
        CodeParent item,
        SetItemMandatoryCode makeMamdatory);

    /**
     * Visit set item valid code.
     * @param item the item
     * @param setValid the set valid
     */
    void visitSetItemValidCode(
        CodeParent item,
        SetItemValidCode setValid);

    /**
     * Visit alert user code.
     * @param item the item
     * @param alertUser the alert user
     */
    void visitAlertUserCode(
        CodeParent item,
        AlertUserCode alertUser);

    /**
     * Visit get path value code.
     * @param item the item
     * @param getPathValue the get path value
     */
    void visitGetPathValueCode(
        CodeParent item,
        GetPathValueCode getPathValue);

    /**
     * Pre visit repeat code.
     * @param item the item
     * @param repeatCode the repeat code
     * @return the int
     */

    /**
     * Pre visit repeat code index.
     * @param item the item
     * @param repeatCode the repeat code
     * @return the int
     */
    int preVisitRepeatCode(
        CodeParent item,
        RepeatCode repeatCode);

    /**
     * Pre visit repeat code index.
     * @param item the item
     * @param repeatCode the repeat code
     * @param index the index
     */
    void preVisitRepeatCodeIndex(
        CodeParent item,
        RepeatCode repeatCode,
        int index);

    /**
     * Gets the repeated code index item.
     * @param codeItem the code item
     * @param src the src
     * @param i the i
     * @return the repeated code index item
     */
    Code getRepeatedCodeIndexItem(
        CodeParent codeItem,
        RepeatCode src,
        int i);

    /**
     * Post visit repeat code index.
     * @param item the item
     * @param repeatCode the repeat code
     * @param index the index
     */
    void postVisitRepeatCodeIndex(
        CodeParent item,
        RepeatCode repeatCode,
        int index);

    /**
     * Post visit repeat code.
     * @param item the item
     * @param repeatCode the repeat code
     */
    void postVisitRepeatCode(
        CodeParent item,
        RepeatCode repeatCode);

    /**
     * Post visit repeat code.
     * @param item the item
     * @param setCode the set code
     */

    /**
     * Visit set code.
     * @param item the item
     * @param setCode the set code
     */
    void visitSetPathValueCode(
        CodeParent item,
        SetPathValueCode setCode);

    /**
     * Visit set item code.
     * @param item the item
     * @param setItemCode the set item code
     */
    void visitSetItemValueCode(
        CodeParent item,
        SetItemValueCode setItemCode);

    /**
     * Visit exec item code code.
     * @param item the item
     * @param execItemCode the exec item code
     */
    void visitExecItemCodeCode(
        CodeParent item,
        ExecItemCodeCode execItemCode);

    /**
     * Pre visit repeated page.
     * @param repeated the repeated
     * @return the int
     */
    int preVisitRepeatedPage(
        RepeatedPage repeated);

    /**
     * Pre visit repeated page index.
     * @param repeated the repeated
     * @param index the index
     */
    void preVisitRepeatedPageIndex(
        RepeatedPage repeated,
        int index);

    /**
     * Gets the repeated page index items.
     * @param repeated the repeated
     * @param index the index
     * @return the repeated page index items
     */
    Page getRepeatedPageIndexItems(
        RepeatedPage repeated,
        int index);

    /**
     * Post visit repeated page index.
     * @param repeated the repeated
     * @param index the index
     */
    void postVisitRepeatedPageIndex(
        RepeatedPage repeated,
        int index);

    /**
     * Post visit repeated page.
     * @param repeated the repeated
     */
    void postVisitRepeatedPage(
        RepeatedPage repeated);

    /**
     * Pre visit choices.
     * @param choices the choices
     * @return the int
     */
    int preVisitChoices(
        Choices choices);

    /**
     * Pre visit selected choice item.
     * @param choice the choice
     * @param choices the choices
     * @param index the index
     */
    void preVisitSelectedChoiceItem(
        ChoiceItem choice,
        Choices choices,
        int index);

    /**
     * Post visit selected choice item.
     * @param choice the choice
     * @param choices the choices
     * @param index the index
     */
    void postVisitSelectedChoiceItem(
        ChoiceItem choice,
        Choices choices,
        int index);

    /**
     * Post visit choices.
     * @param choices the choices
     */
    void postVisitChoices(
        Choices choices);

}
