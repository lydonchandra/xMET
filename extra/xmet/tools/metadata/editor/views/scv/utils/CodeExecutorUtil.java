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

import java.awt.EventQueue;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import n.reporting.Reporting;
import n.ui.JOptionPaneUtils;
import xmet.tools.metadata.editor.views.scv.impl.AlertUserCode;
import xmet.tools.metadata.editor.views.scv.impl.Code;
import xmet.tools.metadata.editor.views.scv.impl.CodeParent;
import xmet.tools.metadata.editor.views.scv.impl.ContentNamedItem;
import xmet.tools.metadata.editor.views.scv.impl.ExecItemCodeCode;
import xmet.tools.metadata.editor.views.scv.impl.GetPathValueCode;
import xmet.tools.metadata.editor.views.scv.impl.HideableNamedItem;
import xmet.tools.metadata.editor.views.scv.impl.IfCode;
import xmet.tools.metadata.editor.views.scv.impl.Item;
import xmet.tools.metadata.editor.views.scv.impl.NamedItem;
import xmet.tools.metadata.editor.views.scv.impl.SetItemMandatoryCode;
import xmet.tools.metadata.editor.views.scv.impl.SetItemValidCode;
import xmet.tools.metadata.editor.views.scv.impl.SetItemValueCode;
import xmet.tools.metadata.editor.views.scv.impl.SetItemVisibleCode;
import xmet.tools.metadata.editor.views.scv.impl.SetPathValueCode;
import xmet.tools.metadata.editor.views.scv.impl.Sheet;

/**
 * Simply executes code.
 * @author Nahid Akbar
 */
@SuppressWarnings("rawtypes")
public class CodeExecutorUtil
    extends DefaultModelVisitor {

    /** The singleton. */
    private static CodeExecutorUtil singleton = new CodeExecutorUtil();

    /**
     * static method for executing given code.
     * @param sheet the sheet
     * @param item the item
     * @param code the code
     */
    public static void executeCode(
        final Sheet sheet,
        final CodeParent item,
        final Code code) {
        if (item instanceof Item) {
            singleton.setLastPath(((Item) item).getIc().getLastParentPath());
        } else {
            singleton.setLastPath(item.getIC().getNodeXpath());
        }
        singleton.execute(
            sheet,
            item,
            code);
    }

    // /** The sheet. */
    /* Sheet sheet; */

    /**
     * Main execution method.
     * @param sheet the sheet
     * @param item the item
     * @param code the code
     */
    public void execute(
        final Sheet sheet,
        final CodeParent item,
        final Code code) {
        /* this.sheet = sheet; */
        SCVUtils.accept(
            code,
            this,
            item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean preVisitIfCode(
        final CodeParent item,
        final IfCode ifcode) {
        super.preVisitIfCode(
            item,
            ifcode);
        return IfCode.isConditionSatisfied(
            item,
            ifcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitSetPathValueCode(
        final CodeParent item,
        final SetPathValueCode setPathValue) {
        super.visitSetPathValueCode(
            item,
            setPathValue);
        if (setPathValue != null) {
            if (setPathValue.getIc() != null) {
                String value = setPathValue.getValue();
                value = extractCodeValueHelper(
                    item,
                    value);
                item.getIC().setPathValue(
                    setPathValue.getIc().getNodeXpath(),
                    value);
            } else {
                Reporting.logUnexpected();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitSetItemValueCode(
        final CodeParent item,
        final SetItemValueCode setItemValue) {
        super.visitSetItemValueCode(
            item,
            setItemValue);
        if ((setItemValue != null)
            && (setItemValue.getName() != null)) {
            String value = setItemValue.getValue();
            if (value.equals("#")) {
                value = item.getValue();
            }
            if (value == null) {
                value = "";
            }
            if (setItemValue.getName().equals(
                "#")) {
                item.setValue(value);
            } else {
                final NamedItem igp = item.getIC().getNamedItem(
                    setItemValue.getName());
                if (igp != null) {
                    if (igp instanceof ContentNamedItem) {
                        ((ContentNamedItem) igp).setValue(value);
                    } else {
                        Reporting.reportUnexpected(
                            "Item %1$s does not contain values",
                            setItemValue.getName());
                    }
                } else {
                    Reporting.reportUnexpected(
                        "Name %1$s not found",
                        setItemValue.getName());
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitSetItemVisibleCode(
        final CodeParent item,
        final SetItemVisibleCode setItemVisible) {
        super.visitSetItemVisibleCode(
            item,
            setItemVisible);
        if ((setItemVisible != null)
            && (setItemVisible.getName() != null)) {
            if (setItemVisible.getName().equals(
                "#")) {
                item.setVisible(setItemVisible.isVisible());
            } else {
                final NamedItem igp = item.getIC().getNamedItem(
                    setItemVisible.getName());
                if (igp != null) {
                    if (igp instanceof HideableNamedItem) {
                        ((HideableNamedItem) igp).setVisible(setItemVisible
                            .isVisible());
                    } else {
                        Reporting.reportUnexpected(
                            "Item %1$s can not be made invisible or visible",
                            setItemVisible.getName());
                    }
                } else {
                    Reporting.reportUnexpected(
                        "Name %1$s not found",
                        setItemVisible.getName());
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitSetItemMandatoryCode(
        final CodeParent item,
        final SetItemMandatoryCode makeMamdatory) {
        super.visitSetItemMandatoryCode(
            item,
            makeMamdatory);
        if ((makeMamdatory != null)
            && (makeMamdatory.getName() != null)) {
            if (makeMamdatory.getName().equals(
                "#")) {
                item.setMandatory(makeMamdatory.isMandatory());
            } else {
                final NamedItem igp = item.getIC().getNamedItem(
                    makeMamdatory.getName());
                if (igp != null) {
                    if (igp instanceof ContentNamedItem) {
                        ((ContentNamedItem) igp).setMandatory(makeMamdatory
                            .isMandatory());
                    } else {
                        Reporting.reportUnexpected(
                            "Item %1$s does not contain values",
                            makeMamdatory.getName());
                    }
                } else {
                    Reporting.reportUnexpected(
                        "Name %1$s not found",
                        makeMamdatory.getName());
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitGetPathValueCode(
        final CodeParent item,
        final GetPathValueCode getPathValue) {
        super.visitGetPathValueCode(
            item,
            getPathValue);
        if ((getPathValue != null)
            && (getPathValue.getIc() != null)
            && (getPathValue.getName() != null)) {
            try {
                if (getPathValue.getName().equals(
                    "#")) {
                    item.setValue(getPathValue.getIc().getSetableValue());
                } else {
                    final NamedItem igp = item.getIC().getNamedItem(
                        getPathValue.getName());
                    if (igp != null) {
                        if (igp instanceof ContentNamedItem) {
                            ((ContentNamedItem) igp).setValue(getPathValue
                                .getIc()
                                .getSetableValue());
                        } else {
                            Reporting.reportUnexpected(
                                "Item %1$s does not contain values",
                                getPathValue.getName());
                        }
                    } else {
                        Reporting.reportUnexpected(
                            "Name %1$s not found",
                            getPathValue.getName());
                    }
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitExecItemCodeCode(
        final CodeParent item,
        final ExecItemCodeCode execItemCode) {
        super.visitExecItemCodeCode(
            item,
            execItemCode);
        if ((execItemCode != null)
            && (execItemCode.getName() != null)) {
            CodeParent itm = null;
            if (execItemCode.getName().equals(
                "#")) {
                itm = item;
            } else {
                final NamedItem igp = item.getIC().getNamedItem(
                    execItemCode.getName());
                if ((igp != null)
                    && (igp instanceof CodeParent)) {
                    itm = (CodeParent) igp;
                } else {
                    Reporting.reportUnexpected(
                        "Name %1$s not found or is not code parent",
                        execItemCode.getName());
                }
            }
            if (itm != null) {
                Reporting.logExpected("exec");
                EventQueue.invokeLater(new CodeExecutionTask(
                    itm,
                    this,
                    execItemCode.getCode(),
                    itm.getClass()));
            }
        }
    }

    /**
     * Wrapper for ExecCode execution.
     */
    private static class CodeExecutionTask
        implements
        Runnable {

        /** The item. */
        private final CodeParent item;

        /**
         * Instantiates a new code execution task.
         * @param aItem the item
         * @param aExecutor the executor
         * @param aFieldName the field name
         * @param aClazz the clazz
         */
        public CodeExecutionTask(
            final CodeParent aItem,
            final CodeExecutorUtil aExecutor,
            final String aFieldName,
            final Class aClazz) {
            super();
            this.item = aItem;
            this.executor = aExecutor;
            this.fieldName = aFieldName;
            this.clazz = aClazz;
        }

        /** The executor. */
        private final CodeExecutorUtil executor;

        /** The field name. */
        private final String fieldName;

        /** The clazz. */
        private final Class clazz;

        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {
            new CodeExecusionTraverserUtil(
                item,
                fieldName,
                executor,
                clazz);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitAlertUserCode(
        final CodeParent item,
        final AlertUserCode alertUser) {
        // CHECKSTYLE OFF: MagicNumber
        if (alertUser != null
            && alertUser.getMessage() != null) {
            final String message = alertUser.getMessage();
            String title = alertUser.getTitle();
            if (title == null
                || (title.trim()).length() == 0) {
                title = message.substring(
                    0,
                    15);
            }
            title = title.trim();
            JOptionPaneUtils.showInfoMessageBox(
                title,
                message);
        }
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitSetItemValidCode(
        final CodeParent item,
        final SetItemValidCode setValid) {
        super.visitSetItemValidCode(
            item,
            setValid);
        if ((setValid != null)
            && (setValid.getName() != null)) {
            if (setValid.getName().equals(
                "#")) {
                item.setValidationError(setValid.getValidationError());
            } else {
                final NamedItem igp = item.getIC().getNamedItem(
                    setValid.getName());
                if (igp != null) {
                    if (igp instanceof ContentNamedItem) {
                        ((ContentNamedItem) igp).setValidationError(setValid
                            .getValidationError());
                    } else {
                        Reporting.reportUnexpected(
                            "Item %1$s does not contain values",
                            setValid.getName());
                    }
                } else {
                    Reporting.reportUnexpected(
                        "Name %1$s not found",
                        setValid.getName());
                }
            }
        }
    }

    /**
     * Extract code value helper.
     * @param item the item
     * @param value the value
     * @return the string
     */
    private String extractCodeValueHelper(
        final CodeParent item,
        final String value) {
        String varValue = value;
        if (varValue == null) {
            varValue = "";
        }
        if (varValue.equals("#")) {
            varValue = item.getValue();
        }
        if (varValue.startsWith("#eval:")) {
            varValue = varValue.substring("#eval:".length());
            if (varValue.equals("uuid")) {
                varValue = UUID.randomUUID().toString();
            } else if (varValue.equals("currentDate")) {
                varValue = (new SimpleDateFormat(
                    "yyyy-MM-dd")).format(new Date());
            } else if (varValue.equals("identifier")) {
                varValue = UUID.randomUUID().toString();
                varValue = varValue.replace(
                    '-',
                    '_');
                varValue = "id"
                    + varValue;
            }
        } else if (varValue.startsWith("%")) {
            varValue = varValue.substring(1);
            final NamedItem referencedItem = item.getIC().getNamedItem(
                varValue);
            if (referencedItem != null) {
                if (referencedItem instanceof ContentNamedItem) {
                    varValue = ((ContentNamedItem) referencedItem).getValue();
                } else {
                    Reporting.logUnexpected("Inappropriate Named Item Type");
                }
            } else {
                Reporting.logUnexpected("Named Item Not Found");
            }
        }
        return varValue;
    }
}
