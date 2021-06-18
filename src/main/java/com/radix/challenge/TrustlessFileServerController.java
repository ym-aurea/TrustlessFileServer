package com.radix.challenge;

import com.google.common.io.BaseEncoding;
import com.radix.challenge.merkletree.impl.MerkleTree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController
@Slf4j
public class TrustlessFileServerController {
    @Autowired
    private Environment env;
    private MerkleTree merkleTree;
    public static final int PIECE_SIZE = 1024;

    @GetMapping(value = "/hashes")
    public List<Hash> getHashes() {
        return Collections.singletonList(new Hash(getSHA2HexValue(merkleTree.getRoot().getHash()), merkleTree.getPiecesSize()));
    }

    @GetMapping(value = "/piece/{hashId}/{pieceIndex}")
    public Piece getPieceByHashIdPieceIndex(@PathVariable("hashId") String hashId, @PathVariable("pieceIndex") int pieceIndex) {
        if (getSHA2HexValue(merkleTree.getRoot().getHash()).equals(hashId)) {
            return new Piece(Base64.getEncoder().encodeToString(merkleTree.getPieces().get(pieceIndex)), merkleTree.getProofs(pieceIndex));
        } else {
            throw new IllegalArgumentException(hashId + " is not valid hashId ");
        }
    }

    @PostConstruct
    public void init() throws IOException {
        buildMerkleTree();
    }

    private void buildMerkleTree() throws IOException {
        List<byte[]> pieces = new ArrayList<>();
        String path = env.getProperty("filePath");
        if (path == null) {
            Path resourceDirectory = Paths.get("src", "main", "resources");
            path = resourceDirectory.toFile().getAbsolutePath() + "/icons_rgb_circle.png";
        }
        try (InputStream stream = Files.newInputStream(Paths.get(path))) {
            byte[] piece;
            do {
                piece = stream.readNBytes(PIECE_SIZE);
                if (piece.length != 0) {
                    pieces.add(piece);
                }
            } while (piece.length != 0);
        }
        merkleTree = new MerkleTree(pieces);
    }

    private String getSHA2HexValue(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(input);
            return BaseEncoding.base16().lowerCase().encode(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new UnsupportedOperationException("SHA-256 algorithm is not supported");
        }
    }
}
