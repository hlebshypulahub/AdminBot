package by.ciao.utils;

import by.ciao.bot.AdminBot;
import by.ciao.model.User;
import com.opencsv.CSVWriter;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.List;

@Service
public class BotService {

    private final RestService restService;
    @Value("${admin_id}")
    private String adminId;
    private static final Logger log = LoggerFactory.getLogger(BotService.class);

    public BotService(RestService restService) {
        this.restService = restService;
    }

    private List<User> getUsers() {
        return restService.getUsers();
    }

    private void fillWriterFromUser(List<User> users, CSVWriter csvWriter) throws IllegalAccessException {
        Field[] fields = User.class.getDeclaredFields();

        String[] header = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            header[i] = fields[i].getName();
        }
        csvWriter.writeNext(header);

        for (User user : users) {
            String[] row = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                Object value = fields[i].get(user);
                row[i] = value != null ? value.toString() : "";
            }
            csvWriter.writeNext(row);
        }
    }

    public SendDocument generateUsersCSV() throws IOException {
        List<User> users = getUsers();

        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer);

        try {
            fillWriterFromUser(users, csvWriter);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }

        File mediaFile = File.createTempFile("users", ".csv");
        FileUtils.writeStringToFile(mediaFile, writer.toString(), Charset.defaultCharset());

        InputFile csvFile = new InputFile(mediaFile, "users.csv");

        SendDocument sendDocument = new SendDocument(adminId, csvFile);
        sendDocument.setCaption("Here is the CSV file with users.");

        return sendDocument;
    }
}
