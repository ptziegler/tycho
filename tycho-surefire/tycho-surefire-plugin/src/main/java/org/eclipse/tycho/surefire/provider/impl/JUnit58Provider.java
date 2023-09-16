/*******************************************************************************
 * Copyright (c) 2018, 2023 SAP SE and others.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     SAP SE - initial API and implementation
 *******************************************************************************/

package org.eclipse.tycho.surefire.provider.impl;

import static org.eclipse.tycho.surefire.provider.impl.ProviderHelper.newDependency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Component;
import org.eclipse.tycho.ClasspathEntry;
import org.eclipse.tycho.surefire.provider.spi.TestFrameworkProvider;
import org.osgi.framework.Version;
import org.osgi.framework.VersionRange;

@Component(role = TestFrameworkProvider.class, hint = "junit58")
public class JUnit58Provider extends AbstractJUnitProvider {

    private static final Version VERSION = Version.parseVersion("5.8.0");
    private boolean withVintage;

    @Override
    protected Set<String> getJUnitBundleNames() {
        return Set.of("org.junit.jupiter.api" /* legacy Orbit bundle */, "junit-jupiter-api");
    }

    @Override
    public String getSurefireProviderClassName() {
        return "org.apache.maven.surefire.junitplatform.JUnitPlatformProvider";
    }

    @Override
    public Version getVersion() {
        return VERSION;
    }

    @Override
    public List<Dependency> getRequiredBundles() {
        List<Dependency> requiredBundles = new ArrayList<>();
        requiredBundles.add(newDependency("org.eclipse.tycho", "org.eclipse.tycho.surefire.junit58"));
        if (withVintage) {
            requiredBundles.add(newDependency("org.eclipse.tycho", "org.eclipse.tycho.surefire.junit58withvintage"));
        }
        return Collections.unmodifiableList(requiredBundles);
    }

    @Override
    protected VersionRange getJUnitVersionRange() {
        return new VersionRange("[5.8,5.9)");
    }

    @Override
    public boolean isEnabled(MavenProject project, List<ClasspathEntry> testBundleClassPath, Properties surefireProperties) {
        withVintage = new JUnit47Provider().containsJunitInClasspath(testBundleClassPath);
        return super.isEnabled(project, testBundleClassPath, surefireProperties);
    }
}
