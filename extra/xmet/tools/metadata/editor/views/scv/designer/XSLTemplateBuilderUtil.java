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

import java.util.Map;
import java.util.Stack;

import n.io.xml.JDOMXmlUtils;

import org.jdom.Element;
import org.jdom.Namespace;

import xmet.profiles.Profile;
import xmet.profiles.ProfileSchema;
import xmet.tools.metadata.editor.views.scv.model.ChoiceItem;
import xmet.tools.metadata.editor.views.scv.model.Choices;
import xmet.tools.metadata.editor.views.scv.model.DefaultModelPathVisitor;
import xmet.tools.metadata.editor.views.scv.model.EditorType;
import xmet.tools.metadata.editor.views.scv.model.Item;
import xmet.tools.metadata.editor.views.scv.model.RepeatedGroup;
import xmet.tools.metadata.editor.views.scv.model.RepeatedItem;
import xmet.tools.metadata.editor.views.scv.model.RepeatedPage;
import xmet.tools.metadata.editor.views.scv.model.Sheet;

/**
 * This Util goes through a model and generates a preview XSL Stylesheet that
 * converts a metadata record to preview HTML.
 * @author Nahid Akbar
 */
public class XSLTemplateBuilderUtil
    extends DefaultModelPathVisitor {

    /** The output buffer/builder. */
    private StringBuilder output;

    /** The profile. */
    private Profile profile;

    /** The elements. */
    private final Stack<Element> elements = new Stack<Element>();

    /**
     * Visit method that starts off the process.
     * @param sheet the sheet
     * @param aProfile the profile
     * @return the string
     */
    String visit(
        final Sheet sheet,
        final Profile aProfile) {
        this.profile = aProfile;
        output = new StringBuilder();
        sheet.accept(this);
        return output.toString();
    }

    /** The xsl namespace. */
    private final Namespace xslns = Namespace.getNamespace(
        "xsl",
        "http://www.w3.org/1999/XSL/Transform");

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitSheet(
        final Sheet sheet) {
        super.preVisitSheet(sheet);
        final Element styleSheet = new Element(
            "stylesheet",
            xslns);
        elements.push(styleSheet);
        // {
        styleSheet.setAttribute(
            "version",
            "1.0");
        final Map<String, ProfileSchema> namespaces =
            profile.getProfileSchemas();
        for (final Map.Entry<String, ProfileSchema> schema : namespaces
            .entrySet()) {
            styleSheet.addNamespaceDeclaration(Namespace.getNamespace(
                schema.getValue().getNamespacePrefix(),
                schema.getValue().getNamespaceUri()));
        }

        final Element aOutput = new Element(
            "output",
            xslns);
        // {
        aOutput.setAttribute(
            "method",
            "html");
        aOutput.setAttribute(
            "version",
            "4.0");
        aOutput.setAttribute(
            "encoding",
            "UTF-8");
        aOutput.setAttribute(
            "indent",
            "yes");
        // }
        styleSheet.addContent(aOutput);
        final Element template = new Element(
            "template",
            xslns);
        // {
        template.setAttribute(
            "match",
            "/");
        final Element html = new Element(
            "html");
        // {
        final Element head = new Element(
            "head");
        // {
        final Element style = new Element(
            "style");
        // {
        style.setAttribute(
            "type",
            "text/css");
        style.setText("body {"
            + "color: #000000;"
            + "background-color: #ffffff;"
            + "}"
            + "table {"
            + "border-width: 1px;"
            + "border-style: solid;"
            + "width: 100%;"
            + "}"
            + "th {"
            + "color: #000000;"
            + "background-color: #eeeeee;"
            + " padding-left: 5px;"
            + "}");
        // }
        head.addContent(style);
        // }
        html.addContent(head);
        final Element body = new Element(
            "body");
        // {
        final Element table = new Element(
            "table");
        elements.add(table);
        body.addContent(table);
        // }
        html.addContent(body);
        // }
        template.addContent(html);
        // }
        styleSheet.addContent(template);
        // }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitSheet(
        final Sheet sheet) {
        // table tag
        elements.pop();
        final Element sheetEl = elements.pop();
        output.append(JDOMXmlUtils.indentedXmlFromElement(sheetEl));
        super.postVisitSheet(sheet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitItem(
        final Item item) {
        super.preVisitItem(item);
        String xpath = item.getXpath();
        final boolean titlePresent = item.getTitle() != null
            && item.getTitle().length() > 0;
        final boolean descriptionPresent = item.getDescription() != null
            && item.getDescription().length() > 0;
        final boolean hasType = item.getType() != EditorType.Unspecified;
        if (((xpath != null) && (xpath.trim().length() > 0))
            && item.isVisible()
            && hasType
            && (titlePresent || descriptionPresent)) {
            xpath = fixPath(xpath);
            final Element tr = new Element(
                "tr");
            // {
            Element th = new Element(
                "th");
            // {
            String varString = null;
            if (item.getTitle() != null) {
                varString = item.getTitle();
            } else {
                varString = item.getDescription();
            }
            th.setText(varString);
            // }
            tr.addContent(th);
            th = new Element(
                "td");
            // {
            final Element valueOf = new Element(
                "value-of",
                xslns);
            // {
            valueOf.setAttribute(
                "select",
                xpath);
            // }
            th.addContent(valueOf);
            // }
            tr.addContent(th);
            // }
            elements.peek().addContent(
                tr);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitRepeatedGroup(
        final RepeatedGroup repeated) {
        preVisitRepeated(repeated);
    }

    /**
     * Pre visit repeated.
     * @param <E> the element type
     * @param repeated the repeated
     */
    private <E> void preVisitRepeated(
        final RepeatedItem<E> repeated) {
        final String base = fixPath(repeated.getBase());
        final Element forEach = new Element(
            "for-each",
            xslns);
        elements.peek().addContent(
            forEach);
        // {
        forEach.setAttribute(
            "select",
            base);

        Element tr = new Element(
            "tr");
        forEach.addContent(tr);
        // {
        final Element td = new Element(
            "th");
        // {
        td.setAttribute(
            "colspan",
            "2");
        td.setText(repeated.getLabel());
        // }
        tr.addContent(td);
        // }

        tr = new Element(
            "tr");
        // {
        final Element td2 = new Element(
            "td");
        // {
        td2.setAttribute(
            "colspan",
            "2");
        final Element table = new Element(
            "table");
        elements.add(table);
        td2.addContent(table);
        // }
        tr.addContent(td2);
        // }
        forEach.addContent(tr);
        // }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitChoices(
        final Choices choices) {
        for (final ChoiceItem cd : choices.getItems()) {
            final Element ifElem = new Element(
                "if",
                xslns);
            elements.peek().addContent(
                ifElem);
            // {
            final String base = fixPath(cd.getTestXpath());
            ifElem.setAttribute(
                "test",
                base);

            Element tr = new Element(
                "tr");
            ifElem.addContent(tr);
            // {
            final Element td = new Element(
                "th");
            // {
            td.setAttribute(
                "colspan",
                "2");
            td.setText(cd.getLabel());
            // }
            tr.addContent(td);
            // }

            tr = new Element(
                "tr");
            ifElem.addContent(tr);
            // {
            final Element td2 = new Element(
                "td");
            // {
            td2.setAttribute(
                "colspan",
                "2");
            final Element table = new Element(
                "table");
            elements.add(table);
            td2.addContent(table);
            cd.getItem().accept(
                this);
            postVisitRepeated();
            // }
            tr.addContent(td2);
            // }
            // }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitRepeatedPage(
        final RepeatedPage repeated) {
        preVisitRepeated(repeated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitRepeatedPage(
        final RepeatedPage repeated) {
        postVisitRepeated();
        super.postVisitRepeatedPage(repeated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitRepeatedGroup(
        final RepeatedGroup repeated) {
        postVisitRepeated();
        super.postVisitRepeatedGroup(repeated);
    }

    /**
     * Post visit repeated.
     */
    private void postVisitRepeated() {
        elements.pop();
    }

    /**
     * Fix path.
     * @param basePath the base path
     * @return the string
     */
    private String fixPath(
        final String basePath) {
        String base = basePath;
        if ((base != null)
            && (base.trim().length() > 0)) {
            if (base.indexOf("$/") != -1) {
                base = base.replaceAll(
                    "\\$/",
                    "");
            }
            base = base.replaceAll(
                "@",
                "/@");
            if (base.indexOf("=") != -1) {
                base = base.replaceAll(
                    "=",
                    " = '")
                    + "'";
            }
        }
        return base;
    }

}
