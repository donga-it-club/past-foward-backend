package aws.retrospective.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class QRCodeService {

    public byte[] generateQRCode(String invitationUrl, LocalDateTime expirationTime) {
        try {
            // Set QR code parameters
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            // Append expiration time to QR code text
            String qrCodeText = invitationUrl + "&expiration=" + expirationTime.toString();

            // Generate QR code matrix
            BitMatrix bitMatrix = new MultiFormatWriter().encode(qrCodeText, BarcodeFormat.QR_CODE, 200, 200, hints);

            // Convert matrix to image
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            return outputStream.toByteArray();
        } catch (WriterException | IOException e) {
            // Handle exceptions
            e.printStackTrace();
            return null;
        }
    }
}
