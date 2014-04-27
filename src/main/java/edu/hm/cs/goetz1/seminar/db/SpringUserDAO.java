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
package edu.hm.cs.goetz1.seminar.db;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import edu.hm.cs.goetz1.seminar.model.User;

@Component
public class SpringUserDAO extends AbstractSqlUserDAO implements UserDAO {

    private JdbcTemplate jdbcTemplate;
    private RowMapper<User> rowMapper;

    @Autowired
    public SpringUserDAO(JdbcTemplate jdbcTemplate, RowMapper<User> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
        if (!tableExists()) {
            jdbcTemplate.execute(CREATE_TABLE);
        }
    }

    private boolean tableExists() {
        try {
            jdbcTemplate.execute("SELECT COUNT(*) FROM Users");
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public User createUser(String email, String firstName, String lastName, String password, Date birthDate) {
        jdbcTemplate.update(CREATE_USER, email, firstName, lastName, password, birthDate);
        return new User(email, firstName, lastName, password, birthDate);
    }

    @Override
    public void deleteUser(String email) {
        jdbcTemplate.update(DELETE_USER, email);
    }

    @Override
    public User getUser(String email) {
        return jdbcTemplate.queryForObject(FIND_BY_EMAIL, rowMapper, email);
    }

    @Override
    public User updateUser(String email, String firstName, String lastName, String password, Date birthDate) {
        jdbcTemplate.update(UPDATE_USER, firstName, lastName, password, birthDate, email);
        return new User(email, firstName, lastName, password, birthDate);
    }
}
