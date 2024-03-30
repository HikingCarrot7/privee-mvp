package me.hikingcarrot7.privee.services;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.java.Log;
import net.glxn.qrgen.javase.QRCode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@ApplicationScoped
@Log
public class QRCodeImageGeneratorService {

  public BufferedImage tryToGenerateQRCodeImage(String textToEncode) {
    try {
      return generateQRCodeImage(textToEncode);
    } catch (Exception e) {
      log.severe("Error generating QR code image");
      throw new RuntimeException(e);
    }
  }

  public BufferedImage generateQRCodeImage(String textToEncode) throws Exception {
    ByteArrayOutputStream stream = QRCode
        .from(textToEncode)
        .withSize(250, 250)
        .stream();
    ByteArrayInputStream bis = new ByteArrayInputStream(stream.toByteArray());
    return ImageIO.read(bis);
  }

}
