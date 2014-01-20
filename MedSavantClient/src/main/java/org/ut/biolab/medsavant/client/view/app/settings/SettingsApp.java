package org.ut.biolab.medsavant.client.view.app.settings;

import javax.swing.ImageIcon;
import org.ut.biolab.medsavant.client.geneset.GeneSetPage;
import org.ut.biolab.medsavant.client.ontology.OntologyPage;
import org.ut.biolab.medsavant.client.project.ProjectManagementPage;
import org.ut.biolab.medsavant.client.reference.ReferenceGenomePage;
import org.ut.biolab.medsavant.client.user.UserManagementPage;
import org.ut.biolab.medsavant.client.variant.VariantFilesPage;
import org.ut.biolab.medsavant.client.view.images.IconFactory;
import org.ut.biolab.medsavant.client.view.subview.AppSubSection;
import org.ut.biolab.medsavant.client.view.subview.MultiSectionApp;

/**
 *
 * @author mfiume
 */
public class SettingsApp extends MultiSectionApp {

    public SettingsApp() {
        super("Settings");
    }

     @Override
    public AppSubSection[] getSubSections() {
        return new AppSubSection[] {
            new UserManagementPage(this),
            new ProjectManagementPage(this),
            new VariantFilesPage(this),
            new AnnotationsPage(this),
            new OntologyPage(this),
            new ReferenceGenomePage(this),
            new GeneSetPage(this),
            new ServerLogPage(this) };
    }

    @Override
    public ImageIcon getIcon() {
        return IconFactory.getInstance().getIcon(IconFactory.StandardIcon.APP_ADMIN);
    }

}
