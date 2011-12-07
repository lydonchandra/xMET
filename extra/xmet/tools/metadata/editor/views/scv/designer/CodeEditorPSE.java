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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.TreeCellRenderer;

import n.reporting.Reporting;
import n.ui.JOptionPaneUtils;
import n.ui.SwingUtils;
import n.ui.patterns.propertySheet.BooleanPSE;
import n.ui.patterns.propertySheet.LongLabelPSE;
import n.ui.patterns.propertySheet.ObjectChoicePSE;
import n.ui.patterns.propertySheet.PropertySheetEditor;
import n.ui.patterns.propertySheet.PropertySheetItem;
import n.ui.patterns.propertySheet.StringPSE;
import n.ui.patterns.stb.JSimpleTreeBuilder;
import n.ui.patterns.stb.JSimpleTreeBuilderExtensionButton;
import n.ui.patterns.stb.JSimpleTreeBuilderModelConvertable;
import n.ui.patterns.stb.JSimpleTreeBuilderModelDragable;
import n.ui.patterns.stb.JSimpleTreeBuilderModelExtended;
import xmet.ClientContext;
import xmet.profiles.model.Entity;
import xmet.tools.metadata.editor.views.scv.model.AlertUserCode;
import xmet.tools.metadata.editor.views.scv.model.Code;
import xmet.tools.metadata.editor.views.scv.model.CodeBlock;
import xmet.tools.metadata.editor.views.scv.model.Condition;
import xmet.tools.metadata.editor.views.scv.model.ExecItemCodeCode;
import xmet.tools.metadata.editor.views.scv.model.GetPathValueCode;
import xmet.tools.metadata.editor.views.scv.model.IfCode;
import xmet.tools.metadata.editor.views.scv.model.ModelItem;
import xmet.tools.metadata.editor.views.scv.model.ModelUtils;
import xmet.tools.metadata.editor.views.scv.model.RepeatCode;
import xmet.tools.metadata.editor.views.scv.model.SetItemMandatoryCode;
import xmet.tools.metadata.editor.views.scv.model.SetItemValidCode;
import xmet.tools.metadata.editor.views.scv.model.SetItemValueCode;
import xmet.tools.metadata.editor.views.scv.model.SetItemVisibleCode;
import xmet.tools.metadata.editor.views.scv.model.SetPathValueCode;

/**
 * This PSE is for editing Code object trees. Appears as a button and does the
 * job through JSimpleTreeBuilder in a popup dialog.
 * @author Nahid Akbar
 */
@SuppressWarnings({
"serial",
"rawtypes"
})
public class CodeEditorPSE
    extends JButton
    implements
    ActionListener,
    JSimpleTreeBuilderModelExtended,
    JSimpleTreeBuilderModelDragable,
    JSimpleTreeBuilderModelConvertable,
    TreeCellRenderer,
    PropertySheetEditor {

    /** The model - needed for xpath setting and whatnot. */
    private Entity model;

    /** The code root. */
    private Code code;

    /** The context. */
    private ClientContext context;

    /**
     * Instantiates a new code editor pse.
     */
    public CodeEditorPSE() {
        updateText();
        addActionListener(this);
    }

    /** The editor Jdialog. */
    private final JDialog editorDialog = new JDialog();

    /** The code tree builder. */
    private JSimpleTreeBuilder codeBuilder;

    /**
     * The split pane. Top for the code Tree and bottom for the editor.
     */
    private final JSplitPane splitPane = new JSplitPane(
        JSplitPane.VERTICAL_SPLIT);

    /** The editor - re-use stuff from SCV Designer. */
    private CustomReflectivePropertySheet editor = null;

    /** The last selected row. */
    private int lastSelectedRow = 0;

    /** The initialized. */
    private boolean initialized = false;

    /**
     * Update text - sets button label to be editCode if code is present
     * otherwise add code if code is not present.
     */
    private void updateText() {
        if (code != null) {
            setText("edit code");
        } else {
            setText("add code");
        }
        if (initialized) {
            codeBuilder.rowChildrenChanged(0);
        }
    }

    /*
     * == ActionListener implementation for the add/edit button callback
     * ================
     */
    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(
        final ActionEvent e) {
        /* open dialog */
        editorDialog.setVisible(true);
        /* update text */
        updateText();
    }

    /* == JSimpleTreeBuilderModelExtended implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void removeChildren(
        final Object parent,
        final Object child) {
        if (parent == null) {
            code = null;
        } else {
            if (parent instanceof IfCode) {
                if (child == ((IfCode) parent).getCode()) {
                    ((IfCode) parent).setCode(null);
                } else {
                    ((IfCode) parent).setElseCode(null);
                }
            } else if (parent instanceof RepeatCode) {
                ((RepeatCode) parent).setCode(null);
            } else if (parent instanceof CodeBlock) {
                if (((CodeBlock) parent).getCode() != null) {
                    ((CodeBlock) parent).getCode().remove(
                        child);
                }
            } else {
                Reporting.logUnexpected(
                    "%1$s has no children",
                    parent.toString());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveChildrenUp(
        final Object parent,
        final Object child) {
        if (parent instanceof CodeBlock) {
            if (((CodeBlock) parent).getCode() != null) {
                final int i = ((CodeBlock) parent).getCode().indexOf(
                    child);
                if (i > 0) {
                    final Code t = ((CodeBlock) parent).getCode().get(
                        i - 1);
                    ((CodeBlock) parent).getCode().set(
                        i - 1,
                        ((CodeBlock) parent).getCode().get(
                            i));
                    ((CodeBlock) parent).getCode().set(
                        i,
                        t);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveChildrenDown(
        final Object parent,
        final Object child) {
        if (parent instanceof CodeBlock) {
            final CodeBlock varParent = (CodeBlock) parent;
            if (varParent.getCode() != null) {
                final int i = varParent.getCode().indexOf(
                    child);
                if ((i >= 0)
                    && (i < varParent.getCode().size() - 1)) {
                    final Code t = varParent.getCode().get(
                        i + 1);
                    varParent.getCode().set(
                        i + 1,
                        varParent.getCode().get(
                            i));
                    varParent.getCode().set(
                        i,
                        t);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getRoot() {
        return code;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIndexOfChild(
        final Object parent,
        final Object child) {
        if (parent instanceof IfCode) {
            final IfCode varIfCode = (IfCode) parent;
            if (varIfCode.getCode() == child) {
                return 0;
            } else {
                if (varIfCode.getElseCode() == child) {
                    return 1;
                } else {
                    return -1;
                }
            }
        } else if (parent instanceof RepeatCode) {
            if (((RepeatCode) parent).getCode() == child) {
                return 0;
            } else {
                return -1;
            }
        } else if (parent instanceof CodeBlock) {
            final CodeBlock varCodeBlock = (CodeBlock) parent;
            if (varCodeBlock.getCode() == null) {
                return -1;
            } else {
                return varCodeBlock.getCode().indexOf(
                    child);
            }
        } else if (parent instanceof SetPathValueCode) { /* no children */
            return -1;
        } else if (parent instanceof ExecItemCodeCode) { /* no children */
            return -1;
        } else if (parent instanceof SetItemVisibleCode) { /* no */
            /* children */
            return -1;
        } else if (parent instanceof SetItemValueCode) { /* no */
            /* children */
            return -1;
        } else if (parent instanceof SetItemMandatoryCode) { /* no */
            /* children */
            return -1;
        } else if (parent instanceof GetPathValueCode) { /* no */
            /* children */
            return -1;
        } else if (parent instanceof SetItemValidCode) { /* no */
            /* children */
            return -1;
        } else if (parent instanceof AlertUserCode) { /* no */
            /* children */
            return -1;
        } else {
            if (parent == null) {
                return 0;
            } else {
                Reporting.logUnexpected(parent.toString());
                return -1;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getChildCount(
        final Object parent) {
        if (parent instanceof IfCode) {
            final IfCode varIfCode = (IfCode) parent;
            int count = 0;
            if (varIfCode.getCode() != null) {
                count++;
            }
            if (varIfCode.getElseCode() != null) {
                count++;
            }
            return count;
        } else if (parent instanceof RepeatCode) {
            if (((RepeatCode) parent).getCode() == null) {
                return 0;
            } else {
                return 1;
            }
        } else if (parent instanceof CodeBlock) {
            if ((((CodeBlock) parent).getCode() == null)) {
                return 0;
            } else {
                return ((CodeBlock) parent).getCode().size();
            }
        } else if (parent instanceof SetPathValueCode) { /* no children */
            return 0;
        } else if (parent instanceof ExecItemCodeCode) { /* no children */
            return 0;
        } else if (parent instanceof SetItemVisibleCode) { /* no */
            /* children */
            return 0;
        } else if (parent instanceof SetItemValueCode) { /* no */
            /* children */
            return 0;
        } else if (parent instanceof SetItemMandatoryCode) { /* no */
            /* children */
            return 0;
        } else if (parent instanceof GetPathValueCode) { /* no */
            /* children */
            return 0;
        } else if (parent instanceof SetItemValidCode) { /* no */
            /* children */
            return 0;
        } else if (parent instanceof AlertUserCode) { /* no */
            /* children */
            return 0;
        } else {
            Reporting.logUnexpected(parent.toString());
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getChild(
        final Object parent,
        final int index) {
        if (parent instanceof IfCode) {
            if (index == 0) {
                return ((IfCode) parent).getCode();
            } else {
                return ((IfCode) parent).getElseCode();
            }
        } else if (parent instanceof RepeatCode) {
            return ((RepeatCode) parent).getCode();
        } else if (parent instanceof CodeBlock) {
            if (((CodeBlock) parent).getCode() != null) {
                return ((CodeBlock) parent).getCode().get(
                    index);
            } else {
                return null;
            }
        } else if (parent instanceof SetPathValueCode) { /* no children */
            return null;
        } else if (parent instanceof ExecItemCodeCode) { /* no children */
            return null;
        } else if (parent instanceof SetItemVisibleCode) { /* no */
            /* children */
            return null;
        } else if (parent instanceof SetItemValueCode) { /* no */
            /* children */
            return null;
        } else if (parent instanceof SetItemMandatoryCode) { /* no */
            /* children */
            return null;
        } else if (parent instanceof GetPathValueCode) { /* no */
            /* children */
            return null;
        } else if (parent instanceof SetItemValidCode) { /* no */
            /* children */
            return null;
        } else if (parent instanceof AlertUserCode) { /* no */
            /* children */
            return null;
        } else {
            Reporting.logUnexpected(parent.toString());
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class[] getAllowedChildrenClasses(
        final Object parent) {
        /* code execution is context free... */
        return ModelUtils.getAllCodeTags();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addChildren(
        final Object parent,
        final Class childClass) {

        Object child = null;
        if (childClass == IfCode.class) {
            child = new IfCode();
        }
        if (childClass == SetPathValueCode.class) {
            child = new SetPathValueCode();
        }
        if (childClass == ExecItemCodeCode.class) {
            child = new ExecItemCodeCode();
        }
        if (childClass == CodeBlock.class) {
            child = new CodeBlock();
        }
        if (childClass == SetItemVisibleCode.class) {
            child = new SetItemVisibleCode();
        }
        if (childClass == RepeatCode.class) {
            child = new RepeatCode();
        }
        if (childClass == SetItemValueCode.class) {
            child = new SetItemValueCode();
        }
        if (childClass == GetPathValueCode.class) {
            child = new GetPathValueCode();
        }
        if (childClass == SetItemValidCode.class) {
            child = new SetItemValidCode();
        }
        if (childClass == AlertUserCode.class) {
            child = new AlertUserCode();
        }
        if (childClass == SetItemMandatoryCode.class) {
            child = new SetItemMandatoryCode();
        }

        if (child != null) {
            if (parent == null) {
                code = (Code) child;
            } else {
                if (parent instanceof IfCode) {
                    if (((IfCode) parent).getCode() == null) {
                        ((IfCode) parent).setCode((Code) child);
                    } else if (((IfCode) parent).getElseCode() == null) {
                        ((IfCode) parent).setElseCode((Code) child);
                    } else {
                        Reporting.logUnexpected();
                    }
                } else if (parent instanceof RepeatCode) {
                    ((RepeatCode) parent).setCode((Code) child);
                } else if (parent instanceof CodeBlock) {
                    if (((CodeBlock) parent).getCode() == null) {
                        ((CodeBlock) parent).setCode(new ArrayList<Code>());
                    }
                    ((CodeBlock) parent).getCode().add(
                        (Code) child);
                } else if (parent instanceof SetPathValueCode) { /* no */
                    /* children */
                    Reporting.reportUnexpected("ERROR!");
                } else if (parent instanceof ExecItemCodeCode) { /* no */
                    /* children */
                    Reporting.reportUnexpected("ERROR!");
                } else if (parent instanceof SetItemVisibleCode) { /* no */
                    /* children */
                    Reporting.reportUnexpected("ERROR!");
                } else if (parent instanceof SetItemValueCode) { /* no */
                    /* children */
                    Reporting.reportUnexpected("ERROR!");
                } else {
                    Reporting.logUnexpected(parent.toString());
                }
            }
        } else {
            Reporting.logUnexpected(childClass.getName());
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaximumChildrenCount(
        final Object parent) {
        if (parent instanceof IfCode) {
            return 2;
        } else if (parent instanceof RepeatCode) {
            return 1;
        } else if (parent instanceof CodeBlock) {
            return -1;
        } else if (parent instanceof SetPathValueCode) { /* no */
            return 0;
        } else if (parent instanceof ExecItemCodeCode) { /* no */
            return 0;
        } else if (parent instanceof SetItemVisibleCode) { /* no */
            return 0;
        } else if (parent instanceof SetItemValueCode) { /* no */
            return 0;
        } else if (parent instanceof GetPathValueCode) { /* no */
            return 0;
        } else if (parent instanceof SetItemValidCode) { /* no */
            return 0;
        } else if (parent instanceof AlertUserCode) { /* no */
            return 0;
        } else if (parent instanceof SetItemMandatoryCode) { /* no */
            return 0;
        } else {
            return 0;
        }
    }

    /* CHECKSTYLE OFF: MethodLength */
    /**
     * {@inheritDoc}
     */
    @Override
    public void selectionChanged(
        final Object selectedItem,
        final int selectedRow) {
        if (editor != null) {
            editor.commit();
            editor = null;
            codeBuilder.rowChanged(lastSelectedRow);
        }
        if (selectedItem != null) {
            lastSelectedRow = selectedRow;
            final ArrayList<Object> params = new ArrayList<Object>();
            params.add("model");
            params.add(model);
            if (selectedItem instanceof IfCode) {
                final IfCode item = (IfCode) selectedItem;
                params.add("choices");
                params.add(Condition.values());
                editor = new CustomReflectivePropertySheet(
                    new CustomReflectivePropertySheetItem[] {
                    new CustomReflectivePropertySheetItem(
                        "Xpath:*",
                        null,
                        XPathEditorPSE.class,
                        item,
                        "base",
                        "model"),
                    new CustomReflectivePropertySheetItem(
                        "Condition:*",
                        null,
                        ObjectChoicePSE.class,
                        item,
                        "condition",
                        "choices"),
                    new CustomReflectivePropertySheetItem(
                        "Expression:*",
                        null,
                        StringPSE.class,
                        item,
                        "expression")
                    },
                    params.toArray());
            } else if (selectedItem instanceof RepeatCode) {
                final RepeatCode item = (RepeatCode) selectedItem;
                editor = new CustomReflectivePropertySheet(
                    new CustomReflectivePropertySheetItem[] {
                        new CustomReflectivePropertySheetItem(
                            "Xpath:*",
                            null,
                            XPathEditorPSE.class,
                            item,
                            "base",
                            "model")
                    },
                    params.toArray());
            } else if (selectedItem instanceof CodeBlock) {
                Reporting.logExpected("Code block does not have an editor");
            } else if (selectedItem instanceof SetPathValueCode) { /* no */
                final SetPathValueCode item = (SetPathValueCode) selectedItem;
                editor = new CustomReflectivePropertySheet(
                    new CustomReflectivePropertySheetItem[] {
                    new CustomReflectivePropertySheetItem(
                        "Xpath:*",
                        null,
                        XPathEditorPSE.class,
                        item,
                        "dest",
                        "model"),
                    new CustomReflectivePropertySheetItem(
                        "Value:*",
                        null,
                        StringPSE.class,
                        item,
                        "value")
                    },
                    params.toArray());
            } else if (selectedItem instanceof ExecItemCodeCode) { /* no */
                final ExecItemCodeCode item = (ExecItemCodeCode) selectedItem;
                editor = new CustomReflectivePropertySheet(
                    new CustomReflectivePropertySheetItem[] {
                    new CustomReflectivePropertySheetItem(
                        "Name:*",
                        null,
                        StringPSE.class,
                        item,
                        "name"),
                    new CustomReflectivePropertySheetItem(
                        "Code:*",
                        null,
                        StringPSE.class,
                        item,
                        "code"),
                    },
                    params.toArray());
            } else if (selectedItem instanceof SetItemVisibleCode) { /* no */
                final SetItemVisibleCode item =
                    (SetItemVisibleCode) selectedItem;
                editor = new CustomReflectivePropertySheet(
                    new CustomReflectivePropertySheetItem[] {
                    new CustomReflectivePropertySheetItem(
                        "Name:*",
                        null,
                        StringPSE.class,
                        item,
                        "name"),
                    new CustomReflectivePropertySheetItem(
                        "Visible:*",
                        null,
                        BooleanPSE.class,
                        item,
                        "visible"),
                    },
                    params.toArray());
            } else if (selectedItem instanceof SetItemValueCode) { /* no */
                final SetItemValueCode item = (SetItemValueCode) selectedItem;
                editor = new CustomReflectivePropertySheet(
                    new CustomReflectivePropertySheetItem[] {
                    new CustomReflectivePropertySheetItem(
                        "Name:*",
                        null,
                        StringPSE.class,
                        item,
                        "name"),
                    new CustomReflectivePropertySheetItem(
                        "Value:*",
                        null,
                        StringPSE.class,
                        item,
                        "value"),
                    },
                    params.toArray());
            } else if (selectedItem instanceof GetPathValueCode) { /* no */
                final GetPathValueCode item = (GetPathValueCode) selectedItem;
                editor = new CustomReflectivePropertySheet(
                    new CustomReflectivePropertySheetItem[] {
                    new CustomReflectivePropertySheetItem(
                        "Xpath:*",
                        null,
                        XPathEditorPSE.class,
                        item,
                        "src",
                        "model"),
                    new CustomReflectivePropertySheetItem(
                        "Name:*",
                        null,
                        StringPSE.class,
                        item,
                        "name")
                    },
                    params.toArray());
            } else if (selectedItem instanceof SetItemValidCode) { /* no */
                final SetItemValidCode item = (SetItemValidCode) selectedItem;
                editor = new CustomReflectivePropertySheet(
                    new CustomReflectivePropertySheetItem[] {
                    new CustomReflectivePropertySheetItem(
                        "Name:*",
                        null,
                        StringPSE.class,
                        item,
                        "name"),
                    new CustomReflectivePropertySheetItem(
                        "Error:*",
                        null,
                        StringPSE.class,
                        item,
                        "validationError"),
                    },
                    params.toArray());
            } else if (selectedItem instanceof AlertUserCode) { /* no */
                final AlertUserCode item = (AlertUserCode) selectedItem;
                editor = new CustomReflectivePropertySheet(
                    new CustomReflectivePropertySheetItem[] {
                    new CustomReflectivePropertySheetItem(
                        "Title:",
                        null,
                        StringPSE.class,
                        item,
                        "title"),
                    new CustomReflectivePropertySheetItem(
                        "Message:*",
                        null,
                        StringPSE.class,
                        item,
                        "message"),
                    },
                    params.toArray());
            } else if (selectedItem instanceof SetItemMandatoryCode) { /* no */
                final SetItemMandatoryCode item =
                    (SetItemMandatoryCode) selectedItem;
                editor = new CustomReflectivePropertySheet(
                    new CustomReflectivePropertySheetItem[] {
                    new CustomReflectivePropertySheetItem(
                        "Name:*",
                        null,
                        StringPSE.class,
                        item,
                        "name"),
                    new CustomReflectivePropertySheetItem(
                        "Mandatory:*",
                        null,
                        BooleanPSE.class,
                        item,
                        "mandatory"),
                    },
                    params.toArray());
            } else {
                Reporting.logUnexpected();
            }
            if (editor == null) {
                splitPane.setBottomComponent(new CustomReflectivePropertySheet(
                    new CustomReflectivePropertySheetItem[] {
                        new CustomReflectivePropertySheetItem(
                            null,
                            "Nothin to Edit",
                            LongLabelPSE.class,
                            null,
                            null)
                    },
                    null));
            } else {
                splitPane.setBottomComponent(editor);
            }
        } else {
            splitPane.setBottomComponent(new CustomReflectivePropertySheet(
                new CustomReflectivePropertySheetItem[] {
                    new CustomReflectivePropertySheetItem(
                        null,
                        "Nothin to Edit",
                        LongLabelPSE.class,
                        null,
                        null)
                },
                null));
        }
    }

    /* CHECKSTYLE ON: MethodLength */

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassTitle(
        final Class classObject) {
        return classObject.getSimpleName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JButton getAddButton() {
        if (getContext() != null) {
            return new JButton(
                getContext().getResources().getImageIconResource(
                    "images/toolbar.common.addItem.png"));
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JButton getRemoveButton() {
        if (getContext() != null) {
            return new JButton(
                getContext().getResources().getImageIconResource(
                    "images/toolbar.common.removeItem.png"));
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JButton getUpButton() {
        if (getContext() != null) {
            return new JButton(
                getContext().getResources().getImageIconResource(
                    "images/toolbar.common.moveItemUp.png"));
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JButton getDownButton() {
        if (getContext() != null) {
            return new JButton(
                getContext().getResources().getImageIconResource(
                    "images/toolbar.common.moveItemDown.png"));
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreeCellRenderer getCellRenderer() {
        return this;
    }

    /* == TreeCellRenderer implementation == */

    /**
     * The code renderer label.
     */
    private final JLabel codeRendererLabel = new JLabel();

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getTreeCellRendererComponent(
        final JTree tree,
        final Object value,
        final boolean selected,
        final boolean expanded,
        final boolean leaf,
        final int row,
        final boolean hasFocus) {
        if (value instanceof IfCode) {
            codeRendererLabel.setText(String.format(
                "if %1$s:%2$s(%3$s) then",
                ((IfCode) value).getBase(),
                ((IfCode) value).getCondition(),
                ((IfCode) value).getExpression()));
        } else if (value instanceof RepeatCode) {
            codeRendererLabel.setText(String.format(
                "repeat for %1$s",
                ((RepeatCode) value).getBase()));
        } else if (value instanceof CodeBlock) {
            codeRendererLabel.setText("block :");
        } else if (value instanceof SetPathValueCode) { /* no */
            codeRendererLabel.setText(String.format(
                "setValueAtPath(%1$s,%2$s)",
                ((SetPathValueCode) value).getDest(),
                ((SetPathValueCode) value).getValue()));
        } else if (value instanceof ExecItemCodeCode) { /* no */
            codeRendererLabel.setText(String.format(
                "getItemByName(%1$s):%2$s()",
                ((ExecItemCodeCode) value).getName(),
                ((ExecItemCodeCode) value).getCode()));
        } else if (value instanceof SetItemVisibleCode) { /* no */
            codeRendererLabel.setText(String.format(
                "getItemByName(%1$s):setVisible(%2$s)",
                ((SetItemVisibleCode) value).getName(),
                ((SetItemVisibleCode) value).isVisible()));
        } else if (value instanceof SetItemValueCode) { /* no */
            codeRendererLabel.setText(String.format(
                "getItemByName(%1$s):setValue(%2$s)",
                ((SetItemValueCode) value).getName(),
                ((SetItemValueCode) value).getValue()));
        } else if (value instanceof GetPathValueCode) { /* no */
            codeRendererLabel.setText(String.format(
                "getItemByName(%1$s):setValue(getValueAtPath(%2$s))",
                ((GetPathValueCode) value).getName(),
                ((GetPathValueCode) value).getSrc()));
        } else if (value instanceof SetItemValidCode) { /* no */
            codeRendererLabel.setText(String.format(
                "getItemByName(%1$s):setValidationError(%2$s)",
                ((SetItemValidCode) value).getName(),
                ((SetItemValidCode) value).getValidationError()));
        } else if (value instanceof AlertUserCode) { /* no */
            codeRendererLabel.setText(String.format(
                "alertUser(%1$s, %2$s)",
                ((AlertUserCode) value).getTitle(),
                ((AlertUserCode) value).getMessage()));
        } else if (value instanceof SetItemMandatoryCode) { /* no */
            codeRendererLabel
                .setText(String.format(
                    "getItemByName(%1$s):setMandatory(%2$s)",
                    ((SetItemMandatoryCode) value).getName(),
                    Boolean.toString(((SetItemMandatoryCode) value)
                        .isMandatory())));
        } else {
            Reporting.logUnexpected();
        }

        if (selected) {
            codeRendererLabel.setBackground(UIManager
                .getColor("Tree.selectionBackground"));
        } else {
            codeRendererLabel.setBackground(UIManager
                .getColor("Tree.background"));
        }
        return codeRendererLabel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getEditor(
        final Object value,
        final PropertySheetItem item) {
        // CHECKSTYLE OFF: MagicNumber
        // {
        /* initialization code */
        if (!initialized) {
            initialized = true;
            editorDialog.setTitle("Edit Code");
            editorDialog.setModal(true);
            editorDialog.setSize(
                800,
                600);
            SwingUtils.WINDOW.centreWindow(editorDialog);
            editorDialog.add(splitPane);
            codeBuilder = new JSimpleTreeBuilder(
                this);
            codeBuilder.setButtonPanelPosition(JSimpleTreeBuilder.RIGHT);
            codeBuilder.setCollapsable(false);
            codeBuilder.addExtensionButtons(new CodeXMLEditExtension());
            splitPane.setTopComponent(codeBuilder);
            splitPane.setBottomComponent(new JLabel(
                "-- --"));
            splitPane.setResizeWeight(0.8);
            splitPane.setBorder(BorderFactory.createEmptyBorder(
                5,
                5,
                5,
                5));
        }
        // }
        code = (Code) value;
        updateText();
        return this;
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValue() {
        return code;
    }

    /* == JSimpleTreeBuilderModelDragable implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void attachChild(
        final Object parent,
        final Object child,
        final int index) {
        if ((parent != null)
            && (child instanceof Code)) {
            if (parent.getClass() == CodeBlock.class) {
                ((CodeBlock) parent).getCode().add(
                    index,
                    (Code) child);
            } else if (parent.getClass() == GetPathValueCode.class) {
                Reporting.reportUnexpected();
            } else if (parent.getClass() == SetItemValidCode.class) {
                Reporting.reportUnexpected();
            } else if (parent.getClass() == AlertUserCode.class) {
                Reporting.reportUnexpected();
            } else if (parent.getClass() == SetItemMandatoryCode.class) {
                Reporting.reportUnexpected();
            } else if (parent.getClass() == SetItemValueCode.class) {
                Reporting.reportUnexpected();
            } else if (parent.getClass() == SetItemVisibleCode.class) {
                Reporting.reportUnexpected();
            } else if (parent.getClass() == SetPathValueCode.class) {
                Reporting.reportUnexpected();
            } else if (parent.getClass() == ExecItemCodeCode.class) {
                Reporting.reportUnexpected();
            } else if (parent.getClass() == IfCode.class) {
                final String option = (String) JOptionPaneUtils.getUserChoice(
                    "Please select which to replace",
                    new Object[] {
                    "if block",
                    "else block"
                    });
                if (option != null) {
                    if (option.equals("if block")) {
                        ((IfCode) parent).setCode((Code) child);
                    } else {
                        ((IfCode) parent).setElseCode((Code) child);
                    }
                }
            } else if (parent.getClass() == RepeatCode.class) {
                ((RepeatCode) parent).setCode((Code) child);
            } else {
                Reporting.logUnexpected();
            }
        } else {
            if (parent == null) {
                code = (Code) child;
            } else {
                Reporting.logUnexpected();
            }

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void detachChild(
        final Object parent,
        final Object child) {
        if ((parent != null)
            && (child instanceof Code)) {
            if (parent.getClass() == CodeBlock.class) {
                ((CodeBlock) parent).getCode().remove(
                    child);
            } else if (parent.getClass() == GetPathValueCode.class) {
                Reporting.reportUnexpected();
            } else if (parent.getClass() == SetItemValidCode.class) {
                Reporting.reportUnexpected();
            } else if (parent.getClass() == AlertUserCode.class) {
                Reporting.reportUnexpected();
            } else if (parent.getClass() == SetItemMandatoryCode.class) {
                Reporting.reportUnexpected();
            } else if (parent.getClass() == SetItemValueCode.class) {
                Reporting.reportUnexpected();
            } else if (parent.getClass() == SetItemVisibleCode.class) {
                Reporting.reportUnexpected();
            } else if (parent.getClass() == SetPathValueCode.class) {
                Reporting.reportUnexpected();
            } else if (parent.getClass() == ExecItemCodeCode.class) {
                Reporting.reportUnexpected();
            } else if (parent.getClass() == IfCode.class) {
                if (((IfCode) parent).getCode() == child) {
                    ((IfCode) parent).setCode(null);
                }
                if (((IfCode) parent).getElseCode() == child) {
                    ((IfCode) parent).setElseCode(null);
                }
                Reporting.logUnexpected();
            } else if (parent.getClass() == RepeatCode.class) {
                if (((RepeatCode) parent).getCode() == child) {
                    ((RepeatCode) parent).setCode(null);
                }
            } else {
                Reporting.logUnexpected();
            }
        } else {
            Reporting.logUnexpected();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object cloneElement(
        final Object child) {
        return ModelUtils.clone((ModelItem) child);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCloneSupported() {
        return true;
    }

    /* == JSimpleTreeBuilderModelConvertable implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public Class[] getCompatibleTypes(
        final Class type,
        final Object aContext) {
        return new Class[] {
        /* context free */
        RepeatCode.class,
        CodeBlock.class,
        IfCode.class
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object convertObjectType(
        final Object object,
        final Class newType) {
        if (newType == RepeatCode.class) {
            final RepeatCode item = new RepeatCode();
            item.setCode((Code) object);
            return item;
        } else if (newType == CodeBlock.class) {
            final CodeBlock item = new CodeBlock();
            item.getCode().add(
                (Code) object);
            return item;
        } else if (newType == IfCode.class) {
            final String option = (String) JOptionPaneUtils.getUserChoice(
                "Please select where to put code",
                new Object[] {
                "if block",
                "else block"
                });
            if (option != null) {
                if (option.equals("if block")) {
                    final IfCode item = new IfCode();
                    item.setCode((Code) object);
                    return item;
                } else {
                    final IfCode item = new IfCode();
                    item.setElseCode((Code) object);
                    return item;
                }
            }
        } else {
            Reporting.logUnexpected();
        }
        return object;
    }

    /**
     * The XML EDit button extension.
     */
    private class CodeXMLEditExtension
        implements
        JSimpleTreeBuilderExtensionButton {

        /**
         * {@inheritDoc}
         */
        @Override
        public void callback() {

            selectionChanged(
                codeBuilder.getSelectedItem(),
                codeBuilder.getSelectedRow());
            Code aCode = CodeEditorPSE.this.code;
            /* if (code != null) */

            String encoded = "";
            if (aCode != null) {
                encoded = ModelUtils.modelItemToString(aCode);
            }

            /* if (encoded != null) { */
            final JTextArea jta = new JTextArea();
            String varString = null;
            if (encoded != null) {
                varString = encoded;
            } else {
                varString = "";
            }
            jta.setText(varString);
            // CHECKSTYLE OFF: MagicNumber
            SwingUtils.COMPONENT.showDialog(
                new JScrollPane(
                    jta),
                640,
                480,
                true);
            // CHECKSTYLE ON: MagicNumber
            encoded = jta.getText();
            try {
                aCode = (Code) ModelUtils.modelItemFromString(encoded);
                if (aCode != null) {
                    CodeEditorPSE.this.code = aCode;
                    CodeEditorPSE.this.codeBuilder.rowChildrenChanged(0);
                }
            } catch (final Exception e) {
                Reporting.reportUnexpected("Could not convert xml into code");
            }
            // }

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getLabel() {
            return "View XML Source";
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ImageIcon getIcon() {
            return getContext().getResources().getImageIconResource(
                "images/scv.designer.codeToXml.png");
        }
    }

    /**
     * Sets the model - needed for xpath setting and whatnot.
     * @param aModel the new model - needed for xpath setting and whatnot
     */
    public void setModel(
        final Entity aModel) {
        model = aModel;
    }

    /**
     * Gets the context.
     * @return the context
     */
    public ClientContext getContext() {
        return context;
    }

    /**
     * Sets the context.
     * @param aContext the new context
     */
    public void setContext(
        final ClientContext aContext) {
        context = aContext;
    }
}
