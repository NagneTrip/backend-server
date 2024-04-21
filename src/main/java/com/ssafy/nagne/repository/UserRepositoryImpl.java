package com.ssafy.nagne.repository;

import static com.ssafy.nagne.utils.DateTimeUtils.timestampOf;
import static java.util.Optional.ofNullable;

import com.ssafy.nagne.entity.User;
import com.ssafy.nagne.utils.DateTimeUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final DataSource dataSource;

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public Optional<User> findById(Long id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        User user = null;

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement("select * from users where id = ?");
            pstmt.setLong(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = User.builder()
                        .id(rs.getLong("id"))
                        .username(rs.getString("username"))
                        .password(rs.getString("password"))
                        .lastLoginDate(DateTimeUtils.dateTimeOf(rs.getTimestamp("last_login_date")))
                        .build();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (pstmt != null) {
                    pstmt.close();
                }

                if (conn != null) {
                    conn.close();
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return ofNullable(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        User user = null;

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement("select * from users where username = ?");
            pstmt.setString(1, username);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = User.builder()
                        .id(rs.getLong("id"))
                        .username(rs.getString("username"))
                        .password(rs.getString("password"))
                        .lastLoginDate(DateTimeUtils.dateTimeOf(rs.getTimestamp("last_login_date")))
                        .build();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (pstmt != null) {
                    pstmt.close();
                }

                if (conn != null) {
                    conn.close();
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return ofNullable(user);
    }

    @Override
    public void update(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement("update users set last_login_date = ?");
            pstmt.setTimestamp(1, timestampOf(user.getLastLoginDate()));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }

                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void delete(Long id) {

    }
}
