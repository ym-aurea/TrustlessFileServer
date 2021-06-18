import com.radix.challenge.Hash;
import com.radix.challenge.Piece;
import com.radix.challenge.TrustlessFileServer;
import com.radix.challenge.TrustlessFileServerController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static com.radix.challenge.TrustlessFileServerController.PIECE_SIZE;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TrustlessFileServer.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TrustlessFileServerControllerTest {

    @Autowired
    private TrustlessFileServerController trustlessFileServerController;

    @Autowired
    private Environment env;

    private final List<String> pieces = new ArrayList<>();

    private static final List<String> EXPECTED_PROOF = List.of("6a10a0b8c1bd3651cba6e5604b31df595e965be137650d296c05afc1084cfe1f",
            "956bf86d100b2f49a8d057ebafa85b8db89a0f19d5627a1226fea1cb3e23d3f3", "04284ddea22b003e6098e7dd1a421a565380d11530a35f2e711a8dd2b9b5e7f8",
            "c66a821b749e0576e54b89dbac8f71211a508f7916e3d6235900372bed6c6c22", "6afb77a17fd0b9b42e1e3c3762d9823797a776090d525d66672fb83c25ea9778");

    private static final String ROOT_HASH = "f7a6d7b7f4e7d8252039c5518ff5e05fe25625be50b5fc9ae94edd217a44ab0e";

    @BeforeAll
    private void setup() throws IOException {
        InputStream stream = TrustlessFileServerController.class.getClassLoader().getResourceAsStream("icons_rgb_circle.png");
        if (stream == null) {
            throw new FileNotFoundException(" is not found");
        }

        try (stream) {
            byte[] piece;
            do {
                piece = stream.readNBytes(PIECE_SIZE);
                if (piece.length != 0) {
                    pieces.add(Base64.getEncoder().encodeToString(piece));
                }
            } while (piece.length != 0);
        }
    }

    @Test
    public void givenFileWhenGetHashesThenExpectedResult() {
        List<Hash> hashes = trustlessFileServerController.getHashes();

        assertEquals(1, hashes.size());
        assertEquals(17, hashes.get(0).getPieces());
        assertEquals(ROOT_HASH, hashes.get(0).getHash());
    }

    @Test
    public void givenFileHashIdPieceIndexWhenGetPieceByHashIdPieceIndexThenExpectedResult() {
        Piece piece = trustlessFileServerController.getPieceByHashIdPieceIndex(ROOT_HASH, 8);

        assertEquals(pieces.get(8), piece.getContent());
        assertEquals(EXPECTED_PROOF, piece.getProof());
    }
}
