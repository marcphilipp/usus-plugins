// Copyright (c) 2009 by the projectusus.org contributors
// This software is released under the terms and conditions
// of the Eclipse Public License (EPL) 1.0.
// See http://www.eclipse.org/legal/epl-v10.html for details.
package org.projectusus.core.internal.proportions.sqi;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

public class ProjectResults extends Results<IFile, FileResults> {

    private final IProject projectOfResults;
    private IFile currentFile;

    public ProjectResults( IProject project ) {
        this.projectOfResults = project;
    }

    public IProject getProjectOfResults() {
        return projectOfResults;
    }

    public void setCurrentFile( IFile currentFile ) {
        this.currentFile = currentFile;
    }

    public FileResults getCurrentFileResults() {
        return getResults( currentFile, new FileResults( currentFile ) );
    }

    public void dropResults( IFile file ) {
        remove( file );
    }

}