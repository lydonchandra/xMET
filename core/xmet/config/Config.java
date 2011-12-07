/******************************************************************************
 * xMET - eXtensible Metadata Editing Tool<br />
 * <br />
 * Copyright (C) 2010-2011 - Office Of Spatial Data Management<br />
 * <br />
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.<br />
 * <br />
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.<br />
 * <br />
 * For a copy of the GNU General Public License, see http://www.gnu.org/licenses
 ******************************************************************************/
package xmet.config;

import java.net.ProxySelector;
import java.util.HashMap;
import java.util.Map;

import n.io.net.AuthentificationProxySelector;
import n.io.net.ProxyInformation;
import n.io.xml.CSXMLSerializationCodec;
import n.reporting.Reporting;
import xmet.utils.PreferencesUtil;

/**
 * Central configuration. <br />
 * There are two types of configurations:<br />
 * <ul>
 * <li>Persistent configuration - either constants or settings which get saved
 * using the preferences API and is accessible across different instances. and
 * temporary ones which don't get saved and are
 * <li>Temporary configuration - Mainly, program parameters that are passed in
 * or inserted by another tool. They are not shared across program instances.
 * </ul>
 * Note: Persistent configuration settings in here relate to xMET framework and
 * common settings only. Tool specific configurations should be saved and used
 * from a separate place. See xmet.utils.PreferencesUtil for a helper class for
 * handing tool specific setting. Please also note that tool specific settings
 * should go under /xmet/[toolname e.g. "metatada.harvest"]/... and not directly
 * under /xmet node for preferences.
 * @author Nahid Akbar
 */
public final class Config {
    /* == Constants == */

    /** Constant setting - The application version. */
    private static final String APPLICATION_VERSION = "1.0";

    /** Constant setting - The application name. */
    private static final String APPLICATION_NAME =
        "xMET - eXtensible Metadata Editing Tool";

    /* == Setting objects== */
    /**
     * The persistent configuration settings.
     */
    private PersistentConfiguration persistentConfigurationSettings =
        new PersistentConfiguration();

    /**
     * The temporary configuration settings.<br />
     * Stored as a map. Configuration values are stored as string.
     */
    private Map<String, String> temporaryConfigurationSettings =
        new HashMap<String, String>();

    /* == Getters and setters for persistent configuration == */
    /**
     * Gets the title for the application window.
     * @return the title
     */
    public String getApplicationName() {
        return Config.APPLICATION_NAME;
    }

    /**
     * Gets the application version.
     * @return the version
     */
    public String getApplicationVersion() {
        return Config.APPLICATION_VERSION;
    }

    /**
     * Gets the local metadata directory.
     * @return the local metadata directory
     */
    public String getLocalMetadataDirectory() {
        return getPersistentConfig()
            .getLocalMetadataDirectory();
    }

    /**
     * Gets the proxy information.
     * @return the proxy information
     */
    public ProxyInformation getProxyInformation() {
        return getPersistentConfig()
            .getProxyInfo();
    }

    /**
     * Checks if it is to show toolbar labels.
     * @return true, if is show toolbar labels
     */
    public boolean isShowToolbarLabels() {
        return getPersistentConfig()
            .isShowToolbarLabels();
    }

    /**
     * Checks if it is to show toolbar icons.
     * @return true, if is show toolbar icons
     */
    public boolean isShowToolbarIcons() {
        return getPersistentConfig()
            .isShowToolbarIcons();
    }

    /**
     * Gets the toolbar icon width.
     * @return the toolbar icon width
     */
    public int getToolbarIconWidth() {
        return getPersistentConfig()
            .getToolbarIconWidth();
    }

    /**
     * Sets the local metadata directory.
     * @param localMetadataDirectory the new local metadata directory
     */
    public void setLocalMetadataDirectory(
        final String localMetadataDirectory) {
        getPersistentConfig()
            .setLocalMetadataDirectory(
                localMetadataDirectory);
    }

    /**
     * Sets the show toolbar icons.
     * @param value the new show toolbar icons
     */
    public void setShowToolbarIcons(
        final boolean value) {
        getPersistentConfig()
            .setShowToolbarIcons(
                value);
    }

    /**
     * Sets the show toolbar labels.
     * @param value the new show toolbar labels
     */
    public void setShowToolbarLabels(
        final boolean value) {
        getPersistentConfig()
            .setShowToolbarLabels(
                value);

    }

    /**
     * Sets the toolbar icon width.
     * @param value the new toolbar icon width
     */
    public void setToolbarIconWidth(
        final int value) {
        getPersistentConfig()
            .setToolbarIconWidth(
                value);
    }

    /* == Getters and setters for temporary configuration == */

    /**
     * Gets the temp setting.
     * @param key the key
     * @param defaultValue the default value
     * @return the temp setting
     */
    public String getTempSetting(
        final String key,
        final String defaultValue) {
        final String value = getTemporaryConfigurationSettings()
            .get(
                key);
        if (value != null) {
            return value;
        } else {
            return defaultValue;
        }
    }

    /* == Misc helper methods == */
    /**
     * Method that parses the program parameters to change some temporary
     * configuration paramaters at load time.
     * @param args the args
     */
    public void parseParams(
        final String[] args) {
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                if (args[i]
                    .length() > 2) {
                    if (args[i]
                        .startsWith("-")) {
                        final String param = args[i]
                            .substring(1);
                        if (param
                            .indexOf('=') > 0) {
                            final String key = param
                                .substring(
                                    0,
                                    param
                                        .indexOf('='));
                            final String value = param
                                .substring(param
                                    .indexOf('=') + 1);
                            getTemporaryConfigurationSettings()
                                .put(
                                    key,
                                    value);
                        } else {
                            getTemporaryConfigurationSettings()
                                .put(
                                    param,
                                    param);
                        }
                    }
                }
            }
        }
    }

    /* == Saving and loading == */
    /**
     * loads settings from preferences.
     */
    public void loadSettings() {
        final PreferencesUtil prefs = new PreferencesUtil(
            null);
        setPersistentConfig((PersistentConfiguration) CSXMLSerializationCodec
            .decodeClassesS(
                prefs
                    .get(
                        "config",
                        ""),
                PersistentConfiguration.class));
        if (getPersistentConfig() == null) {
            setPersistentConfig(new PersistentConfiguration());
        }
        if (getPersistentConfig()
            .getProxyInfo()
            .hasHTTPProxy()) {
            ProxySelector
                .setDefault(new AuthentificationProxySelector(
                    getPersistentConfig()
                        .getProxyInfo()));
        }
    }

    /**
     * Save settings from preferences.
     */
    public void saveSettings() {
        try {
            final String encoded = CSXMLSerializationCodec
                .encode(getPersistentConfig());
            final PreferencesUtil prefs = new PreferencesUtil(
                null);
            prefs
                .put(
                    "config",
                    encoded);
        } catch (final Exception e) {
            Reporting
                .reportUnexpected(e);
        }
        if (getPersistentConfig()
            .getProxyInfo()
            .hasHTTPProxy()) {
            ProxySelector
                .setDefault(new AuthentificationProxySelector(
                    getPersistentConfig()
                        .getProxyInfo()));
        }
    }

    /**
     * Gets the persistent configuration settings.
     * @return the persistent configuration settings
     */
    public PersistentConfiguration getPersistentConfig() {
        return persistentConfigurationSettings;
    }

    /**
     * Sets the persistent configuration settings.
     * @param persistentConfig the new persistent configuration settings
     */
    public void setPersistentConfig(
        final PersistentConfiguration persistentConfig) {
        this.persistentConfigurationSettings = persistentConfig;
    }

    /**
     * Gets the temporary configuration settings.
     * @return the temporary configuration settings
     */
    public Map<String, String> getTemporaryConfigurationSettings() {
        return temporaryConfigurationSettings;
    }

    /**
     * Sets the tempporary configuration settings.
     * @param aTempporaryConfigurationSettings the tempporary configuration
     *            settings
     */
    public void setTempporaryConfigurationSettings(
        final Map<String, String> aTempporaryConfigurationSettings) {
        this.temporaryConfigurationSettings = aTempporaryConfigurationSettings;
    }

}
