package org.sonatype.maven.polyglot.clojure;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Stack;

public class ClojurePrintWriter extends PrintWriter {
    private Stack<Integer> indents = new Stack<Integer>();
    private boolean needNewLine = false;

    ClojurePrintWriter(Writer out) {
        super(out);
        indents.push(0);
    }


    public ClojurePrintWriter printAtCurrent(String s) {
        if (needNewLine) {
            append("\n");
            for (int i = 0; i < indents.peek(); i++) {
                append(" ");
            }
            needNewLine = false;
        }
        append(s);
        return this;
    }

    public ClojurePrintWriter printLnAtCurrent(String s) {
        if (needNewLine) {
            append("\n");
            for (int i = 0; i < indents.peek(); i++) {
                append(" ");
            }
            needNewLine = false;
        }
        append(s);
        needNewLine = true;
        return this;
    }

    public ClojurePrintWriter printAtNewIndent(String s) {
        if (needNewLine) {
            append("\n");
            for (int i = 0; i < indents.peek(); i++) {
                append(" ");
            }
            needNewLine = false;
        }
        append(s);
        pushIndent(s.length());
        return this;
    }

    public ClojurePrintWriter pushIndent(Integer i) {
        indents.push(indents.peek() + i);
        return this;
    }

    public ClojurePrintWriter popIndent() {
        needNewLine = true;
        indents.pop();
        return this;
    }

    public ClojurePrintWriter printField(String fieldName, String fieldValue) {
        if (fieldValue != null) {
            printLnAtCurrent(fieldName + " \"" + fieldValue + "\"");
        }
        return this;
    }

    public ClojurePrintWriter printField(String fieldName, Boolean fieldValue) {
        if (fieldValue != null) {
            printLnAtCurrent(fieldName + " " + fieldValue);
        }
        return this;
    }

}
