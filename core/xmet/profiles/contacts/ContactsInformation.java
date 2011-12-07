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
package xmet.profiles.contacts;

import n.io.CS;
import n.io.CSC;
import n.ui.patterns.propertySheet.PSELabel;
import n.ui.patterns.propertySheet.UseBigStringPSE;

/**
 * xMET's Model of Contacts Information. Contains storage and property sheet
 * annotation information for easy use.
 * @author Nahid Akbar
 */
@CSC("ContactsInformation")
public class ContactsInformation {

    /** The reference id. */
    @CS
    private String referenceID;

    /** The individual name. */
    @CS
    @PSELabel("Name")
    private String individualName;

    /** The organization name. */
    @CS
    @PSELabel("Organization Name")
    private String organizationName;

    /** The position name. */
    @CS
    @PSELabel("Position Name")
    private String positionName;

    /** The phone. */
    @CS
    @PSELabel("Phone Number")
    private String phone;

    /** The fax. */
    @CS
    @PSELabel("Fax Number")
    private String fax;

    /** The email. */
    @CS
    @PSELabel("E-mail Address")
    private String email;

    /** The postal addresses. */
    @CSC
    private PostalAddress postalAddresses;

    /** The online addresses. */
    @CSC
    private OnlineAddress onlineAddresses;

    /** The service hours. */
    @CS
    @PSELabel("Service hours")
    @UseBigStringPSE
    private String serviceHours;

    /** The contact instructions. */
    @CS
    @PSELabel("Contact Instructions")
    @UseBigStringPSE
    private String contactInstructions;

    /**
     * Representation of physical address.
     */
    @CSC("PostalAddress")
    public static class PostalAddress {

        /** The address1. */
        @PSELabel("Street Address")
        @CS
        private String address1;

        /** The administrative area. */
        @CS
        @PSELabel("State/Administrative Area")
        private String administrativeArea;

        /** The post code. */
        @CS
        @PSELabel("PostCode")
        private String postCode;

        /** The city. */
        @CS
        @PSELabel("City")
        private String city;

        /** The country. */
        @CS
        @PSELabel("Country")
        private String country;

        /**
         * Gets the address1.
         * @return the address1
         */
        public String getAddress1() {
            return address1;
        }

        /**
         * Sets the address1.
         * @param aAddress1 the new address1
         */
        public void setAddress1(
            final String aAddress1) {
            this.address1 = aAddress1;
        }

        /**
         * Gets the administrative area.
         * @return the administrative area
         */
        public String getAdministrativeArea() {
            return administrativeArea;
        }

        /**
         * Sets the administrative area.
         * @param aAdministrativeArea the new administrative area
         */
        public void setAdministrativeArea(
            final String aAdministrativeArea) {
            this.administrativeArea = aAdministrativeArea;
        }

        /**
         * Gets the post code.
         * @return the post code
         */
        public String getPostCode() {
            return postCode;
        }

        /**
         * Sets the post code.
         * @param aPostCode the new post code
         */
        public void setPostCode(
            final String aPostCode) {
            this.postCode = aPostCode;
        }

        /**
         * Gets the city.
         * @return the city
         */
        public String getCity() {
            return city;
        }

        /**
         * Sets the city.
         * @param aCity the new city
         */
        public void setCity(
            final String aCity) {
            this.city = aCity;
        }

        /**
         * Gets the country.
         * @return the country
         */
        public String getCountry() {
            return country;
        }

        /**
         * Sets the country.
         * @param aCountry the new country
         */
        public void setCountry(
            final String aCountry) {
            this.country = aCountry;
        }

        /**
         * Checks if is empty.
         * @return true if has no contents
         */
        public boolean isEmpty() {
            return (getAddress1() == null || getAddress1().trim().length() == 0)
                && (getAdministrativeArea() == null || getAdministrativeArea()
                    .trim()
                    .length() == 0)
                && (getPostCode() == null || getPostCode().trim().length() == 0)
                && (getCity() == null || getCity().trim().length() == 0)
                && (getCountry() == null || getCountry().trim().length() == 0);
        }
    }

    /**
     * Online contact information.
     */
    @CSC("OnlineAddress")
    public static class OnlineAddress {

        /** The uri. */
        @CS
        @PSELabel("URI")
        private String uri;

        /** The description. */
        @CS
        @PSELabel("Desctiption")
        @UseBigStringPSE
        private String description;

        /** The function. */
        @CS
        @PSELabel("Function")
        private String function; /* e.g. download, information */

        /**
         * Gets the uri.
         * @return the uri
         */
        public String getUri() {
            return uri;
        }

        /**
         * Sets the uri.
         * @param aUri the new uri
         */
        public void setUri(
            final String aUri) {
            this.uri = aUri;
        }

        /**
         * Gets the description.
         * @return the description
         */
        public String getDescription() {
            return description;
        }

        /**
         * Sets the description.
         * @param aDescription the new description
         */
        public void setDescription(
            final String aDescription) {
            this.description = aDescription;
        }

        /**
         * Gets the function.
         * @return the function
         */
        public String getFunction() {
            return function;
        }

        /**
         * Sets the function.
         * @param aFunction the new function
         */
        public void setFunction(
            final String aFunction) {
            this.function = aFunction;
        }

        /**
         * Checks if is empty.
         * @return true if has no contents
         */
        public boolean isEmpty() {
            final boolean vNoUrlPresent = getUri() == null
                || getUri().trim().length() == 0;
            final boolean vNoDescriptionPresent = getDescription() == null
                || getDescription().trim().length() == 0;
            final boolean vNoFunctionPresent = getFunction() == null
                || getFunction().trim().length() == 0;
            return vNoUrlPresent
                && vNoDescriptionPresent
                && vNoFunctionPresent;
        }
    }

    /**
     * Gets the reference id.
     * @return the reference id
     */
    public String getReferenceID() {
        return referenceID;
    }

    /**
     * Sets the reference id.
     * @param aReferenceID the new reference id
     */
    public void setReferenceID(
        final String aReferenceID) {
        this.referenceID = aReferenceID;
    }

    /**
     * Gets the individual name.
     * @return the individual name
     */
    public String getIndividualName() {
        return individualName;
    }

    /**
     * Sets the individual name.
     * @param aIndividualName the new individual name
     */
    public void setIndividualName(
        final String aIndividualName) {
        this.individualName = aIndividualName;
    }

    /**
     * Gets the organization name.
     * @return the organization name
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * Sets the organization name.
     * @param aOrganizationName the new organization name
     */
    public void setOrganizationName(
        final String aOrganizationName) {
        this.organizationName = aOrganizationName;
    }

    /**
     * Gets the position name.
     * @return the position name
     */
    public String getPositionName() {
        return positionName;
    }

    /**
     * Sets the position name.
     * @param aPositionName the new position name
     */
    public void setPositionName(
        final String aPositionName) {
        this.positionName = aPositionName;
    }

    /**
     * Gets the phone.
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone.
     * @param aPhone the new phone
     */
    public void setPhone(
        final String aPhone) {
        this.phone = aPhone;
    }

    /**
     * Gets the fax.
     * @return the fax
     */
    public String getFax() {
        return fax;
    }

    /**
     * Sets the fax.
     * @param aFax the new fax
     */
    public void setFax(
        final String aFax) {
        this.fax = aFax;
    }

    /**
     * Gets the facimile.
     * @return the facimile
     */
    public String getFacimile() {
        return getFax();
    }

    /**
     * Sets the facimile.
     * @param facimile the new facimile
     */
    public void setFacimile(
        final String facimile) {
        setFax(facimile);
    }

    /**
     * Gets the email.
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     * @param aEmail the new email
     */
    public void setEmail(
        final String aEmail) {
        this.email = aEmail;
    }

    /**
     * Gets the postal addresses.
     * @return the postal addresses
     */
    public PostalAddress getPostalAddresses() {
        return postalAddresses;
    }

    /**
     * Sets the postal addresses.
     * @param aPostalAddresses the new postal addresses
     */
    public void setPostalAddresses(
        final PostalAddress aPostalAddresses) {
        this.postalAddresses = aPostalAddresses;
    }

    /**
     * Gets the online addresses.
     * @return the online addresses
     */
    public OnlineAddress getOnlineAddresses() {
        return onlineAddresses;
    }

    /**
     * Sets the online addresses.
     * @param aOnlineAddresses the new online addresses
     */
    public void setOnlineAddresses(
        final OnlineAddress aOnlineAddresses) {
        this.onlineAddresses = aOnlineAddresses;
    }

    /**
     * Gets the service hours.
     * @return the service hours
     */
    public String getServiceHours() {
        return serviceHours;
    }

    /**
     * Sets the service hours.
     * @param aServiceHours the new service hours
     */
    public void setServiceHours(
        final String aServiceHours) {
        this.serviceHours = aServiceHours;
    }

    /**
     * Gets the contact instructions.
     * @return the contact instructions
     */
    public String getContactInstructions() {
        return contactInstructions;
    }

    /**
     * Sets the contact instructions.
     * @param aContactInstructions the new contact instructions
     */
    public void setContactInstructions(
        final String aContactInstructions) {
        this.contactInstructions = aContactInstructions;
    }

    /**
     * Checks if is empty.
     * @return true if has no contents
     */
    public boolean isEmpty() {
        final String varOrgName = getOrganizationName();
        final boolean vNoOrgPresent = varOrgName == null
            || varOrgName.trim().length() == 0;
        final boolean vNoIndPresent = getIndividualName() == null
            || getIndividualName().trim().length() == 0;
        final boolean vNoPosPresent = getPositionName() == null
            || getPositionName().trim().length() == 0;
        final boolean vNoPhonePresent = getPhone() == null
            || getPhone().trim().length() == 0;
        final boolean vNoFaxPresent = getFax() == null
            || getFax().trim().length() == 0;
        final boolean vNoEmailPresent = getEmail() == null
            || getEmail().trim().length() == 0;
        final boolean vNoServPresent = getServiceHours() == null
            || getServiceHours().trim().length() == 0;
        final boolean vNoContIPresent = getContactInstructions() == null
            || getContactInstructions().trim().length() == 0;
        final boolean vNoPostalAddrPresent = (getPostalAddresses() == null)
            || getPostalAddresses().isEmpty();
        final boolean vNoOnlineAddrPresent = (getOnlineAddresses() == null)
            || getOnlineAddresses().isEmpty();
        return vNoIndPresent
            && vNoOrgPresent
            && vNoPosPresent
            && vNoPhonePresent
            && vNoFaxPresent
            && vNoEmailPresent
            && vNoServPresent
            && vNoContIPresent
            && vNoPostalAddrPresent
            && vNoOnlineAddrPresent;
    }

}
