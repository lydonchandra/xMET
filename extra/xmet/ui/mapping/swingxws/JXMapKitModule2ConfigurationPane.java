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
package xmet.ui.mapping.swingxws;

import java.awt.GridBagLayout;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import n.io.bin.Files;
import n.io.net.CharacterSetUtils;
import n.io.xml.JDOMXmlUtils;
import n.reporting.Reporting;
import n.ui.SwingUtils;
import n.ui.patterns.callback.Callback;

import org.jdom.Element;

import xmet.utils.PreferencesUtil;

/**
 * The ui window for this mapping module.
 * @author Nahid Akbar
 */
@SuppressWarnings({
"rawtypes",
"serial"
})
public final class JXMapKitModule2ConfigurationPane
    extends JPanel {

    /** The Constant URL_TEXT_FIELD_WIDTH. */
    private static final int URL_TEXT_FIELD_WIDTH = 40;

    /** The Constant DEFAULT_WMS_URL. */
    private static final String DEFAULT_WMS_URL =
        "http://www.mymaps.gov.au:80/geoserver/wms?SERVICE=WMS&";

    /** The Constant WINDOW_BORDER. */
    private static final int WINDOW_BORDER = 5;

    /** The Constant WINDOW_HEIGHT. */
    private static final int WINDOW_HEIGHT = 200;

    /** The Constant WINDOW_WIDTH. */
    private static final int WINDOW_WIDTH = 500;

    /** The type combo box. */
    private JComboBox typeComboBox;

    /** The base url text field. */
    private JTextField baseURLTextField;

    /** The layer combo box. */
    private JComboBox layerComboBox;

    /** The dialog. */
    private JDialog dialog;

    {
        typeComboBox = new JComboBox(
            new String[] {
                JXMapKitModule2.FACTORY_WMS
            });
        baseURLTextField = new JTextField(
            URL_TEXT_FIELD_WIDTH);
        layerComboBox = new JComboBox();
        layerComboBox.setEditable(true);
        setLayout(new GridBagLayout());
        SwingUtils.GridBag.add(
            this,
            new JLabel(
                "Tile Source:"),
            "x=0;y=0;");
        SwingUtils.GridBag.add(
            this,
            typeComboBox,
            "x=1;y=0;w=2;f=h;wx=1;");
        SwingUtils.GridBag.add(
            this,
            new JLabel(
                "Base URL:"),
            "x=0;y=1;");
        SwingUtils.GridBag.add(
            this,
            baseURLTextField,
            "x=1;y=1;f=h;wx=1;");
        SwingUtils.GridBag.add(
            this,
            SwingUtils.BUTTON.getNew(
                "Get WMS Layers",
                getGetLayersCallback()),
            "x=2;y=1");
        SwingUtils.GridBag.add(
            this,
            new JLabel(
                "Layer Name:"),
            "x=0;y=2;");
        SwingUtils.GridBag.add(
            this,
            layerComboBox,
            "x=1;y=2;f=h;wx=1;");
        SwingUtils.GridBag.add(
            this,
            SwingUtils.BUTTON.getNew(
                "Ok",
                getOkButtonCallback()),
            "x=2;y=3;");
        SwingUtils.GridBag.add(
            this,
            SwingUtils.BUTTON.getNew(
                "Cancel",
                new Callback() {

                    @Override
                    public void callback() {
                        dialog.setVisible(false);
                    }
                }),
            "x=0;y=3;");
        SwingUtils.GridBag.add(
            this,
            SwingUtils.BUTTON.getNew(
                "Default",
                new Callback() {

                    @Override
                    public void callback() {
                        typeComboBox
                            .setSelectedItem(JXMapKitModule2.FACTORY_WMS);
                        baseURLTextField.setText(DEFAULT_WMS_URL);
                        layerComboBox.setSelectedItem("gn:world");
                    }
                }),
            "x=2;y=2;");
        setBorder(BorderFactory.createEmptyBorder(
            WINDOW_BORDER,
            WINDOW_BORDER,
            WINDOW_BORDER,
            WINDOW_BORDER));
    }

    /**
     * Gets the gets the layers callback.
     * @return the gets the layers callback
     */
    private Callback getGetLayersCallback() {
        return new Callback() {

            @Override
            public void callback() {
                try {
                    String url = baseURLTextField.getText();
                    if (url != null) {
                        url = url
                            + "&version=1.1.1&request=GetCapabilities";
                        ByteBuffer contents;

                        contents = Files.downloadURLContents(new URL(
                            url));
                        final String contentsString =
                            CharacterSetUtils.decodeBuffer(
                                contents,
                                "UTF-8");
                        if (contentsString != null
                            && contentsString.length() > 0) {
                            final Element wmtMsCapabilities =
                                JDOMXmlUtils.elementFromXml(contentsString);
                            if (wmtMsCapabilities != null) {
                                final Element elemCapability =
                                    wmtMsCapabilities.getChild("Capability");
                                if (elemCapability != null) {
                                    final Element elemLayer =
                                        elemCapability.getChild("Layer");
                                    if (elemLayer != null) {
                                        final List layersEs =
                                            elemLayer.getChildren("Layer");
                                        for (final Object layerEo : layersEs) {
                                            final Element layerE =
                                                (Element) layerEo;
                                            if (layerE != null) {
                                                final Element elemName =
                                                    layerE.getChild("Name");
                                                if (elemName != null) {
                                                    layerComboBox
                                                        .addItem(elemName
                                                            .getTextTrim());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Reporting.reportUnexpected("URL Not specified");
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * Gets the ok button callback.
     * @return the ok button callback
     */
    private Callback getOkButtonCallback() {
        return new Callback() {

            @Override
            public void callback() {
                dialog.setVisible(false);
                final JXMapKitModule2Configuration configuration =
                    new JXMapKitModule2Configuration(
                        baseURLTextField.getText(),
                        (String) layerComboBox.getSelectedItem(),
                        (String) typeComboBox.getSelectedItem());
                final PreferencesUtil prefs = new PreferencesUtil(
                    "xmet.client.ui.mapping.swingxws");
                prefs.putObject(
                    "setting",
                    configuration);
            }
        };
    }

    /**
     * Instantiates a new jX map kit module2 configuration pane.
     */
    private JXMapKitModule2ConfigurationPane() {
        JXMapKitModule2Configuration configuration = null;
        try {
            final PreferencesUtil prefs = new PreferencesUtil(
                "xmet.client.ui.mapping.swingxws");
            configuration =
                (JXMapKitModule2Configuration) prefs.getObject("setting");
        } catch (final Exception e) {
            e.printStackTrace();
        }
        if (configuration == null) {
            configuration = new JXMapKitModule2Configuration(
                DEFAULT_WMS_URL,
                "gn:world",
                JXMapKitModule2.FACTORY_WMS);
        }
        typeComboBox.setSelectedItem(configuration.getType());
        baseURLTextField.setText(configuration.getBaseURL());
        layerComboBox.setSelectedItem(configuration.getLayer());

    }

    /**
     * Show configuration dialog.
     */
    public static void showConfigurationDialog() {
        (new JXMapKitModule2ConfigurationPane()).showDialog();
    }

    /**
     * Show dialog.
     */
    private void showDialog() {
        /* EventQueue.invokeLater(new Runnable() { */
        // @Override
        /* public void run() { */
        dialog = SwingUtils.DIALOG.createDialog(
            JXMapKitModule2ConfigurationPane.this,
            "Mapping Configuration",
            WINDOW_WIDTH,
            WINDOW_HEIGHT,
            true);
        dialog.pack();
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
        // }
        // });
    }

}
