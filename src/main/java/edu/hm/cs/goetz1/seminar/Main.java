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
package edu.hm.cs.goetz1.seminar;

import java.util.Date;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.hm.cs.goetz1.seminar.db.UserDAO;
import edu.hm.cs.goetz1.seminar.model.User;

public class Main {

    public static void main(String[] args) {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        UserDAO userDatabase = (UserDAO) context.getBean("springUserDAO");

        userDatabase.createUser("test@email.de", "Max", "Mustermann", "geheim", new Date());
        userDatabase.updateUser("test@email.de", "Max", "Mustermann", "totalGeheim", new Date());
        User mustermann = userDatabase.getUser("test@email.de");
        System.out.println(mustermann.toString());

        context.close();
    }
}