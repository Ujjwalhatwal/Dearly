package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.repository.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

@SpringBootTest
// it load whole spring context/application , so not using it here , user inject mock instead of autowired
public class UserDetailsServiceImplementsTests {

//    @Autowired
    @InjectMocks
    private UserDetailServiceImplements userDetails;

//    @MockBean //: works with the application context of spring
    @Mock // when application context not there then use mock
    private UserRepo userRepo; // with mock it gave null , as spring not start no context of userRepo created so done using @BeforeEach

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Disabled
    void loadUserByUserNameTest(){
//        when(userRepo.findByUserName(ArgumentMatchers.anyString()).thenReturn(User.builder().username("Ram").password("Ram").roles(new ArrayList<>()).build());
        UserDetails user = userDetails.loadUserByUsername("Ram");
        Assertions.assertNotNull(user);
    }
}
