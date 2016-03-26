package com.projectdemo.basic.common.util;

import com.projectdemo.basic.common.exception.ProjectJcrException;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.*;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class contains logic to store related nodes.
 *
 * @author khoi.tran
 * @deprecated Not finished yet:
 * 1) When remove an item in related list, the relatedNodes of removed items is not updated, so we must handle this case.
 * 2) When a node in related group is deleted, we don't update the relatedNodes links of remain nodes. So when rendering related node, must check whether the links to relatedNodes are good or not.
 * 3) Cannot prevent user from saving relative to itself.
 * 3) Not refactor yet.
 */
public class JcrRelatedNodesHelper {

    public static final Logger LOGGER = LoggerFactory.getLogger(JcrRelatedNodesHelper.class);

    @Inject
    private JcrNodeHelper jcrNodeHelper;

    @Inject
    private JcrQueryHelper jcrQueryHelper;

    /**
     * @param oldNode                 the node with updated related nodes. We must get the old related nodes and remove this current from old related nodes.
     * @param propertyOfRelatedNodes
     * @param workspaceOfRelatedNodes
     * @param nodeTypeOfRelatedNodes
     */
    public void saveRelatedNodesGroup(JcrNodeAdapter jcrNodeAdapter, String propertyOfRelatedNodes, String workspaceOfRelatedNodes, String nodeTypeOfRelatedNodes) throws RepositoryException {
        if (!jcrNodeAdapter.isNode()) {
            LOGGER.warn("Cannot save related pages because jcrNodeAdapter is not a Node.");
            return;
        }

        Node oldNode = jcrNodeAdapter.getJcrItem();
        if (oldNode != null) {
            List<Node> relatedNodesToOldNode = getRelatedNodes(oldNode, propertyOfRelatedNodes, workspaceOfRelatedNodes, nodeTypeOfRelatedNodes);
            for (Node node : relatedNodesToOldNode) {
                removeNodeFromRelation(node, oldNode, propertyOfRelatedNodes);
            }
        }

        Node updatedNode = jcrNodeAdapter.applyChanges();
        String updatedNodeIdentifier = NodeUtil.getNodeIdentifierIfPossible(updatedNode);
        List<Node> relatedNodesToUpdatedNode = getRelatedNodes(updatedNode, propertyOfRelatedNodes, workspaceOfRelatedNodes, nodeTypeOfRelatedNodes);
        for (Node relatedNode : relatedNodesToUpdatedNode) {
            String relatedNodeIdentifier = NodeUtil.getNodeIdentifierIfPossible(relatedNode);
            if (!relatedNodeIdentifier.equals(updatedNodeIdentifier)) {
                List<String> relatedIdentifiersOfRelationNode = getRelatedNodeIdentifiers(relatedNode, propertyOfRelatedNodes);
                if (relatedIdentifiersOfRelationNode == null) {
                    relatedIdentifiersOfRelationNode = new ArrayList<>();
                }
                relatedIdentifiersOfRelationNode.add(updatedNodeIdentifier);
                relatedIdentifiersOfRelationNode = relatedIdentifiersOfRelationNode.stream().distinct().collect(Collectors.toList());
                saveRelatedNodesIdentifiers(relatedNode, relatedIdentifiersOfRelationNode, propertyOfRelatedNodes);
            }
        }
        //The updatedNode will be saved after finishing the current method (in SaveNodeWithRelationDialogAction).
    }
    private List<String> getRelatedNodeIdentifiers(Node node, String propertyOfRelatedNodes){
        return JcrPropertyHelper.tryGetListStrings(node, propertyOfRelatedNodes);
    }
    private void removeNodeFromRelation(Node node, Node removedNode, String propertyOfRelatedNodes) {
        String removedNodeIdentifier = jcrNodeHelper.getNodeIdentifier(removedNode);
        List<String> relatedNodesIds = getRelatedNodeIdentifiers(node, propertyOfRelatedNodes);
        if (relatedNodesIds != null) {
            relatedNodesIds.remove(removedNodeIdentifier);
            saveRelatedNodesIdentifiers(node, relatedNodesIds, propertyOfRelatedNodes);
        }
    }

    public List<Node> getRelatedNodes(Node currentNode, String propertyOfRelatedNodes, String workspaceOfRelatedNodes, String nodeTypeOfRelatedNodes) {
        List<String> relatedNodesIdsToCurrentNode = JcrPropertyHelper.tryGetListStrings(currentNode, propertyOfRelatedNodes);
        //Don't save relation to itself.
        String currentIdentifier = NodeUtil.getNodeIdentifierIfPossible(currentNode);
        if (currentIdentifier != null){
            relatedNodesIdsToCurrentNode.remove(currentIdentifier);
        }
        return jcrQueryHelper.searchNodesByUuids(workspaceOfRelatedNodes, nodeTypeOfRelatedNodes, relatedNodesIdsToCurrentNode);
    }

    private void saveRelatedNodesGroup(List<Node> relatedNodesGroups, String propertyOfRelatedNodes) {
        for (Node node : relatedNodesGroups) {
            List<Node> relatedNodes = getOtherNodesStream(relatedNodesGroups, node);
            saveRelatedNodes(node, relatedNodes, propertyOfRelatedNodes);
        }

    }

    private void saveRelatedNodes(Node node, List<Node> relatedNodes, String propertyOfRelatedNodes) {
        List<String> relatedNodesIdentifiers = relatedNodes.stream()
                .map(relatedNode -> jcrNodeHelper.getNodeIdentifier(relatedNode))
                .filter(includedNode -> includedNode != null)
                .distinct()
                .collect(Collectors.toList());
        saveRelatedNodesIdentifiers(node, relatedNodesIdentifiers, propertyOfRelatedNodes);
    }
    private void setRelatedNodesIdentifiers(Node node, List<String> relatedNodesIdentifiers, String propertyOfRelatedNodes) throws RepositoryException {
        node.setProperty(propertyOfRelatedNodes, relatedNodesIdentifiers.toArray(new String[0]));
    }
    private void saveRelatedNodesIdentifiers(Node node, List<String> relatedNodesIdentifiers, String propertyOfRelatedNodes) {
        try {
            setRelatedNodesIdentifiers(node, relatedNodesIdentifiers, propertyOfRelatedNodes);
            jcrNodeHelper.save(node);
        } catch (RepositoryException e) {
            String msg = String.format("Cannot save related nodes identifiers into node.\n Node: %s.\n Related nodes' identifiers: %s", node, relatedNodesIdentifiers);
            throw new ProjectJcrException(msg);
        }
    }

    private List<Node> getOtherNodesStream(List<Node> nodes, Node node) {
        return nodes.stream()
                .filter(otherNode -> !otherNode.equals(node))
                .distinct()
                .collect(Collectors.toList());
    }

}
