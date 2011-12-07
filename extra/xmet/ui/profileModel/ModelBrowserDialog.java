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
package xmet.ui.profileModel;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import n.ui.SwingUtils;
import n.ui.SwingUtils.GridBag;
import n.ui.patterns.callback.ClassMethodCallback;
import xmet.profiles.model.ChoiceGroup;
import xmet.profiles.model.ElementAttribute;
import xmet.profiles.model.ElementDeclaration;
import xmet.profiles.model.Entity;
import xmet.profiles.model.ModelUtils;
import xmet.profiles.model.Optional;
import xmet.profiles.model.Repeated;
import xmet.profiles.model.Settable;

/**
 * A dialog that wraps a profile model control.
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
public class ModelBrowserDialog
    extends JDialog
    implements
    ModelBrowserSelectionUpdateListener,
    ActionListener {

    /* == Constants == */
    // CHECKSTYLE OFF: MagicNumber
    /** The Constant SELECT_SETABLE. */
    public static final int SELECT_SETABLE = 1 << 0;

    /** The Constant SELECT_NONSUBSTITUTABLE_ELEMENT_DECLARATION. */
    public static final int SELECT_NONSUBSTITUTABLE_ELEMENT_DECLARATION =
        1 << 1;

    /** The Constant SELECT_SUBSTITUTABLE_ELEMENT_DECLARATION. */
    public static final int SELECT_SUBSTITUTABLE_ELEMENT_DECLARATION = 1 << 2;

    /** The Constant SELECT_SETABLE_ELEMENT_DECLARATION. */
    public static final int SELECT_SETABLE_ELEMENT_DECLARATION = 1 << 3;

    /** The Constant SELECT_REPEATED. */
    public static final int SELECT_REPEATED = 1 << 4;

    /** The Constant SELECT_CHOICE. */
    public static final int SELECT_CHOICE = 1 << 5;

    /** The Constant SELECT_OPTIONAL. */
    public static final int SELECT_OPTIONAL = 1 << 6;

    /** The Constant SELECT_ATTRIBUTE. */
    public static final int SELECT_ATTRIBUTE = 1 << 7;

    /** The Constant SELECT_ELEMENT_DECLARATION. */
    public static final int SELECT_ELEMENT_DECLARATION =
        SELECT_NONSUBSTITUTABLE_ELEMENT_DECLARATION
            | SELECT_SUBSTITUTABLE_ELEMENT_DECLARATION;

    /** The Constant SELECT_ALL. */
    public static final int SELECT_ALL = -1;

    // CHECKSTYLE ON: MagicNumber

    /* == Properties == */

    /** The root. */
    private Entity root;

    /** The selection mask. */
    private int selectionMask = SELECT_ALL;

    /** The name selection mask. */
    private String nameSelectionMask;

    /** The browser control. */
    private ModelBrowserControl browserControl;

    /* UI stuff */

    /** The select button. */
    private JButton selectButton;

    /** The cancel button. */
    private JButton cancelButton;

    /** The xpath field. */
    private final JTextField xpathField = new JTextField();;

    /* == ModelBrowserSelectionUpdateListener Implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public void selectedValueChanged(
        final Object parent,
        final Object newValue) {
        boolean pass = false;
        final int mark = selectionMask;
        if ((newValue instanceof Settable)
            && ((mark & SELECT_SETABLE) != 0)) {
            pass = true;
        }
        if ((newValue instanceof ElementAttribute)
            && ((mark & SELECT_ATTRIBUTE) != 0)) {
            pass = true;
        }
        if ((newValue instanceof Repeated)
            && ((mark & SELECT_REPEATED) != 0)) {
            pass = true;
        }
        if ((newValue instanceof Optional)
            && ((mark & SELECT_OPTIONAL) != 0)) {
            pass = true;
        }
        if ((newValue instanceof ChoiceGroup)
            && ((mark & SELECT_CHOICE) != 0)) {
            pass = true;
        }
        if ((newValue instanceof ElementDeclaration)
            && ((ElementDeclaration) newValue).hasSubtitutables()
            && ((mark & SELECT_SUBSTITUTABLE_ELEMENT_DECLARATION) != 0)) {
            pass = true;
        }
        if ((newValue instanceof ElementDeclaration)
            && !((ElementDeclaration) newValue).hasSubtitutables()
            && ((mark & SELECT_NONSUBSTITUTABLE_ELEMENT_DECLARATION) != 0)) {
            pass = true;
        }

        if ((newValue instanceof ElementDeclaration)
            && (ModelUtils.getSetable((Entity) newValue) != null)
            && ((mark & SELECT_SETABLE_ELEMENT_DECLARATION) != 0)) {
            pass = true;
        }
        if (pass
            && (nameSelectionMask != null)
            && (newValue instanceof Entity)) {
            final String n = ((Entity) newValue).getQualifiedName();
            pass = nameSelectionMask.equals(n)
                || nameSelectionMask.contains(n
                    + "|")
                || nameSelectionMask.contains("|"
                    + n);
        }
        if (pass) {
            String xpath;
            if (newValue instanceof ChoiceGroup) {
                xpath =
                    ModelUtils.getPath(((ChoiceGroup) newValue).getParent());
            } else {
                xpath = ModelUtils.getPath((Entity) newValue);
            }
            xpathField.setText(xpath);
        } else {
            xpathField.setText(null);
        }
    }

    /* == ActionListener Implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(
        final ActionEvent arg0) {
        if (arg0.getSource() == selectButton) {
            if (getSelected().length() > 0) {
                setVisible(false);
            }
        } else {
            onClose();
        }
    }

    /**
     * On close.
     */
    public void onClose() {
        xpathField.setText("");
        setVisible(false);
    }

    /** The last scroll position. */
    private static int lastScrollPosition = 0;

    /**
     * Show selection dialog.
     */
    public void showSelectionDialog() {
        // CHECKSTYLE OFF: MagicNumber

        setTitle("Select an appropriate node...");

        final JPanel panel = GridBag.getNew();
        setLayout(new GridBagLayout());
        SwingUtils.GridBag.add(
            panel,
            getBrowserControl(),
            "x=0;y=0;w=2;f=b;wx=1;wy=1;i=0,0,10,0;");

        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "xPath:"),
            "x=0;y=1;");
        SwingUtils.GridBag.add(
            panel,
            xpathField,
            "x=1;y=1;f=h;wx=0.5;");

        final JPanel buttonsPanel = SwingUtils.BoxLayouts.getHorizontalPanel();
        buttonsPanel.add(Box.createHorizontalGlue());
        selectButton = new JButton(
            "Open");
        buttonsPanel.add(selectButton);
        cancelButton = new JButton(
            "Cancel");
        buttonsPanel.add(cancelButton);
        SwingUtils.GridBag.add(
            panel,
            buttonsPanel,
            "x=1;y=2;i=10,0,0,0;f=h;wx=1;");

        panel.setBorder(BorderFactory.createEmptyBorder(
            10,
            10,
            10,
            10));

        cancelButton.addActionListener(this);
        selectButton.addActionListener(this);
        /* fileTextField.addActionListener(this); */
        getBrowserControl().removeSelectionUpdateListener(
            this);
        getBrowserControl().addSelectionUpdateListener(
            this);

        setContentPane(panel);

        validate();

        SwingUtils.WINDOW.centreSizeWindow(
            this,
            50);

        setModal(true);

        getBrowserControl().setScrollPosition(
            getLastScrollPosition());

        setVisible(true);

        setLastScrollPosition(getBrowserControl().getScrollPosition());
        // CHECKSTYLE ON: MagicNumber
    }

    /* == Helper methods == */

    /**
     * Gets the browser control.
     * @return the browser control
     */
    public ModelBrowserControl getBrowserControl() {
        return browserControl;
    }

    /**
     * Sets the browser control.
     * @param aBrowserControl the new browser control
     */
    public void setBrowserControl(
        final ModelBrowserControl aBrowserControl) {
        this.browserControl = aBrowserControl;
        root = aBrowserControl.getModel();
    }

    /**
     * Gets the root.
     * @return the root
     */
    public Entity getRoot() {
        return root;
    }

    /**
     * Sets the root.
     * @param aRoot the new root
     */
    public void setRoot(
        final Entity aRoot) {
        setBrowserControl(new ModelBrowserControl(
            aRoot));
    }

    /**
     * Gets the selection mask.
     * @return the selection mask
     */
    public int getSelectionMask() {
        return selectionMask;
    }

    /**
     * Sets the selection mask.
     * @param aSelectionMask the new selection mask
     */
    public void setSelectionMask(
        final int aSelectionMask) {
        this.selectionMask = aSelectionMask;
    }

    /**
     * Gets the name selection mask.
     * @return the name selection mask
     */
    public String getNameSelectionMask() {
        return nameSelectionMask;
    }

    /**
     * Sets the name selection mask.
     * @param aNameSelectionMask the new name selection mask
     */
    public void setNameSelectionMask(
        final String aNameSelectionMask) {
        this.nameSelectionMask = aNameSelectionMask;
    }

    /**
     * Gets the selected.
     * @return the selected
     */
    public String getSelected() {
        return xpathField.getText();
    }

    /* == Constructors == */
    /**
     * Instantiates a new model browser dialog.
     */
    public ModelBrowserDialog() {
        super();
        final Object[] params = {};
        SwingUtils.WINDOW.addCloseButtonCallback(
            this,
            new ClassMethodCallback(
                this,
                "onClose",
                params));
    }

    /**
     * Gets the last scroll position.
     * @return the last scroll position
     */
    public static int getLastScrollPosition() {
        return lastScrollPosition;
    }

    /**
     * Sets the last scroll position.
     * @param aLastScrollPosition the new last scroll position
     */
    public static void setLastScrollPosition(
        final int aLastScrollPosition) {
        lastScrollPosition = aLastScrollPosition;
    }

}
