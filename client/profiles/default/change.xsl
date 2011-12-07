<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes" />
	<xsl:template match="display_items">
    <xsl:element name="xsl:choose">
      <xsl:for-each select="item">
        <xsl:element name="xsl:when" >
          <xsl:attribute name="test">name() = '<xsl:value-of select="name" />'</xsl:attribute>
          <xsl:value-of select="displayName" />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
	</xsl:template>

</xsl:stylesheet>
