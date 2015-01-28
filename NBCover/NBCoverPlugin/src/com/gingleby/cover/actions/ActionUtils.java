/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gingleby.cover.actions;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ActionProvider;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.Lookup;

/**
 *
 * @author graeme
 */
public class ActionUtils {
   /** Finds all projects in given lookup. If the command is not null it will check 
     * whether given command is enabled on all projects. If and only if all projects
     * have the command supported it will return array including the project. If there
     * is one project with the command disabled it will return empty array.
     */
    public static Project[] getProjectsFromLookup( Lookup lookup, String command ) {    
        // First find out whether there is a project directly in the Lookup
        Set<Project> result = new LinkedHashSet<>(); // XXX or use OpenProjectList.projectByDisplayName?
        for (Project p : lookup.lookupAll(Project.class)) {
            result.add(p);
        }
        // Now try to guess the project from dataobjects
        for (DataObject dObj : lookup.lookupAll(DataObject.class)) {
            FileObject fObj = dObj.getPrimaryFile();
            Project p = FileOwnerQuery.getOwner(fObj);
            if ( p != null ) {
                result.add( p );
            }
        }
        Project[] projectsArray = result.toArray(new Project[result.size()]);
                
        if ( command != null ) {
            // All projects have to have the command enabled
            for (Project p : projectsArray) {
                if (!commandSupported(p, command, lookup)) {
                    return new Project[0];
                }
            }
        }
        
        return projectsArray;
    }
     
    /** 
     * Tests whether given command is available on the project and whether
     * the action as to be enabled in current Context
     * @param project Project to test
     * @param command Command for test
     * @param context Lookup representing current context or null if context
     *                does not matter.
     */    
    public static boolean commandSupported( Project project, String command, Lookup context ) {
        //We have to look whether the command is supported by the project
        ActionProvider ap = project.getLookup().lookup(ActionProvider.class);
        if ( ap != null ) {
            List<String> commands = Arrays.asList(ap.getSupportedActions());
            if ( commands.contains( command ) ) {
                try {
                if (context == null || ap.isActionEnabled(command, context)) {
                    //System.err.println("cS: true project=" + project + " command=" + command + " context=" + context);
                    return true;
                }
                } catch (IllegalArgumentException x) {
                    Logger.getLogger(ActionUtils.class.getName()).log(Level.INFO, "#213589: possible race condition in MergedActionProvider", x);
                }
            }
        }            
        //System.err.println("cS: false project=" + project + " command=" + command + " context=" + context);
        return false;
    }
    
}
