package com.example.checkhrbackend_v2.service;

import com.example.checkhrbackend_v2.model.Leaves;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
public class PdfService {


    private final TemplateEngine templateEngine;

    @Autowired
    LeaveService leaveService;

    public PdfService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public ByteArrayInputStream generatePdfReport(Long userId, int year, List<Leaves> leaves, String userName) throws DocumentException, IOException {
        Context context = new Context();
        userName = leaveService.getUserFullNameById(userId);
        context.setVariable("userName", userName);
        context.setVariable("year", year);
        context.setVariable("leaves", leaves);

        String html = templateEngine.process("leaveReport", context);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
