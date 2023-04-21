package by.ciao;

import by.ciao.model.User;
import by.ciao.utils.BotService;
import by.ciao.utils.RestService;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class BotServiceTest {

    @Mock
    private RestService restService;

    @Autowired
    private BotService botService;

    @Value("${admin_id}")
    private String adminId;

    private List<User> generateAndFillUserList(Field[] fields) throws IllegalAccessException {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            users.add(new User());
            for (Field field : fields) {
                field.setAccessible(true);
                field.set(users.get(i), field.getName());
            }
        }
        return users;
    }

    // We have to do it here, because the code wraps it with quotes in case of facing quotes in string values of fields
    private static String[] wrapValuesWithQuotes(String[] arr) {
        String[] wrappedArr = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            wrappedArr[i] = '"' + arr[i] + '"';
        }
        return wrappedArr;
    }

    @Test
    public void testGenerateUsersCSV() throws IOException, IllegalAccessException {
        // init data for test
        Field[] fields = User.class.getDeclaredFields();
        String[] header = botService.generateCsvHeader(fields);
        String[] headerWrapped =  wrapValuesWithQuotes(header);
        String headerAsSingleString = String.join(",", headerWrapped);
        List<User> users = generateAndFillUserList(fields);

        // expected CSV contents
        StringBuilder expectedCsvContent = new StringBuilder();
        expectedCsvContent.append(headerAsSingleString).append("\n");
        for (User user : users) {
            String[] fieldsAsString = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                fieldsAsString[i] = "\"" + fields[i].get(user).toString() + "\"";
            }
            expectedCsvContent.append(String.join(",", fieldsAsString)).append("\n");
        }

        // mock the getUsers method to return the list of users
        when(restService.getUsers()).thenReturn(users);

        // call the generateUsersCSV method
        SendDocument sendDocument = botService.generateUsersCSV(users);

        // get the contents of the generated CSV file
        String csvContent = FileUtils.readFileToString(sendDocument.getDocument().getNewMediaFile(), Charset.defaultCharset());

        // compare the generated CSV content with the expected CSV content
        assertEquals(expectedCsvContent.toString(), csvContent);

        // verify that the SendDocument object has the expected properties
        assertEquals(sendDocument.getChatId(), adminId);
        assertNotNull(sendDocument.getDocument());
        assertNotNull(sendDocument.getDocument().getMediaName());
        assertTrue(sendDocument.getDocument().getMediaName().endsWith(".csv"));
        assertNotNull(sendDocument.getCaption());
    }
}
