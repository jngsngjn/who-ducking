package hello.service.admin;

import hello.entity.animation.Animation;
import hello.entity.animation.AnimationRating;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ExcelService {

    private final AdminService adminService;

    public void importAnimationsFromExcel(MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        List<Animation> animations = new ArrayList<>();
        List<List<String>> genreNames = new ArrayList<>();

        while (rows.hasNext()) {
            Row currentRow = rows.next();
            if (currentRow.getRowNum() == 0) {
                // Skip header row
                continue;
            }

            Animation animation = new Animation();
            animation.setName(getCellValueAsString(currentRow.getCell(0)));
            animation.setAuthor(getCellValueAsString(currentRow.getCell(1)));
            animation.setDescription(getCellValueAsString(currentRow.getCell(2)));
            animation.setRating(AnimationRating.valueOf(getCellValueAsString(currentRow.getCell(3)).toUpperCase()));
            animation.setFirstDate(LocalDate.parse(getCellValueAsString(currentRow.getCell(4))));
            animation.setImageName(getCellValueAsString(currentRow.getCell(5)));
            animation.setImagePath(getCellValueAsString(currentRow.getCell(6)));
            animation.setIsFinished(Boolean.parseBoolean(getCellValueAsString(currentRow.getCell(7))));

            animations.add(animation);
            String genresCell = getCellValueAsString(currentRow.getCell(8));
            List<String> genres = Arrays.asList(genresCell.split(","));
            genreNames.add(genres);
        }

        adminService.addAnimationsWithGenres(animations, genreNames);

        workbook.close();
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }
}