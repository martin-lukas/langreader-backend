package net.langreader.controller;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.net.URL;

@RestController
@RequestMapping("/api/ext")
public class ExternalRestController {
    @GetMapping
    public ResponseEntity<String> getExternalResource(@RequestParam(value = "url") String url) {
        try {
            InputSource is = new InputSource();
            is.setEncoding("UTF-8");
            is.setByteStream(new URL(url).openStream());
            String text = ArticleExtractor.INSTANCE.getText(is);
            return new ResponseEntity<>(text, HttpStatus.OK);
        } catch (IOException | BoilerpipeProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
