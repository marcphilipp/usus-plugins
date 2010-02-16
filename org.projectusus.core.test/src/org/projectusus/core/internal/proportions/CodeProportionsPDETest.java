// Copyright (c) 2009-2010 by the projectusus.org contributors
// This software is released under the terms and conditions
// of the Eclipse Public License (EPL) 1.0.
// See http://www.eclipse.org/legal/epl-v10.html for details.
package org.projectusus.core.internal.proportions;

import static org.eclipse.core.runtime.jobs.Job.getJobManager;
import static org.junit.Assert.assertEquals;
import static org.projectusus.core.internal.UsusCorePlugin.getUsusModel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;
import org.projectusus.core.internal.UsusCorePlugin;
import org.projectusus.core.internal.proportions.model.CodeProportion;
import org.projectusus.core.internal.proportions.model.IUsusElement;
import org.projectusus.core.internal.proportions.modelcomputation.CodeProportionsComputerJob;
import org.projectusus.core.internal.proportions.modelupdate.ComputationRunModelUpdate;
import org.projectusus.core.internal.proportions.modelupdate.IUsusModelHistory;

public class CodeProportionsPDETest {

    @Before
    public void setUp() throws Exception {
        getJobManager().join( CodeProportionsComputerJob.FAMILY, new NullProgressMonitor() );
    }
    
    @Test
    public void listenerHandling() throws InterruptedException {
        DummyCodeProportionsListener listener = new DummyCodeProportionsListener();
        getUsusModel().addUsusModelListener( listener );

        getUsusModelWriteAccess().update( new ComputationRunModelUpdate( new ArrayList<CodeProportion>(), true ) );
        assertEquals( 1, listener.getCallCount() );

        getUsusModel().removeUsusModelListener( listener );
        getUsusModelWriteAccess().update( new ComputationRunModelUpdate( new ArrayList<CodeProportion>(), true ) );
        // no more calls
        assertEquals( 1, listener.getCallCount() );
    }

    private IUsusModelWriteAccess getUsusModelWriteAccess() {
        return UsusCorePlugin.getDefault().getUsusModelWriteAccess();
    }

    private final class DummyCodeProportionsListener implements IUsusModelListener {
        private int callCount;

        public void ususModelChanged( IUsusModelHistory history, List<IUsusElement> elements ) {
          callCount++;
        }

        public int getCallCount() {
            return callCount;
        }
    }
}
