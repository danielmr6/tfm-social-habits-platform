package com.unir.socialhabits.services;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

import com.unir.socialhabits.dto.UserDetailDTO;
import com.unir.socialhabits.dto. HabitDTO;

import com.unir.socialhabits.entities.User;
import com.unir.socialhabits.repositories.UserRepository;

import com.lowagie.text.pdf.PdfPTable;

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

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        UserDetailDTO dto =
                userService.toDetailDTO(user);

        ByteArrayOutputStream out =
                new ByteArrayOutputStream();

        Document document =
                new Document(PageSize.A4);

        try {

            PdfWriter.getInstance(
                    document,
                    out
            );

            document.open();

            Font titleFont =
                    new Font(
                            Font.HELVETICA,
                            20,
                            Font.BOLD
                    );

            Font sectionFont =
                    new Font(
                            Font.HELVETICA,
                            14,
                            Font.BOLD
                    );

            Font normalFont =
                    new Font(
                            Font.HELVETICA,
                            11
                    );

            // ==================================
            // HEADER
            // ==================================

            Paragraph title =
                    new Paragraph(
                            "USER EVOLUTION REPORT",
                            titleFont
                    );

            title.setAlignment(
                    Element.ALIGN_CENTER
            );

            document.add(title);

            Paragraph date =
                    new Paragraph(
                            "Generated on " +
                                    LocalDate.now()
                    );

            date.setAlignment(
                    Element.ALIGN_CENTER
            );

            document.add(date);

            document.add(
                    new Paragraph(" ")
            );

            // ==================================
            // USER INFORMATION
            // ==================================

            document.add(
                    new Paragraph(
                            "USER INFORMATION",
                            sectionFont
                    )
            );

            PdfPTable userTable =
                    new PdfPTable(2);

            userTable.setWidthPercentage(100);

            userTable.addCell("Name");

            userTable.addCell(
                    dto.getFirstName() +
                            " " +
                            dto.getLastName()
            );

            userTable.addCell("Age");

            userTable.addCell(
                    String.valueOf(
                            dto.getAge()
                    )
            );

            userTable.addCell(
                    "General Notes"
            );

            userTable.addCell(
                    dto.getGeneralObservations() != null
                            ? dto.getGeneralObservations()
                            : "-"
            );

            document.add(userTable);

            document.add(
                    new Paragraph(" ")
            );

            // ==================================
            // EVOLUTION SUMMARY
            // ==================================

            document.add(
                    new Paragraph(
                            "EVOLUTION SUMMARY",
                            sectionFont
                    )
            );

            PdfPTable summaryTable =
                    new PdfPTable(2);

            summaryTable.setWidthPercentage(
                    100
            );

            summaryTable.addCell(
                    "Global Status"
            );

            summaryTable.addCell(
                    String.valueOf(
                            dto.getHabitStatus()
                    )
            );

            summaryTable.addCell(
                    "Risky Habits Today"
            );

            summaryTable.addCell(
                    dto.isRiskyHabitsToday()
                            ? "YES"
                            : "NO"
            );

            summaryTable.addCell(
                    "Missing Habits Today"
            );

            summaryTable.addCell(
                    dto.isMissingTodayHabits()
                            ? "YES"
                            : "NO"
            );

            document.add(summaryTable);

            document.add(
                    new Paragraph(" ")
            );

            // ==================================
            // HABITS
            // ==================================

            document.add(
                    new Paragraph(
                            "HABITS",
                            sectionFont
                    )
            );

            PdfPTable habitsTable =
                    new PdfPTable(4);

            habitsTable.setWidthPercentage(
                    100
            );

            habitsTable.addCell("Type");
            habitsTable.addCell("Status");
            habitsTable.addCell("Date");
            habitsTable.addCell("Description");

            if (
                    dto.getHabits() != null
                            &&
                            !dto.getHabits().isEmpty()
            ) {

                for(HabitDTO h :
                        dto.getHabits()) {

                    habitsTable.addCell(
                            String.valueOf(
                                    h.getType()
                            )
                    );

                    habitsTable.addCell(
                            String.valueOf(
                                    h.getStatus()
                            )
                    );

                    habitsTable.addCell(
                            h.getDate() != null
                                    ? h.getDate().toString()
                                    : "-"
                    );

                    habitsTable.addCell(
                            h.getDescription() != null
                                    ? h.getDescription()
                                    : "-"
                    );

                }

            } else {

                habitsTable.addCell(
                        "No habits registered"
                );

            }

            document.add(
                    habitsTable
            );

            document.add(
                    new Paragraph(" ")
            );

            // ==================================
            // OBSERVATIONS
            // ==================================

            document.add(
                    new Paragraph(
                            "OBSERVATIONS",
                            sectionFont
                    )
            );

            PdfPTable observationsTable =
                    new PdfPTable(3);

            observationsTable.setWidthPercentage(
                    100
            );

            observationsTable.addCell(
                    "Date"
            );

            observationsTable.addCell(
                    "Professional"
            );

            observationsTable.addCell(
                    "Observation"
            );

            if (
                    dto.getObservations() != null
                            &&
                            !dto.getObservations().isEmpty()
            ) {

                dto.getObservations()
                        .forEach(o -> {

                            observationsTable.addCell(
                                    o.getCreatedAt() != null
                                            ? o.getCreatedAt()
                                            .toLocalDate()
                                            .toString()
                                            : "-"
                            );

                            observationsTable.addCell(
                                    o.getProfessionalName() != null
                                            ? o.getProfessionalName()
                                            : "-"
                            );

                            observationsTable.addCell(
                                    o.getContent() != null
                                            ? o.getContent()
                                            : "-"
                            );

                        });

            }

            document.add(
                    observationsTable
            );

            document.add(
                    new Paragraph(" ")
            );

            // ==================================
            // STATISTICS
            // ==================================

            List<HabitDTO> habits =
                    dto.getHabits() != null
                            ? dto.getHabits()
                            : List.of();

            long total =
                    habits.size();

            long correct =
                    habits.stream()
                            .filter(h ->
                                    h.getStatus()
                                            .name()
                                            .equals("CORRECT")
                            )
                            .count();

            long completionRate =
                    total == 0
                            ? 0
                            : (correct * 100 / total);

            document.add(
                    new Paragraph(
                            "STATISTICS",
                            sectionFont
                    )
            );

            document.add(
                    new Paragraph(
                            "Total habits: " +
                                    total,
                            normalFont
                    )
            );

            document.add(
                    new Paragraph(
                            "Completed habits: " +
                                    correct,
                            normalFont
                    )
            );

            document.add(
                    new Paragraph(
                            "Completion rate: " +
                                    completionRate +
                                    "%",
                            normalFont
                    )
            );

            document.close();

            return out.toByteArray();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error generating PDF report",
                    e
            );
        }
    }
}