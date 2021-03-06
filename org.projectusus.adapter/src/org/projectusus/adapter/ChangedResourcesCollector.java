// Copyright (c) 2009-2010 by the projectusus.org contributors
// This software is released under the terms and conditions
// of the Eclipse Public License (EPL) 1.0.
// See http://www.eclipse.org/legal/epl-v10.html for details.
package org.projectusus.adapter;

import static org.eclipse.core.resources.IResourceDelta.ADDED;
import static org.eclipse.core.resources.IResourceDelta.CHANGED;
import static org.eclipse.core.resources.IResourceDelta.MARKERS;
import static org.eclipse.core.resources.IResourceDelta.REMOVED;
import static org.projectusus.adapter.TracingOption.RESOURCE_CHANGES;
import static org.projectusus.core.project2.UsusProjectSupport.asUsusProject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.projectusus.core.util.FileSupport;

class ChangedResourcesCollector implements IResourceDeltaVisitor {

    private final List<IProject> removedProjects = new ArrayList<IProject>();
    private final Map<IProject, List<IFile>> changes = new HashMap<IProject, List<IFile>>();
    private final Map<IProject, List<IFile>> deletions = new HashMap<IProject, List<IFile>>();

    public boolean visit( IResourceDelta delta ) throws CoreException {
        boolean result = true;
        IResource resource = delta.getResource();
        if( handleRemovedProject( delta ) || isNonUsusProject( resource ) ) {
            RESOURCE_CHANGES.trace( "Removed " + resource.getFullPath() ); //$NON-NLS-1$
            removedProjects.add( (IProject)resource );
            result = false; // ignore the entire delta
        } else if( resource instanceof IFile ) {
            handleFileDelta( delta, (IFile)resource );
        }
        return result;
    }

    private boolean isNonUsusProject( IResource resource ) {
        return resource instanceof IProject && !(asUsusProject( (IProject)resource ).isUsusProject());
    }

    private boolean handleRemovedProject( IResourceDelta delta ) {
        boolean result = false;
        IResource resource = delta.getResource();
        if( resource instanceof IProject ) {
            IProject project = (IProject)resource;
            result = wasClosed( delta, project ) || isDeleted( delta );
        }
        return result;
    }

    private boolean wasClosed( IResourceDelta delta, IProject project ) {
        return isOpenCloseStatusChanged( delta ) && !project.isOpen();
    }

    private void handleFileDelta( IResourceDelta delta, IFile file ) {
        if( !FileSupport.isJavaFile( file ) )
            return;

        if( isInteresting( delta ) ) {
            RESOURCE_CHANGES.trace( "Changed file " + file.getFullPath() ); //$NON-NLS-1$
            addToMap( file, changes );
        } else if( isDeleted( delta ) ) {
            RESOURCE_CHANGES.trace( "Deleted file " + file.getFullPath() ); //$NON-NLS-1$
            addToMap( file, deletions );
        }
    }

    private void addToMap( IFile file, Map<IProject, List<IFile>> collector ) {
        IProject project = file.getProject();
        if( !collector.containsKey( project ) ) {
            collector.put( project, new ArrayList<IFile>() );
        }
        collector.get( project ).add( file );
    }

    private boolean isOpenCloseStatusChanged( IResourceDelta delta ) {
        return (delta.getFlags() & IResourceDelta.OPEN) != 0;
    }

    private boolean isDeleted( IResourceDelta delta ) {
        int kind = delta.getKind();
        return (kind & REMOVED) != 0;
    }

    private boolean isInteresting( IResourceDelta delta ) {
        int kind = delta.getKind();
        boolean changed = (kind & CHANGED) != 0;
        boolean added = (kind & ADDED) != 0;
        if( !(added || changed) ) {
            return false;
        }

        boolean onlyMarkers = delta.getFlags() == MARKERS;
        if( onlyMarkers ) {
            IMarkerDelta[] markerDeltas = delta.getMarkerDeltas();
            for( IMarkerDelta iMarkerDelta : markerDeltas ) {
                boolean markerComesFromJdt = "JDT".equals( iMarkerDelta.getAttribute( "sourceId", null ) ); //$NON-NLS-1$//$NON-NLS-2$
                if( markerComesFromJdt ) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public List<IProject> getRemovedProjects() {
        return removedProjects;
    }

    public Map<IProject, List<IFile>> getChanges() {
        return changes;
    }

    public Map<IProject, List<IFile>> getDeletions() {
        return deletions;
    }
}
