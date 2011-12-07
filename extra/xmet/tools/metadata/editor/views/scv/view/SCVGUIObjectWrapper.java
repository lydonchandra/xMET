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

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Observer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import n.reporting.Reporting;
import n.ui.SwingUtils;
import n.ui.patterns.callback.ClassMethodCallback;
import xmet.ClientContext;
import xmet.profiles.contacts.ContactsInformation;
import xmet.profiles.geometry.SpatialExtent;
import xmet.profiles.keywords.KeywordsList;
import xmet.ui.controls.CompositeGUIObject;
import xmet.ui.controls.ContactInformationGUIObject;
import xmet.ui.controls.GUIObject;
import xmet.ui.controls.KeywordsListGUIObject;
import xmet.ui.controls.SpatialExtentGUIObject;

/**
 * This is a wrapper for GUI Objects for SCV that provides validation icon and
 * help button to gui objects.
 * @author Nahid Akbar
 */
@SuppressWarnings({
"serial",
"unchecked",
"rawtypes"
})
public class SCVGUIObjectWrapper
    extends JPanel {

    /** The gui object. */
    private final GUIObject guiObject;

    /**
     * Gets the gui object.
     * @return the gui object
     */
    public GUIObject getGuiObject() {
        return guiObject;
    }

    /** The client context. */
    private final ClientContext client;

    /**
     * Instantiates a new sCVGUI object wrapper.
     * @param aClient the client
     * @param aGuiObject the gui object
     */
    public SCVGUIObjectWrapper(
        final ClientContext aClient,
        final GUIObject aGuiObject) {
        this.client = aClient;
        this.guiObject = aGuiObject;
        label = new JLabel(
            getInvalidIcon(),
            SwingConstants.CENTER);
        rebuildPanel();
    }

    /** The label. */
    private final JLabel label;

    /**
     * Rebuild whole panel.
     */
    public void rebuildPanel() {
        removeAll();
        setLayout(new BorderLayout());
        add(guiObject);
        add(
            label,
            BorderLayout.EAST);
        if ((helpContextID != null)
            && (helpContextID.length() > 0)) {
            add(
                getHelpButton(),
                BorderLayout.EAST);
        }
        updatePanel();
    }

    /**
     * Just updates the icons and status.
     */
    public void updatePanel() {
        if (validationErrorMessage != null) {
            label.setToolTipText(validationErrorMessage);
            label.setVisible(true);
        } else {
            label.setVisible(false);
        }
        revalidate();
        /* can not repaint here because it gets rid of focus */
    }

    /* == Validation error message == */
    /**
     * The validation error message - if set to anything orther than null, the
     * wrapper will display invalid icon.
     */
    private String validationErrorMessage = null;

    /**
     * Sets the validation error message.
     * @param aValidationErrorMessage the new validation error message
     */
    public void setValidationErrorMessage(
        final String aValidationErrorMessage) {
        this.validationErrorMessage = aValidationErrorMessage;
    }

    /* == help context id for help button == */

    /** The help context id. */
    private String helpContextID;

    /**
     * Sets the help context id.
     * @param aHelpContextID the new help context id
     */
    public void setHelpContextID(
        final String aHelpContextID) {
        this.helpContextID = aHelpContextID;
    }

    /* == misc: Icons and helper methods == */
    /** The help icon. */
    private static ImageIcon helpIcon = null;

    /**
     * Gets the help button.
     * @return the help button
     */
    private Component getHelpButton() {
        if (helpIcon == null) {
            helpIcon = client.getResources().getImageIconResource(
                "images/scv.helpIcon.png");
        }
        final Object[] params = {
            helpContextID
        };
        return SwingUtils.BUTTON.getNew(
            helpIcon,
            new ClassMethodCallback(
                this,
                "helpCallback",
                params));
    }

    /**
     * Help button callback.
     * @param context the context
     */
    public void helpCallback(
        final String context) {
        client.getTools().invokeToolByName(
            "application.profileHelp",
            "url",
            context);
    }

    /** The invalid icon. */
    private static ImageIcon invalidIcon = null;

    /**
     * Gets the invalid icon.
     * @return the invalid icon
     */
    private Icon getInvalidIcon() {
        if (invalidIcon == null) {
            invalidIcon = client.getResources().getImageIconResource(
                "images/scv.invalidIcon.png");
        }
        return invalidIcon;
    }

    /* == GUIObject Deligate Methods == */
    /**
     * Adds the observer.
     * @param o the o
     */
    public void addObserver(
        final Observer o) {
        guiObject.addObserver(o);
    }

    /**
     * Delete observer.
     * @param o the o
     */
    public void deleteObserver(
        final Observer o) {
        guiObject.deleteObserver(o);
    }

    /**
     * Delete observers.
     */
    public void deleteObservers() {
        guiObject.deleteObservers();
    }

    /**
     * Gets the value.
     * @return the value
     */
    public String getValue() {
        return guiObject.getValue();
    }

    /**
     * Sets the value.
     * @param value the new value
     */
    public void setValue(
        final String value) {
        guiObject.setValue(value);
    }

    /**
     * Checks if is sEGUI object.
     * @return true, if is sEGUI object
     */
    public boolean isSEGUIObject() {
        return guiObject instanceof SpatialExtentGUIObject;
    }

    /**
     * Sets the spatial extent.
     * @param extent the new spatial extent
     */
    public void setSpatialExtent(
        final SpatialExtent extent) {
        if (guiObject instanceof SpatialExtentGUIObject) {
            ((SpatialExtentGUIObject) guiObject).setSpatialExtent(extent);
        } else {
            Reporting
                .logUnexpected("GUI Object does not support spatial extencts");
        }
    }

    /**
     * Gets the spatial extent.
     * @return the spatial extent
     */
    public SpatialExtent getSpatialExtent() {
        if (guiObject instanceof SpatialExtentGUIObject) {
            return ((SpatialExtentGUIObject) guiObject).getSpatialExtent();
        } else {
            Reporting
                .logUnexpected("GUI Object does not support spatial extencts");
            return null;
        }
    }

    /**
     * Checks if is cIGUI object.
     * @return true, if is cIGUI object
     */
    public boolean isCIGUIObject() {
        return guiObject instanceof ContactInformationGUIObject;
    }

    /**
     * Checks if is kLGUI object.
     * @return true, if is kLGUI object
     */
    public boolean isKLGUIObject() {
        return guiObject instanceof KeywordsListGUIObject;
    }

    /**
     * Sets the contact information.
     * @param extent the new contact information
     */
    public void setContactInformation(
        final ContactsInformation extent) {
        if (guiObject instanceof ContactInformationGUIObject) {
            ((ContactInformationGUIObject) guiObject)
                .setContactInformation(extent);
        } else {
            Reporting.logUnexpected("GUI Object does not"
                + " support contact information");
        }
    }

    /**
     * Gets the contact information.
     * @return the contact information
     */
    public ContactsInformation getContactInformation() {
        if (guiObject instanceof ContactInformationGUIObject) {
            return ((ContactInformationGUIObject) guiObject)
                .getContactInformation();
        } else {
            Reporting.logUnexpected("GUI Object does not "
                + "support contact information");
            return null;
        }
    }

    /**
     * Checks if is composite control.
     * @return true, if is composite control
     */
    public boolean isCompositeControl() {
        return guiObject instanceof CompositeGUIObject;
    }

    /**
     * Sets the values.
     * @param values the new values
     */

    public void setValues(
        final Object[] values) {
        if (guiObject instanceof CompositeGUIObject) {
            ((CompositeGUIObject) guiObject).setValues(values);
        } else {
            Reporting.logUnexpected("GUI Object does not"
                + " support composite information");
        }
    }

    /**
     * Gets the values.
     * @return the values
     */
    public Object[] getValues() {
        if (guiObject instanceof CompositeGUIObject) {
            return ((CompositeGUIObject) guiObject).getValues();
        } else {
            Reporting.logUnexpected("GUI Object does not support"
                + " composite information");
            return new Object[0];
        }

    }

    /**
     * Checks if is present.
     * @return true, if is present
     */
    public boolean isPresent() {
        return guiObject.isPresent();
    }

    /**
     * Gets the keywords list.
     * @return the keywords list
     */
    public KeywordsList getKeywordsList() {
        if (guiObject instanceof KeywordsListGUIObject) {
            return ((KeywordsListGUIObject) guiObject).getKeywordsList();
        } else {
            Reporting.logUnexpected();
            return null;
        }

    }

    /**
     * Sets the keywords list.
     * @param keywordsListValue the new keywords list
     */
    public void setKeywordsList(
        final KeywordsList keywordsListValue) {
        if (guiObject instanceof KeywordsListGUIObject) {
            ((KeywordsListGUIObject) guiObject)
                .setKeywordsList(keywordsListValue);
        } else {
            Reporting.logUnexpected();
        }
    }

}
