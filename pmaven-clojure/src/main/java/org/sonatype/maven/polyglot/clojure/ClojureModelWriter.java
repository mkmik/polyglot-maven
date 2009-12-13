package org.sonatype.maven.polyglot.clojure;

import clojure.lang.Var;
import clojure.lang.RT;

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
        return plugin.getExecutions() != null;
    }

    public void buildDependencyString(ClojurePrintWriter out, Dependency dependency) {

        if (isExtendedDependency(dependency)) {

            out.printAtNewIndent("[" + MessageFormat.format("\"{0}:{1}:{2}\"",
                    dependency.getGroupId(),
                    dependency.getArtifactId(),
                    dependency.getVersion()) + " {");

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

            out.printLnAtCurrent(MessageFormat.format("\"{0}:{1}:{2}\"",
                    dependency.getGroupId(),
                    dependency.getArtifactId(),
                    dependency.getVersion()));
        }

    }

    public void buildPluginString(ClojurePrintWriter out, Plugin plugin) {

        if (isExtendedPlugin(plugin)) {
            out.printAtNewIndent("[");
            out.printLnAtCurrent(MessageFormat.format("\"{0}:{1}:{2}\"",
                    plugin.getGroupId(),
                    plugin.getArtifactId(),
                    plugin.getVersion()));

            if (!plugin.getExecutions().isEmpty()) {

                for (PluginExecution execution : plugin.getExecutions()) {
                    out.printAtCurrent("(plugin-execution");
                    out.printAtCurrent(" \"" + execution.getId() + "\"");
                    out.printAtCurrent(" \"" + execution.getPhase() + "\"");

                    for (String goal : execution.getGoals()) {
                        out.printAtCurrent(" \"" + goal + "\"");
                    }
                    out.printLnAtCurrent(")");

                }
                out.append("]");
                out.popIndent();
            }

            out.popIndent();

        } else {

            out.printLnAtCurrent(MessageFormat.format("\"{0}:{1}:{2}\"",
                    plugin.getGroupId(),
                    plugin.getArtifactId(),
                    plugin.getVersion()));
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
