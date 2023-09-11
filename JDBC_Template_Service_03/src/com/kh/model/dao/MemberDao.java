package com.kh.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.kh.common.JDBCTemplate;
import com.kh.model.vo.Member;

//DAO(Data Access Object) : DB에 직접적으로 접근해서 사용자으,ㅣ 요청에 맞는 sql은 실행 후 결과 반환(JDBC) 
//							결과를 Controller로 다시 리턴
public class MemberDao {
	/*
	 * * JDBC용 객체
	 * -Connection : DB의 연결정보를 담고있는 객체
	 * -[Prepared]Statement : 연결된 DB에 SQL문을 전달해서 실행하고, 결과를 받아내는 객체 
	 * -ResultSet : SELECT문 실행 후 조회된 결과물들이 담겨있는 객체
	 * 
	 * *JDBC 과정(순서중요) 
	 * 1) jdbc driver 들록 : 해당 DBMS(오라클)가 제공하는 클래스 등록 
	 * 2) Connection생성 : 연결하고자 하는 DB정보를 입력해서 해당 DB와 연결하면서 생성 
	 * 3) Statement생성 : Connection 객체를 이용해서 생성(sql문 실행 및 결과받는 객체) 
	 * 4) sql문 전달하면서 실행 : Statement 객체를 이용해서 sql문 실행 
	 * 5) 결과받기
	 * 		> SELECT문 실행 => ResultSet 객체(조회된 데이터들이 담겨있음 )=>6_1)
	 * 		> DML문 실행 => int(처리된 행 수)
	 * 
	 * 6_1) ResultSet에 담겨있는 데이터들을 하나씩 하니씩 뽑아서 vo객체에 차근차근 옮겨담기[+ ArrayList에 담아주기]
	 * 6_2) 트랜잭션 처리 (성공했다면 commit, 실패했다면 rollback 실행)
	 * 
	 * 7) 다 사용한 JDNC용 객체들 반드시 자원 반납(close) =>생성된 역순으로
	 */
	
	/**
	 * 사용자가 입력한 정보들을 db에 추가시켜주는 메소드
	 * @param m : 사용자가 입력한 값들이 담겨있는 member객체
	 * @return : insert문 실행 후 처리된 행수
	 */
	public int insertMember(Connection conn, Member m) {
		//insert => 처리된 행 수
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = "INSERT INTO MEMBER VALUES(SEQ_USERNO.NEXTVAL,?,?,?,?,?,?,?,?,?,SYSDATE)";
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, m.getUserId());
			pstmt.setString(2, m.getUserPwd());
			pstmt.setString(3, m.getUserName());
			pstmt.setString(4, m.getGender());
			pstmt.setInt(5, m.getAge());
			pstmt.setString(6, m.getEmail());
			pstmt.setString(7, m.getPhone());
			pstmt.setString(8, m.getAddress());
			pstmt.setString(9, m.getHobby());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(conn);
		}

		return result;
	}

	
	public ArrayList<Member> selectList(Connection conn) {
		// select문 (여러행 조회) => ResultSet객체 => ArratList에 담아 넘기기
		
		ArrayList<Member> list = new ArrayList<Member>(); // 비어있는상태
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String sql = "SELECT * FROM MEMBER ORDER BY USERNAME";
		
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				Member m = new Member();
				m.setUserNo(rset.getInt("userno"));
				m.setUserId(rset.getString("userid"));
				m.setUserPwd(rset.getString("userpwd"));
				m.setUserName(rset.getString("username"));
				m.setGender(rset.getString("gender"));
				m.setAge(rset.getInt("age"));
				m.setEmail(rset.getString("email"));
				m.setPhone(rset.getString("phone"));
				m.setAddress(rset.getString("address"));
				m.setHobby(rset.getString("hobby"));
				m.setEnrollDate(rset.getDate("enrolldate"));
				
				list.add(m);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		
		return list;
	}
	public Member selectByUserId(Connection conn, String userId) {
		// select문 (한 행) => ResultSet객체 => Member객체
		Member m = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String sql = "SELECT * FROM MEMBER WHERE USERID = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rset = pstmt.executeQuery();
			if(rset.next()) {
				m = new Member(rset.getInt("userno"),
				rset.getString("userid"),
				rset.getString("userpwd"),
				rset.getString("username"),
				rset.getString("gender"),
				rset.getInt("age"),
				rset.getString("email"),
				rset.getString("phone"),
				rset.getString("address"),
				rset.getString("hobby"),
				rset.getDate("enrolldate")
				);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		
		
		return m;
	}
	
//	public Member selectList() {
//		
//	}
//	
	public ArrayList<Member> selectByUserName(Connection conn, String keyword) {
		//select문(여러행) => ResultSet객체 => ArrayList객체
		
		ArrayList<Member> list = new ArrayList<Member>();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
//		String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE ?";
		String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE '%' || ? || '%'";
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				Member m = new Member();
				m.setUserNo(rset.getInt("userno"));
				m.setUserId(rset.getString("userid"));
				m.setUserPwd(rset.getString("userpwd"));
				m.setUserName(rset.getString("username"));
				m.setGender(rset.getString("gender"));
				m.setAge(rset.getInt("age"));
				m.setEmail(rset.getString("email"));
				m.setPhone(rset.getString("phone"));
				m.setAddress(rset.getString("address"));
				m.setHobby(rset.getString("hobby"));
				m.setEnrollDate(rset.getDate("enrolldate"));
				
				list.add(m);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		return list;
	}
		
	public int deleteMember(Connection conn, String userId) {
		// delete문(처리된 행 수)=>반환
		int result = 0;
		
		PreparedStatement pstmt = null;
		String sql = "DELETE FROM MEMBER WHERE USERID = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
	}
	public int updateMember(Connection conn, Member m) {
		// update문 => 처리된행수(int)
		int result = 0;
		
		PreparedStatement pstmt = null;
		String sql = "UPDATE MEMBER SET USERPWD = ?, EMAIL = ? , PHONE = ?, ADDRESS = ?, WHERE USERID = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, m.getUserPwd());
			pstmt.setString(2, m.getEmail());
			pstmt.setString(3, m.getPhone());
			pstmt.setString(4, m.getAddress());
			pstmt.setString(5, m.getUserId());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}
		
		
		
		
		
		
		
		
		
		
		
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
