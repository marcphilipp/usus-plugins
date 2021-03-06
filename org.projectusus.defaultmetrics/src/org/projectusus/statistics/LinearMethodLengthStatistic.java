package org.projectusus.statistics;

import org.projectusus.core.basis.MetricsResults;
import org.projectusus.core.basis.SourceCodeLocation;
import org.projectusus.core.statistics.CockpitExtension;

public class LinearMethodLengthStatistic extends CockpitExtension {

    public static final int ML_LIMIT = 9;

    private double linearViolations = 0.0;

    public LinearMethodLengthStatistic() {
        super( codeProportionUnit_METHOD_label, ML_LIMIT );
    }

    @Override
    public void inspectMethod( SourceCodeLocation location, MetricsResults result ) {
        int methodLength = valueForMethod( result );
        addResult( location, methodLength );
        int exceedingLines = methodLength - ML_LIMIT;
        if( exceedingLines > 0 ) {
            linearViolations += ((double)exceedingLines / ML_LIMIT);
        }
    }

    public int valueForMethod( MetricsResults result ) {
        return result.getIntValue( MetricsResults.ML );
    }

    @Override
    public double getAverage() {
        return calculateAverage( linearViolations, getBasis() );
    }

    @Override
    protected String hotspotsAreUnits() {
        return format( "with more than %d statements.", ML_LIMIT );
    }

    @Override
    public String getLabel() {
        return "Method length"; //$NON-NLS-1$
    }

    @Override
    protected String getTooltip() {
        return "The underlying metric determines the number of statements in each method body.\n" //$NON-NLS-1$
                + "This value is similar to the number of lines of code, but it ignores empty lines, comments and single brackets.\n" + getDescription(); //$NON-NLS-1$
    }

}
