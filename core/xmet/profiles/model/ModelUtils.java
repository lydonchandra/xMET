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
package xmet.profiles.model;

import java.lang.reflect.Method;
import java.util.Stack;

import n.java.ReflectionUtils;
import n.reporting.Reporting;

/**
 * Utilities to be used for manipulating the model and what not in this package.
 * This is to be used and extended where appropriate so that there is a single
 * point of modification for when and if the model is changed in any way
 * @author Nahid Akbar
 */
public final class ModelUtils {

    /**
     * Instantiates a new model utils.
     */
    private ModelUtils() {

    }

    // ---------------------------------------------------------------
    // -- Helper methods for tracing elements in the model - this should be
    /* exclusively used for manipulating the model structure */
    // ---------------------------------------------------------------

    /**
     * Returns the xpath of the entity.
     * @param e the e
     * @return the path
     */
    public static String getPath(
        final Entity e) {
        /* 1 step lookup implementation */
        /* TODO: Simplify when a bit more solid */
        /* build a list going up the chain ad then build path downwards */
        final Stack<Entity> path = new Stack<Entity>();
        final StringBuilder builder = new StringBuilder();
        Entity varE = e;
        while (varE != null) {
            path.push(varE);
            varE = varE.getParent();
        }

        while (!path.isEmpty()) {
            varE = path.pop();
            /* pre */
            // if (varE instanceof Comment) {
            // } else
            if (varE instanceof ElementAttribute) {
                builder.append('@');
                builder.append(varE.getQualifiedName());
            } else if (varE instanceof ElementDeclaration) {
                builder.append('/');
                builder.append(varE.getQualifiedName());
            } else if (varE instanceof Extra) {
                builder.append('/');
                builder.append(varE.getQualifiedName());
                // } else if (varE instanceof ImplicitGroup) {
                // } else if (varE instanceof ChoiceGroup) {
                // } else if (varE instanceof SequenceGroup) {
                // } else if (varE instanceof AllGroup) {
                // } else if (varE instanceof Optional) {
            } else if (varE instanceof Repeated) {
                /* TODO: Dublin Core Hack */
                /* if (!ModelUtils.isGroup(((Repeated) e).getBaseTerm())) { */
                builder.append('/');
                if (!path.isEmpty()) {
                    builder.append(path.peek().getQualifiedName());
                } else {
                    builder.append(varE.getQualifiedName());
                    // }
                }
            } else if (varE instanceof Simple) {
                builder.append('/');
                builder.append(varE.getQualifiedName());
            }
            // else {
            // // Reporting.unimplemented(varE.toString());
            // }
            /* post */
            // if (varE instanceof Comment) {
            // } else if (varE instanceof ElementAttribute) {
            // } else if (varE instanceof ElementDeclaration) {
            // } else if (varE instanceof Extra) {
            // } else if (varE instanceof ImplicitGroup) {
            // } else if (varE instanceof ChoiceGroup) {
            // } else if (varE instanceof SequenceGroup) {
            // } else if (varE instanceof AllGroup) {
            // } else
            if (varE instanceof Optional) {
                if (path.isEmpty()) {
                    builder.append('/');
                    builder.append(varE.getQualifiedName());
                }
            } else if (varE instanceof Repeated) {
                if (!path.isEmpty()) {
                    final Entity child = path.pop();
                    final int index = ((Repeated) varE).indexOfEntity(child);
                    /* for (int i = 0; i < index; i++) { */
                    /* if (!child.getName().equals(((Repeated) */
                    /* e).getChildren().get(i))) { */
                    /* index--; */
                    // }
                    // }
                    assert (index >= 0);
                    if (index != 0) {
                        builder.append('[');
                        builder.append((index + 1));
                        builder.append(']');
                    }
                }
                // } else if (varE instanceof Simple) {
            }
            // else {
            // // Reporting.unimplemented(varE.toString());
            // }
        }
        final String xpath = builder.toString();
        return xpath;
    }

    /**
     * Helper method for extracting the children of an entity.
     * @param parent the parent
     * @param name the name
     * @return the entity
     */
    public static Entity hardGetEntityChild(
        final Entity parent,
        final String name) {
        Entity varParent = parent;
        if (name != null) {
            final String baseName = removeNameAnnoations(name);
            if (varParent instanceof ElementDeclaration) {
                if (name.startsWith("@")) {
                    return getAttribute(
                        varParent,
                        baseName);
                } else {
                    return hardGetEntityChild(
                        ((ElementDeclaration) varParent).getGroup(),
                        name);
                }
            } else if (varParent instanceof Optional) {
                ((Optional) varParent).setTermPresent(true);
                return hardGetEntityChild(
                    ((Optional) varParent).getSetTerm(),
                    name);
            } else if (varParent instanceof Repeated) {
                int index = 1;
                /* extracts index if one is set...otherwise defaults to the */
                /* first element */
                if ((name.indexOf('[') != -1)
                    && (name.indexOf(']') != -1)) {
                    String indexString = name.substring(
                        name.indexOf('[') + 1,
                        name.indexOf(']'));
                    try {
                        if (indexString.equals("last()")) {
                            indexString =
                                Integer.toString(((Repeated) varParent)
                                    .entityCount());
                        } else {
                            index = Integer.parseInt(indexString);
                        }
                    } catch (final Throwable t) {
                        index = 1;
                    }
                }
                while (index > ((Repeated) varParent).entityCount()) {
                    repeatedAddNew((Repeated) varParent);
                }
                if ((index > 0)
                    && (index <= ((Repeated) varParent).entityCount())) {
                    return ((Repeated) varParent).getEntityByIndex(index - 1);
                } else {
                    Reporting.logUnexpected();
                    return null;
                }
            } else if (varParent instanceof Group) { /* and its children */
                Entity child = groupGetChildByByName(
                    varParent,
                    baseName);
                /* set route through choice items */
                while (isGroup(varParent)) {
                    Entity last = child;
                    if (isChoiceGroup(varParent)) {
                        choiceSetSelected(
                            asChoiceGroup(varParent),
                            last);
                    }
                    last = varParent;
                    varParent = varParent.getParent();
                }
                /*
                 * post-process so we don't get things like repeated or optional
                 */
                /* containers */
                if ((child != null)
                    && !child.getQualifiedName().equals(
                        baseName)) { /* substitutables */
                    if (child instanceof ElementDeclaration) {
                        return substituteChild(
                            child,
                            name);
                    } else if (child instanceof Optional) {
                        ((Optional) child).setTermPresent(true);
                        return substituteChild(
                            ((Optional) child).getSetTerm(),
                            name);
                    } else if (child instanceof Repeated) {
                        final Entity e = hardGetEntityChild(
                            child,
                            name);
                        return substituteChild(
                            e,
                            baseName);
                    } else {
                        Reporting.logUnexpected();
                        return child;
                    }
                } else { /* non-substitutables */
                    if (child instanceof Optional) {
                        ((Optional) child).setTermPresent(true);
                        child = ((Optional) child).getSetTerm();
                    } else if (child instanceof Repeated) {
                        return hardGetEntityChild(
                            child,
                            name);
                    }
                    return child;
                }
            } else {
                /* Comment, Element Attribute, Extra, Simple */
                return null;
            }
        } else { /* name is null */
            return varParent;
        }
    }

    /**
     * Helper method for extracting the children of an entity.
     * @param parent the parent
     * @param name the name
     * @return the entity
     */
    public static Entity softGetEntityChild(
        final Entity parent,
        final String name) {
        if (name != null) {
            final String baseName = removeNameAnnoations(name);
            if (parent instanceof ElementDeclaration) {
                if (name.startsWith("@")) {
                    return getAttribute(
                        parent,
                        baseName);
                } else {
                    return softGetEntityChild(
                        ((ElementDeclaration) parent).getGroup(),
                        name);
                }
            } else if (parent instanceof Optional) {
                if (((Optional) parent).isSetTermPresent()) {
                    return softGetEntityChild(
                        ((Optional) parent).getSetTerm(),
                        name);
                } else {
                    return null;
                }
            } else if (parent instanceof Repeated) {
                int index = 1;
                /* extracts index if one is set...otherwise defaults to the */
                /* first element */
                if ((name.indexOf('[') != -1)
                    && (name.indexOf(']') != -1)) {
                    final String indexString = name.substring(
                        name.indexOf('[') + 1,
                        name.indexOf(']'));
                    try {
                        if (indexString.equals("last()")) {
                            index = ((Repeated) parent).entityCount();
                        } else {
                            index = Integer.parseInt(indexString);
                        }
                    } catch (final Throwable t) {
                        index = 1;
                    }
                }
                if ((index > 0)
                    && (index <= ((Repeated) parent).entityCount())) {
                    return ((Repeated) parent).getEntityByIndex(index - 1);
                } else {
                    return null;
                }
            } else if (parent instanceof Group) { /* and its children */
                final Entity child = softGroupGetChildByByName(
                    parent,
                    baseName);
                /* set route through choice items */
                /* while (isGroup(parent)) { */
                /* Entity last = child; */
                /* if (isChoiceGroup(parent)) { */
                /* choiceSetSelected(asChoiceGroup(parent), last); */
                // }
                /* last = parent; */
                /* parent = parent.getParent(); */
                // }
                /*
                 * post-process so we don't get things like repeated or optional
                 */
                /* containers */
                if ((child != null)
                    && !child.getQualifiedName().equals(
                        baseName)) { /* substitutables */
                    return null;
                } else { /* non-substitutables */
                    if (child instanceof Optional) {
                        if (((Optional) child).getSetTerm() != null) {
                            return ((Optional) child).getSetTerm();
                        }
                    } else if (child instanceof Repeated) {
                        return softGetEntityChild(
                            child,
                            name);
                    }
                    return child;
                }
            } else {
                /* Comment, Element Attribute, Extra, Simple */
                return null;
            }
        } else { /* name is null */
            return parent;
        }
    }

    /**
     * Soft has children.
     * @param parent the parent
     * @param name the name
     * @return true, if successful
     */
    public static boolean softHasChildren(
        final Entity parent,
        final String name) {
        if (name != null) {
            final String baseName = removeNameAnnoations(name);
            Entity varParent = parent;
            if (varParent instanceof ElementDeclaration) {
                if (name.startsWith("@")) {
                    return getAttribute(
                        varParent,
                        baseName) != null;
                } else {
                    return softHasChildren(
                        ((ElementDeclaration) varParent).getGroup(),
                        name);
                }
            } else if (varParent instanceof Optional) {
                final Entity e = ((Optional) varParent).getSetTerm();
                if (e != null) {
                    if (isGroup(e)) {
                        return softHasChildren(
                            e,
                            name);
                    } else {
                        return e.getQualifiedName().equals(
                            name);
                    }
                } else {
                    return false;
                }
            } else if (varParent instanceof Repeated) {
                int index = 1;
                /* extracts index if one is set...otherwise defaults to the */
                if ((name.indexOf('[') != -1)
                    && (name.indexOf(']') != -1)) {
                    String indexString = name.substring(
                        name.indexOf('[') + 1,
                        name.indexOf(']'));
                    try {
                        if (indexString.equals("last()")) {
                            indexString =
                                Integer.toString(((Repeated) varParent)
                                    .entityCount());
                        } else {
                            index = Integer.parseInt(indexString);
                        }
                    } catch (final Throwable t) {
                        index = 1;
                    }
                }
                /* while (index > ((Repeated) parent).size()) { */
                /* repeatedAddNew((Repeated) parent); */
                // }
                if ((index > 0)
                    && (index <= ((Repeated) varParent).entityCount())) {
                    return ((Repeated) varParent).entityCount() > index - 1;
                } else {
                    return false;
                }
            } else if (varParent instanceof Group) { /* and its children */
                Entity child = groupGetChildByByName(
                    varParent,
                    baseName);
                /* set route through choice items */
                while (isGroup(varParent)) {
                    Entity last = child;
                    if (isChoiceGroup(varParent)) {
                        choiceSetSelected(
                            asChoiceGroup(varParent),
                            last);
                    }
                    last = varParent;
                    varParent = varParent.getParent();
                }
                /*
                 * post-process so we don't get things like repeated or optional
                 */
                /* containers */
                if ((child != null)
                    && !child.getQualifiedName().equals(
                        baseName)) { /* substitutables */
                    return false;
                } else { /* non-substitutables */
                    if (child instanceof Optional) {
                        ((Optional) child).setTermPresent(true);
                        child = ((Optional) child).getSetTerm();
                    } else if (child instanceof Repeated) {
                        return softHasChildren(
                            child,
                            name);
                    }
                    return (child != null)
                        && child.getQualifiedName().equals(
                            name);
                }
            } else {
                /* Comment, Element Attribute, Extra, Simple */
                return false;
            }
        } else { /* name is null */
            return false;
        }
    }

    /**
     * Hard trace setable xpath.
     * @param root the root
     * @param xpath the xpath
     * @return the setable
     */
    public static Settable hardTraceSetableXpath(
        final Entity root,
        final String xpath) {
        return getSetable(hardTraceXPath(
            root,
            xpath));
    }

    /**
     * Soft trace setable xpath.
     * @param root the root
     * @param xpath the xpath
     * @return the setable
     */
    public static Settable softTraceSetableXpath(
        final Entity root,
        final String xpath) {
        return getSetable(softTraceXPath(
            root,
            xpath));
    }

    /**
     * Removes the name annoations.
     * @param name the name
     * @return the string
     */
    private static String removeNameAnnoations(
        final String name) {
        String varName = name;
        if (varName.startsWith("@")) {
            varName = varName.substring(1);
        }
        if (varName.indexOf('[') != -1) { /* repeated entity */
            varName = varName.substring(
                0,
                varName.indexOf('['));
        }
        if (varName.indexOf('#') != -1) { /* bookmark? */
            varName = varName.substring(
                0,
                varName.indexOf('#'));
        }
        return varName;
    }

    /**
     * Trace x path for entity.
     * @param root the root
     * @param xpath the xpath
     * @return the entity
     */
    public static Entity traceXPathForEntity(
        final Entity root,
        final String xpath) {
        return hardTraceXPath(
            root,
            xpath);
    }

    /**
     * Trace x path for attribute.
     * @param root the root
     * @param xpath the xpath
     * @return the element attribute
     */
    public static ElementAttribute traceXPathForAttribute(
        final Entity root,
        final String xpath) {
        return (ElementAttribute) hardTraceXPath(
            root,
            xpath);
    }

    /**
     * Traces a model tree to return the element at the end.
     * @param root root element
     * @param xpath xpath expression
     * @return Either an Entity (ED or Simple) or an Attribute
     */
    public static Entity hardTraceXPath(
        final Entity root,
        final String xpath) {
        String varXpath = xpath;
        if ((root != null)
            && (varXpath != null)) {
            varXpath = preProcessXpath(varXpath);
            final String[] parts = varXpath.trim().split(
                "/");
            if (parts.length > 0) {
                Entity parent = root;
                Entity child = root;

                for (int i = 0; i < parts.length; i++) {
                    final String part = parts[i];

                    if (part.equals(".")) {
                        continue;

                    } else if (part.equals("..")) {
                        if ((child != root)
                            && (child.getParent() != null)) {
                            child = parent;
                            parent = child.getParent();
                            while (isContainer(parent)) {
                                parent = child.getParent();
                            }
                        } else {
                            Reporting.logUnexpected();
                        }
                    } else {
                        if ((i == 0)
                            && (part.length() == 0)) {
                            continue;
                        } else if ((i == 0)
                            && parent.getQualifiedName().equals(
                                part)) {
                            continue;
                        } else if ((i == 1)
                            && (parts[0].length() == 0)
                            && parent.getQualifiedName().equals(
                                part)) {
                            continue;
                        } else {
                            /* NOTE: NORMAL STRING - original - start */
                            parent = child;
                            child = hardGetEntityChild(
                                parent,
                                part);
                        }
                    }
                }
                if (child != null) {
                    if (!varXpath.endsWith(child.getQualifiedName())
                        && !(child instanceof ElementDeclaration)
                        && !(child instanceof Simple)) {
                        Reporting.logExpected(varXpath);
                        Reporting.logExpected(child.toString());
                    }
                }
                return child;
            } else {
                /* empty path */
                return root;
            }
        }
        return root;
    }

    /**
     * Pre process xpath.
     * @param xpath the xpath
     * @return the string
     */
    private static String preProcessXpath(
        final String xpath) {
        String varXpath = xpath;
        if ((varXpath.indexOf("@") != -1)
            && (varXpath.indexOf("/@") == -1)) {
            varXpath = varXpath.replaceAll(
                "@",
                "/@");
        }
        varXpath = varXpath.replaceAll(
            "\\s",
            "");
        return varXpath;
    }

    /**
     * Soft trace x path.
     * @param root the root
     * @param xpath the xpath
     * @return the entity
     */
    public static Entity softTraceXPath(
        final Entity root,
        final String xpath) {
        String varXpath = xpath;
        if ((root != null)
            && (varXpath != null)) {
            varXpath = preProcessXpath(varXpath);
            final String[] parts = varXpath.trim().split(
                "/");
            if (parts.length > 0) {
                Entity parent = root;
                Entity child = root;

                for (int i = 0; i < parts.length; i++) {
                    final String part = parts[i];

                    if (part.equals(".")) {
                        continue;

                    } else if (part.equals("..")) {
                        if ((child != root)
                            && (child.getParent() != null)) {
                            child = parent;
                            parent = child.getParent();
                            while (isContainer(parent)) {
                                parent = child.getParent();
                            }
                        } else {
                            Reporting.logUnexpected();
                        }
                    } else {
                        if ((i == 0)
                            && (part.length() == 0)) {
                            continue;
                        } else if ((i == 0)
                            && parent.getQualifiedName().equals(
                                part)) {
                            continue;
                        } else if ((i == 1)
                            && (parts[0].length() == 0)
                            && parent.getQualifiedName().equals(
                                part)) {
                            continue;
                        } else {
                            parent = child;
                            child = softGetEntityChild(
                                parent,
                                part);
                        }
                    }
                }
                if (child != null) {
                    if (!varXpath.endsWith(child.getQualifiedName())
                        && !(child instanceof ElementDeclaration)
                        && !(child instanceof Simple)) {
                        Reporting.logExpected(varXpath);
                        Reporting.logExpected(child.toString());
                    }
                }
                return child;
            } else {
                /* empty path */
                return root;
            }
        }
        return root;
    }

    /**
     * Gets the setable.
     * @param entity the entity
     * @return the setable
     */
    public static Settable getSetable(
        final Entity entity) {
        if (entity != null) {
            if (isSetable(entity)) {
                return asSetable(entity);
            } else {
                if (isElementDeclaration(entity)) {
                    final Group group = extractEntityGroup(entity);
                    if (isImplicitGroup(group)) {
                        final Entity e = asImplicitGroup(
                            group).getChildren().get(
                            0);
                        if (isSetable(e)) {
                            return (Settable) e;
                        } else {
                            return null;
                        }
                    } else if (isSequenceGroup(group)
                        && (asSequenceGroup(
                            group).getChildren().size() == 1)) {
                        return getSetable(group.getChildren().get(
                            0));
                    }
                }
                /* Reporting.unimplemented(); */
            }
        }
        // else {
        // /* Reporting.err(""); */
        // }
        return null;
    }

    /* == Choice == */
    /**
     * Sets the selected.
     * @param choice the choice
     * @param selected the new selected
     */
    public static void choiceSetSelected(
        final ChoiceGroup choice,
        final String selected) {
        int index = 0;
        choice.setSelected(-1);
        for (final Entity e : choice.getChildren()) {
            if (e.getQualifiedName().equals(
                selected)) {
                choice.setSelected(index);
                return;
            }
            index++;
        }
    }

    /**
     * Choice set selected.
     * @param choice the choice
     * @param entity the entity
     */
    public static void choiceSetSelected(
        final ChoiceGroup choice,
        final Entity entity) {
        int index = 0;
        for (final Entity e : choice.getChildren()) {
            if (e == entity) {
                choice.setSelected(index);
                return;
            }
            index++;
        }
        choice.setSelected(0);
    }

    /* == Substitutable == */
    /**
     * Substitution replace child.
     * @param parent the parent
     * @param original the original
     * @param replacement the replacement
     * @return the entity
     */
    public static Entity substitutionReplaceChild(
        final Entity parent,
        final ElementDeclaration original,
        final ElementDeclaration replacement) {
        if (parent instanceof Group) {
            final Group group = (Group) parent;
            int i = group.getChildren().indexOf(
                original);
            if (i != -1) {
                group.getChildren().set(
                    i,
                    replacement);
            } else {
                i = group.getChildren().indexOf(
                    groupGetChildByByName(
                        group,
                        original.getQualifiedName()));
                if (i != -1) {
                    group.getChildren().set(
                        i,
                        replacement);
                } else {
                    System.err.println("changeable not found");
                }
            }
        } else if (parent instanceof ElementDeclaration) {
            substitutionReplaceChild(
                ((ElementDeclaration) parent).getGroup(),
                original,
                replacement);
        } else if (parent instanceof Optional) {
            ((Optional) parent).setSetTerm(replacement);
        } else if (parent instanceof Repeated) {
            final int i = ((Repeated) parent).getEntities().indexOf(
                original);
            if (i != -1) {
                ((Repeated) parent).getEntities().set(
                    i,
                    replacement);
            } else {
                Reporting.logUnexpected();
            }
        } else {
            if (parent != null) {
                Reporting.reportUnexpected();
            }
        }
        return replacement;
    }

    /**
     * Substitute child.
     * @param child the child
     * @param name the name
     * @return the entity
     */
    public static Entity substituteChild(
        final Entity child,
        final String name) {
        if (isElementDeclaration(child)
            && !child.getQualifiedName().equals(
                name)) {
            final ElementDeclaration element = asElementDeclaration(child);
            if (element.hasSubtitutables()) {
                final ElementDeclaration substuted =
                    element.getSubstitutables().getElementDeclaration(
                        name,
                        child.getParent());
                return substitutionReplaceChild(
                    child.getParent(),
                    element,
                    substuted);
            }
        }
        return child;
    }

    /* == Repeated == */
    /**
     * Repeated add new.
     * @param parent the parent
     * @return the entity
     */
    private static Entity repeatedAddNew(
        final Repeated parent) {
        return parent.addNewEntity();
    }

    /* == entity name and namespace == */

    /**
     * Extract entity namespace.
     * @param e the e
     * @return the string
     */
    public static String extractEntityNamespace(
        final Entity e) {
        return e.getNamespace();
    }

    /**
     * Extract entity name prefix.
     * @param e the e
     * @return the string
     */
    public static String extractEntityNamePrefix(
        final Entity e) {
        final String name = e.getQualifiedName();
        final int i = name.indexOf(":");
        if (i != -1) {
            return name.substring(
                0,
                i);
        } else {
            return "";
        }
    }

    /**
     * Extract entity plain name.
     * @param e the e
     * @return the string
     */
    public static String extractEntityPlainName(
        final Entity e) {
        final String name = e.getQualifiedName();
        final int i = name.indexOf(":");
        if (i != -1) {
            return name.substring(i + 1);
        } else {
            return name;
        }
    }

    /**
     * Extract entity name.
     * @param e the e
     * @return the string
     */
    public static String extractEntityName(
        final Entity e) {
        return e.getQualifiedName();
    }

    /* == attributes == */
    /**
     * Adds the attribute.
     * @param entity the entity
     * @param attribute the attribute
     */
    public static void addAttribute(
        final Entity entity,
        final ElementAttribute attribute) {
        if (isElementDeclaration(entity)) {
            final ElementDeclaration elementDeclaration =
                asElementDeclaration(entity);
            if (!elementDeclaration.hasAttributes()) {
                elementDeclaration.setAttributes(new AttributesList());
            }
            elementDeclaration.getAttributes().add(
                attribute);
        }
    }

    /**
     * Gets the attribute.
     * @param entity the entity
     * @param attributeName the attribute name
     * @return the attribute
     */
    public static ElementAttribute getAttribute(
        final Entity entity,
        final String attributeName) {
        if (isElementDeclaration(entity)
            && asElementDeclaration(
                entity).hasAttributes()) {
            return asElementDeclaration(
                entity).getAttributes().getAttributeByName(
                attributeName);
        }
        return null;
    }

    /**
     * Checks for attributes.
     * @param entity the entity
     * @return true, if successful
     */
    public static boolean hasAttributes(
        final Entity entity) {
        if (isElementDeclaration(entity)) {
            return asElementDeclaration(
                entity).hasAttributes();
        }
        return false;
    }

    /**
     * Gets the attribute value.
     * @param e the e
     * @return the attribute value
     */
    public static String getAttributeValue(
        final ElementAttribute e) {
        if (e.getValue() == null) {
            return "";
        } else {
            return e.getValue();
        }
    }

    /* == constraints == */

    /**
     * Adds the constraints.
     * @param term the term
     * @param muc the muc
     */
    public static void addConstraints(
        final Entity term,
        final MultiplicityConstraints muc) {
        if (term != null) {
            if (term instanceof ElementDeclaration) {
                ((ElementDeclaration) term).setConstraints(muc);
            } else if (term instanceof Group) {
                ((Group) term).setConstraints(muc);
            } else if (term instanceof Simple) {
                ((Simple) term).setConstraints(muc);
            } else if (term instanceof Any) {
                ((Any) term).setConstraints(muc);
            } else {
                Reporting.logUnexpected(term.getClass().getName());
            }
        } else {
            Reporting.logUnexpected("null term");
        }
    }

    /**
     * Gets the constraints.
     * @param term the term
     * @return the constraints
     */
    public static MultiplicityConstraints getConstraints(
        final Entity term) {
        if (term != null) {
            if (term instanceof ElementDeclaration) {
                return ((ElementDeclaration) term).getConstraints();
            } else if (term instanceof Group) {
                return ((Group) term).getConstraints();
            } else if (term instanceof Simple) {
                return ((Simple) term).getConstraints();
            } else if (term instanceof Any) {
                return ((Any) term).getConstraints();
            } else {
                Reporting.logUnexpected(term.getClass().getName());
            }
        } else {
            Reporting.logUnexpected("null term");
        }
        return null;
    }

    /* == Group specific functions == */

    /**
     * Group add.
     * @param group the group
     * @param element the element
     * @return true, if successful
     */
    public static boolean groupAdd(
        final Group group,
        final Entity element) {
        element.setParent(group);
        return group.getChildren().add(
            element);
    }

    /**
     * Group add.
     * @param group the group
     * @param index the index
     * @param element the element
     */
    public static void groupAdd(
        final Group group,
        final int index,
        final Entity element) {
        element.setParent(group);
        group.getChildren().add(
            index,
            element);
    }

    /**
     * Adds an element after specified element.
     * @param group the group
     * @param afterElement the after element
     * @param element the element
     */
    public static void groupAddAfter(
        final Group group,
        final Entity afterElement,
        final Entity element) {
        element.setParent(group);
        Entity varAfterElement = afterElement;
        if (varAfterElement == null) {
            groupAdd(
                group,
                0,
                element);
        } else {
            int i = group.getChildren().indexOf(
                varAfterElement);
            while ((i == -1)
                && (varAfterElement.getParent() != null)) {
                varAfterElement = varAfterElement.getParent();
                i = group.getChildren().indexOf(
                    varAfterElement);
            }
            if (i != -1) {
                group.getChildren().add(
                    i + 1,
                    element);
            } else {
                groupAdd(
                    group,
                    element);
            }
        }
    }

    /**
     * Extend group.
     * @param basePGroup the base p group
     * @param extPGroup the extensions p group
     * @return the entity
     */
    public static Entity extendGroup(
        final Entity basePGroup,
        final Entity extPGroup) {
        Entity dstGroup = basePGroup;

        if (basePGroup != null) {
            if (extPGroup != null) {
                while (!isGroup(dstGroup)) {
                    if (dstGroup instanceof Optional) {
                        dstGroup = ((Optional) dstGroup).getBaseTerm();
                    } else if (dstGroup instanceof Repeated) {
                        dstGroup = ((Repeated) dstGroup).getBaseEntity();
                    } else {
                        Reporting.logUnexpected();
                    }
                }

                if (isGroup(dstGroup)) {

                    if (dstGroup != basePGroup) { /* i.e. in a container */
                        /* create a new container for both groups */
                        final Group newGroup = (Group) cloneEntity(dstGroup);
                        newGroup.getChildren().clear();
                        newGroup.getChildren().add(
                            dstGroup);
                        newGroup.getChildren().add(
                            extPGroup);
                        return newGroup;
                    } else {
                        /*
                         * implies both of the same model group and the second
                         * group is not in a container
                         */
                        if (extPGroup.getClass() == dstGroup.getClass()) {
                            ((Group) dstGroup).getChildren().addAll(
                                ((Group) extPGroup).getChildren());
                        } else {
                            ((Group) dstGroup).getChildren().add(
                                extPGroup);
                        }
                        return dstGroup;
                    }
                }
                Reporting.logUnexpected();
                /*
                 * could not locate the original group..live with extensions
                 */
                return extPGroup;
            } else {
                return basePGroup;
            }
        } else {
            return extPGroup;
        }
    }

    /**
     * Extract entity group.
     * @param entity the entity
     * @return the group
     */
    public static Group extractEntityGroup(
        final Entity entity) {
        Entity varEntity = entity;
        if (varEntity != null) {
            while (!isGroup(varEntity)) {
                if (varEntity instanceof Optional) {
                    ((Optional) varEntity).setTermPresent(true);
                    varEntity = ((Optional) varEntity).getSetTerm();
                } else if (varEntity instanceof Repeated) {
                    varEntity = ((Repeated) varEntity).getEntityByIndex(0);
                } else if (varEntity instanceof ElementDeclaration) {
                    varEntity = ((ElementDeclaration) varEntity).getGroup();
                } else {
                    if (varEntity != null) {
                        Reporting.logUnexpected(varEntity.toString());
                        return null;
                    } else {
                        return null;
                    }
                }
            }
            if (isGroup(varEntity)) {
                return (Group) varEntity;
            }
        }
        return null;
    }

    /**
     * Extract entity parent group.
     * @param entity the entity
     * @return the group
     */
    public static Group extractEntityParentGroup(
        final Entity entity) {
        Entity varEntity = entity;
        if (varEntity != null) {
            varEntity = varEntity.getParent();
            while (!isGroup(varEntity)) {
                if (varEntity instanceof Optional) {
                    varEntity = ((Optional) varEntity).getParent();
                } else if (varEntity instanceof Repeated) {
                    varEntity = ((Repeated) varEntity).getParent();
                } else if (varEntity instanceof ElementDeclaration) {
                    varEntity = ((ElementDeclaration) varEntity).getParent();
                } else {
                    if (varEntity != null) {
                        Reporting.logUnexpected(varEntity.toString());
                        return null;
                    } else {
                        return null;
                    }
                }
            }
            if (isGroup(varEntity)) {
                return (Group) varEntity;
            }
        }
        return null;
    }

    /**
     * Gets the parent repeated entity.
     * @param e the e
     * @return the parent repeated entity
     */
    public static Repeated getParentRepeatedEntity(
        final Entity e) {
        if (e instanceof Repeated) {
            return (Repeated) e;
        } else if ((e instanceof ElementDeclaration)) {
            final ElementDeclaration varElementDeclaration =
                (ElementDeclaration) e;
            return getParentRepeatedEntity(varElementDeclaration.getParent());
        } else if ((e instanceof Group)) {
            return getParentRepeatedEntity(((Group) e).getParent());
        } else {
            Reporting.logUnexpected(
                "%1$s",
                e);
        }
        return null;
    }

    /**
     * Group remove child.
     * @param group the group
     * @param entity the entity
     */
    public static void groupRemoveChild(
        final Group group,
        final Entity entity) {
        group.getChildren().remove(
            entity);
        if (group instanceof ChoiceGroup) {
            if (((ChoiceGroup) group).getSelected() >= group
                .getChildren()
                .size()) {
                ((ChoiceGroup) group).setSelected(((ChoiceGroup) group)
                    .getSelected() - 1);
            }
        }
    }

    /**
     * Soft group get child by by name.
     * @param group the group
     * @param name the name
     * @return the entity
     */
    public static Entity softGroupGetChildByByName(
        final Entity group,
        final String name) {
        if ((group != null)
            && isGroup(group)) {
            if (isChoiceGroup(group)) {
                final ChoiceGroup cg = asChoiceGroup(group);
                final Entity e = cg.getChildren().get(
                    cg.getSelected());
                if ((e != null)
                    && e.getQualifiedName().equals(
                        name)) {
                    return e;
                }
            } else {
                for (final Entity e : asGroup(
                    group).getChildren()) {
                    if (e.getQualifiedName().equals(
                        name)) {
                        return e;
                    } else if (isGroup(e)) {
                        final Entity recurse = softGroupGetChildByByName(
                            e,
                            name);
                        if (recurse != null) {
                            return recurse;
                        }
                    }
                }
            }

        }
        return null;
    }

    /**
     * Group get child by by name.
     * @param group the group
     * @param name the name
     * @return the entity
     */
    public static Entity groupGetChildByByName(
        final Entity group,
        final String name) {
        if ((group != null)
            && isGroup(group)) {
            for (final Entity e : asGroup(
                group).getChildren()) {
                if (e.getQualifiedName().equals(
                    name)) {
                    if (isChoiceGroup(group)) {
                        choiceSetSelected(
                            asChoiceGroup(group),
                            e);
                    }
                    return e;
                } else if (isGroup(e)) {
                    final Entity recurse = groupGetChildByByName(
                        e,
                        name);
                    if (recurse != null) {
                        return recurse;
                    }
                }
            }
            for (final Entity e : asGroup(
                group).getChildren()) {
                if (e instanceof ElementDeclaration) {
                    final ElementDeclaration ed = (ElementDeclaration) e;
                    if ((ed.getSubstitutables() != null)
                        && ed.getSubstitutables().hasSubstitutable(
                            name)) {
                        return e;
                    }
                } else if (e instanceof Optional) {
                    ((Optional) e).setTermPresent(true);
                    final ElementDeclaration ed =
                        (ElementDeclaration) ((Optional) e).getSetTerm();
                    if (ed != null) {
                        if ((ed.getSubstitutables() != null)
                            && ed.getSubstitutables().hasSubstitutable(
                                name)) {
                            return e;
                        }
                    } else {
                        Reporting.logUnexpected();
                    }
                } else if (e instanceof Repeated) {
                    final Entity entity = ((Repeated) e).getBaseEntity();
                    if (isElementDeclaration(entity)) {
                        final ElementDeclaration ed =
                            asElementDeclaration(entity);
                        if (ed.hasSubtitutables()
                            && ed.getSubstitutables().hasSubstitutable(
                                name)) {
                            return e;
                        }
                    } else {
                        Reporting.logUnexpected(
                            "groupGetChildByByName(%1$s, %1$s)",
                            e,
                            name);
                    }
                } else if (isGroup(e)) {
                    final Entity recurse = groupGetChildByByName(
                        e,
                        name);
                    if (recurse != null) {
                        return recurse;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Removes the entity.
     * @param entity the entity
     */
    public static void removeEntity(
        final Entity entity) {
        final Entity parent = entity.getParent();
        while (parent != null) {
            if (isGroup(parent)) {
                groupRemoveChild(
                    (Group) parent,
                    entity);
                return;
            } else {
                Reporting.logUnexpected();
            }
        }
    }

    /* manual */
    /**
     * Checks if is container.
     * @param entity the entity
     * @return true, if is container
     */
    public static boolean isContainer(
        final Entity entity) {
        return isGroup(entity)
            || isOptional(entity)
            || isRepeated(entity);
    }

    /* autogenerated */

    /**
     * Checks if is implicit group.
     * @param entity the entity
     * @return true, if is implicit group
     */
    public static boolean isImplicitGroup(
        final Entity entity) {
        return entity instanceof ImplicitGroup;
    }

    /**
     * As implicit group.
     * @param entity the entity
     * @return the implicit group
     */
    public static ImplicitGroup asImplicitGroup(
        final Entity entity) {
        if (entity instanceof ImplicitGroup) {
            return (ImplicitGroup) entity;
        } else {
            return null;
        }
    }

    /**
     * Checks if is all group.
     * @param entity the entity
     * @return true, if is all group
     */
    public static boolean isAllGroup(
        final Entity entity) {
        return entity instanceof AllGroup;
    }

    /**
     * As all group.
     * @param entity the entity
     * @return the all group
     */
    public static AllGroup asAllGroup(
        final Entity entity) {
        if (entity instanceof AllGroup) {
            return (AllGroup) entity;
        } else {
            return null;
        }
    }

    /**
     * Checks if is any.
     * @param term the term
     * @return true, if is any
     */
    public static boolean isAny(
        final Entity term) {
        return term instanceof Any;
    }

    /**
     * As any.
     * @param term the term
     * @return the any
     */
    public static Any asAny(
        final Entity term) {
        if (term instanceof Any) {
            return (Any) term;
        } else {
            return null;
        }
    }

    /**
     * Checks if is choice group.
     * @param entity the entity
     * @return true, if is choice group
     */
    public static boolean isChoiceGroup(
        final Entity entity) {
        return entity instanceof ChoiceGroup;
    }

    /**
     * As choice group.
     * @param entity the entity
     * @return the choice group
     */
    public static ChoiceGroup asChoiceGroup(
        final Entity entity) {
        if (entity instanceof ChoiceGroup) {
            return (ChoiceGroup) entity;
        } else {
            return null;
        }
    }

    /**
     * Checks if is comment.
     * @param entity the entity
     * @return true, if is comment
     */
    public static boolean isComment(
        final Entity entity) {
        return entity instanceof Comment;
    }

    /**
     * As comment.
     * @param entity the entity
     * @return the comment
     */
    public static Comment asComment(
        final Entity entity) {
        if (entity instanceof Comment) {
            return (Comment) entity;
        } else {
            return null;
        }
    }

    /**
     * Checks if is element attribute.
     * @param entity the entity
     * @return true, if is element attribute
     */
    public static boolean isElementAttribute(
        final Entity entity) {
        return entity instanceof ElementAttribute;
    }

    /**
     * As element attribute.
     * @param entity the entity
     * @return the element attribute
     */
    public static ElementAttribute asElementAttribute(
        final Entity entity) {
        if (entity instanceof ElementAttribute) {
            return (ElementAttribute) entity;
        } else {
            return null;
        }
    }

    /**
     * Checks if is element declaration.
     * @param entity the entity
     * @return true, if is element declaration
     */
    public static boolean isElementDeclaration(
        final Entity entity) {
        return entity instanceof ElementDeclaration;
    }

    /**
     * As element declaration.
     * @param entity the entity
     * @return the element declaration
     */
    public static ElementDeclaration asElementDeclaration(
        final Entity entity) {
        if (entity instanceof ElementDeclaration) {
            return (ElementDeclaration) entity;
        } else {
            return null;
        }
    }

    /**
     * Checks if is extra.
     * @param entity the entity
     * @return true, if is extra
     */
    public static boolean isExtra(
        final Entity entity) {
        return entity instanceof Extra;
    }

    /**
     * As extra.
     * @param entity the entity
     * @return the extra
     */
    public static Extra asExtra(
        final Entity entity) {
        if (entity instanceof Extra) {
            return (Extra) entity;
        } else {
            return null;
        }
    }

    /**
     * Checks if is group.
     * @param entity the entity
     * @return true, if is group
     */
    public static boolean isGroup(
        final Entity entity) {
        return entity instanceof Group;
    }

    /**
     * As group.
     * @param entity the entity
     * @return the group
     */
    public static Group asGroup(
        final Entity entity) {
        if (entity instanceof Group) {
            return (Group) entity;
        } else {
            return null;
        }
    }

    /**
     * Checks if is list.
     * @param entity the entity
     * @return true, if is list
     */
    public static boolean isList(
        final Entity entity) {
        return entity instanceof List;
    }

    /**
     * As list.
     * @param entity the entity
     * @return the list
     */
    public static List asList(
        final Entity entity) {
        if (entity instanceof List) {
            return (List) entity;
        } else {
            return null;
        }
    }

    /**
     * Checks if is optional.
     * @param entity the entity
     * @return true, if is optional
     */
    public static boolean isOptional(
        final Entity entity) {
        return entity instanceof Optional;
    }

    /**
     * As optional.
     * @param entity the entity
     * @return the optional
     */
    public static Optional asOptional(
        final Entity entity) {
        if (entity instanceof Optional) {
            return (Optional) entity;
        } else {
            return null;
        }
    }

    /**
     * Checks if is repeated.
     * @param entity the entity
     * @return true, if is repeated
     */
    public static boolean isRepeated(
        final Entity entity) {
        return entity instanceof Repeated;
    }

    /**
     * As repeated.
     * @param entity the entity
     * @return the repeated
     */
    public static Repeated asRepeated(
        final Entity entity) {
        if (entity instanceof Repeated) {
            return (Repeated) entity;
        } else {
            return null;
        }
    }

    /**
     * Checks if is sequence group.
     * @param entity the entity
     * @return true, if is sequence group
     */
    public static boolean isSequenceGroup(
        final Entity entity) {
        return entity instanceof SequenceGroup;
    }

    /**
     * As sequence group.
     * @param entity the entity
     * @return the sequence group
     */
    public static SequenceGroup asSequenceGroup(
        final Entity entity) {
        if (entity instanceof SequenceGroup) {
            return (SequenceGroup) entity;
        } else {
            return null;
        }
    }

    /**
     * Checks if is setable.
     * @param entity the entity
     * @return true, if is setable
     */
    public static boolean isSetable(
        final Entity entity) {
        return entity instanceof Settable;
    }

    /**
     * As setable.
     * @param entity the entity
     * @return the setable
     */
    public static Settable asSetable(
        final Entity entity) {
        if (entity instanceof Settable) {
            return (Settable) entity;
        } else {
            return null;
        }
    }

    /**
     * Checks if is simple.
     * @param entity the entity
     * @return true, if is simple
     */
    public static boolean isSimple(
        final Entity entity) {
        return entity instanceof Simple;
    }

    /**
     * As simple.
     * @param entity the entity
     * @return the simple
     */
    public static Simple asSimple(
        final Entity entity) {
        if (entity instanceof Simple) {
            return (Simple) entity;
        } else {
            return null;
        }
    }

    /**
     * Checks if is simple attribute.
     * @param entity the entity
     * @return true, if is simple attribute
     */
    public static boolean isSimpleAttribute(
        final Entity entity) {
        return entity instanceof SimpleAttribute;
    }

    /**
     * As simple attribute.
     * @param entity the entity
     * @return the simple attribute
     */
    public static SimpleAttribute asSimpleAttribute(
        final Entity entity) {
        if (entity instanceof SimpleAttribute) {
            return (SimpleAttribute) entity;
        } else {
            return null;
        }
    }

    /**
     * Checks if is union.
     * @param entity the entity
     * @return true, if is union
     */
    public static boolean isUnion(
        final Entity entity) {
        return entity instanceof Union;
    }

    /**
     * As union.
     * @param entity the entity
     * @return the union
     */
    public static Union asUnion(
        final Entity entity) {
        if (entity instanceof Union) {
            return (Union) entity;
        } else {
            return null;
        }
    }

    /* CHECKSTYLE OFF: MethodLength */
    /**
     * Clone model tree. Note: This is clonning for the purpose of re-using the
     * model. This method shall not clone data or realizations of structures.
     * @param entity the entity
     * @return the entity
     */
    public static Entity cloneEntity(
        final Entity entity) {
        if (entity != null) {
            if (entity.getClass() == Union.class) {
                final Union src = (Union) entity;
                final Union union = new Union(
                    src.getParent(),
                    src.getQualifiedName(),
                    src.getNamespace());
                for (final Simple s : src.getChildren()) {
                    union.getChildren().add(
                        (Simple) cloneEntity(s));
                }
                return union;
            } else if (entity.getClass() == SimpleAttribute.class) {
                final SimpleAttribute src = (SimpleAttribute) entity;
                return new SimpleAttribute(
                    src.getParent(),
                    src.getQualifiedName(),
                    src.getValue(),
                    src.getNamespace(),
                    src.isRequired());
            } else if (entity.getClass() == FixedAttribute.class) {
                final FixedAttribute src = (FixedAttribute) entity;
                return new FixedAttribute(
                    src.getParent(),
                    src.getQualifiedName(),
                    src.getValue(),
                    src.getNamespace(),
                    src.isRequired());
            } else if (entity.getClass() == Simple.class) {
                final Simple src = (Simple) entity;
                final Simple simple = new Simple(
                    src.getParent(),
                    src.getQualifiedName(),
                    src.getValue());
                /* Good enough since they never change */
                simple.setRestrictions(src.getRestrictions());
                /* simple.setValue(getValue()); // not needed for our purpose */
                return simple;
            } else if (entity.getClass() == SequenceGroup.class) {
                final SequenceGroup src = (SequenceGroup) entity;
                final SequenceGroup newSequence = new SequenceGroup(
                    src.getParent());
                for (final Entity e : src.getChildren()) {
                    ModelUtils.groupAdd(
                        newSequence,
                        cloneEntity(e));
                }
                newSequence.setConstraints(src.getConstraints());
                return newSequence;
            } else if (entity.getClass() == Repeated.class) {
                final Repeated src = (Repeated) entity;
                final Repeated rep = new Repeated(
                    src.getParent(),
                    src.getBaseEntity());
                return rep;
            } else if (entity.getClass() == Optional.class) {
                final Optional src = (Optional) entity;
                return new Optional(
                    src.getParent(),
                    src.getBaseTerm());
            } else if (entity.getClass() == List.class) {
                final List src = (List) entity;
                final List newl = new List(
                    src.getListType(),
                    src.getQualifiedName(),
                    src.getNamespace());
                newl.setListType(src.getListType());
                return newl;
            } else if (entity.getClass() == ImplicitGroup.class) {
                final ImplicitGroup src = (ImplicitGroup) entity;
                final ImplicitGroup newHidden = new ImplicitGroup(
                    src.getParent());
                for (final Entity e : src.getChildren()) {
                    ModelUtils.groupAdd(
                        newHidden,
                        cloneEntity(e));
                }
                newHidden.setConstraints(src.getConstraints());
                return newHidden;
            } else if (entity.getClass() == Extra.class) {
                /* inappropriate */
                return null;
            } else if (entity.getClass() == ElementDeclaration.class) {
                final ElementDeclaration src = (ElementDeclaration) entity;
                final ElementDeclaration ed = new ElementDeclaration(
                    src.getParent(),
                    src.getQualifiedName(),
                    src.getNamespace());
                if (src.getAttributes() != null) {
                    ed.setAttributes((AttributesList) src
                        .getAttributes()
                        .clone());
                    for (int i = 0; i < ed.getAttributes().size(); i++) {
                        ElementAttribute vCloneEntity =
                            (ElementAttribute) cloneEntity(ed
                                .getAttributes()
                                .get(
                                    i));
                        ed.getAttributes().set(
                            i,
                            vCloneEntity);
                    }
                    ed.getAttributes().setAttributesParent(
                        ed);
                }
                if (src.hasGroup()) {
                    ed.setGroup(cloneEntity(src.getGroup()));
                }
                ed.setSubtitutables(src.getSubstitutables());
                ed.setConstraints(src.getConstraints());
                return ed;
            } else if (entity.getClass() == Any.class) {
                final Any src = (Any) entity;
                final Any ed = new Any(
                    src.getParent(),
                    src.getQualifiedName(),
                    src.getNamespace());
                ed.setConstraints(src.getConstraints());
                return ed;
            } else if (entity.getClass() == Comment.class) {
                final Comment src = (Comment) entity;
                return new Comment(
                    src.getParent(),
                    src.getComment());
            } else if (entity.getClass() == ChoiceGroup.class) {
                final ChoiceGroup src = (ChoiceGroup) entity;
                final ChoiceGroup newChoice = new ChoiceGroup(
                    src.getParent());
                for (final Entity e : src.getChildren()) {
                    ModelUtils.groupAdd(
                        newChoice,
                        cloneEntity(e));
                }
                newChoice.setConstraints(src.getConstraints());
                return newChoice;
            } else if (entity.getClass() == AllGroup.class) {
                final AllGroup src = (AllGroup) entity;
                final AllGroup newAll = new AllGroup(
                    src.getParent());
                for (final Entity e : src.getChildren()) {
                    ModelUtils.groupAdd(
                        newAll,
                        cloneEntity(e));
                }
                newAll.setConstraints(src.getConstraints());
                return newAll;
            } else {

                Method clone;
                clone = ReflectionUtils.getClassMethodByName(
                    entity.getClass(),
                    "cloneEntity");
                if ((clone) != null) {
                    try {
                        return (Entity) ReflectionUtils.callMethodWithParams(
                            entity,
                            clone);
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }

                Reporting.logUnexpected(
                    "%1$s",
                    entity);
                return null;
            }
        } else {
            return null;
        }
    }

    /* CHECKSTYLE ON: MethodLength */

    /**
     * Checks if is present.
     * @param entity the entity
     * @return true, if is present
     */
    public static boolean isPresent(
        final Entity entity) {
        if (entity.getClass() == Union.class) {
            final Union src = (Union) entity;
            for (final Simple c : src.getChildren()) {
                if (isPresent(c)) {
                    return true;
                }
            }
            return (src.getValue() != null)
                && (src.getValue().trim().length() > 0);
        } else if (entity.getClass() == SimpleAttribute.class) {
            final SimpleAttribute src = (SimpleAttribute) entity;
            return (src.getValue() != null)
                && (src.getValue().trim().length() > 0);
        } else if (entity.getClass() == FixedAttribute.class) {
            final FixedAttribute src = (FixedAttribute) entity;
            return (src.getValue() != null)
                && (src.getValue().trim().length() > 0);
        } else if (entity.getClass() == Simple.class) {
            final Simple src = (Simple) entity;
            return (src.getValue() != null)
                && (src.getValue().trim().length() > 0);
        } else if (entity.getClass() == SequenceGroup.class) {
            final SequenceGroup src = (SequenceGroup) entity;
            for (final Entity e : src.getChildren()) {
                if ((e != null)
                    && isPresent(e)) {
                    return true;
                }
            }
            return false;
        } else if (entity.getClass() == Repeated.class) {
            final Repeated src = (Repeated) entity;
            for (final Entity e : src.getEntities()) {
                if (isPresent(e)) {
                    return true;
                }
            }
            return false;
        } else if (entity.getClass() == Optional.class) {
            final Optional src = (Optional) entity;
            return src.isSetTermPresent()
                && isPresent(src.getSetTerm());
        } else if (entity.getClass() == List.class) {
            final List src = (List) entity;
            return (src.getValue() != null)
                && (src.getValue().trim().length() > 0);
        } else if (entity.getClass() == ImplicitGroup.class) {
            final ImplicitGroup src = (ImplicitGroup) entity;
            for (final Entity e : src.getChildren()) {
                if ((e != null)
                    && isPresent(e)) {
                    return true;
                }
            }
            return false;
        } else if (entity.getClass() == Extra.class) {
            return true;
        } else if (entity.getClass() == ElementDeclaration.class) {
            final ElementDeclaration src = (ElementDeclaration) entity;
            if (src.hasAttributes()) {
                for (final ElementAttribute ea : src.getAttributes()) {
                    if (ModelUtils.isPresent(ea)) {
                        return true;
                    }
                }
            }
            return ((src.hasGroup() && isPresent(src.getGroup())));
        } else if (entity.getClass() == Comment.class) {
            return true;
        } else if (entity.getClass() == ChoiceGroup.class) {
            final ChoiceGroup src = (ChoiceGroup) entity;
            return (src.getSelected() != -1)
                && (src.getSelected() < src.getChildren().size())
                && isPresent(src.getChildren().get(
                    src.getSelected()));
        } else if (entity.getClass() == AllGroup.class) {
            final AllGroup src = (AllGroup) entity;
            for (final Entity e : src.getChildren()) {
                if ((e != null)
                    && isPresent(e)) {
                    return true;
                }
            }
            return false;
        } else if (entity.getClass() == Any.class) {
            final Any src = (Any) entity;
            return (src.getValue() != null)
                && (src.getValue().trim().length() > 0);
        } else {
            Object ret;
            try {
                ret = ReflectionUtils.callMethodByNameWithParams(
                    entity,
                    "isPresent");
                return (ret != null)
                    && ret.equals(Boolean.TRUE);
            } catch (final Exception e) {
                Reporting.reportUnexpected(e);
                return false;
            }
        }
    }

    /* == Visitor implementation == */

    /**
     * Accept.
     * @param entity the entity
     * @param visitor the visitor
     */
    // CHECKSTYLE OFF: MethodLength
    public static void accept(
        final Entity entity,
        final ModelVisitor visitor) {
        if (entity.getClass() == Union.class) {
            final Union src = (Union) entity;
            visitor.preVisitUnion(
                src,
                visitor);
            for (final Simple me : src.getChildren()) {
                accept(
                    me,
                    visitor);
            }
            visitor.postVisitUnion(
                src,
                visitor);
        } else if (entity instanceof ElementAttribute) {
            final ElementAttribute src = (ElementAttribute) entity;
            visitor.visitElementAttribute(
                src,
                visitor);
        } else if (entity.getClass() == Simple.class) {
            final Simple src = (Simple) entity;
            visitor.preVisitSimple(
                src,
                visitor);
            if (src.getRestrictions() != null) {
                for (final Restriction r : src.getRestrictions()) {
                    visitor.visitRestriction(
                        r,
                        visitor);
                }
            }
            visitor.postVisitSimple(
                src,
                visitor);
        } else if (entity.getClass() == SequenceGroup.class) {
            final SequenceGroup src = (SequenceGroup) entity;
            visitor.preVisitSequenceGroup(
                src,
                visitor);
            for (final Entity e : src.getChildren()) {
                accept(
                    e,
                    visitor);
            }
            visitor.postVisitSequenceGroup(
                src,
                visitor);
        } else if (entity.getClass() == Repeated.class) {
            final Repeated src = (Repeated) entity;
            visitor.preVisitRepeated(
                src,
                visitor);
            for (final Entity e : src) {
                accept(
                    e,
                    visitor);
            }
            visitor.postVisitRepeated(
                src,
                visitor);
        } else if (entity.getClass() == Optional.class) {
            final Optional src = (Optional) entity;
            visitor.preVisitOptional(
                src,
                visitor);
            if (src.isSetTermPresent()) {
                accept(
                    src.getSetTerm(),
                    visitor);
            }
            visitor.postVisitOptional(
                src,
                visitor);
        } else if (entity.getClass() == List.class) {
            final List src = (List) entity;
            visitor.preVisitList(
                src,
                visitor);
            if (src.getListType() != null) {
                accept(
                    src.getListType(),
                    visitor);
            }
            visitor.postVisitList(
                src,
                visitor);
        } else if (entity.getClass() == ImplicitGroup.class) {
            final ImplicitGroup src = (ImplicitGroup) entity;
            visitor.preVisitImplicitGroup(
                src,
                visitor);
            for (final Entity e : src.getChildren()) {
                accept(
                    e,
                    visitor);
            }
            visitor.postVisitImplicitGroup(
                src,
                visitor);
        } else if (entity.getClass() == Extra.class) {
            final Extra src = (Extra) entity;
            visitor.visitExtra(
                src,
                visitor);
        } else if (entity.getClass() == ElementDeclaration.class) {
            final ElementDeclaration src = (ElementDeclaration) entity;
            visitor.preVisitElementDeclaration(
                src,
                visitor);
            if (src.getAttributes() != null) {
                final AttributesList attributes = src.getAttributes();
                visitor.preVisitAttributesList(
                    attributes,
                    visitor);
                for (final ElementAttribute ea : attributes) {
                    accept(
                        ea,
                        visitor);
                }
                visitor.postVisitAttributesList(
                    attributes,
                    visitor);
            }
            if (src.getGroup() != null) {
                accept(
                    src.getGroup(),
                    visitor);
            }
            visitor.postVisitElementDeclaration(
                src,
                visitor);
        } else if (entity.getClass() == Comment.class) {
            final Comment src = (Comment) entity;
            visitor.visitComment(
                src,
                visitor);
        } else if (entity.getClass() == ChoiceGroup.class) {
            final ChoiceGroup src = (ChoiceGroup) entity;
            visitor.preVisitChoiceGroup(
                src,
                visitor);
            for (final Entity e : src.getChildren()) {
                accept(
                    e,
                    visitor);
            }
            visitor.postVisitChoiceGroup(
                src,
                visitor);
        } else if (entity.getClass() == AllGroup.class) {
            final AllGroup src = (AllGroup) entity;
            visitor.preVisitAllGroup(
                src,
                visitor);
            for (final Entity e : src.getChildren()) {
                accept(
                    e,
                    visitor);
            }
            visitor.postVisitAllGroup(
                src,
                visitor);
        } else if (entity.getClass() == Any.class) {
            final Any src = (Any) entity;
            visitor.visitAny(
                src,
                visitor);
        } else {
            Reporting.logUnexpected();
        }
    }
    // CHECKSTYLE ON: MethodLength

}
