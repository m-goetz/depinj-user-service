/* 
 * Copyright 2014 Maximilian Goetz.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package edu.hm.cs.goetz1.seminar.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

import edu.hm.cs.goetz1.seminar.db.UserDAO;
import edu.hm.cs.goetz1.seminar.model.User;
import edu.hm.cs.goetz1.seminar.util.PasswordHash;

public class SpringUserServiceTest {

    private static final String TEST_MAIL = "test@email.de";
    private static final String TEST_FIRSTNAME = "Max";
    private static final String TEST_LASTNAME = "Mustermann";
    private static final String TEST_PASSWORD = "totalGeheim";
    private static final Date TEST_BIRTHDATE = new GregorianCalendar(1993, 1, 21).getTime();

    @Test
    public void testChangeUserPassword() {
        PasswordHash passwordHash = mock(PasswordHash.class);
        UserDAO userDatabase = mock(UserDAO.class);
        SpringUserService userService = new SpringUserService(passwordHash);
        userService.setUserDatabase(userDatabase);
        when(userDatabase.getUser(TEST_MAIL)).thenReturn(
                new User(TEST_MAIL, TEST_FIRSTNAME, TEST_LASTNAME, "altesGehashtesPasswort", TEST_BIRTHDATE));
        when(passwordHash.validatePassword(TEST_PASSWORD, "altesGehashtesPasswort")).thenReturn(true);
        when(passwordHash.createHash("nochGeheimerAlsVorher")).thenReturn("neuesGehashtesPasswort");

        userService.changeUserPassword(TEST_MAIL, TEST_PASSWORD, "nochGeheimerAlsVorher");

        verify(userDatabase, times(1)).getUser(TEST_MAIL);
        verify(userDatabase, times(1)).updateUser(TEST_MAIL, TEST_FIRSTNAME, TEST_LASTNAME, "neuesGehashtesPasswort",
                TEST_BIRTHDATE);
    }

    @Test
    public void testAddNewUser() {
        PasswordHash passwordHash = mock(PasswordHash.class);
        UserDAO userDatabase = mock(UserDAO.class);
        SpringUserService userService = new SpringUserService(passwordHash);
        userService.setUserDatabase(userDatabase);
        when(passwordHash.createHash(TEST_PASSWORD)).thenReturn("neuesGehashtesPasswort");

        userService.addNewUser(TEST_MAIL, TEST_FIRSTNAME, TEST_LASTNAME, TEST_PASSWORD, TEST_BIRTHDATE);

        verify(userDatabase).createUser(TEST_MAIL, TEST_FIRSTNAME, TEST_LASTNAME, "neuesGehashtesPasswort",
                TEST_BIRTHDATE);
    }

}
