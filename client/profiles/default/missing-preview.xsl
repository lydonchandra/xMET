<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" gco:isoType="gmd:MD_Metadata"
	xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:gco="http://www.isotc211.org/2005/gco"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:gml="http://www.opengis.net/gml"
	xmlns:gts="http://www.isotc211.org/2005/gts" xmlns:gmx="http://www.isotc211.org/2005/gmx"
	xmlns:xlink="http://www.w3.org/1999/xlink">
  <xsl:output method="html" version="4.0" encoding="UTF-8"
		indent="yes" />
  <xsl:template match="*">s</xsl:template>
  <xsl:template match="/">
    <html>
      <head>
        <style type="text/css">
          table { font-family: "Arial",sans-serif ; font-size: 9px; }
          th {text-align: left; vertical-align: text-top; background-color: #dddddd;}
          td {margin-left: 5px;background-color: #ffffff;}
        </style>
      </head>
      <body>
        <div style="font-size: 15px; color: red;">
          <b>
            WARNING: XMET SPECIFIC PROFILE IDENTIFICATION METADATA IS
            MISSING FROM THIS FILE.
          </b>
        </div>
        <div style="font-size: 15px; color: red;">
          <b>WARNING: RAW XML PREVIEW.</b>
        </div>
        <xsl:apply-templates />
      </body>
    </html>
  </xsl:template>
  <xsl:template match="node()">
    <xsl:if test="name() != ''">
      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <th>
            <xsl:value-of select="normalize-space(name())" />
          </th>
        </tr>
        <xsl:apply-templates select="@*|child::node()" />
        <xsl:if test="normalize-space(node()) != ''">
          <tr>
            <td>
              <xsl:value-of select="normalize-space(node())" />
            </td>
          </tr>
        </xsl:if>
      </table>
    </xsl:if>
    <xsl:if test="name() = '' and normalize-space(node()) != ''">
      <tr>
        <td>
          <xsl:value-of select="normalize-space(node())" />_
        </td>
      </tr>
    </xsl:if>
  </xsl:template>

  <xsl:template match="@*">
    <tr>
      <td>
        <xsl:value-of select="name()" />=<xsl:value-of select="normalize-space(current())" />
      </td>
    </tr>
  </xsl:template>


</xsl:stylesheet>
