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
package xmet.profiles.codecs.iso19xxx;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.filter.ElementFilter;

/**
 * A CT_Catalogue. See figure 10 of ISO 19139:2008
 * @author Nahid Akbar
 */
@SuppressWarnings("rawtypes")
public class CtCatalogue {

    /** The name. */
    private String name;

    /** The scope. */
    private ArrayList<String> scope = new ArrayList<String>();

    /** The field of application. */
    private ArrayList<String> fieldOfApplication = new ArrayList<String>();

    /** The version number. */
    private String versionNumber;

    /** The version date. */
    private String versionDate;

    /** The language. */
    private String language = null;

    /** The character set. */
    private String characterSet = null;

    /** The locale. */
    private ArrayList<String> locale = new ArrayList<String>();

    /**
     * Instantiates a new c t_ catalogue.
     * @param element the element
     */
    public CtCatalogue(
        final Element element) {
        final Element nameElement = element.getChild(
            "name",
            ISO19139CodelistCodec.getGmxNamespace());
        final List scopeElement = element.getContent(new ElementFilter(
            "scope",
            ISO19139CodelistCodec.getGmxNamespace()));
        final List fieldOfApplicationElement =
            element.getContent(new ElementFilter(
                "fieldOfApplication",
                ISO19139CodelistCodec.getGmxNamespace()));
        final Element versionNumberElement = element.getChild(
            "versionNumber",
            ISO19139CodelistCodec.getGmxNamespace());
        final Element versionDateElement = element.getChild(
            "versionDate",
            ISO19139CodelistCodec.getGmxNamespace());
        final Namespace varGcoNamespace =
            ISO19139CodelistCodec.getGcoNamespace();
        if (nameElement != null) {
            name = nameElement.getChildTextTrim(
                "CharacterString",
                varGcoNamespace);
        }

        for (final Object scopeo : scopeElement) {
            scope.add(((Element) scopeo).getChildTextTrim(
                "CharacterString",
                varGcoNamespace));
        }
        for (final Object fieldOfApplicationo : fieldOfApplicationElement) {
            fieldOfApplication.add(((Element) fieldOfApplicationo)
                .getChildTextTrim(
                    "CharacterString",
                    varGcoNamespace));
        }
        if (versionNumberElement != null) {
            versionNumber = versionNumberElement.getChildTextTrim(
                "CharacterString",
                varGcoNamespace);
        }
        if (versionDateElement != null) {
            versionDate = versionDateElement.getChildTextTrim(
                "CharacterString",
                varGcoNamespace);
        }
        //
        /* if (name.equals("name")) { */
        /* setName(element.getTextContent()); */
        // } else if (name.equals("scope")) {
        /* getScope().add(element.getTextContent()); */
        // } else if (name.equals("fieldOfApplication")) {
        /* getFieldOfApplication().add(element.getTextContent()); */
        // } else if (name.equals("versionNumber")) {
        /* setVersionNumber(element.getTextContent()); */
        // } else if (name.equals("versionDate")) {
        /* setVersionDate(element.getTextContent()); */
        // }
    }

    /**
     * Gets the name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * @param aName the name to set
     */
    public void setName(
        final String aName) {
        this.name = aName;
    }

    /**
     * Gets the scope.
     * @return the scope
     */
    public ArrayList<String> getScope() {
        return scope;
    }

    /**
     * Sets the scope.
     * @param aScope the scope to set
     */
    public void setScope(
        final ArrayList<String> aScope) {
        this.scope = aScope;
    }

    /**
     * Gets the field of application.
     * @return the fieldOfApplication
     */
    public ArrayList<String> getFieldOfApplication() {
        return fieldOfApplication;
    }

    /**
     * Sets the field of application.
     * @param aFieldOfApplication the fieldOfApplication to set
     */
    public void setFieldOfApplication(
        final ArrayList<String> aFieldOfApplication) {
        this.fieldOfApplication = aFieldOfApplication;
    }

    /**
     * Gets the version number.
     * @return the versionNumber
     */
    public String getVersionNumber() {
        return versionNumber;
    }

    /**
     * Sets the version number.
     * @param aVersionNumber the versionNumber to set
     */
    public void setVersionNumber(
        final String aVersionNumber) {
        this.versionNumber = aVersionNumber;
    }

    /**
     * Gets the version date.
     * @return the versionDate
     */
    public String getVersionDate() {
        return versionDate;
    }

    /**
     * Sets the version date.
     * @param aVersionDate the versionDate to set
     */
    public void setVersionDate(
        final String aVersionDate) {
        this.versionDate = aVersionDate;
    }

    /**
     * Gets the language.
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the language.
     * @param aLanguage the language to set
     */
    public void setLanguage(
        final String aLanguage) {
        this.language = aLanguage;
    }

    /**
     * Gets the character set.
     * @return the characterSet
     */
    public String getCharacterSet() {
        return characterSet;
    }

    /**
     * Sets the character set.
     * @param aCharacterSet the characterSet to set
     */
    public void setCharacterSet(
        final String aCharacterSet) {
        this.characterSet = aCharacterSet;
    }

    /**
     * Gets the locale.
     * @return the locale
     */
    public ArrayList<String> getLocale() {
        return locale;
    }

    /**
     * Sets the locale.
     * @param aLocale the locale to set
     */
    public void setLocale(
        final ArrayList<String> aLocale) {
        this.locale = aLocale;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if (getName() != null) {
            return name;
        } else {
            return super.toString();
        }
    }

}
