// Copyright (c) 2009-2010 by the projectusus.org contributors
// This software is released under the terms and conditions
// of the Eclipse Public License (EPL) 1.0.
// See http://www.eclipse.org/legal/epl-v10.html for details.
package org.projectusus.ui.viewer;

import static org.eclipse.swt.layout.GridData.FILL_BOTH;

import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;

public class UsusTreeViewer<T> extends TreeViewer {

    private final static int STYLE = SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION;

    public UsusTreeViewer( Composite parent, IColumnDesc<T>[] columns ) {
        super( new Tree( parent, STYLE ) );
        TableLayout layout = createTree();
        createColumns( layout, columns );
    }

    private TableLayout createTree() {
        TableLayout layout = new TableLayout();

        Tree tree = getTree();
        tree.setLayout( layout );
        tree.setLayoutData( new GridData( FILL_BOTH ) );
        tree.setLinesVisible( true );
        tree.setHeaderVisible( true );
        tree.layout( true );

        return layout;
    }

    private void createColumns( TableLayout layout, IColumnDesc<T>[] columns ) {
        for( int i = 0; i < columns.length; i++ ) {
            IColumnDesc<T> desc = columns[i];
            UsusTreeColumn ususTreeColumn = new AnnotationReader( desc ).compute();
            TreeViewerColumn column = createColumn( ususTreeColumn.align().toSwtStyle() );
            column.getColumn().setText( ususTreeColumn.header() );
            if( ususTreeColumn.sortable() ) {
                new ColumnByLabelSorter( this, column.getColumn(), i, ususTreeColumn.numeric() );
            }
            layout.addColumnData( new ColumnWeightData( ususTreeColumn.weight() ) );
        }
    }

    private TreeViewerColumn createColumn( int style ) {
        TreeViewerColumn column = new TreeViewerColumn( this, style );
        column.getColumn().setResizable( true );
        column.getColumn().setMoveable( true );
        return column;
    }

    public void resetColumnSorting() {
        getTree().setSortColumn( null );
        getTree().setSortDirection( SWT.NONE );
    }
}
