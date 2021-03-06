// Copyright (c) 2009-2010 by the projectusus.org contributors
// This software is released under the terms and conditions
// of the Eclipse Public License (EPL) 1.0.
// See http://www.eclipse.org/legal/epl-v10.html for details.
package org.projectusus.adapter;

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

/**
 * implementors can specify which projects and files to run when collecting code proportion info.
 * 
 * @author leiffrenzel
 */
public interface ICodeProportionComputationTarget {

    Collection<IProject> getProjects();

    Collection<IProject> getRemovedProjects();

    Collection<IFile> getFiles( IProject project ) throws CoreException;

    Collection<IFile> getRemovedFiles( IProject project ) throws CoreException;

    boolean isNotEmpty();
}
