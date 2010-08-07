// Copyright (c) 2009-2010 by the projectusus.org contributors
// This software is released under the terms and conditions
// of the Eclipse Public License (EPL) 1.0.
// See http://www.eclipse.org/legal/epl-v10.html for details.
package org.projectusus.ui.internal.hotspots.pages;

import static java.util.Arrays.asList;
import static java.util.Collections.EMPTY_MAP;

import java.util.List;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.part.Page;
import org.projectusus.core.util.Defect;
import org.projectusus.ui.internal.AnalysisDisplayEntry;
import org.projectusus.ui.internal.DisplayHotspot;
import org.projectusus.ui.internal.hotspots.commands.AbstractOpenHotspotHandler;
import org.projectusus.ui.internal.selection.ExtractHotspots;
import org.projectusus.ui.viewer.UsusTreeViewer;

public class HotspotsPage extends Page implements IHotspotsPage {

    protected UsusTreeViewer<DisplayHotspot<?>> viewer;
    private final AnalysisDisplayEntry entry;
    private ViewerComparator comparator;

    public HotspotsPage( AnalysisDisplayEntry entry ) {
        super();
        this.entry = entry;
    }

    public boolean isInitialized() {
        return viewer != null;
    }

    protected IStructuredContentProvider createContentProvider() {
        return new HotspotsCP();
    }

    private void createViewer( Composite parent ) {
        HotspotsColumnDesc[] columnDescs = HotspotsColumnDesc.values();
        viewer = new UsusTreeViewer<DisplayHotspot<?>>( parent, columnDescs );
        viewer.setLabelProvider( new HotspotsLP( asList( columnDescs ) ) );
        viewer.setContentProvider( createContentProvider() );
        comparator = new ViewerComparator() {
            @Override
            public int compare( @SuppressWarnings( "unused" ) Viewer viewr, Object e1, Object e2 ) {
                DisplayHotspot<?> hotspot1 = (DisplayHotspot<?>)e1;
                DisplayHotspot<?> hotspot2 = (DisplayHotspot<?>)e2;
                int trend1 = hotspot1.getTrend();
                int trend2 = hotspot2.getTrend();
                if( trend1 < 0 || trend2 < 0 ) {
                    if( trend1 != trend2 ) {
                        return trend1 - trend2;
                    }
                }
                return hotspot2.getMetricsValue() - hotspot1.getMetricsValue();
            }
        };
        viewer.setComparator( comparator );

    }

    protected void initOpenListener() {
        viewer.addOpenListener( new IOpenListener() {
            public void open( OpenEvent event ) {
                List<DisplayHotspot<?>> hotspots = new ExtractHotspots( event.getSelection() ).compute();
                ICommandService service = (ICommandService)PlatformUI.getWorkbench().getService( ICommandService.class );
                Command command = service.getCommand( AbstractOpenHotspotHandler.COMMAND_ID );
                try {
                    command.executeWithChecks( new ExecutionEvent( command, EMPTY_MAP, event.getSource(), hotspots ) );
                } catch( NotHandledException ignore ) {
                    // to be ignored
                } catch( Exception exception ) {
                    throw new Defect( exception );
                }
            }
        } );
    }

    public void setInput( AnalysisDisplayEntry entry ) {
        viewer.setInput( entry );
    }

    @Override
    public void createControl( Composite parent ) {
        createViewer( parent );
        initOpenListener();
    }

    @Override
    public Control getControl() {
        return viewer.getControl();
    }

    @Override
    public void setFocus() {
        if( viewer != null && !viewer.getControl().isDisposed() ) {
            viewer.getControl().setFocus();
        }
    }

    public void refresh() {
        setInput( entry );
    }

    public boolean matches( AnalysisDisplayEntry otherEntry ) {
        return entry.isSameKindAs( otherEntry );
    }

    public void resetSort() {
        viewer.setComparator( comparator );
        viewer.resetColumnSorting();
    }

    public ISelectionProvider getSelectionProvider() {
        return viewer;
    }
}
