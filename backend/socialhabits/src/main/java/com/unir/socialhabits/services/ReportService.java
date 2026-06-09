package com.unir.socialhabits.services;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

import com.unir.socialhabits.dto.UserDetailDTO;
import com.unir.socialhabits.dto. HabitDTO;

import com.unir.socialhabits.entities.User;
import com.unir.socialhabits.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final UserRepository userRepository;
    private final UserService userService;

    public byte[] generateUserReport(UUID userId) {
        System.out.println("PDF START");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetailDTO dto = userService.toDetailDTO(user);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // ========================
            // TITLE
            // ========================
            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            document.add(new Paragraph("USER REPORT", titleFont));
            document.add(new Paragraph("Generated on: " + LocalDate.now()));
            document.add(new Paragraph(" "));

            // ========================
            // USER INFO
            // ========================
            Font sectionFont = new Font(Font.HELVETICA, 14, Font.BOLD);

            document.add(new Paragraph("USER INFORMATION", sectionFont));

            document.add(new Paragraph("Name: " + dto.getFirstName() + " " + dto.getLastName()));
            document.add(new Paragraph("Age: " + dto.getAge()));

            document.add(new Paragraph(
                    "General Notes: " +
                            (dto.getGeneralObservations() != null ? dto.getGeneralObservations() : "-")
            ));

            document.add(new Paragraph(" "));

            // ========================
            // EVOLUTION SUMMARY
            // ========================
            document.add(new Paragraph("EVOLUTION SUMMARY", sectionFont));

            document.add(new Paragraph("Global Status: " + dto.getHabitStatus()));
            document.add(new Paragraph("Risky habits today: " + dto.isRiskyHabitsToday()));
            document.add(new Paragraph("Missing habits today: " + dto.isMissingTodayHabits()));

            document.add(new Paragraph(" "));

            // ========================
            // HABITS
            // ========================
            document.add(new Paragraph("HABITS", sectionFont));

            if (dto.getHabits() == null || dto.getHabits().isEmpty()) {
                document.add(new Paragraph("No habits registered"));
            } else {
                dto.getHabits().forEach(h -> {
                    try {
                        String type = h.getType() != null ? h.getType().toString() : "-";
                        String status = h.getStatus() != null ? h.getStatus().toString() : "-";
                        String date = h.getDate() != null ? h.getDate().toString() : "";
                        String desc = h.getDescription() != null ? h.getDescription() : "";

                        document.add(new Paragraph(
                                "- " + type + " | " + status + " | " + date + " | " + desc
                        ));

                    } catch (Exception e) {
                        System.out.println("Error rendering habit: " + e.getMessage());
                    }
                });
            }

            document.add(new Paragraph(" "));

            // ========================
            // OBSERVATIONS
            // ========================
            document.add(new Paragraph("OBSERVATIONS", sectionFont));

            if (dto.getObservations() == null || dto.getObservations().isEmpty()) {
                document.add(new Paragraph("No observations"));
            } else {
                dto.getObservations().forEach(o -> {
                    try {
                        document.add(new Paragraph(
                                "- " +
                                        (o.getCreatedAt() != null ? o.getCreatedAt() : "") + " | " +
                                        (o.getProfessionalName() != null ? o.getProfessionalName() : "") + " | " +
                                        (o.getContent() != null ? o.getContent() : "")
                        ));
                    } catch (Exception e) {
                        System.out.println("Error rendering observation: " + e.getMessage());
                    }
                });
            }

            // ========================
            // COMPLETION RATE
            // ========================
            List<HabitDTO> habits =
                    dto.getHabits() != null ? dto.getHabits() : List.of();

            long total = habits.size();

            long correct = habits.stream()
                    .filter(h -> h.getStatus().name().equals("CORRECT"))
                    .count();

            long completionRate = total == 0 ? 0 : (correct * 100 / total);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Completion rate: " + completionRate + "%"));

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF report", e);
        } finally {
            if (document != null && document.isOpen()) {
                document.close();
            }
        }

        System.out.println("PDF SIZE = " + out.size());
        return out.toByteArray();
    }
}