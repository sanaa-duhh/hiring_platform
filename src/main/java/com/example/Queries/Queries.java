package com.example.Queries;

import com.example.Connection.ConnectionManager;
import com.example.ResponseEntity.CandidateResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Queries {

    // Frequent Query -1
    // List all candidates in the “interview” stage for a given job.
    public static List<CandidateResponse> listCandidatesInInterviewStage(int jobId) {
        List<CandidateResponse> candidates = new ArrayList<>();
        String statement =
                """
                        SELECT a.application_id , c.candidate_id ,u1.name , u1.gender , c.resume_link_path , c.experience
                        FROM candidate AS c
                        JOIN applications AS a
                        ON c.candidate_id = a.candidate_id AND a.job_id = ?
                        JOIN application_stage AS a1
                        ON a.current_stage_id = a1.stage_id AND a1.title = 'interview'
                        JOIN user AS u1
                        ON u1.user_id = c.user_id
                        """;
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, jobId); // setting jobId in place of question mark
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    CandidateResponse candidateResponse = new CandidateResponse(
                            resultSet.getInt("application_id"),
                            resultSet.getInt("candidate_id"),
                            resultSet.getString("name"),
                            resultSet.getString("gender") != null ?
                                    com.example.Enums.Gender.valueOf(resultSet.getString("gender").toUpperCase())
                                    : null,
                            resultSet.getString("resume_link_path"),
                            resultSet.getInt("experience")
                    );
                    candidates.add(candidateResponse);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return candidates;
    }
}