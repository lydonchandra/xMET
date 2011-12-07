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

import n.reporting.Reporting;
import xmet.profiles.codecs.ContactsInformationCodec;
import xmet.profiles.contacts.ContactsInformation;
import xmet.profiles.contacts.ContactsInformation.PostalAddress;
import xmet.profiles.model.ElementDeclaration;
import xmet.profiles.model.Entity;
import xmet.profiles.model.ModelUtils;
import xmet.profiles.model.Settable;

/**
 * ISO19115 implementation of contacts codec.
 * @author Nahid Akbar
 */
public class ISO19115ContactsCodec
    implements
    ContactsInformationCodec {

    /**
     * {@inheritDoc}
     */
    @Override
    public ContactsInformation extractContactInformation(
        final Entity profileModelNode) {
        if (profileModelNode.getQualifiedName().equals(
            "gmd:CI_ResponsibleParty")) {
            final ElementDeclaration gmdCiResponsibleParty =
                ModelUtils.asElementDeclaration(profileModelNode);
            if (gmdCiResponsibleParty != null) {
                return extractGmdCiResponsibleParty(gmdCiResponsibleParty);
            } else {
                Reporting.reportUnexpected("gmdCiResponsibleParty not found");
            }
        } else {
            Reporting.logUnexpected("only gmdCiResponsibleParty is supported");
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertContactInformation(
        final ContactsInformation contactsInfo,
        final Entity profileModelNode) {
        if (contactsInfo != null) {
            if (profileModelNode.getQualifiedName().equals(
                "gmd:CI_ResponsibleParty")) {
                final ElementDeclaration gmdCiResponsibleParty =
                    ModelUtils.asElementDeclaration(profileModelNode);
                if (gmdCiResponsibleParty != null) {
                    insertgmdCiResponsibleParty(
                        contactsInfo,
                        gmdCiResponsibleParty);
                } else {
                    Reporting.reportUnexpected("gmd:EX_Extent not found");
                }
            } else {
                Reporting.logUnexpected("only gmd:extent is supported");
            }
        }
    }

    /**
     * Insertgmd$ c i_ responsible party.
     * @param contactsInformation the contacts information
     * @param gmdCiResponsibleParty the gmd$ci_ responsible party
     */
    private void insertgmdCiResponsibleParty(
        final ContactsInformation contactsInformation,
        final ElementDeclaration gmdCiResponsibleParty) {
        if (contactsInformation.getIndividualName() != null) {
            final Settable setable = ModelUtils.hardTraceSetableXpath(
                gmdCiResponsibleParty,
                "gmd:individualName/gco:CharacterString");
            if (setable != null) {
                setable.setValue(contactsInformation.getIndividualName());
            }
        }
        if (contactsInformation.getOrganizationName() != null) {
            final Settable setable = ModelUtils.hardTraceSetableXpath(
                gmdCiResponsibleParty,
                "gmd:organisationName/gco:CharacterString");
            if (setable != null) {
                setable.setValue(contactsInformation.getOrganizationName());
            }
        }
        if (contactsInformation.getPositionName() != null) {
            final Settable setable = ModelUtils.hardTraceSetableXpath(
                gmdCiResponsibleParty,
                "gmd:positionName/gco:CharacterString");
            if (setable != null) {
                setable.setValue(contactsInformation.getPositionName());
            }
        }
        if (contactsInformation.getPhone() != null) {
            final Settable setable = ModelUtils.hardTraceSetableXpath(
                gmdCiResponsibleParty,
                "gmd:contactInfo"
                    + "/gmd:CI_Contact"
                    + "/gmd:phone"
                    + "/gmd:CI_Telephone"
                    + "/gmd:voice"
                    + "/gco:CharacterString");
            if (setable != null) {
                setable.setValue(contactsInformation.getPhone());
            }
        }

        if (contactsInformation.getFacimile() != null) {
            final Settable setable = ModelUtils.hardTraceSetableXpath(
                gmdCiResponsibleParty,
                "gmd:contactInfo"
                    + "/gmd:CI_Contact"
                    + "/gmd:phone"
                    + "/gmd:CI_Telephone"
                    + "/gmd:facsimile"
                    + "/gco:CharacterString");
            if (setable != null) {
                setable.setValue(contactsInformation.getFacimile());
            }
        }

        insertPostalAddress(
            contactsInformation,
            gmdCiResponsibleParty);
        if (contactsInformation.getEmail() != null) {
            final Settable setable = ModelUtils.hardTraceSetableXpath(
                gmdCiResponsibleParty,
                "gmd:contactInfo"
                    + "/gmd:CI_Contact"
                    + "/gmd:address"
                    + "/gmd:CI_Address"
                    + "/gmd:electronicMailAddress"
                    + "/gco:CharacterString");
            if (setable != null) {
                setable.setValue(contactsInformation.getEmail());
            }
        }
        insertOnlineAddress(
            contactsInformation,
            gmdCiResponsibleParty);
        if (contactsInformation.getServiceHours() != null) {
            final Settable setable = ModelUtils.hardTraceSetableXpath(
                gmdCiResponsibleParty,
                "gmd:contactInfo"
                    + "/gmd:CI_Contact"
                    + "/gmd:hoursOfService"
                    + "/gco:CharacterString");
            if (setable != null) {
                setable.setValue(contactsInformation.getServiceHours());
            }
        }
        if (contactsInformation.getContactInstructions() != null) {
            final Settable setable = ModelUtils.hardTraceSetableXpath(
                gmdCiResponsibleParty,
                "gmd:contactInfo"
                    + "/gmd:CI_Contact"
                    + "/gmd:contactInstructions"
                    + "/gco:CharacterString");
            if (setable != null) {
                setable.setValue(contactsInformation.getContactInstructions());
            }
        }
    }

    /**
     * Insert online address.
     * @param contactsInfo the contacts information
     * @param gmdCiResponsibleParty the gmd ci responsible party
     */
    private void insertOnlineAddress(
        final ContactsInformation contactsInfo,
        final ElementDeclaration gmdCiResponsibleParty) {
        if ((contactsInfo.getOnlineAddresses() != null)
            && (contactsInfo.getOnlineAddresses().getUri() != null)) {
            final Settable setable = ModelUtils.hardTraceSetableXpath(
                gmdCiResponsibleParty,
                "gmd:contactInfo"
                    + "/gmd:CI_Contact"
                    + "/gmd:onlineResource"
                    + "/gmd:CI_OnlineResource"
                    + "/gmd:linkage"
                    + "/gmd:URL");
            if (setable != null) {
                setable.setValue(contactsInfo.getOnlineAddresses().getUri());
            }
        }
        if ((contactsInfo.getOnlineAddresses() != null)
            && (contactsInfo.getOnlineAddresses().getDescription() != null)) {
            final Settable setable = ModelUtils.hardTraceSetableXpath(
                gmdCiResponsibleParty,
                "gmd:contactInfo"
                    + "/gmd:CI_Contact"
                    + "/gmd:onlineResource"
                    + "/gmd:CI_OnlineResource"
                    + "/gmd:description"
                    + "/gco:CharacterString");
            if (setable != null) {
                setable.setValue(contactsInfo
                    .getOnlineAddresses()
                    .getDescription());
            }
        }
        if ((contactsInfo.getOnlineAddresses() != null)
            && (contactsInfo.getOnlineAddresses().getFunction() != null)) {
            final Settable setable = ModelUtils.hardTraceSetableXpath(
                gmdCiResponsibleParty,
                "gmd:contactInfo"
                    + "/gmd:CI_Contact"
                    + "/gmd:onlineResource"
                    + "/gmd:CI_OnlineResource"
                    + "/gmd:function"
                    + "/gmd:CI_OnLineFunctionCode");
            if (setable != null) {
                setable.setValue(contactsInfo
                    .getOnlineAddresses()
                    .getFunction());
            }
        }
    }

    /**
     * Insert postal address.
     * @param contactsInfo the contacts information
     * @param gmdCiResponsibleParty the gmd ci responsible party
     */
    private void insertPostalAddress(
        final ContactsInformation contactsInfo,
        final ElementDeclaration gmdCiResponsibleParty) {
        final PostalAddress varPostalAddresses =
            contactsInfo.getPostalAddresses();
        if ((varPostalAddresses != null)
            && (varPostalAddresses.getAddress1() != null)) {
            final Settable setable = ModelUtils.hardTraceSetableXpath(
                gmdCiResponsibleParty,
                "gmd:contactInfo"
                    + "/gmd:CI_Contact"
                    + "/gmd:address"
                    + "/gmd:CI_Address"
                    + "/gmd:deliveryPoint"
                    + "/gco:CharacterString");
            if (setable != null) {
                setable.setValue(varPostalAddresses.getAddress1());
            }
        }

        if ((varPostalAddresses != null)
            && (varPostalAddresses.getCity() != null)) {
            final Settable setable = ModelUtils.hardTraceSetableXpath(
                gmdCiResponsibleParty,
                "gmd:contactInfo"
                    + "/gmd:CI_Contact"
                    + "/gmd:address"
                    + "/gmd:CI_Address"
                    + "/gmd:city"
                    + "/gco:CharacterString");
            if (setable != null) {
                setable.setValue(varPostalAddresses.getCity());
            }
        }

        if ((varPostalAddresses != null)
            && (varPostalAddresses.getAdministrativeArea() != null)) {
            final Settable setable = ModelUtils.hardTraceSetableXpath(
                gmdCiResponsibleParty,
                "gmd:contactInfo"
                    + "/gmd:CI_Contact"
                    + "/gmd:address"
                    + "/gmd:CI_Address"
                    + "/gmd:administrativeArea"
                    + "/gco:CharacterString");
            if (setable != null) {
                setable.setValue(varPostalAddresses.getAdministrativeArea());
            }
        }

        if ((varPostalAddresses != null)
            && (varPostalAddresses.getPostCode() != null)) {
            final Settable setable = ModelUtils.hardTraceSetableXpath(
                gmdCiResponsibleParty,
                "gmd:contactInfo"
                    + "/gmd:CI_Contact"
                    + "/gmd:address"
                    + "/gmd:CI_Address"
                    + "/gmd:postalCode"
                    + "/gco:CharacterString");
            if (setable != null) {
                setable.setValue(varPostalAddresses.getPostCode());
            }
        }
        if ((varPostalAddresses != null)
            && (varPostalAddresses.getCountry() != null)) {
            final Settable setable = ModelUtils.hardTraceSetableXpath(
                gmdCiResponsibleParty,
                "gmd:contactInfo"
                    + "/gmd:CI_Contact"
                    + "/gmd:address"
                    + "/gmd:CI_Address"
                    + "/gmd:country"
                    + "/gco:CharacterString");
            if (setable != null) {
                setable.setValue(varPostalAddresses.getCountry());
            }
        }
    }

    /**
     * Extractgmd$ c i_ responsible party.
     * @param gmdCiResponsibleParty the gmd$ci_ responsible party
     * @return the contacts information
     */
    private ContactsInformation extractGmdCiResponsibleParty(
        final ElementDeclaration gmdCiResponsibleParty) {
        final ContactsInformation ci = new ContactsInformation();
        // {
        final Settable setable51 = ModelUtils.softTraceSetableXpath(
            gmdCiResponsibleParty,
            "gmd:individualName/gco:CharacterString");
        if (setable51 != null) {
            ci.setIndividualName(setable51.getValue());
        }
        // }
        // {
        final Settable setable49 = ModelUtils.softTraceSetableXpath(
            gmdCiResponsibleParty,
            "gmd:organisationName/gco:CharacterString");
        if (setable49 != null) {
            ci.setOrganizationName(setable49.getValue());
        }
        // }
        // {
        final Settable setable58 = ModelUtils.softTraceSetableXpath(
            gmdCiResponsibleParty,
            "gmd:positionName/gco:CharacterString");
        if (setable58 != null) {
            ci.setPositionName(setable58.getValue());
        }
        // }
        // {
        final Settable setable47 = ModelUtils.softTraceSetableXpath(
            gmdCiResponsibleParty,
            "gmd:contactInfo"
                + "/gmd:CI_Contact"
                + "/gmd:phone"
                + "/gmd:CI_Telephone"
                + "/gmd:voice"
                + "/gco:CharacterString");
        if (setable47 != null) {
            ci.setPhone(setable47.getValue());
        }
        // }
        //
        // {
        final Settable setable46 = ModelUtils.softTraceSetableXpath(
            gmdCiResponsibleParty,
            "gmd:contactInfo"
                + "/gmd:CI_Contact"
                + "/gmd:phone"
                + "/gmd:CI_Telephone"
                + "/gmd:facsimile"
                + "/gco:CharacterString");
        if (setable46 != null) {
            ci.setFacimile(setable46.getValue());
        }
        // }
        //
        // {
        extractPostalAddress(
            gmdCiResponsibleParty,
            ci);
        // }
        // {
        final Settable setable52 = ModelUtils.softTraceSetableXpath(
            gmdCiResponsibleParty,
            "gmd:contactInfo"
                + "/gmd:CI_Contact"
                + "/gmd:address"
                + "/gmd:CI_Address"
                + "/gmd:electronicMailAddress"
                + "/gco:CharacterString");
        if (setable52 != null) {
            ci.setEmail(setable52.getValue());
        }
        // }
        // {
        extractOnlineAddress(
            gmdCiResponsibleParty,
            ci);
        // }
        // {
        final Settable setable4 = ModelUtils.softTraceSetableXpath(
            gmdCiResponsibleParty,
            "gmd:contactInfo"
                + "/gmd:CI_Contact"
                + "/gmd:hoursOfService"
                + "/gco:CharacterString");
        if (setable4 != null) {
            ci.setServiceHours(setable4.getValue());
        }
        // }
        // {
        final Settable setable5 = ModelUtils.softTraceSetableXpath(
            gmdCiResponsibleParty,
            "gmd:contactInfo"
                + "/gmd:CI_Contact"
                + "/gmd:contactInstructions"
                + "/gco:CharacterString");
        if (setable5 != null) {
            ci.setContactInstructions(setable5.getValue());
        }
        // }
        return ci;
    }

    /**
     * Extract online address.
     * @param gmdCiResponsibleParty the gmd ci responsible party
     * @param ci the ci
     */
    private void extractOnlineAddress(
        final ElementDeclaration gmdCiResponsibleParty,
        final ContactsInformation ci) {
        final Settable setable = ModelUtils.softTraceSetableXpath(
            gmdCiResponsibleParty,
            "gmd:contactInfo"
                + "/gmd:CI_Contact"
                + "/gmd:onlineResource"
                + "/gmd:CI_OnlineResource"
                + "/gmd:linkage"
                + "/gmd:URL");
        if (setable != null) {
            if (ci.getOnlineAddresses() == null) {
                ci.setOnlineAddresses(new ContactsInformation.OnlineAddress());
            }
            ci.getOnlineAddresses().setUri(
                setable.getValue());

        }
        // }
        // {
        final Settable setable2 = ModelUtils.softTraceSetableXpath(
            gmdCiResponsibleParty,
            "gmd:contactInfo"
                + "/gmd:CI_Contact"
                + "/gmd:onlineResource"
                + "/gmd:CI_OnlineResource"
                + "/gmd:description"
                + "/gco:CharacterString");
        if (setable2 != null) {
            if (ci.getOnlineAddresses() == null) {
                ci.setOnlineAddresses(new ContactsInformation.OnlineAddress());
            }
            ci.getOnlineAddresses().setDescription(
                setable2.getValue());
        }
        // }
        // {
        final Settable setable3 = ModelUtils.softTraceSetableXpath(
            gmdCiResponsibleParty,
            "gmd:contactInfo"
                + "/gmd:CI_Contact"
                + "/gmd:onlineResource"
                + "/gmd:CI_OnlineResource"
                + "/gmd:function"
                + "/gmd:CI_OnLineFunctionCode");
        if (setable3 != null) {
            if (ci.getOnlineAddresses() == null) {
                ci.setOnlineAddresses(new ContactsInformation.OnlineAddress());
            }
            ci.getOnlineAddresses().setFunction(
                setable3.getValue());
        }
    }

    /**
     * Extract postal address.
     * @param gmdCiResponsibleParty the gmd ci responsible party
     * @param ci the ci
     */
    private void extractPostalAddress(
        final ElementDeclaration gmdCiResponsibleParty,
        final ContactsInformation ci) {
        final Settable setable45 = ModelUtils.softTraceSetableXpath(
            gmdCiResponsibleParty,
            "gmd:contactInfo"
                + "/gmd:CI_Contact"
                + "/gmd:address"
                + "/gmd:CI_Address"
                + "/gmd:deliveryPoint"
                + "/gco:CharacterString");
        if (setable45 != null) {
            if (ci.getPostalAddresses() == null) {
                ci.setPostalAddresses(new ContactsInformation.PostalAddress());
            }
            ci.getPostalAddresses().setAddress1(
                setable45.getValue());
        }
        // }
        //
        // {
        final Settable setable23 = ModelUtils.softTraceSetableXpath(
            gmdCiResponsibleParty,
            "gmd:contactInfo"
                + "/gmd:CI_Contact"
                + "/gmd:address"
                + "/gmd:CI_Address"
                + "/gmd:city"
                + "/gco:CharacterString");
        if (setable23 != null) {
            if (ci.getPostalAddresses() == null) {
                ci.setPostalAddresses(new ContactsInformation.PostalAddress());
            }
            ci.getPostalAddresses().setCity(
                setable23.getValue());
        }
        // }
        //
        // {
        final Settable setable19 = ModelUtils.softTraceSetableXpath(
            gmdCiResponsibleParty,
            "gmd:contactInfo"
                + "/gmd:CI_Contact"
                + "/gmd:address"
                + "/gmd:CI_Address"
                + "/gmd:administrativeArea"
                + "/gco:CharacterString");
        if (setable19 != null) {
            if (ci.getPostalAddresses() == null) {
                ci.setPostalAddresses(new ContactsInformation.PostalAddress());
            }
            ci.getPostalAddresses().setAdministrativeArea(
                setable19.getValue());
        }
        // }
        //
        // {
        final Settable setable16 = ModelUtils.softTraceSetableXpath(
            gmdCiResponsibleParty,
            "gmd:contactInfo"
                + "/gmd:CI_Contact"
                + "/gmd:address"
                + "/gmd:CI_Address"
                + "/gmd:postalCode"
                + "/gco:CharacterString");
        if (setable16 != null) {
            if (ci.getPostalAddresses() == null) {
                ci.setPostalAddresses(new ContactsInformation.PostalAddress());
            }
            ci.getPostalAddresses().setPostCode(
                setable16.getValue());
        }
        // }
        // {
        final Settable setable66 = ModelUtils.softTraceSetableXpath(
            gmdCiResponsibleParty,
            "gmd:contactInfo"
                + "/gmd:CI_Contact"
                + "/gmd:address"
                + "/gmd:CI_Address"
                + "/gmd:country"
                + "/gco:CharacterString");
        if (setable66 != null) {
            if (ci.getPostalAddresses() == null) {
                ci.setPostalAddresses(new ContactsInformation.PostalAddress());
            }
            ci.getPostalAddresses().setCountry(
                setable66.getValue());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "iso19115";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSupportedNodeNames() {
        return "gmd:CI_ResponsibleParty";
    }

}
