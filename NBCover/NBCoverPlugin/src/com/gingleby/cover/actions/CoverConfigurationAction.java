package com.gingleby.cover.actions;

import com.gingleby.cover.ui.CoverConfigurationPanel;
import java.awt.event.ActionEvent;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.loaders.DataObject;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;

@ActionID(
        category = "Cover",
        id = "com.gingleby.cover.actions.CoverConfigurationAction"
)
@ActionRegistration(
        displayName = "Cover",
        lazy = false
)
@ActionReferences({
    @ActionReference(path = "Projects/org-netbeans-modules-apisupport-project/Actions", position = 3185),
    @ActionReference(path = "Projects/org-netbeans-modules-apisupport-project-suite/Actions", position = 2585),
    @ActionReference(path = "Projects/org-netbeans-modules-java-j2seproject/Actions", position = 2485),
    @ActionReference(path = "Projects/org-netbeans-modules-web-project/Actions", position = 2485)
})
public class CoverConfigurationAction extends AbstractAction implements LookupListener, ContextAwareAction {
    private Lookup context;
    private Lookup.Result<DataObject> lkpInfo;
 
    public CoverConfigurationAction() {
        this(Utilities.actionsGlobalContext());
    }
 
    private CoverConfigurationAction(Lookup context) {
        putValue(Action.NAME, "Cover Configuration...");
        this.context = context;
    }
 
    void init() {
        if (lkpInfo != null) {
            return;
        }
 
        lkpInfo = context.lookupResult(DataObject.class);
        lkpInfo.addLookupListener(this);
        
        resultChanged(null);
    }
 
    @Override
    public boolean isEnabled() {
        init();
        return super.isEnabled();
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        init();
        Collection<? extends DataObject> dataObjects = lkpInfo.allInstances();
        Project project = FileOwnerQuery.getOwner(dataObjects.iterator().next().getPrimaryFile());
        
        CoverConfigurationPanel panel = new CoverConfigurationPanel(project);
        NotifyDescriptor nd = new DialogDescriptor(panel, "Cover Configuration");
        DialogDisplayer.getDefault().notify(nd);
    }
 
    @Override
    public void resultChanged(LookupEvent ev) {
        setEnabled(true); // temp
    }
 
    @Override
    public Action createContextAwareInstance(Lookup context) {
        return new CoverConfigurationAction(context);
    }
}