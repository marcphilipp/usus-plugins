// Copyright (c) 2009-2010 by the projectusus.org contributors
// This software is released under the terms and conditions
// of the Eclipse Public License (EPL) 1.0.
// See http://www.eclipse.org/legal/epl-v10.html for details.
package org.projectusus.ui.internal.hotspots.pages;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.projectusus.ui.internal.AnalysisDisplayEntry;

class HotspotsCP implements ITreeContentProvider {

    public Object[] getElements( Object inputElement ) {
        Object[] result = new Object[0];
        if( inputElement instanceof AnalysisDisplayEntry ) {
            result = ((AnalysisDisplayEntry)inputElement).getHotspots().toArray();
        }
        return result;
    }

    public void dispose() {
        // unused
    }

    public void inputChanged( @SuppressWarnings( "unused" ) Viewer viewer, @SuppressWarnings( "unused" ) Object oldInput, @SuppressWarnings( "unused" ) Object newInput ) {
        // unused
    }

    public Object[] getChildren( @SuppressWarnings( "unused" ) Object parentElement ) {
        return null;
    }

    public Object getParent( @SuppressWarnings( "unused" ) Object element ) {
        return null;
    }

    public boolean hasChildren( Object element ) {
        return getChildren( element ) != null && getChildren( element ).length > 0;
    }
}
