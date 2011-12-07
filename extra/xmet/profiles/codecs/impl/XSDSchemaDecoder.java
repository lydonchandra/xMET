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
package xmet.profiles.codecs.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import n.reporting.Reporting;
import xmet.ClientContext;
import xmet.profiles.ProfileSchema;
import xmet.profiles.codecs.DefaultCodec;
import xmet.profiles.codecs.ProfileCodec;
import xmet.profiles.codecs.ProfileDecodingFailedException;
import xmet.profiles.model.AllGroup;
import xmet.profiles.model.Any;
import xmet.profiles.model.ChoiceGroup;
import xmet.profiles.model.ElementDeclaration;
import xmet.profiles.model.Entity;
import xmet.profiles.model.FixedAttribute;
import xmet.profiles.model.Group;
import xmet.profiles.model.ImplicitGroup;
import xmet.profiles.model.List;
import xmet.profiles.model.ModelUtils;
import xmet.profiles.model.MultiplicityConstraints;
import xmet.profiles.model.Optional;
import xmet.profiles.model.Repeated;
import xmet.profiles.model.Restriction;
import xmet.profiles.model.SequenceGroup;
import xmet.profiles.model.Simple;
import xmet.profiles.model.SimpleAttribute;
import xmet.profiles.model.Substitutables;
import xmet.profiles.model.Union;

import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSContentType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSFacet;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSRestrictionSimpleType;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSSimpleType;
import com.sun.xml.xsom.XSTerm;
import com.sun.xml.xsom.XSType;
import com.sun.xml.xsom.XSUnionSimpleType;
import com.sun.xml.xsom.XSVariety;
import com.sun.xml.xsom.XmlString;

/**
 * Decodes XSDs into profile models.
 * @author Nahid Akbar
 */
@SuppressWarnings({
"rawtypes",
"serial"
})
public class XSDSchemaDecoder
    implements
    ProfileCodec,
    DefaultCodec {

    /*
     * Its stupidly non-obvious. Make sure you get a good understanding of the
     * real XSD model (the specification, not the w3c tutorial version) before
     * attempting to modify this shit. I had to come back to it a few times over
     * a few months and restructure it to get it to its present state.
     */

    /** The substitutions. */
    private Map<String, String> substitutions;

    /** The cache. */
    private final HashMap<String, ElementDeclaration> cache =
        new HashMap<String, ElementDeclaration>();

    /**
     * Extract model.
     * @param element the element
     * @return the entity
     */
    public synchronized Entity extractModel(
        final XSElementDecl element) {
        if (element != null) {
            final Entity e = extractElementDecl(
                null,
                element,
                true);
            return e;
        } else {
            return null;
        }
    }

    /**
     * Extracts and returns an element declaration.
     * @param parent the parent
     * @param element the element
     * @param processSubstitutables the process substitutables
     * @return the element declaration
     */
    @SuppressWarnings("deprecation")
    private ElementDeclaration extractElementDecl(
        final Entity parent,
        final XSElementDecl element,
        final boolean processSubstitutables) {

        final String name = getShortName(element);
        ElementDeclaration modelED = null;

        /* find existing to reuse - otherwise will continue to derive for */
        /* ever */
        if (parent != null) {
            modelED = cache.get(getUniqueName(
                name,
                element));
        }

        /* when not found */
        if (modelED == null) {

            modelED = new ElementDeclaration(
                parent,
                name,
                element.getTargetNamespace());

            /* process substitutables */
            if (processSubstitutables) {
                final XSElementDecl[] substitubles =
                    element.listSubstitutables();

                if (substitubles.length > 1) {
                    final Substitutables subs = new Substitutables();

                    for (int i = 0; i < substitubles.length; i++) {
                        final String n = getShortName(substitubles[i]);
                        if (!isAbstractName(n)
                            && !substitubles[i].isAbstract()) {
                            if (!n.equals(name)) {
                                subs.addSubstitutable(
                                    n,
                                    extractElementDecl(
                                        parent,
                                        substitubles[i],
                                        false));
                            } else {
                                subs.addSubstitutable(
                                    n,
                                    modelED);
                            }
                        }
                        // else {
                        // /* Reporting.log("Abstract " + n + */
                        // // " not added.");
                        // }
                    }
                    modelED.setSubtitutables(subs);
                }
            }

            /* store for re-use */
            if (parent != null) {
                cache.put(
                    getUniqueName(
                        name,
                        element),
                    modelED);
            }

            /* get rid of abstract elements - as Discussed with John */
            /* Hockaday */
            if (element.isAbstract()
                && modelED.hasSubtitutables()) {
                String subName = null;
                if (substitutions == null) {
                    subName = null;
                } else {
                    subName = substitutions.get(name);
                }
                final ElementDeclaration[] eds =
                    modelED.getSubstitutables().asEdList();
                if (subName != null) {
                    for (final ElementDeclaration e : eds) {
                        if (e.getQualifiedName().equals(
                            subName)) {
                            return e;
                        }
                    }
                }
                for (final ElementDeclaration e : eds) {
                    /* if (!isAbstract(e)) { */
                    return e;
                    // }
                }
            }

            /* resolve element contents. It is important that it is done */
            /* after storing for re-use because of chains of parent type */
            /* referencing in some of the iso schemas. Otherwise it'll be */
            /* resolving the type for ever. */
            processElementType(
                modelED,
                element.getType());

        }
        return modelED;
    }

    /**
     * processes an element declaration body.
     * @param parent the parent
     * @param type the type
     */
    private void processElementType(
        final ElementDeclaration parent,
        final XSType type) {

        /* Reporting.log("processElementType(%1$s, %2$s)",parent, */

        switch (type.getDerivationMethod()) {
        case XSType.EXTENSION:
            final XSType baseType = type.getBaseType();
            processElementType(
                parent,
                baseType);
            if (type.isComplexType()) {
                final XSComplexType complexType = type.asComplexType();
                parent.setGroup(ModelUtils.extendGroup(
                    parent.getGroup(),
                    processComplexContentType(
                        parent,
                        complexType.getExplicitContent())));
            } else {
                Reporting.logUnexpected();
            }
            processAttributes(
                parent,
                type.asComplexType());
            break;
        case XSType.RESTRICTION:
            /* sometimes complex types are formed with a simple type */
            /* as a basis and then some attribute declarations */
            if (type.isSimpleType()
                || (type.asSimpleType() != null)) {
                parent.setGroup(processSimpleContentType(
                    parent,
                    type.asSimpleType()));
            } else if (type.isComplexType()
                && (type.asComplexType() != null)) {
                parent.setGroup(processComplexContentType(
                    parent,
                    type.asComplexType().getContentType()));
            } else {
                Reporting.logUnexpected();
            }
            processAttributes(
                parent,
                type.asComplexType());
            break;
        case XSType.SUBSTITUTION:
            Reporting.logUnexpected();
            break;
        default:
            break;
        }
    }

    /**
     * Process simple content type.
     * @param parent the parent
     * @param simple the simple
     * @return the entity
     */
    private Entity processSimpleContentType(
        final ElementDeclaration parent,
        final XSSimpleType simple) {
        final Simple modelSimple = processSimpleType(simple);
        if (modelSimple != null) {
            final ImplicitGroup group = new ImplicitGroup(
                parent);
            group.getChildren().add(
                modelSimple);
            return group;
        }
        return null;
    }

    /**
     * Processes element with a complex type.
     * @param modelElement the model element
     * @param complexType the complex type
     * @return the entity
     */
    private Entity processComplexContentType(
        final ElementDeclaration modelElement,
        final XSContentType complexType) {
        /* Reporting.log("processComplexContentType(%1$s)", complexType); */
        if (complexType != null) {
            if (complexType.asParticle() != null) {
                return extractParticle(
                    modelElement,
                    complexType.asParticle());
            } else if (complexType.asEmpty() != null) {
                return null;
            } else {
                if (complexType.asSimpleType() != null) {
                    return processSimpleContentType(
                        modelElement,
                        complexType.asSimpleType());
                } else {
                    Reporting.reportUnexpected(modelElement
                        + " - "
                        + complexType);
                }
            }
        }
        return null;
    }

    /**
     * Extract particle.
     * @param parent the parent
     * @param pa the pa
     * @return the entity
     */
    private Entity extractParticle(
        final Entity parent,
        final XSParticle pa) {

        /* Reporting.log("extractParticle(%1$s,%2$s)", parent, */
        /* pa.getTerm()); */
        MultiplicityConstraints constraints;

        final Entity term = extractTerm(
            parent,
            pa.getTerm());

        if (term != null) {
            constraints = MultiplicityConstraints.getNew(
                pa.getMinOccurs(),
                pa.getMaxOccurs());
            ModelUtils.addConstraints(
                term,
                constraints);
            switch (constraints.getContainerType()) {
            case MultiplicityConstraints.NORMAL:
                return term;
            case MultiplicityConstraints.OPTIONAL:
                return new Optional(
                    parent,
                    term);
            case MultiplicityConstraints.REPEATED:
                final Repeated repeated = new Repeated(
                    parent,
                    term);
                // {
                if (ModelUtils.isChoiceGroup(term)) {
                    final ChoiceGroup cg = ModelUtils.asChoiceGroup(term);
                    if (cg != null) {
                        boolean allEds = true;
                        for (final Entity e : cg.getChildren()) {
                            allEds &= ModelUtils.isElementDeclaration(e);
                        }
                        if (allEds) {
                            final ArrayList<Entity> children = cg.getChildren();
                            if (children.size() > 0) {
                                final ElementDeclaration edMain =
                                    (ElementDeclaration) children.get(0);
                                /*
                                 * Reporting.log("%1$s",edMain.getQualifiedName
                                 * ());
                                 */
                                if (!edMain.hasSubtitutables()) {
                                    edMain
                                        .setSubtitutables(new Substitutables());
                                }
                                for (int i = 0; i < children.size(); i++) {
                                    edMain
                                        .getSubstitutables()
                                        .addSubstitutable(
                                            children.get(
                                                i).getQualifiedName(),
                                            (ElementDeclaration) children
                                                .get(i));
                                }
                                repeated.setBaseEntity(edMain);
                            }
                        }
                    }

                } else if (ModelUtils.isAny(term)) {
                    term.setParent(parent);
                    return term;
                }
                // }
                return repeated;
            default:
                Reporting.logUnexpected();
                break;
            }
        }
        return null;
    }

    /**
     * Extract term.
     * @param parent the parent
     * @param term the term
     * @return the entity
     */
    private Entity extractTerm(
        final Entity parent,
        final XSTerm term) {
        /* Reporting.log("extractTerm(%1$s,%2$s)", parent, term); */
        if (term.isElementDecl()) {
            return extractElementDecl(
                parent,
                term.asElementDecl(),
                true);
        } else if (term.isModelGroup()) {
            return extractModalGroup(
                parent,
                term.asModelGroup());
        } else if (term.isModelGroupDecl()) {
            return extractModalGroup(
                parent,
                term.asModelGroupDecl().getModelGroup());
        } else if (term.isWildcard()) {
            return new Any(
                parent,
                "any",
                parent.getNamespace());
        } else {
            Reporting.logUnexpected();
        }
        return null;
    }

    /**
     * Extract modal group.
     * @param parent the parent
     * @param modelGroup the model group
     * @return the group
     */
    private Group extractModalGroup(
        final Entity parent,
        final XSModelGroup modelGroup) {
        /* Reporting.log("extractModalGroup(%1$s,%2$s)", parent, */
        /* modelGroup); */
        Group group = null;
        switch (modelGroup.getCompositor()) {
        case ALL:
            group = new AllGroup(
                parent);
            break;
        case CHOICE:
            group = new ChoiceGroup(
                parent);
            break;
        default:
            group = new SequenceGroup(
                parent);
            break;
        }
        for (final XSParticle pa : modelGroup.getChildren()) {
            final Entity term = extractParticle(
                group,
                pa);
            if (term != null) {
                ModelUtils.groupAdd(
                    group,
                    term);
            }
        }
        return group;
    }

    /**
     * Process attributes.
     * @param ed the ed
     * @param type the type
     */
    private void processAttributes(
        final ElementDeclaration ed,
        final XSComplexType type) {
        if ((type != null)
            && (ed != null)) {
            if (type.getAttributeUses() != null) {
                for (final XSAttributeUse u : type.getAttributeUses()) {
                    XmlString vFixedValue = u.getFixedValue();
                    if (vFixedValue != null) {
                        Reporting.logExpected(
                            "%1$s",
                            vFixedValue);
                        ModelUtils.addAttribute(
                            ed,
                            new FixedAttribute(
                                ed,
                                getShortName(
                                    u.getDecl().getName(),
                                    u.getDecl().getTargetNamespace()),
                                vFixedValue.value,
                                u.getDecl().getTargetNamespace(),
                                u.isRequired()));
                    } else {
                        ModelUtils.addAttribute(
                            ed,
                            new SimpleAttribute(
                                ed,
                                getShortName(
                                    u.getDecl().getName(),
                                    u.getDecl().getTargetNamespace()),
                                null,
                                u.getDecl().getTargetNamespace(),
                                u.isRequired()));
                    }
                }
            }
        }
    }

    /* End For Additions for Simple Type Restrictions */

    /* Model Generation Stuff */
    /**
     * Process simple type.
     * @param simple the simple
     * @return the simple
     */
    private Simple processSimpleType(
        final XSSimpleType simple) {
        /* Handle primitive types straight away */
        if (simple.isPrimitive()) {
            final Class edClass = simplePrimitiveTypes.get(simple.getName());
            try {
                final Simple me = (Simple) edClass.newInstance();
                me.setQualifiedName(simple.getName());
                me.setType(simple.getName());
                return me;
            } catch (final Exception e) {
                Reporting.reportUnexpected();
                return null;
            }
        } else if (simple.isUnion()) {
            return processUnion(simple);
        } else if (simple.isList()) {
            return processList(simple);
        } else if (simple.isRestriction()) {
            return processRestriction((XSRestrictionSimpleType) simple);
        } else {
            Reporting.logExpected("-->"
                + simple.toString());
            if (simple.getVariety() == XSVariety.ATOMIC) {
                final Class edClass =
                    simplePrimitiveTypes.get(simple.getName());
                if (edClass != null) {
                    try {
                        final Simple me = (Simple) edClass.newInstance();
                        me.setQualifiedName(simple.getName());
                        return me;
                    } catch (final Exception e) {
                        Reporting.logUnexpected();
                    }
                } else {

                    if (simple.getName() == null) {
                        if (simple.asRestriction() != null) {
                            return processRestriction(simple.asRestriction());
                        } else {
                            Reporting.logUnexpected();
                            return new Simple(
                                null,
                                simple.getName(),
                                simple.getTargetNamespace());
                        }
                    } else if (simple.getName().equals(
                        "anySimpleType")) {
                        return new Simple(
                            null,
                            simple.getName(),
                            simple.getTargetNamespace());
                    } else {
                        if (simple.asRestriction() != null) {
                            return processRestriction(simple.asRestriction());
                        } else {
                            Reporting.logUnexpected();
                            /* return new SimpleType(ed, simple); */
                            return new Simple(
                                null,
                                simple.getName(),
                                simple.getTargetNamespace());
                        }
                        /* Reporting.unimplemented(); */
                        /* System.err.println(simple.getName() + " " */
                        // + ed.toString());
                        /* ed.getGroup().add(new Simple(ed, */
                        /* simple.getName())); */
                        // /*return new SimpleType(ed, simple); */
                    }
                }
            } else if (simple.getVariety() == XSVariety.LIST) {
                if (simple.asUnion() != null) {
                    /* return new UnionSimpleType(ed, simple); */
                    return processUnion(simple);
                } else if (simple.asList() != null) {
                    return processList(simple);
                } else if (simple.asRestriction() != null) {
                    return processRestriction(simple.asRestriction());
                } else if (simple.asEmpty() != null) {
                    Reporting.logUnexpected();
                }
                /* return new SimpleType(ed, simple); */
            } else if (simple.getVariety() == XSVariety.UNION) {
                Reporting.logUnexpected();
            } else {
                Reporting.logUnexpected();
                return new Simple(
                    null,
                    simple.getName(),
                    simple.getTargetNamespace());
            }
        }
        Reporting.reportUnexpected(simple.toString());
        return new Simple(
            null,
            simple.getName(),
            simple.getTargetNamespace());
    }

    /**
     * processes a list simple type.
     * @param simple the simple
     * @return the simple
     */
    private Simple processList(
        final XSSimpleType simple) {
        List list;
        list = new List(
            simple.getName(),
            simple.getTargetNamespace());
        list.setListType(processSimpleType(simple.asList().getItemType()));
        if (list.getValueType() == null) {
            list.setListType(new Simple(
                list,
                "string",
                null));
        }
        return list;
    }

    /**
     * processes an union simple element.
     * @param simple the simple
     * @return the simple
     */
    private Simple processUnion(
        final XSSimpleType simple) {
        Union u;
        u = new Union(
            simple.getName(),
            simple.getTargetNamespace());

        final XSUnionSimpleType ust = simple.asUnion();
        for (int i = 0; i < ust.getMemberSize(); i++) {
            final Simple s = processSimpleType(ust.getMember(i));
            if (s != null) {
                u.getChildren().add(
                    s);
            }
        }
        return u;
    }

    /**
     * Process restriction.
     * @param restriction the restriction
     * @return the simple
     */
    private Simple processRestriction(
        final XSRestrictionSimpleType restriction) {
        // final Class parentClass =
        // simplePrimitiveTypes.get(restriction.getBaseType().getName());
        final Simple base =
            processSimpleType(restriction.getBaseType().asSimpleType());
        try {
            final ArrayList<Restriction> restrictions =
                new ArrayList<Restriction>();
            final Collection<? extends XSFacet> facets =
                restriction.getDeclaredFacets();
            for (final XSFacet facet : facets) {
                if (facet.getName().equals(
                    XSFacet.FACET_LENGTH)) {
                    final Restriction rest = new Restriction.Length();
                    rest.setValue(facet.getValue().toString());
                    restrictions.add(rest);
                } else if (facet.getName().equals(
                    XSFacet.FACET_ENUMERATION)) {
                    final Restriction rest = new Restriction.Enumeration();
                    rest.setValue(facet.getValue().toString());
                    restrictions.add(rest);
                } else if (facet.getName().equals(
                    XSFacet.FACET_FRACTIONDIGITS)) {
                    final Restriction rest = new Restriction.FractionDigits();
                    rest.setValue(facet.getValue().toString());
                    restrictions.add(rest);
                } else if (facet.getName().equals(
                    XSFacet.FACET_MAXEXCLUSIVE)) {
                    final Restriction rest = new Restriction.MaxExclusive();
                    rest.setValue(facet.getValue().toString());
                    restrictions.add(rest);
                } else if (facet.getName().equals(
                    XSFacet.FACET_MAXINCLUSIVE)) {
                    final Restriction rest = new Restriction.MinInclusive();
                    rest.setValue(facet.getValue().toString());
                    restrictions.add(rest);
                } else if (facet.getName().equals(
                    XSFacet.FACET_MAXLENGTH)) {
                    final Restriction rest = new Restriction.MaxLength();
                    rest.setValue(facet.getValue().toString());
                    restrictions.add(rest);
                } else if (facet.getName().equals(
                    XSFacet.FACET_MINEXCLUSIVE)) {
                    final Restriction rest = new Restriction.MinExclusive();
                    rest.setValue(facet.getValue().toString());
                    restrictions.add(rest);
                } else if (facet.getName().equals(
                    XSFacet.FACET_MININCLUSIVE)) {
                    final Restriction rest = new Restriction.MinInclusive();
                    rest.setValue(facet.getValue().toString());
                    restrictions.add(rest);
                } else if (facet.getName().equals(
                    XSFacet.FACET_MINLENGTH)) {
                    final Restriction rest = new Restriction.MinLength();
                    rest.setValue(facet.getValue().toString());
                    restrictions.add(rest);
                } else if (facet.getName().equals(
                    XSFacet.FACET_PATTERN)) {
                    final Restriction rest = new Restriction.Pattern();
                    rest.setValue(facet.getValue().toString());
                    restrictions.add(rest);
                } else if (facet.getName().equals(
                    XSFacet.FACET_TOTALDIGITS)) {
                    final Restriction rest = new Restriction.TotalDigits();
                    rest.setValue(facet.getValue().toString());
                    restrictions.add(rest);
                } else if (facet.getName().equals(
                    XSFacet.FACET_WHITESPACE)) {
                    final Restriction rest = new Restriction.Whitespace();
                    rest.setValue(facet.getValue().toString());
                    restrictions.add(rest);
                } else {
                    Reporting.logUnexpected();
                }
            }
            if (restrictions.size() > 0) {
                base.setRestrictions(restrictions);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return base;
    }

    /** The simple primitive types. */
    private static Map<String, Class> simplePrimitiveTypes =
        new HashMap<String, Class>() {

            private boolean initialized = false;

            @Override
            public Class get(
                final Object key) {
                if (!initialized) {
                    put(
                        "string",
                        Simple.class);
                    put(
                        "boolean",
                        Simple.class);
                    put(
                        "float",
                        Simple.class);
                    put(
                        "double",
                        Simple.class);
                    put(
                        "decimal",
                        Simple.class);
                    put(
                        "duration",
                        Simple.class);
                    put(
                        "dateTime",
                        Simple.class);
                    put(
                        "time",
                        Simple.class);
                    put(
                        "date",
                        Simple.class);
                    put(
                        "gYearMonth",
                        Simple.class);
                    put(
                        "gYear",
                        Simple.class);
                    put(
                        "gMonthDay",
                        Simple.class);
                    put(
                        "gDay",
                        Simple.class);
                    put(
                        "gMonth",
                        Simple.class);
                    put(
                        "hexBinary",
                        Simple.class);
                    put(
                        "base64Binary",
                        Simple.class);
                    put(
                        "anyURI",
                        Simple.class);
                    put(
                        "QName",
                        Simple.class);
                    put(
                        "NOTATION",
                        Simple.class);

                    put(
                        "normalizedString",
                        Simple.class);
                    put(
                        "token",
                        Simple.class);
                    put(
                        "language",
                        Simple.class);
                    put(
                        "IDREFS",
                        Simple.class);
                    put(
                        "ENTITIES",
                        Simple.class);
                    put(
                        "NMTOKEN",
                        Simple.class);
                    put(
                        "NMTOKENS",
                        Simple.class);
                    put(
                        "Name",
                        Simple.class);
                    put(
                        "NCName",
                        Simple.class);
                    put(
                        "ID",
                        Simple.class);
                    put(
                        "IDREF",
                        Simple.class);
                    put(
                        "ENTITY",
                        Simple.class);
                    put(
                        "integer",
                        Simple.class);
                    put(
                        "nonPositiveInteger",
                        Simple.class);
                    put(
                        "negativeInteger",
                        Simple.class);
                    put(
                        "long",
                        Simple.class);
                    put(
                        "int",
                        Simple.class);
                    put(
                        "short",
                        Simple.class);
                    put(
                        "byte",
                        Simple.class);
                    put(
                        "nonNegativeInteger",
                        Simple.class);
                    put(
                        "unsignedLong",
                        Simple.class);
                    put(
                        "unsignedInt",
                        Simple.class);
                    put(
                        "unsignedShort",
                        Simple.class);
                    put(
                        "unsignedByte",
                        Simple.class);
                    put(
                        "positiveInteger",
                        Simple.class);
                    put(
                        "anonymous simple type",
                        Simple.class);
                    initialized = true;
                }
                final Class c = super.get(key);
                if (c != null) {
                    return c;
                } else {
                    return null;
                }
                /* Simple.class */
            }
        };

    /* helper methods */
    /**
     * Gets the unique name.
     * @param name the name
     * @param element the element
     * @return the unique name
     */
    private String getUniqueName(
        final String name,
        final XSElementDecl element) {
        return name
            + "_"
            + element.getType().getName();
    }

    /**
     * Checks if is abstract.
     * @param ed the ed
     * @param element the element
     * @return true, if is abstract
     */
    public static boolean isAbstract(
        final ElementDeclaration ed,
        final XSElementDecl element) {
        return element.isAbstract();
    }

    /**
     * Checks if is abstract.
     * @param ed the ed
     * @return true, if is abstract
     */
    public static boolean isAbstract(
        final ElementDeclaration ed) {
        return isAbstractName(ed.getQualifiedName());
    }

    /**
     * Checks if is abstract name.
     * @param ed the ed
     * @return true, if is abstract name
     */
    public static boolean isAbstractName(
        final String ed) {
        return ed.contains(":Abstract");
    }

    /* == Abbreviating names == */
    /** The profile schemas. */
    private Map<String, ProfileSchema> profileSchemas;

    /**
     * Gets the short name.
     * @param element the element
     * @return the short name
     */
    private String getShortName(
        final XSElementDecl element) {
        String ns = element.getTargetNamespace();
        if (profileSchemas != null) {
            ns = getShortNamespace(ns);
        } else {
            Reporting.logUnexpected();
        }
        if (ns != null
            && ns.trim().length() > 0) {
            return ns.trim()
                + ":"
                + element.getName();
        } else {
            return element.getName();
        }
    }

    /**
     * Gets the short name.
     * @param name the name
     * @param nameSpace the name space
     * @return the short name
     */
    private String getShortName(
        final String name,
        final String nameSpace) {
        String ns = nameSpace;
        if (profileSchemas != null) {
            ns = getShortNamespace(ns);
        } else {
            Reporting.logUnexpected();
        }
        if (ns != null
            && ns.trim().length() > 0) {
            return ns.trim()
                + ":"
                + name;
        } else {
            return name;
        }
    }

    /**
     * returns short namespace of a namespace uri.
     * @param ns the ns
     * @return the short namespace
     */
    private String getShortNamespace(
        final String ns) {
        if (ns.length() == 0) {
            return ns;
        }
        final ProfileSchema profileSchema =
            profileSchemas.get(ns.toLowerCase());
        if (profileSchema != null) {
            final String nsPrefix = profileSchema.getNamespacePrefix();
            if (nsPrefix != null) {
                return nsPrefix;
            }
        }
        return ns;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "xsd";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entity loadProfileModel(
        final String rootEntity,
        final ClientContext context)
        throws ProfileDecodingFailedException {
        String namespace = null;
        String namespaceURI = null;
        String elementName = null;
        String schemaPath = null;

        if ((rootEntity != null)
            && (rootEntity.length() > 0)) {
            if (rootEntity.indexOf(':') != -1) {
                namespace = rootEntity.substring(
                    0,
                    rootEntity.indexOf(':'));
                elementName = rootEntity.substring(rootEntity.indexOf(':') + 1);
                namespaceURI = getNamespaceURI(namespace);
            } else {
                elementName = rootEntity;
            }
            schemaPath = getSchemaPath(
                namespace,
                context);
            if ((schemaPath != null)
                && (schemaPath.length() > 0)) {

                final File[] schemas = context.getResources().getFilesList(
                    schemaPath);

                if (schemas.length > 0) {

                    long totalMemory;
                    long freeMemory;
                    Reporting.logExpected(
                        "Loading === %1$s",
                        schemaPath);
                    Reporting.logExpected("Before");
                    totalMemory = Runtime.getRuntime().totalMemory();
                    Reporting.logExpected(
                        "Total Memory: %1$d",
                        totalMemory);
                    freeMemory = Runtime.getRuntime().freeMemory();
                    Reporting.logExpected(
                        "Free Memory: %1$d",
                        freeMemory);
                    Reporting.logExpected(
                        "Occupied Memory: %1$d",
                        (Runtime.getRuntime().totalMemory() - Runtime
                            .getRuntime()
                            .freeMemory()));

                    final XSSchemaSet schemaSet = SchemaParser.parseSchema(
                        schemas[0],
                        context);
                    if (schemaSet == null) {
                        throw new ProfileDecodingFailedException(
                            schemaPath);
                    }
                    Entity e = null;
                    try {
                        if (namespace == null) {
                            e = extractModel(schemaSet.getElementDecl(
                                "",
                                elementName));
                        } else {
                            /*
                             * schema.getRoot().getElementDecl(rootNamespaceURL,
                             * rootEntity);
                             */
                            // for (final Iterator iterator = schema.getRoot()
                            // .iterateElementDecls(); iterator.hasNext();) {
                            // final XSElementDecl x = (XSElementDecl) iterator
                            // .next();
                            // if (x.getTargetNamespace().equals(namespaceURI)
                            // && x.getName().equals(elementName)) {
                            // e = extractModel(x);
                            // break;
                            // }
                            // }
                            e = extractModel(schemaSet.getElementDecl(
                                namespaceURI,
                                elementName));
                        }
                    } catch (final Exception ex) {
                        ex.printStackTrace();
                    }
                    if (e == null) {
                        Reporting.reportUnexpected(
                            "Root Element %1$s of namespace %2$s not found",
                            elementName,
                            namespace);
                    }
                    Reporting.logExpected("After");
                    Reporting.logExpected(
                        "Total Memory: %1$d; Delta: %2$d",
                        Runtime.getRuntime().totalMemory(),
                        Runtime.getRuntime().totalMemory()
                            - totalMemory);
                    Reporting.logExpected(
                        "Free Memory: %1$d; Delta: %2$d",
                        Runtime.getRuntime().freeMemory(),
                        Runtime.getRuntime().freeMemory()
                            - freeMemory);
                    Reporting.logExpected(
                        "Occupied Memory: %1$d; Delta: %2$d",
                        (Runtime.getRuntime().totalMemory() - Runtime
                            .getRuntime()
                            .freeMemory()),
                        (Runtime.getRuntime().totalMemory() - Runtime
                            .getRuntime()
                            .freeMemory())
                            - (totalMemory - freeMemory));
                    return e;
                }

            } else {
                Reporting.reportUnexpected("Schema path could not be located.");
            }
        } else {
            Reporting.reportUnexpected("Profile Root Element Not Specified");
        }

        return null;
    }

    /**
     * Gets the namespace uri.
     * @param nameSpace the name space
     * @return the namespace uri
     */
    private String getNamespaceURI(
        final String nameSpace) {
        String varNameSpace = nameSpace;
        if (varNameSpace == null) {
            varNameSpace = "";
        }
        for (final String uri : profileSchemas.keySet()) {
            final ProfileSchema ps = profileSchemas.get(uri);
            if (ps.getNamespacePrefix().equals(
                varNameSpace)
                || ps.getNamespaceUri().equals(
                    varNameSpace)) {
                return ps.getNamespaceUri();
            }
        }
        return null;
    }

    /**
     * Gets the schema path.
     * @param nameSpace the name space
     * @param context the context
     * @return the schema path
     */
    private String getSchemaPath(
        final String nameSpace,
        final ClientContext context) {
        String varNameSpace = nameSpace;
        if (varNameSpace == null) {
            varNameSpace = "";
        }
        for (final String uri : profileSchemas.keySet()) {
            final ProfileSchema ps = profileSchemas.get(uri);
            if (ps.getNamespacePrefix().equals(
                varNameSpace)
                || ps.getNamespaceUri().equals(
                    varNameSpace)) {
                String scPath = ps.getSchemaPath();
                if ((scPath == null || scPath.length() == 0)
                    && ps.getSchemaUrl() != null
                    && ps.getSchemaUrl().length() > 0) {
                    scPath = context.getProfiles().getSchemaCache().get(
                        ps.getSchemaUrl());
                }
                return scPath;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setElementSubstitutions(
        final Map<String, String> aSubstitutions) {
        this.substitutions = aSubstitutions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProfileSchemas(
        final Map<String, ProfileSchema> aProfileSchemas) {
        this.profileSchemas = aProfileSchemas;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getPriority() {
        return 0;
    }

}
