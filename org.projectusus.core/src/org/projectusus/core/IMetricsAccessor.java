package org.projectusus.core;

import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.projectusus.core.basis.CodeProportionKind;
import org.projectusus.core.basis.CodeProportionUnit;
import org.projectusus.core.basis.GraphNode;
import org.projectusus.core.basis.YellowCountResult;
import org.projectusus.core.internal.proportions.rawdata.MetricsResultVisitor;

public interface IMetricsAccessor {

    int getNumberOf( CodeProportionUnit unit );

    int getNumberOf( IProject project, CodeProportionUnit unit );

    int getOverallMetric( CodeProportionKind metric );

    int getOverallMetric( IProject project, CodeProportionKind metric );

    int getViolationCount( IProject project, CodeProportionKind metric );

    Set<GraphNode> getAllClassRepresenters();

    Set<GraphNode> getAllPackages();

    Set<GraphNode> getAllCrossPackageClasses();

    double getRelativeACD();

    int getNumberOfWarnings( IFile file );

    YellowCountResult getWarnings();

    int getNumberOfProjectsViolatingCW();

    void acceptAndGuide( MetricsResultVisitor visitor );

}