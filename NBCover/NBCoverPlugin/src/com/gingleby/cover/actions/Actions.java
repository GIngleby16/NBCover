package com.gingleby.cover.actions;

import javax.swing.Action;
import javax.swing.JOptionPane;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.support.MainProjectSensitiveActions;
import org.netbeans.spi.project.ui.support.ProjectActionPerformer;

/**
 * Project & File Context Sensitive Actions
 * 
 * @author graeme
 */
public class Actions {

    /**
     * This action performs code coverage for the main project (using test goal)
     *
     * It is used in the ToolBar & Menu and has a custom name that shows the
     * project name
     *
     * @return
     */
    public static Action coverMainProjectTestAction() {
        Action a = MainProjectSensitiveActions.mainProjectSensitiveAction(new ProjectActionPerformer() {
            @Override
            public boolean enable(Project project) {
                return project != null;
            }

            @Override
            public void perform(Project project) {
                JOptionPane.showMessageDialog(null, "This will eventually kick off main project coverage");
            }
        }, "Cover {0,choice,0#Project|1#Project ({1})|1<{0} Projects}", null);
        a.putValue("iconBase", "com/gingleby/cover/actions/coverage.png");
        return a;
    }
}
