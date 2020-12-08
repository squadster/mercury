package by.mercury.core.service.impl;

import by.mercury.core.dao.ScheduleDao;
import by.mercury.core.dao.SquadMemberDao;
import by.mercury.core.model.LessonModel;
import by.mercury.core.model.ScheduleModel;
import by.mercury.core.model.SquadMemberModel;
import by.mercury.core.model.UserModel;
import by.mercury.core.service.ScheduleService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DefaultScheduleService implements ScheduleService {

    @Value("${font.path}")
    private String fontPath;
    private static final String PDF_PREFIX = "pdf_";
    private static final String PDF_EXTENSION = ".pdf";
    private static final String TITLE = "Расписание для %s на %s\n";
    private static final Integer COLUMNS = 6;

    private Font font;

    private final ResourceLoader resourceLoader;
    
    private final ScheduleDao scheduleDao;
    
    private final SquadMemberDao squadMemberDao;

    public DefaultScheduleService(ResourceLoader resourceLoader, ScheduleDao scheduleDao, 
                                  SquadMemberDao squadMemberDao) {
        this.resourceLoader = resourceLoader;
        this.scheduleDao = scheduleDao;
        this.squadMemberDao = squadMemberDao;
    }
    
    @PostConstruct
    private void initFont() {
        try {
            var resource = this.getClass().getClassLoader().getResourceAsStream(fontPath);
            var tempFile = File.createTempFile("temp_font", "_HelveticaRegular.ttf");
            try (var out = new FileOutputStream(tempFile)) {
                IOUtils.copy(resource, out);
            }
            FontFactory.register(tempFile.getPath(), "HelveticaRegular");
            font = FontFactory.getFont("HelveticaRegular", BaseFont.IDENTITY_H, true);
            tempFile.deleteOnExit();
        } catch (IOException exception) {
            log.info("Exception during loading font", exception);
        }
    }

    @Override
    public List<ScheduleModel> getScheduleForUser(UserModel user) {
        return squadMemberDao.findByUserId(user.getId())
                .map(SquadMemberModel::getSquadId)
                .map(scheduleDao::findAllBySquad)
                .orElse(Collections.emptyList());
                
    }

    @Override
    public File generateSchedule(List<ScheduleModel> schedules) {
        try {
            var pdfDocument = new Document();
            var tempFile = createTempFile();
            PdfWriter.getInstance(pdfDocument, new FileOutputStream(tempFile.getAbsolutePath()));
            pdfDocument.open();
            for (var schedule : schedules) {
                createTitle(schedule, pdfDocument);
                var sortedLessons = getLessons(schedule);
                writeLessons(sortedLessons, pdfDocument);
                pdfDocument.add(new Paragraph("\n\n"));
            }
            pdfDocument.close();
            return tempFile;
        } catch (DocumentException | IOException | IllegalArgumentException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private File createTempFile() throws IOException {
        return File.createTempFile(PDF_PREFIX, PDF_EXTENSION);
    }

    private void createTitle(ScheduleModel schedule, Document pdfDocument) throws DocumentException {
        var date = schedule.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        pdfDocument.add(new Paragraph(String.format(TITLE, schedule.getSquad(), date), font));
    }

    private List<LessonModel> getLessons(ScheduleModel schedule) {
        return schedule.getLessons().stream()
                .sorted(Comparator.comparing(LessonModel::getIndex))
                .collect(Collectors.toList());
    }

    private void writeLessons(List<LessonModel> lessons, Document pdfDocument) throws DocumentException {
        var table = new PdfPTable(COLUMNS);
        table.addCell(new Paragraph("№", font));
        table.addCell(new Paragraph("Название", font));
        table.addCell(new Paragraph("Тип", font));
        table.addCell(new Paragraph("Преподаватель", font));
        table.addCell(new Paragraph("Аудитория", font));
        table.addCell(new Paragraph("Заметка", font));
        lessons.forEach(lesson -> {
            table.addCell(new Paragraph(lesson.getIndex().toString(), font));
            table.addCell(new Paragraph(lesson.getName(), font));
            table.addCell(new Paragraph(lesson.getType(), font));
            table.addCell(new Paragraph(lesson.getTeacher(), font));
            table.addCell(new Paragraph(lesson.getClassroom(), font));
            table.addCell(new Paragraph(lesson.getNote(), font));
        });
        pdfDocument.add(table);
    }
}
