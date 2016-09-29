<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html" indent="yes"/>

    <xsl:template match="map">
        <HTML>
            <HEAD>
                <TITLE>Map</TITLE>
            </HEAD>
            <BODY>
                <xsl:apply-templates/>
            </BODY>
        </HTML>
    </xsl:template>


    <xsl:template match="entry">
        <xsl:value-of select="@key"/>=<xsl:value-of select="@value"/>
        <br></br>
    </xsl:template>


</xsl:stylesheet>