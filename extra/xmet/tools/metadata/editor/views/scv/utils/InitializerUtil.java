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

import xmet.ClientContext;
import xmet.profiles.Profile;
import xmet.profiles.model.Entity;
import xmet.tools.metadata.editor.views.scv.impl.ChoiceItem;
import xmet.tools.metadata.editor.views.scv.impl.Choices;
import xmet.tools.metadata.editor.views.scv.impl.Code;
import xmet.tools.metadata.editor.views.scv.impl.CodeBlock;
import xmet.tools.metadata.editor.views.scv.impl.CodeParent;
import xmet.tools.metadata.editor.views.scv.impl.CompositeItem;
import xmet.tools.metadata.editor.views.scv.impl.EditorType;
import xmet.tools.metadata.editor.views.scv.impl.GetPathValueCode;
import xmet.tools.metadata.editor.views.scv.impl.Group;
import xmet.tools.metadata.editor.views.scv.impl.IfCode;
import xmet.tools.metadata.editor.views.scv.impl.Item;
import xmet.tools.metadata.editor.views.scv.impl.LabeledGroup;
import xmet.tools.metadata.editor.views.scv.impl.ModelItem;
import xmet.tools.metadata.editor.views.scv.impl.Page;
import xmet.tools.metadata.editor.views.scv.impl.ParentItem;
import xmet.tools.metadata.editor.views.scv.impl.RepeatCode;
import xmet.tools.metadata.editor.views.scv.impl.RepeatedGroup;
import xmet.tools.metadata.editor.views.scv.impl.RepeatedPage;
import xmet.tools.metadata.editor.views.scv.impl.SetPathValueCode;
import xmet.tools.metadata.editor.views.scv.impl.Sheet;

/**
 * Pre-processes items to be run-time usage ready i.e. it fills the
 * initialization context element of the items Note: do not re-initialize an
 * already initialized sheet
 * @author Nahid Akbar
 */
public class InitializerUtil
    extends DefaultModelVisitor {

    /** The sheet - root. */
    private final Sheet sheet;

    /** The model root. */
    private final Entity model;

    /** The current profile. */
    private Profile profile;

    /** The context. */
    private ClientContext context;

    /* == Constructors == */
    /**
     * Instantiates a new initializer util.
     * @param aSheet the sheet
     * @param aModel the model
     * @param aProfile the profile
     * @param aContext the context
     */
    public InitializerUtil(
        final Sheet aSheet,
        final Entity aModel,
        final Profile aProfile,
        final ClientContext aContext) {
        super();
        this.sheet = aSheet;
        this.model = aModel;
        this.context = aContext;
        this.profile = aProfile;
        if (aSheet != null) {
            SCVUtils.accept(
                aSheet,
                this);
            parent.push(aSheet);
            SCVUtils.setSheetModified(
                aSheet,
                true);
        }
    }

    /**
     * Instantiates a new initializer util.
     * @param aSheet the sheet
     * @param aParent the parent
     * @param item the item
     * @param aModel the model
     * @param parentPath the parent path
     */
    public InitializerUtil(
        final Sheet aSheet,
        final ParentItem aParent,
        final ModelItem item,
        final Entity aModel,
        final String parentPath) {
        super(parentPath);
        this.sheet = aSheet;
        this.model = aModel;
        this.parent.push(aSheet);
        if (aParent != null) {
            this.parent.push(aParent);
        }
        SCVUtils.accept(
            item,
            this);
        SCVUtils.setSheetModified(
            aSheet,
            true);
    }

    /* == DefaultModelVisitor Overrides == */
    /** The parent. */
    private final Stack<ParentItem> parent = new Stack<ParentItem>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitSheet(
        final Sheet aSheet) {
        if (aSheet.getIc() == null) {
            aSheet.setIc(new SheetIC(
                model,
                profile,
                context,
                aSheet));
        }
        SCVUtils.sortSheetPages(aSheet);
        parent.push(aSheet);
        super.preVisitSheet(aSheet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitSheet(
        final Sheet aSheet) {
        parent.pop();
        super.postVisitSheet(aSheet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitPage(
        final Page page) {
        if (page.getIc() == null) {
            page.setIc(new PageIC(
                sheet,
                parent.peek(),
                page));
            SCVUtils.registerNamedItem(
                page,
                parent.peek());
        }
        parent.push(page);
        super.preVisitPage(page);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitPage(
        final Page page) {
        parent.pop();
        super.postVisitPage(page);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitGroup(
        final Group group) {
        if (group.getIc() == null) {
            group.setIc(new GroupIC(
                sheet,
                parent.peek(),
                group));

            SCVUtils.registerNamedItem(
                group,
                parent.peek());

            if (group.getItems() == null) {
                group.setVisible(false);
            }
        }
        parent.push(group);
        super.preVisitGroup(group);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitGroup(
        final Group group) {
        super.postVisitGroup(group);
        parent.pop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int preVisitRepeatedGroup(
        final RepeatedGroup repeated) {
        if (repeated.getRIC() == null) {
            final Group group = new Group();
            Group.copyGroup(
                repeated,
                group);
            repeated.setRIC(new RepeatedIC<Group>(
                sheet,
                parent.peek(),
                group,
                repeated));
            repeated.getRIC().hardTraceSubstitutableXpath(
                null,
                repeated.getBase());
            super.preVisitRepeatedGroup(repeated);
            return 0;
        } else {
            super.preVisitRepeatedGroup(repeated);
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int preVisitRepeatedPage(
        final RepeatedPage repeated) {
        if (repeated.getIc() == null) {
            final Page page = new Page();
            Page.copyPage(
                repeated,
                page);
            repeated.setIc(new RepeatedIC<Page>(
                sheet,
                parent.peek(),
                page,
                repeated));
            repeated.getRIC().hardTraceSubstitutableXpath(
                null,
                repeated.getBase());
            parent.push(repeated);
            super.preVisitRepeatedPage(repeated);
            return 0;
        } else {
            parent.push(repeated);
            super.preVisitRepeatedPage(repeated);
            return 0;
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitRepeatedPage(
        final RepeatedPage repeated) {
        parent.pop();
        super.postVisitRepeatedPage(repeated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int preVisitRepeatCode(
        final CodeParent item,
        final RepeatCode repeatCode) {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitRepeatCode(
        final CodeParent item,
        final RepeatCode repeated) {
        repeated.setIc(new RepeatedIC<Code>(
            sheet,
            parent.peek(),
            (Code) SCVUtils.clone(repeated.getCode()),
            repeated));
        repeated.getIc().hardTraceSubstitutableXpath(
            null,
            repeated.getBase());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitLabeledGroup(
        final LabeledGroup labeled) {
        labeled.setIc(new GroupIC(
            sheet,
            parent.peek(),
            labeled));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitItem(
        final Item item) {
        if (item.getIc() == null) {
            item.setIc(new ItemIC(
                sheet,
                parent.peek(),
                item));
            item.getIc().hardTraceSubstitutableXpath(
                null,
                item.getXpath());
            SCVUtils.registerNamedItem(
                item,
                parent.peek());

            /* If item type is null then it must not be visible */
            if ((item.getType() == null)
                || (item.getType() == EditorType.Unspecified)) {
                item.setVisible(false);
            }
        }

        super.preVisitItem(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitCompositeItem(
        final CompositeItem item) {
        if (item.getIc() == null) {
            if (item.getRelative() != null
                && item.getRelative().indexOf(
                    "$/") == 0) {
                item.setRelative(item.getRelative().trim());
                item.setRelative(item.getRelative().replaceFirst(
                    "\\$/",
                    ""));
            }
            item.setIc(new ItemIC(
                sheet,
                parent.peek(),
                item));
            item.getIc().hardTraceSubstitutableXpath(
                null,
                item.getXpath());

            SCVUtils.registerNamedItem(
                item,
                parent.peek());

            /* If item type is null then it must not be visible */
            if ((item.getType() == null)
                || (item.getType() == EditorType.Unspecified)) {
                item.setVisible(false);
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitIfCode(
        final CodeParent item,
        final IfCode ifcode) {
        ifcode.setIc(new SetableIC(
            sheet,
            parent.peek()));
        if ((ifcode.getBase() != null)
            && !ifcode.getBase().equals(
                ".")) {
            ifcode.getIc().softTraceSubstitutableXpath(
                null,
                ifcode.getBase());
        }
        if (ifcode.getCode() != null) {
            SCVUtils.accept(
                ifcode.getCode(),
                this,
                item);
        }
        if (ifcode.getElseCode() != null) {
            SCVUtils.accept(
                ifcode.getElseCode(),
                this,
                item);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitSetPathValueCode(
        final CodeParent item,
        final SetPathValueCode setCode) {
        if (setCode.getDest() == null) {
            setCode.setDest(item.getXpath());
        }
        if (setCode.getDest() != null) {
            setCode.setDest(setCode.getDest().trim());
        }
        setCode.setIc(new SetableIC(
            sheet,
            parent.peek()));
        setCode.getIc().softTraceSubstitutableXpath(
            null,
            setCode.getDest());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitGetPathValueCode(
        final CodeParent item,
        final GetPathValueCode getPathValue) {
        getPathValue.setIc(new SetableIC(
            sheet,
            parent.peek()));
        getPathValue.getIc().softTraceSubstitutableXpath(
            null,
            getPathValue.getSrc());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int preVisitChoices(
        final Choices choices) {
        if (choices.getIc() == null) {
            choices.setIc(new ChoiceIC(
                sheet,
                parent.peek(),
                choices));
            for (final ChoiceItem ch : choices.getItems()) {
                ch.setIc(new SetableIC(
                    sheet,
                    parent.peek()));
                ch.getIc().softTraceSubstitutableXpath(
                    null,
                    ch.getTestXpath());
                /* SCVUtils.accept(ch.item, this); */
            }
        }
        return super.preVisitChoices(choices);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitCodeBlock(
        final CodeParent item,
        final CodeBlock codeBlock) {
        if (codeBlock.getIc() == null) {
            codeBlock.setIc(new CodeBlockIC(
                sheet,
                parent.peek(),
                codeBlock));
        }
        super.preVisitCodeBlock(
            item,
            codeBlock);
    }
}
