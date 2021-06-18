package com.radix.challenge.merkletree.impl;

import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Bytes;
import lombok.Data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Data
public class MerkleTree {
    private final List<byte[]> pieces;
    private final MerkleTreeNode root;

    @Data
    public static class MerkleTreeNode {
        private MerkleTreeNode left;
        private MerkleTreeNode right;
        private byte[] hash;

        public MerkleTreeNode(MerkleTreeNode left, MerkleTreeNode right, byte[] hash) {
            this.hash = hash;
            this.left = left;
            this.right = right;
        }

        public MerkleTreeNode(byte[] hash) {
            this.hash = hash;
        }

        @Override
        public String toString() {
            return toHex(hash);
        }
    }

    public MerkleTree(List<byte[]> pieces) {
        this.pieces = Collections.unmodifiableList(pieces);
        this.root = buildTree();
    }

    public void printTree() {
        Queue<MerkleTreeNode> queue = new ArrayDeque<>();
        queue.add(root);
        int count = 0;
        int level = 0;
        while (!queue.isEmpty()) {
            MerkleTreeNode node = queue.poll();
            System.out.println("Level: " + level + " node: " + toHex(node.getHash()));
            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
            count++;
            if (count >= Math.pow(2, level)) {
                count = 0;
                level++;
            }

        }
    }

    public static boolean hasPath(MerkleTreeNode root, List<MerkleTreeNode> path, byte[] hash) {
        if (root == null) {
            return false;
        }

        path.add(root);

        if (Arrays.equals(root.getHash(), hash)) {
            return true;
        }

        if (hasPath(root.left, path, hash) ||
                hasPath(root.right, path, hash)) {
            return true;
        }
        path.remove(path.size() - 1);
        return false;
    }

    public List<String> getProofs(int pieceIndex) {
        List<String> proofs = new ArrayList<>();
        byte[] hash = getSHA256(pieces.get(pieceIndex));
        List<MerkleTreeNode> path = new ArrayList<>();
        hasPath(root, path, hash);
        List<MerkleTreeNode> proofNodes = new ArrayList<>();
        for (int i = path.size() - 1; i > 0; i--) {
            MerkleTreeNode parent = path.get(i - 1);
            if (parent.getLeft().equals(path.get(i))) {
                proofNodes.add(parent.getRight());
            } else {
                proofNodes.add(parent.getLeft());
            }
        }

        for (MerkleTreeNode node : proofNodes) {
            proofs.add(toHex(node.getHash()));
        }

        return proofs;
    }

    private static String toHex(byte[] input) {
        return BaseEncoding.base16().lowerCase().encode(input);
    }

    public int getPiecesSize() {
        return pieces.size();
    }

    private MerkleTreeNode buildTree() {
        Queue<MerkleTreeNode> queue = new ArrayDeque<>();
        for (byte[] piece : pieces) {
            queue.add(new MerkleTreeNode(getSHA256(piece)));
        }
        int width = 1;
        while (width < pieces.size()) {
            width *= 2;
        }
        int fillerSize = width - pieces.size();

        byte[] fillerHash = new byte[32];
        while (fillerSize > 0) {
            queue.add(new MerkleTreeNode(fillerHash));
            fillerSize--;
        }
        while (queue.size() > 1) {
            MerkleTreeNode left = queue.poll();
            MerkleTreeNode right = queue.poll();
            MerkleTreeNode node = new MerkleTreeNode(left, right, getSHA256(Bytes.concat(left.getHash(), right.getHash())));
            queue.offer(node);
        }

        return queue.poll();
    }

    private byte[] getSHA256(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(input);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new UnsupportedOperationException("SHA-256 algorithm is not supported");
        }
    }
}
