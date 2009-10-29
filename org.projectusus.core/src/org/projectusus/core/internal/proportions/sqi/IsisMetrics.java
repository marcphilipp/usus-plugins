// Copyright (c) 2009 by the projectusus.org contributors
// This software is released under the terms and conditions
// of the Eclipse Public License (EPL) 1.0.
// See http://www.eclipse.org/legal/epl-v10.html for details.
package org.projectusus.core.internal.proportions.sqi;

import static org.projectusus.core.internal.util.CoreTexts.isisMetrics_acd;
import static org.projectusus.core.internal.util.CoreTexts.isisMetrics_cc;
import static org.projectusus.core.internal.util.CoreTexts.isisMetrics_cw;
import static org.projectusus.core.internal.util.CoreTexts.isisMetrics_kg;
import static org.projectusus.core.internal.util.CoreTexts.isisMetrics_ml;
import static org.projectusus.core.internal.util.CoreTexts.isisMetrics_pc;
import static org.projectusus.core.internal.util.CoreTexts.isisMetrics_ta;

public enum IsisMetrics {

    TA( isisMetrics_ta ), //
    PC( isisMetrics_pc ), //
    CC( isisMetrics_cc ) {
        @Override
        public boolean isViolatedBy( MethodResults methodResults ) {
            return methodResults.getCCResult() > MetricsLimits.CC_LIMIT;
        }

        @Override
        public boolean isMethodTest() {
            return true;
        }

        @Override
        public double getCalibration() {
            return 100.0;
        }
    },
    ACD( isisMetrics_acd ), //
    KG( isisMetrics_kg ) {
        @Override
        public boolean isViolatedBy( ClassResults classResult ) {
            return classResult.getResultCount() > MetricsLimits.KG_LIMIT;
        }

        @Override
        public boolean isMethodTest() {
            return false;
        }

        @Override
        public double getCalibration() {
            return 25.0;
        }
    }, //
    ML( isisMetrics_ml ) {
        @Override
        public boolean isViolatedBy( MethodResults methodResults ) {
            return methodResults.getMLResult() > MetricsLimits.ML_LIMIT;
        }

        @Override
        public boolean isMethodTest() {
            return true;
        }

        @Override
        public double getCalibration() {
            return 25.0;
        }
    }, //
    CW( isisMetrics_cw );

    private final String label;

    private IsisMetrics( String label ) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public boolean isViolatedBy( MethodResults methodResult ) {
        return false;
    }

    public boolean isViolatedBy( ClassResults methodResult ) {
        return false;
    }

    public boolean isMethodTest() {
        return false;
    }

    public double getCalibration() {
        throw new UnsupportedOperationException( "No computation yet for this metric." ); //$NON-NLS-1$
    }
}