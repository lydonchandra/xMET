/******************************************************************************
 * xMet - eXtensible Metadata Editor Copyright (C) 2010-2011 - Office Of Spatial
 * Data Management This is a free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version. This software is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU Lesser General Public License for more details. For a copy of the GNU
 * General Public License, see http://www.gnu.org/licenses/
 ******************************************************************************/
package xmet.profiles;

import java.io.Serializable;

/**
 * A profile schema definitions.<br />
 * <br />
 * Consists of one or more of the following:
 * <ul>
 * <li>a namespace prefix
 * <li>a namespace URI
 * <li>a schema path (local)
 * <li>a schema URL (remote)
 * </ul>
 * @author Nahid Akbar
 */
public class ProfileSchema
    implements
    Serializable {

    /* == Attributes == */

    /** The namespace prefix. */
    private String namespacePrefix;

    /** The namespace uri. */
    private String namespaceUri;

    /** The schema path. */
    private String schemaPath;

    /** The schema url. */
    private String schemaUrl;

    /* == Constructors == */

    /**
     * Instantiates a new profile schema.
     * @param aNsPrefix the ns prefix
     * @param aNsUri the ns uri
     * @param aScPath the sc path
     * @param aScUrl the sc url
     */
    public ProfileSchema(
        final String aNsPrefix,
        final String aNsUri,
        final String aScPath,
        final String aScUrl) {
        super();
        this
            .setNamespacePrefix(aNsPrefix);
        this
            .setNamespaceUri(aNsUri);
        this
            .setSchemaPath(aScPath);
        this
            .setSchemaUrl(aScUrl);
    }

    /* == Getters and setters == */

    /**
     * Gets the namespace prefix.
     * @return the namespace prefix
     */
    public String getNamespacePrefix() {
        return namespacePrefix;
    }

    /**
     * Sets the namespace prefix.
     * @param aNamespacePrefix the new namespace prefix
     */
    public void setNamespacePrefix(
        final String aNamespacePrefix) {
        namespacePrefix = aNamespacePrefix;
    }

    /**
     * Gets the namespace uri.
     * @return the namespace uri
     */
    public String getNamespaceUri() {
        return namespaceUri;
    }

    /**
     * Sets the namespace uri.
     * @param aNamespaceUri the new namespace uri
     */
    public void setNamespaceUri(
        final String aNamespaceUri) {
        namespaceUri = aNamespaceUri;
    }

    /**
     * Gets the schema path.
     * @return the schema path
     */
    public String getSchemaPath() {
        return schemaPath;
    }

    /**
     * Sets the schema path.
     * @param aSchemaPath the new schema path
     */
    public void setSchemaPath(
        final String aSchemaPath) {
        schemaPath = aSchemaPath;
    }

    /**
     * Gets the schema url.
     * @return the schema url
     */
    public String getSchemaUrl() {
        return schemaUrl;
    }

    /**
     * Sets the schema url.
     * @param aSchemaUrl the new schema url
     */
    public void setSchemaUrl(
        final String aSchemaUrl) {
        schemaUrl = aSchemaUrl;
    }

    /* == Misc == */

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String
            .format(
                "[nsu=%1$s;nsp=%2$s;scp=%3$s;scu=%4$s]",
                namespaceUri,
                namespacePrefix,
                schemaPath,
                schemaUrl);
    }

    /* == Serializable == */
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

}
