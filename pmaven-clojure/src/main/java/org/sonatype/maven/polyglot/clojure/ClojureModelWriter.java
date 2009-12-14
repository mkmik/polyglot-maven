package org.sonatype.maven.polyglot.clojure;

import clojure.lang.Var;
import clojure.lang.RT;

import com.google.common.base.Join;
import org.apache.maven.model.*;
import org.apache.maven.model.io.ModelWriter;
import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.maven.polyglot.io.ModelWriterSupport;

import java.io.*;
import java.text.MessageFormat;
import java.util.Map;

@Component(role = ModelWriter.class, hint = "clojure")
public class ClojureModelWriter extends ModelWriterSupport {

    private boolean isExtendedDependency(Dependency dependency) {
        return dependency.getScope() != null || dependency.getClassifier() != null ||
                !dependency.getExclusions().isEmpty();
    }

    private boolean isExtendedPlugin(Plugin plugin) {
        return plugin.getExecutions() != null && !plugin.getExecutions().isEmpty();
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
            out.printField("classifier", dependency.getClassifier());
            out.printField("scope", dependency.getScope());

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

    public void buildPluginString(ClojurePrintWriter out, Plugin plugin) {

        if (isExtendedPlugin(plugin)) {
            out.printAtNewIndent("[");

            String ref = MessageFormat.format("\"{0}:{1}",
                    plugin.getGroupId(),
                    plugin.getArtifactId());

            if (plugin.getVersion() != null) {
                ref += ":" + plugin.getVersion();
            }

            ref += "\"";

            out.printLnAtCurrent(ref);

            if (!plugin.getExecutions().isEmpty() || plugin.getConfiguration() != null) {

                out.printAtNewIndent("{");

                if (!plugin.getExecutions().isEmpty()) {

                    out.printAtNewIndent(":executions [");

                    for (PluginExecution execution : plugin.getExecutions()) {
                        out.printAtNewIndent("{");
                        out.printField("id", execution.getId());
                        out.printField("phase", execution.getPhase());

                        if (execution.getGoals() != null && !execution.getGoals().isEmpty()) {
                            out.printLnAtCurrent(":goals [\"" + Join.join(" ", execution.getGoals()) + "\"]");
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

        } else {

            String ref = MessageFormat.format("[\"{0}:{1}",
                    plugin.getGroupId(),
                    plugin.getArtifactId());

            if (plugin.getVersion() != null) {
                ref += ":" + plugin.getVersion();
            }

            ref += "\"]";

            out.printLnAtCurrent(ref);

        }

    }

    public void write(Writer writer, Map<String, Object> stringObjectMap, Model model) throws IOException {

        ClojurePrintWriter out = new ClojurePrintWriter(writer);

        out.printLnAtCurrent("(defproject \"" + model.getGroupId() + ":" + model.getArtifactId() + ":" + model.getVersion() + "\"");
        out.pushIndent(4);
        out.printField("model-version", model.getModelVersion());

        final Parent parent = model.getParent();
        if (parent != null) {
            out.printField("parent", parent.getGroupId() + ":" + parent.getArtifactId() + ":" + parent.getVersion());
        }

        out
                .printField("name", model.getName())
                .printField("description", model.getDescription())
                .printField("packaging", model.getPackaging())
                .printField("url", model.getUrl())
                .printField("inceptionYear", model.getInceptionYear());


        if (!model.getDependencies().isEmpty()) {

            out.printAtNewIndent(":dependencies [");

            for (Dependency dependency : model.getDependencies()) {
                buildDependencyString(out, dependency);
            }

            out.print("]");
            out.popIndent();
        }

        if (model.getBuild() != null && !model.getBuild().getPlugins().isEmpty()) {

            out.printAtNewIndent(":plugins [");

            for (Plugin plugin : model.getBuild().getPlugins()) {
                buildPluginString(out, plugin);
            }

            out.print("]");
            out.popIndent();
        }

        out.print(")\n");
        out.flush();

    }
}
