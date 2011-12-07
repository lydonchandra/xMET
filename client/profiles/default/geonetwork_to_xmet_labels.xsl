<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output omit-xml-declaration="yes" method="xml" />
<xsl:template match="/">
<display_items>
<xsl:for-each select="labels/element">
    <item>
        <name><xsl:value-of select="@name"/></name>
        <displayName><xsl:value-of select="label"/></displayName>
        <description><xsl:value-of select="description"/></description>
    </item>
</xsl:for-each>
</display_items>
</xsl:template>

</xsl:stylesheet>
