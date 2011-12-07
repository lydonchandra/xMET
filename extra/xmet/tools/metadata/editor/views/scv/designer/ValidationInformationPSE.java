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
import java.awt.GridBagLayout;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import n.ui.SwingUtils;
import n.ui.patterns.propertySheet.PropertySheetEditor;
import n.ui.patterns.propertySheet.PropertySheetItem;
import xmet.tools.metadata.editor.views.scv.model.ValidationDataType;
import xmet.tools.metadata.editor.views.scv.model.ValidationInformation;

/**
 * PSE for editing the validation field of items;.
 * @author Nahid Akbar
 */
public class ValidationInformationPSE
    extends JPanel
    implements
    PropertySheetEditor {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The mandatory checkbox. */
    private transient JCheckBox mandatoryCheckbox;

    /** The pattern text field. */
    private transient JTextField patternTextField;

    /** The min length text field. */
    private transient JFormattedTextField minLengthTextField;

    /** The max length text field. */
    private transient JFormattedTextField maxLengthTextField;

    /** The type combo box. */
    private transient JComboBox typeComboBox;

    /** The depreciated checkbox. */
    private transient JCheckBox depreciatedCheckbox;

    {
        mandatoryCheckbox = new JCheckBox(
            "Mandatory");
        patternTextField = new JTextField();
        minLengthTextField = new JFormattedTextField(
            NumberFormat.getIntegerInstance());
        maxLengthTextField = new JFormattedTextField(
            NumberFormat.getIntegerInstance());
        typeComboBox = new JComboBox(
            ValidationDataType.values());
        depreciatedCheckbox = new JCheckBox(
            "Depreciated");

        setLayout(new GridBagLayout());
        SwingUtils.GridBag.add(
            this,
            new JLabel(
                "Value Type"),
            "w=rel;");
        SwingUtils.GridBag.add(
            this,
            typeComboBox,
            "w=rem;f=b;wx=1;");
        SwingUtils.GridBag.add(
            this,
            mandatoryCheckbox,
            "w=rem;f=b;wx=1;");
        SwingUtils.GridBag.add(
            this,
            new JLabel(
                "Minimum Length:"),
            "w=rel;");
        SwingUtils.GridBag.add(
            this,
            minLengthTextField,
            "w=rem;f=b;wx=1;");
        SwingUtils.GridBag.add(
            this,
            new JLabel(
                "Maximum Length:"),
            "w=rel;");
        SwingUtils.GridBag.add(
            this,
            maxLengthTextField,
            "w=rem;f=b;wx=1;");
        SwingUtils.GridBag.add(
            this,
            new JLabel(
                "Pattern:"),
            "w=rel;");
        SwingUtils.GridBag.add(
            this,
            patternTextField,
            "w=rem;f=b;wx=1;");
        SwingUtils.GridBag.add(
            this,
            depreciatedCheckbox,
            "w=rem;f=b;wx=1;");

        setBorder(BorderFactory.createLineBorder(getForeground()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getEditor(
        final Object value,
        final PropertySheetItem item) {
        if (value instanceof ValidationInformation) {
            final ValidationInformation validation =
                (ValidationInformation) value;
            ValidationDataType varValidationDataType = null;
            if (validation.getType() != null) {
                varValidationDataType = validation.getType();
            } else {
                varValidationDataType = ValidationDataType.STRING;
            }
            typeComboBox.setSelectedItem(varValidationDataType);
            mandatoryCheckbox.setSelected(validation.isMandatory());
            minLengthTextField.setText(Integer.toString(validation
                .getMinLength()));
            maxLengthTextField.setText(Integer.toString(validation
                .getMaxLength()));
            String varString = null;
            if (validation.getPattern() != null) {
                varString = validation.getPattern();
            } else {
                varString = "";
            }
            patternTextField.setText(varString);
            depreciatedCheckbox.setSelected(validation.isDepreciated());
        } else {
            typeComboBox.setSelectedIndex(-1);
            mandatoryCheckbox.setSelected(false);
            minLengthTextField.setText("");
            maxLengthTextField.setText("");
            patternTextField.setText("");
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValue() {
        final ValidationInformation validation = new ValidationInformation();
        try {
            validation.setMandatory(mandatoryCheckbox.isSelected());
        } catch (final Exception e) {
            e.printStackTrace();
        }
        try {
            validation.setPattern(patternTextField.getText());
        } catch (final Exception e) {
            e.printStackTrace();
        }
        try {
            validation.setMinLength(Integer.parseInt(minLengthTextField
                .getText()));
        } catch (final Exception e) {
            validation.setMinLength(ValidationInformation
                .getDefaultLengthConstraints());
        }
        try {
            validation.setMaxLength(Integer.parseInt(maxLengthTextField
                .getText()));
        } catch (final Exception e) {
            validation.setMaxLength(ValidationInformation
                .getDefaultLengthConstraints());
        }
        try {
            validation.setType((ValidationDataType) typeComboBox
                .getSelectedItem());
        } catch (final Exception e) {
            e.printStackTrace();
        }
        try {
            validation.setDepreciated(depreciatedCheckbox.isSelected());
        } catch (final Exception e) {
            e.printStackTrace();
        }
        if (validation.hasDefaultValues()) {
            return null;
        }
        return validation;
    }

}
