<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:exsl="http://exslt.org/common" extension-element-prefixes="exsl"
    xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:svg="http://www.w3.org/2000/svg" version="1.0" xmlns:rx="http://www.renderx.com/XSL/Extensions">
                
    <xsl:template match="/">
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
  <fo:layout-master-set>
  	<fo:simple-page-master master-name="my-page" page-width="12in" page-height="18in" >
      <fo:region-body margin="0.0in" background-image="url('C:\GP\backgrounds\SS_2up.pdf')"/>
    </fo:simple-page-master>
  </fo:layout-master-set>
  <fo:page-sequence master-reference="my-page">
    <fo:flow flow-name="xsl-region-body">
   
			<xsl:for-each select="/binnys/group">
<!--
    <fo:block-container reference-orientation="-90" overflow="hidden" break-after="page">
    <fo:block-container overflow="hidden" break-after="page">
-->
    <fo:block-container reference-orientation="-90" overflow="hidden" break-after="page">
    			<fo:block break-after="page" keep-with-previous = "auto" keep-together="always">
  
					<fo:table>
						<fo:table-column column-width="50%"/>
						<fo:table-column column-width="50%"/>
								<fo:table-body>
										<fo:table-row>
											<xsl:for-each select="file">
												<fo:table-cell >
													<xsl:choose>
														<xsl:when test="position()=1">
															<fo:block margin-left="0.5in" margin-top="0.5in">
																<fo:external-graphic src="url('{@directory}\{node()}')"/>
															</fo:block>
														</xsl:when>
														<xsl:otherwise>
															<fo:block margin-left="0in" margin-top="0.5in">
																<fo:external-graphic src="url('{@directory}\{node()}')"/>
															</fo:block>
														</xsl:otherwise>
													</xsl:choose>
													</fo:table-cell>
											</xsl:for-each>
								</fo:table-row>
																
						</fo:table-body>
					</fo:table>
      			</fo:block>
    </fo:block-container>      

			</xsl:for-each>
       
    </fo:flow>

  </fo:page-sequence>
</fo:root>	
</xsl:template>
</xsl:stylesheet>