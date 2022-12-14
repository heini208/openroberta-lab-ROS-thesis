package de.fhg.iais.roberta.javaServer.basics;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.persistence.bo.PendingEmailConfirmations;
import de.fhg.iais.roberta.persistence.bo.Role;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.PendingEmailConfirmationsDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.dbc.Assert;

public class PersistEmailConfirmationTest {
    private static TestConfiguration tc;
    private DbSession session;
    private UserDao userDao;
    private PendingEmailConfirmationsDao confirmationsDao;

    private static final int TOTAL_USERS = 5;

    @BeforeClass
    public static void mkSessionFactory() {
        tc = TestConfiguration.setup();
    }

    @Before
    public void setup() throws Exception {
        tc.deleteAllFromUserAndProgramTmpPasswords();
        this.session = tc.getSessionFactoryWrapper().getSession();
        this.userDao = new UserDao(this.session);
        this.confirmationsDao = new PendingEmailConfirmationsDao(this.session);

        // Create list of users
        for ( int userNumber = 0; userNumber < PersistEmailConfirmationTest.TOTAL_USERS; userNumber++ ) {
            User user = this.userDao.loadUser(null, "account-" + userNumber);
            if ( user == null ) {
                User user2 = new User(null, "account-" + userNumber);
                user2.setEmail("stuff-" + userNumber);
                user2.setPassword("pass-" + userNumber);
                user2.setRole(Role.STUDENT);
                user2.setTags("rwth");
                this.session.save(user2);
            }
        }
        this.session.commit();
    }

    @After
    public void tearDown() {
        this.session.close();
    }

    @Test
    public void createUrls() throws Exception {
        List<User> userList = this.userDao.loadUserList("rwth", "created", 0, 10);
        Assert.isTrue(userList.size() == 5);

        for ( int userNumber = 0; userNumber < PersistEmailConfirmationTest.TOTAL_USERS; userNumber++ ) {
            User user = this.userDao.loadUser(null, "account-" + userNumber);
            PendingEmailConfirmations confirmation = this.confirmationsDao.loadConfirmationByUser(user.getId());
            if ( confirmation == null ) {
                PendingEmailConfirmations confirmation2 = new PendingEmailConfirmations(user.getId());
                this.session.save(confirmation2);
                this.session.commit();
            }
        }
        Assert.notNull(this.confirmationsDao.get(2));
        Assert.isNull(this.confirmationsDao.get(8));
    }

    @Test
    public void activateAccount() throws Exception {
        User user2 = this.userDao.loadUserByEmail("stuff-2");
        Assert.notNull(user2);
        Assert.isTrue(user2.getAccount().equals("account-2"));
        PendingEmailConfirmations confirmation = this.confirmationsDao.persistConfirmation(user2.getId());
        Assert.notNull(confirmation);
        this.session.save(confirmation);

        User user = this.userDao.get(confirmation.getUserID());
        if ( user != null ) {
            user.setActivated(true);
            this.session.save(user);
        }
        User userChanged = this.userDao.loadUser(null, "account-2");
        Assert.isTrue(userChanged.isActivated());
    }

}
