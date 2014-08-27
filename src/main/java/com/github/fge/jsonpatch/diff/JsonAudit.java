package com.github.fge.jsonpatch.diff;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.NodeType;
import com.github.fge.jackson.jsonpointer.JsonPointer;

import java.util.List;

import static com.github.fge.jsonpatch.diff.DiffOperation.REPLACE;

/**
 * User: kgignatyev
 * Date: 8/27/14
 */
public class JsonAudit extends JsonDiff {


    /**
         * Generate differences between source and target node.
         *
         * @param diffs list of differences (in order)
         * @param path parent path for both nodes
         * @param source source node
         * @param target target node
         */
         void generateDiffs(final List<Diff> diffs,
            final JsonPointer path, final JsonNode source, final JsonNode target)
        {
            /*
             * If both nodes are equivalent, there is nothing to do
             */
            if (EQUIVALENCE.equivalent(source, target))
                return;

            /*
             * Get both node types. We shortcut to a simple replace operation in the
             * following scenarios:
             *
             * - nodes are not the same type; or
             * - they are the same type, but are not containers (ie, they are
             *   neither objects nor arrays).
             */
            final NodeType sourceType = NodeType.getNodeType(source);
            final NodeType targetType = NodeType.getNodeType(target);
            if (sourceType != targetType || !source.isContainerNode()) {
                diffs.add(Diff.simpleDiff(REPLACE, path, target,source));
                return;
            }

            /*
             * At this point, both nodes are either objects or arrays. Call the
             * appropriate diff generation methods.
             */

            if (sourceType == NodeType.OBJECT)
                generateObjectDiffs(diffs, path, source, target);
            else // array
                generateArrayDiffs(diffs, path, source, target);
        }


}
