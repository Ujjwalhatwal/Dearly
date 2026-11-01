package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class UseServiceTests {

    @Autowired
    UserRepo userRepo;

//    @BeforeEach // before running each tests this code/tests will run
//    void setup(){}
//    @BeforeAll // for creating the resource generally
//    @AfterAll // for deleting the resource
//    @AfterEach

    @Test
    @Disabled // to avoid the running of tests, it disabled this tests running.
    public void findByUserNameTest(){
        User user = userRepo.findByUserName("vishant_nimesh").orElseThrow();
        assertTrue(!user.getJournalEntries().isEmpty());
        assertTrue(userRepo.findByUserName("Ram").isPresent());
    }

    @ParameterizedTest
    @CsvSource({
            "1,2,3",
            "3,3,6",
            "4,1,2",
            "6,6,1",
            "3,4,7"
    })
//    @ValueSource(strings = {"ram","ramesh"})
//    @EnumSource
//        @ArgumentsSource() // for custom source
    @Disabled
    public void tests(int a, int b, int expected){
        assertEquals(expected,a+b);
    }
}
