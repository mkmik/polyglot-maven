package org.sonatype.maven.polyglot.clojure;

import com.google.common.base.Join;
import com.google.common.base.Nullable;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.maven.model.*;
import org.apache.maven.model.io.ModelWriter;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.sonatype.maven.polyglot.io.ModelWriterSupport;

import java.io.*;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Component(role = ModelWriter.class, hint = "clojure")
public class ClojureModelWriter extends ModelWriterSupport {

    private boolean isExtendedDependency(Dependency dependency) {
        return (dependency.getType() != null && !"jar".equals(dependency.getType()))
                || dependency.getClassifier() != null
                || !dependency.getExclusions().isEmpty();
    }

    public void buildDependencyString(ClojurePrintWriter out, Dependency dependency) {

        if (isExtendedDependency(dependency)) {

            String dep = MessageFormat.format("\"{0}:{1}",
                    dependency.getGroupId(),
                    dependency.getArtifactId());

            if (dependency.getVersion() != null) {
                dep += ":" + dependency.getVersion();
            }
            dep += "\"";

            out.printAtNewIndent("[" + dep + " {");
            out.printField(":classifier", dependency.getClassifier());

            if (!"jar".equals(dependency.getType())) {
                out.printField(":type", dependency.getType());
            }

            if (!dependency.getExclusions().isEmpty()) {

                out.printAtNewIndent(":exclusions [");
                for (Exclusion exclusion : dependency.getExclusions()) {

                    out.printLnAtCurrent(
                            "\"" + exclusion.getGroupId() + ":" +
                                    exclusion.getArtifactId() + "\"");
                }
                out.append("]");
                out.popIndent();
            }

            out.append("}]");
            out.popIndent();

        } else {

            String dep = MessageFormat.format("[\"{0}:{1}",
                    dependency.getGroupId(),
                    dependency.getArtifactId());

            if (dependency.getVersion() != null) {
                dep += ":" + dependency.getVersion();
            }

            dep += "\"]";

            out.printLnAtCurrent(dep);

        }

    }

    private void writeDom(ClojurePrintWriter out, Xpp3Dom dom) {

        if (dom.getChildCount() == 0) {
            out.printLnAtCurrent("\"" + dom.getName() + "\" \"" + dom.getValue() + "\"");
        } else {
            out.printAtNewIndent("\"" + dom.getChild(0).getName() + "\" [");

            boolean pad = false;
            for (Xpp3Dom xpp3Dom : dom.getChildren()) {
                if (pad) {
                    out.printAtCurrent(" ");
                } else {
                    pad = true;
                }
                out.printAtCurrent("\"" + xpp3Dom.getValue() + "\"");
            }

            out.printLnAtCurrent("]");
            out.popIndent();
        }
    }

    private boolean pluginHasConfiguration(Plugin plugin) {
        return plugin.getConfiguration() != null
                && ((Xpp3Dom) plugin.getConfiguration()).getChildCount() != 0;
    }

    private boolean pluginExecutionHasConfiguration(PluginExecution pluginExecution) {
        return pluginExecution.getConfiguration() != null
                && ((Xpp3Dom) pluginExecution.getConfiguration()).getChildCount() != 0;
    }

    public void buildPluginString(ClojurePrintWriter out, Plugin plugin) {

        String ref = MessageFormat.format("\"{0}:{1}",
                plugin.getGroupId(),
                plugin.getArtifactId());

        if (plugin.getVersion() != null) {
            ref += ":" + plugin.getVersion();
        }

        ref += "\"";

        out.printAtNewIndent("[" + ref);

        if (!plugin.getExecutions().isEmpty()
                || pluginHasConfiguration(plugin)) {

            out.printAtNewIndent(" {");

            if (pluginHasConfiguration(plugin)) {
                appendConfiguration(out, plugin.getConfiguration());
            }

            if (!plugin.getExecutions().isEmpty()) {

                out.printAtNewIndent(":executions [");

                for (PluginExecution execution : plugin.getExecutions()) {
                    out.printAtNewIndent("{");
                    out.printField(":id", execution.getId());
                    out.printField(":phase", execution.getPhase());

                    if (pluginExecutionHasConfiguration(execution)) {
                        appendConfiguration(out, execution.getConfiguration());
                    }

                    if (execution.getGoals() != null && !execution.getGoals().isEmpty()) {
                        out.printLnAtCurrent(":goals [\"" + Join.join("\" \"", execution.getGoals()) + "\"]");
                    }

                    out.append("}");
                    out.popIndent();
                }
                out.append("]");
                out.popIndent();
            }
            out.append("}");
            out.popIndent();
        }
        out.append("]");
        out.popIndent();

    }

    private void appendConfiguration(ClojurePrintWriter out, Object con) {
        Xpp3Dom configuration = (Xpp3Dom) con;

        out.printAtNewIndent(":configuration {");

        for (Xpp3Dom xpp3Dom : configuration.getChildren()) {
            writeDom(out, xpp3Dom);
        }

        out.append("}");
        out.popIndent();
    }

    public void write(Writer writer, Map<String, Object> stringObjectMap, Model model) throws IOException {

        ClojurePrintWriter out = new ClojurePrintWriter(writer);

        out.printLnAtCurrent("(defproject main \"" + model.getGroupId() + ":" + model.getArtifactId() + ":" + model.getVersion() + "\"");
        out.pushIndent(4);
        out.printField(":model-version", model.getModelVersion());
        out.printField(":add-default-plugins", false);

        final Parent parent = model.getParent();
        if (parent != null) {
            out.printField(":parent", parent.getGroupId() + ":" + parent.getArtifactId() + ":" + parent.getVersion());
        }

        out
                .printField(":name", model.getName())
                .printField(":description", model.getDescription())
                .printField(":packaging", model.getPackaging())
                .printField(":url", model.getUrl())
                .printField(":inceptionYear", model.getInceptionYear());

        writeProperties(model.getProperties(), out);
        writeScm(model, out);
        writeDistributionManagement(model.getDistributionManagement(), out);
        writeCiManagement(model, out);
        writeIssueManagement(model, out);
        writeProfiles(model, out);
        writeDependencyManagement(model.getDependencyManagement(), out);
        writeDependencies(model.getDependencies(), out);
        writeModules(model.getModules(), out);
        writeBuild(model.getBuild(), out);

        out.print(")\n");
        out.flush();

    }

    private void writeDistributionManagement(DistributionManagement distributionManagement, ClojurePrintWriter out) {

        if (distributionManagement != null) {
            out.printAtNewIndent(":distribution-management {");
            out.printField(":download-url", distributionManagement.getDownloadUrl());
            out.printField(":status", distributionManagement.getStatus());

            if (distributionManagement.getRelocation() != null) {
                out.printField(":relocation", distributionManagement.getRelocation().toString());
            }

            if (distributionManagement.getRepository() != null) {
                out.printField(":repository", distributionManagement.getRepository().toString());
            }

            if (distributionManagement.getSnapshotRepository() != null) {
                out.printField(":snapshot-repository", distributionManagement.getSnapshotRepository().toString());
            }

            out.popIndent();
        }


    }

    private void writeScm(Model model, ClojurePrintWriter out) {

        if (model.getScm() != null) {
            out.printAtNewIndent(":scm {");
            out.printField(":connection", model.getScm().getConnection());
            out.printField(":developer-connection", model.getScm().getDeveloperConnection());
            out.printField(":tag", model.getScm().getTag());
            out.printField(":url", model.getScm().getUrl());
            out.append("}");
            out.popIndent();
        }

    }

    private void writeCiManagement(Model model, ClojurePrintWriter out) {

        if (model.getCiManagement() != null) {
            out.printAtNewIndent(":ci-management {");
            out.printField(":system", model.getCiManagement().getSystem());
            out.printField(":url", model.getCiManagement().getUrl());

            if (model.getCiManagement().getNotifiers() != null && !model.getCiManagement().getNotifiers().isEmpty()) {

                out.printAtNewIndent(":notifiers [");

                for (Notifier notifier : model.getCiManagement().getNotifiers()) {
                    out.printAtNewIndent("{");
                    out.printField(":address", notifier.getAddress());
                    out.printField(":type", notifier.getType());
                    out.printField(":send-on-error", Boolean.toString(notifier.isSendOnError()));
                    out.printField(":send-on-failure", Boolean.toString(notifier.isSendOnFailure()));
                    out.printField(":send-on-success", Boolean.toString(notifier.isSendOnSuccess()));
                    out.printField(":send-on-warning", Boolean.toString(notifier.isSendOnWarning()));
                    if (notifier.getConfiguration() != null && !notifier.getConfiguration().isEmpty()) {
                        out.printAtNewIndent(":configuration {");
                        for (Map.Entry<Object, Object> entry : notifier.getConfiguration().entrySet()) {
                            out.printLnAtCurrent(entry.getKey() + " " + entry.getValue());
                        }
                        out.popIndent();
                    }
                    out.popIndent();
                }
                out.popIndent();
            }
            out.append("}");
            out.popIndent();
        }

    }

    private void writeIssueManagement(Model model, ClojurePrintWriter out) {

        if (model.getIssueManagement() != null) {
            out.printAtNewIndent(":issue-management {");
            out.printField(":system", model.getIssueManagement().getSystem());
            out.printField(":url", model.getIssueManagement().getUrl());
            out.append("}");
            out.popIndent();
        }

    }

    private void writeProfiles(Model model, ClojurePrintWriter out) {
        if (model.getProfiles() != null && !model.getProfiles().isEmpty()) {

            out.printAtNewIndent(":profiles [");
            for (Profile profile : model.getProfiles()) {
                out.printAtNewIndent("{");
                out.printField(":id", profile.getId());
                out.printField(":source", profile.getSource());

//                profile.getActivation();
//                profile.getDependencyManagement();
//                profile.getRepositories();
//                profile.getPluginRepositories();
//                profile.getReporting();
//                profile.getReports();

                writeDistributionManagement(profile.getDistributionManagement(), out);
                writeProperties(profile.getProperties(), out);
                writeModules(profile.getModules(), out);
                writeDependencies(profile.getDependencies(), out);
                writeBuild(profile.getBuild(), out);
                out.append("}");
                out.popIndent();
            }
            out.append("]");
            out.popIndent();
        }
    }

    private void writeModules(List<String> modules, ClojurePrintWriter out) {
        if (modules != null && !modules.isEmpty()) {
            out.printLnAtCurrent(":modules [\"" + Join.join("\" \"", modules) + "\"]");
        }
    }

    private void writeBuild(final BuildBase buildBase, ClojurePrintWriter out) {
        if (buildBase != null && !buildBase.getPlugins().isEmpty()) {

            out.printField(":default-goal", buildBase.getDefaultGoal());
            out.printField(":final-name", buildBase.getFinalName());
            out.printField(":directory", buildBase.getDirectory());

            if (buildBase instanceof Build) {
                Build build = (Build) buildBase;
                out.printField(":output-directory", build.getOutputDirectory());
                out.printField(":script-source-directory", build.getScriptSourceDirectory());
                out.printField(":source-directory", build.getSourceDirectory());
                out.printField(":test-source-directory", build.getTestSourceDirectory());
                out.printField(":test-output-directory", build.getTestOutputDirectory());
            }

            out.printAtNewIndent(":plugins [");

            for (Plugin plugin : buildBase.getPlugins()) {
                buildPluginString(out, plugin);
            }

            out.print("]");
            out.popIndent();
        }
    }

    private void writeDependencyManagement(DependencyManagement dependencyManagement, ClojurePrintWriter out) {

        if (dependencyManagement != null
                && dependencyManagement.getDependencies() != null
                && !dependencyManagement.getDependencies().isEmpty()) {

            out.printAtNewIndent(":dependency-management {");
            writeDependencies(dependencyManagement.getDependencies(), out);
            out.print("}");
            out.popIndent();
        }

    }

    private void writeDependencies(List<Dependency> dependencies, ClojurePrintWriter out) {
        writeDependencies(dependencies, "compile", "", true, out);
        writeDependencies(dependencies, "provided", "provided-", out);
        writeDependencies(dependencies, "runtime", "runtime-", out);
        writeDependencies(dependencies, "test", "test-", out);
        writeDependencies(dependencies, "system", "system-", out);
        writeDependencies(dependencies, "import", "import-", out);
    }

    private void writeDependencies(List<Dependency> dependencies, final String scope, String prefix, ClojurePrintWriter out) {
        writeDependencies(dependencies, scope, prefix, false, out);
    }

    private void writeDependencies(List<Dependency> dependencies, final String scope, String prefix, final boolean includeNullScope, ClojurePrintWriter out) {

        Iterable<Dependency> scopedDependencies = Iterables.filter(dependencies, new Predicate<Dependency>() {
            public boolean apply(@Nullable Dependency dependency) {
                return includeNullScope
                  ? (scope.equals(dependency.getScope()) || dependency.getScope() == null)
                  : scope.equals(dependency.getScope());
            }
        });

        if (!Iterables.isEmpty(scopedDependencies)) {

            out.printAtNewIndent(":" + prefix + "dependencies [");

            for (Dependency dependency : scopedDependencies) {
                buildDependencyString(out, dependency);
            }

            out.print("]");
            out.popIndent();
        }
    }

    private void writeProperties(Properties properties, ClojurePrintWriter out) {
        if (properties != null && !properties.isEmpty()) {

            out.printAtNewIndent(":properties {");
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                out.printLnAtCurrent("\"" + entry.getKey() + "\" \"" + entry.getValue() + "\"");
            }

            out.print("}");
            out.popIndent();

        }
    }
}
