<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns="http://www.w3.org/2000/svg"
  xmlns:svg="http://www.w3.org/2000/svg"
  xmlns:abc="http://php.net/xsl"
  xmlns:saxon="http://saxon.sf.net"/>
<xsl:output method="xml" indent="yes" standalone="no" doctype-public="-//W3C//DTD SVG 1.1//EN" doctype-system="http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd" media-type="text/plain" />

<xsl:template match="/svg:svg">
  <svg width="2000cm" height="2000cm"
    xmlns="http://www.w3.org/2000/svg">
    <out xmlns:env="clitype:System.Environment"
      xmlns:os="clitype:System.OperatingSystem">
      <xsl:value-of select="env:CurrentDirectory()"/>
    </out>
  </svg>
</xsl:template>
</xsl:stylesheet>