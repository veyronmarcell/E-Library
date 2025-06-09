package com.library.service;

import com.library.model.Member;
import com.library.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MemberService {
    
    public void registerMember(Member member) throws SQLException {
        String query = "INSERT INTO members (first_name, last_name, email, phone, address, registration_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, member.getFirstName());
            pstmt.setString(2, member.getLastName());
            pstmt.setString(3, member.getEmail());
            pstmt.setString(4, member.getPhone());
            pstmt.setString(5, member.getAddress());
            pstmt.setDate(6, Date.valueOf(member.getRegistrationDate()));
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    member.setMemberId(generatedKeys.getInt(1));
                }
            }
        }
    }
    
    public Member getMemberById(int memberId) throws SQLException {
        String query = "SELECT * FROM members WHERE member_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Member member = new Member();
                    member.setMemberId(rs.getInt("member_id"));
                    member.setFirstName(rs.getString("first_name"));
                    member.setLastName(rs.getString("last_name"));
                    member.setEmail(rs.getString("email"));
                    member.setPhone(rs.getString("phone"));
                    member.setAddress(rs.getString("address"));
                    member.setRegistrationDate(rs.getDate("registration_date").toLocalDate());
                    return member;
                }
            }
        }
        return null;
    }
    
    public List<Member> getAllMembers() throws SQLException {
        List<Member> members = new ArrayList<>();
        String query = "SELECT * FROM members";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getInt("member_id"));
                member.setFirstName(rs.getString("first_name"));
                member.setLastName(rs.getString("last_name"));
                member.setEmail(rs.getString("email"));
                member.setPhone(rs.getString("phone"));
                member.setAddress(rs.getString("address"));
                member.setRegistrationDate(rs.getDate("registration_date").toLocalDate());
                members.add(member);
            }
        }
        return members;
    }
    
    public void updateMember(Member member) throws SQLException {
        String query = "UPDATE members SET first_name = ?, last_name = ?, email = ?, phone = ?, address = ? WHERE member_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, member.getFirstName());
            pstmt.setString(2, member.getLastName());
            pstmt.setString(3, member.getEmail());
            pstmt.setString(4, member.getPhone());
            pstmt.setString(5, member.getAddress());
            pstmt.setInt(6, member.getMemberId());
            
            pstmt.executeUpdate();
        }
    }
    
    public void deleteMember(int memberId) throws SQLException {
        String query = "DELETE FROM members WHERE member_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, memberId);
            pstmt.executeUpdate();
        }
    }
    
    public List<Member> searchMembers(String searchTerm) throws SQLException {
        List<Member> members = new ArrayList<>();
        String query = "SELECT * FROM members WHERE first_name LIKE ? OR last_name LIKE ? OR email LIKE ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            String searchPattern = "%" + searchTerm + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Member member = new Member();
                    member.setMemberId(rs.getInt("member_id"));
                    member.setFirstName(rs.getString("first_name"));
                    member.setLastName(rs.getString("last_name"));
                    member.setEmail(rs.getString("email"));
                    member.setPhone(rs.getString("phone"));
                    member.setAddress(rs.getString("address"));
                    member.setRegistrationDate(rs.getDate("registration_date").toLocalDate());
                    members.add(member);
                }
            }
        }
        return members;
    }
} 