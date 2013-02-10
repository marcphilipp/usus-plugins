package org.projectusus.core.proportions.rawdata.jdtdriver.acd;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.projectusus.core.filerelations.model.WrappedTypeBinding;
import org.projectusus.metrics.ACDCollector;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class ACDInspector extends ACDCollector {

    private Multimap<String, String> typeConnections = ArrayListMultimap.create();

    @Override
    public void connectTypes( WrappedTypeBinding sourceType, WrappedTypeBinding targetType ) {
        typeConnections.put( pathOf( sourceType ), pathOf( targetType ) );
    }

    @Override
    public void initRawDataTree( AbstractTypeDeclaration node ) {
        // do nothing
    }

    private String pathOf( WrappedTypeBinding aType ) {
        return aType.getPackagename().toString() + "/" + aType.getClassname().toString();
    }

    public Multimap<String, String> getTypeConnections() {
        return typeConnections;
    }
}