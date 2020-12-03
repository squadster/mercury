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
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class DefaultScheduleService implements ScheduleService {

    private static final String PDF_PREFIX = "pdf_%d_";
    private static final String PDF_EXTENSION = ".pdf";
    private static final String TITLE = "Schedule for %s on %s";
    private static final Integer COLUMNS = 4;

    private ScheduleDao scheduleDao;
    
    private SquadMemberDao squadMemberDao;

    public DefaultScheduleService(ScheduleDao scheduleDao, SquadMemberDao squadMemberDao) {
        this.scheduleDao = scheduleDao;
        this.squadMemberDao = squadMemberDao;
    }

    @Override
    public ScheduleModel getScheduleForUser(UserModel user) {
        return squadMemberDao.findByUserId(user.getId())
                .map(SquadMemberModel::getSquadId)
                .flatMap(scheduleDao::findBySquad)
                .orElseThrow(() -> new IllegalArgumentException("There is no schedule for " + user.getId() + "\n"));
    }

    @Override
    public File generateSchedule(ScheduleModel schedule) {
        try {
            var pdfDocument = new Document();
            var tempFile = createTempFile(schedule);
            PdfWriter.getInstance(pdfDocument, new FileOutputStream(tempFile.getAbsolutePath()));
            pdfDocument.open();
            createTitle(schedule, pdfDocument);
            var sortedLessons = getLessons(schedule);
            writeLessons(sortedLessons, pdfDocument);
            pdfDocument.close();
            return tempFile;
        } catch (DocumentException | IOException | IllegalArgumentException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private File createTempFile(ScheduleModel scheduleModel) throws IOException {
        var filePrefix = format(PDF_PREFIX, scheduleModel.getId());
        return File.createTempFile(filePrefix, PDF_EXTENSION);
    }

    private void createTitle(ScheduleModel schedule, Document pdfDocument) throws DocumentException {
        var date = schedule.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        pdfDocument.add(new Paragraph(String.format(TITLE, schedule.getSquad(), date)));
    }

    private List<LessonModel> getLessons(ScheduleModel schedule) {
        return schedule.getLessons().stream()
                .sorted(Comparator.comparing(LessonModel::getIndex))
                .collect(Collectors.toList());
    }

    private void writeLessons(List<LessonModel> lessons, Document pdfDocument) throws DocumentException {
        var table = new PdfPTable(COLUMNS);
        table.addCell("Index");
        table.addCell("Name");
        table.addCell("Teacher");
        table.addCell("Note");
        lessons.forEach(lesson -> {
            table.addCell(lesson.getIndex().toString());
            table.addCell(lesson.getName());
            table.addCell(lesson.getTeacher());
            table.addCell(lesson.getNote());
        });
        pdfDocument.add(table);
    }
}
