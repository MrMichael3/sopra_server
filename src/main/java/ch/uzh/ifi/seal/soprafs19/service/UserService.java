package ch.uzh.ifi.seal.soprafs19.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.entity.UserLogin;
import ch.uzh.ifi.seal.soprafs19.entity.UserToken;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User createUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.OFFLINE);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }
    //verify user
    public UserToken verifyUser(UserLogin unverifiedUser){
        String pw = unverifiedUser.getPassword();
        String un = unverifiedUser.getUsername();
        UserToken token = new UserToken();
        try {
            User toVerifyUser = userRepository.findByUsername(un);
            String expectedPw = toVerifyUser.getPassword();
            System.out.println("FFFFFFFFFFFFFFFFFFF" + un);

            if (pw.equals(expectedPw)) {
                //validation succeeded
                toVerifyUser.setStatus(UserStatus.ONLINE);
                userRepository.save(toVerifyUser);
                log.debug("Logged in User: {}", toVerifyUser);
                token.setToken(toVerifyUser.getToken());
            } else {
                //validation failed
                log.debug("Login failed");
                token.setToken("");
            }
        }catch (Exception e){
            token.setToken("");
        }
        return token;
    }
    public User getUser(long userId) {
        return userRepository.findById(userId);
    }
}
