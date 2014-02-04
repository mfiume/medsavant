package org.ut.biolab.medsavant.client.view.genetics.variantinfo;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.ut.biolab.medsavant.client.util.ClientMiscUtils;
import org.ut.biolab.medsavant.client.view.component.KeyValuePairPanel;
import org.ut.biolab.medsavant.client.view.genetics.inspector.SubInspector;
import org.ut.biolab.medsavant.client.view.images.IconFactory;
import org.ut.biolab.medsavant.client.view.util.ViewUtil;

/**
 * Human Gene Mutation Database (HGMD) SubInspector to display variant details.
 * 
 * @author rammar
 */
public class HGMDSubInspector extends SubInspector {
	
	private static final String URL_CHARSET = "UTF-8";
	private final String HGMD_RSID_TEXT= "hgmd_pro_allmut, dbsnp";
	private final String HGMD_PMID_TEXT= "hgmd_pro_allmut, pmid";
	private final String HGMD_OMIM_TEXT= "hgmd_pro_allmut, omimid";
	private final String HGMD_DISEASE_TEXT= "hgmd_pro_allmut, disease";
	private final String HGMD_DESCRIPTION_TEXT= "hgmd_pro_allmut, descr";
	private final String HGMD_ACC_TEXT= "hgmd_pro_allmut, acc_num";
	private final String HGMD_COMMENTS_TEXT= "hgmd_pro_allmut, comments";
	private final String baseDBSNPUrl= "http://www.ncbi.nlm.nih.gov/SNP/snp_ref.cgi?searchType=adhoc_search&rs=";
	private final String baseOMIMUrl= "http://www.omim.org/entry/";
	private final String basePubmedUrl= "http://www.ncbi.nlm.nih.gov/pubmed/";
	
	private final int KEY_VALUE_PAIR_PANEL_ADDITIONAL_COLUMN_NUMBER= 4;
	private final String KEY_HGMD_RSID= "dbSNP ID";	
	private final String KEY_HGMD_OMIM= "OMIM Disease ID";
	private final String KEY_HGMD_DISEASE= "Disease Name";
	private final String KEY_HGMD_ACC= "HGMD Accession";
	private final String KEY_HGMD_PMID= "Pubmed ID";
	private final String KEY_HGMD_DESCR= "Description";
	private final String KEY_HGMD_COMMENTS= "HGMD Comments";	
	
	private Object[] currentLine;
	private KeyValuePairPanel p;
	
	// HGMD entry properties
	private String rsID;
	private String omimID;
	private String pubmedID;
	private String disease;
	private String accession;
	private String comments;
	private String description;
	
	public HGMDSubInspector() {
    }

    @Override
    public String getName() {
        return "HGMD Information";
    }
	
	@Override
    public JPanel getInfoPanel() {
		if (p == null) {
			p = new KeyValuePairPanel(KEY_VALUE_PAIR_PANEL_ADDITIONAL_COLUMN_NUMBER);
			
			p.addKey(KEY_HGMD_RSID);
			p.addKey(KEY_HGMD_OMIM);
			p.addKey(KEY_HGMD_DISEASE);
			p.addKey(KEY_HGMD_ACC);
			p.addKey(KEY_HGMD_PMID);
			p.addKey(KEY_HGMD_DESCR);
			p.addKey(KEY_HGMD_COMMENTS);
		}
		return p;
	}
	
	
	/**
	 * Sets the current variant line from the table for this subinspector
	 * @param line the current line from the table (unedited and unsliced)
	 * @param header the header for the table (unedited and unsliced)
	 */
	public void setVariantLine(Object[] line, List<String> header) {
		currentLine= line;

		getHGMDValues(header);
		
		// Create inspector table
		int buttonNumber;
		
		p.setValue(KEY_HGMD_ACC, accession);
		
		p.setValue(KEY_HGMD_RSID, rsID);
		buttonNumber= 0;
		p.setAdditionalColumn(KEY_HGMD_RSID, buttonNumber++, KeyValuePairPanel.getCopyButton(KEY_HGMD_RSID, p));
		p.setAdditionalColumn(KEY_HGMD_RSID, buttonNumber++, 
			getKeyValuePairPanelButton(KEY_HGMD_RSID, baseDBSNPUrl, p.getValue(KEY_HGMD_RSID), true));
		
		p.setValue(KEY_HGMD_DISEASE, disease);
		
		p.setValue(KEY_HGMD_OMIM, omimID);
		buttonNumber= 0;
		p.setAdditionalColumn(KEY_HGMD_OMIM, buttonNumber++, KeyValuePairPanel.getCopyButton(KEY_HGMD_OMIM, p));
		p.setAdditionalColumn(KEY_HGMD_OMIM, buttonNumber++, 
			getKeyValuePairPanelButton(KEY_HGMD_OMIM, baseOMIMUrl, omimID, true));
		
		p.setValue(KEY_HGMD_PMID, pubmedID);
		buttonNumber= 0;
		p.setAdditionalColumn(KEY_HGMD_PMID, buttonNumber++, KeyValuePairPanel.getCopyButton(KEY_HGMD_PMID, p));
		p.setAdditionalColumn(KEY_HGMD_PMID, buttonNumber++, 
			getKeyValuePairPanelButton(KEY_HGMD_PMID, basePubmedUrl, pubmedID, true));
		
		p.setValue(KEY_HGMD_DESCR, description);
		p.setValue(KEY_HGMD_COMMENTS, comments);
	}
	
	
	/**
	 * Gets all relevant HGMD fields.
	 * @param header the header for the table (unedited and unsliced)
	 */
	private void getHGMDValues(List<String> header) {
		resetHGMDFields();
		
		rsID= "";
		if (!(header.indexOf(HGMD_RSID_TEXT) == -1 || rsID == null || rsID.equals("\\N")))
			rsID= (String) currentLine[header.indexOf(HGMD_RSID_TEXT)];
			
		omimID= "";
		if (!(header.indexOf(HGMD_OMIM_TEXT) == -1 || omimID == null))
			omimID= (String) currentLine[header.indexOf(HGMD_OMIM_TEXT)];
		
		pubmedID= "";
		if (!(header.indexOf(HGMD_PMID_TEXT) == -1 || pubmedID == null))
			pubmedID= (String) currentLine[header.indexOf(HGMD_PMID_TEXT)];
		
		disease= "";
		if (!(header.indexOf(HGMD_DISEASE_TEXT) == -1 || disease == null))
			disease= ((String) currentLine[header.indexOf(HGMD_DISEASE_TEXT)]).substring(0, 15) + "..."; // Current workaround
				
		accession= "";
		if (!(header.indexOf(HGMD_ACC_TEXT) == -1 || accession == null))
			accession= (String) currentLine[header.indexOf(HGMD_ACC_TEXT)];
		
		comments= "";
		if (!(header.indexOf(HGMD_COMMENTS_TEXT) == -1 || comments == null))
			comments= ((String) currentLine[header.indexOf(HGMD_COMMENTS_TEXT)]).substring(0, 15) + "..."; // Current workaround
		
		description= "";
		if (!(header.indexOf(HGMD_DESCRIPTION_TEXT) == -1 || description == null))
			description= ((String) currentLine[header.indexOf(HGMD_DESCRIPTION_TEXT)]).substring(0, 15) + "..."; // Current workaround;
		
	}
	
	
	/**
	 * Reset the HGMD fields.
	 */
	private void resetHGMDFields() {
		// Reset to empty strings
		rsID= "";
		omimID= "";
		pubmedID= "";
		disease= "";
		accession= "";
		comments= "";
		description= "";
	}
	
	
	/**
	 * Create a button to search the web for this property.
	 * @param key
	 * @param baseUrl
	 * @param appendToUrl
	 * @return 
	 */
	private Component getKeyValuePairPanelButton(final String key, final String baseUrl, 
			final String appendToUrl, final boolean doEncode) {
		
		JButton ncbiButton = ViewUtil.getTexturedButton("", IconFactory.getInstance().getIcon(IconFactory.StandardIcon.LINKOUT));
		ncbiButton.setToolTipText("Lookup " + key + " on the web");
		ncbiButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					URL url;
					if (doEncode)
						url = new URL(baseUrl + URLEncoder.encode(appendToUrl, URL_CHARSET));
					else
						url = new URL(baseUrl + appendToUrl);
					
					java.awt.Desktop.getDesktop().browse(url.toURI());
				} catch (Exception ex) {
					ClientMiscUtils.reportError("Problem launching website: %s", ex);
				}
			}
		});

		return ncbiButton;
    }
	
}
