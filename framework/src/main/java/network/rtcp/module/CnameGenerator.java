package network.rtcp.module;

import javax.xml.bind.DatatypeConverter;
import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com)
 */
public class CnameGenerator {

    ////////////////////////////////////////////////////////////
    // FUNCTIONS
    public static String generateCname() {
        // generate unique identifier
        UUID uuid = UUID.randomUUID();
        // get the 64 least significant bits
        long leastSignificantBits = uuid.getLeastSignificantBits();
        // get the 64 most significant bits
        long mostSignificantBits = uuid.getMostSignificantBits();

        // convert the 128 bits to byte array
        // note: 128 / 8 = 16 bytes
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(leastSignificantBits).putLong(mostSignificantBits);
        buffer.flip();

        // get the 96 least significant bits
        // note: 96 / 8 = 12
        byte[] data = new byte[12];
        buffer.get(data, 0, 12);

        // convert the least 96 bits to ASCII Base64
        return DatatypeConverter.printBase64Binary(data);
    }
    ////////////////////////////////////////////////////////////

}
