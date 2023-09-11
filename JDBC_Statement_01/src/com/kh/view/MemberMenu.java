package com.kh.view;

import java.util.ArrayList;
import java.util.Scanner;

import com.kh.controller.MemberController;
import com.kh.model.dao.MemberDao;
import com.kh.model.vo.Member;

//사용자가 보게될 시각적인 요소(화면) 출력및 입력
public class MemberMenu {
	private Scanner sc = new Scanner(System.in);
	//
	private MemberController mc = new MemberController();
	
	/**
	 * 사용자가 보게될 첫 화면(메인화면)
	 */
	public void mainMenu() {
		while(true) {
			System.out.println("\n==회원관리 프로그램==");
			System.out.println("1. 회원 추가");
			System.out.println("2. 회원 전체 조회");
			System.out.println("3. 회원 아이디 검색");
			System.out.println("4. 회원 이름으로 키워드 검색");
			System.out.println("5. 회원 정보 변경");
			System.out.println("6. 회원 탈퇴");
			System.out.println("0. 프로그램 종료");
			
			System.out.print(">> 메뉴 선택 :");
			int menu = sc.nextInt();
			sc.nextLine();
			
			switch(menu) {
			case 1 : 
				inputMember();
				break;
			case 2 : 
				mc.selectList();
				break;
			case 3 : 
//				String userId = inputMemberId();
//				mc.selectByUserId(userId);
				mc.selectByUserId(inputMemberId());
				break;
			case 4 : 
				mc.selectByUserName(inputMemberName());
				break;
			case 5 : 
				updateMember();
				break;	
			case 6 : 
				deleteMember(inputMemberId());
				break;
			case 0 : 
				System.out.println("이용해주셔서 감사합니다. 프로그램을 종료합니다.");
				return;
			default :			
				System.out.println("메뉴를 잘못 입력하셨습니다. 다시입력해주세요.");
			}
	
		}
		
	}
	/**
	 * 회원 추가창(서브화면) 즉, 추가하고자 하는 회원의 정보를 입력받아 회원을 추가요청하는 창
	 */
	public void inputMember() {
		
		System.out.println("\n=== 회원 추가 ===");
		
		System.out.print("아이디 : ");
		String userId = sc.nextLine();
		System.out.print("비밀번호 : ");
		String userPwd = sc.nextLine();
		System.out.print("이름 : ");
		String userName = sc.nextLine();
		System.out.print("성별(M/F) : ");
		String gender = sc.nextLine().toUpperCase();
		System.out.print("나이 : ");
		String age = sc.nextLine(); // "20"
		System.out.print("이메일 : ");
		String email = sc.nextLine();
		System.out.print("전화번호(-빼고 입력) : ");
		String phone = sc.nextLine();
		System.out.print("주소 : ");
		String address = sc.nextLine();
		System.out.print("취미(,로 연이어서 작성) : ");
		String hobby = sc.nextLine();
		
		// 회원 추가 요청 == Controller메소드 요청
		mc.insertMember(userId, userPwd, userName, gender,
				age, email, phone, address, hobby);
		
	}
	
	public String inputMemberId() {
		System.out.print("\n회원 아이디 입력 : ");
		return sc.nextLine();
	}
	public String inputMemberName() {
		System.out.print("\n회원 이름(키워드) 입력 : ");
		return sc.nextLine();
	}
	
	// ----------------------------------응답 화면------------------------------------
	/**
	 * 서비스요청 처리후 성공했을 경우 사용자가 보게될 응답화면
	 * @param message : 객체별 성공메세지
	 */
	public void displaySuccess(String message) {
		System.out.println("\n서비스 요청 성공 : "+message);
	}
	
	/**
	 * 서비스요청 처리후 실패했을 경우 사용자가 보게될 응답화면
	 * @param message : 객체별 실패메세지
	 */
	public void displayFail(String message) {
		System.out.println("\n서비스 요청 실패 : "+message);
	}
	/**
	 * 조회 서비스 요청시 조회 결과가 없을 때 사용자가 보게될 응답화면
	 * @param message : 조회결과에 대한 응답메세지
	 */
	public void displayNoData(String message) {
		System.out.println("\n"+message);
	}
	/**
	 * 조회 서비스 요청시 조회 결과가 여러행일 경우 사용자가 보게될 응답화면
	 * @param list : 출력할 member들이 담겨있는 list
	 */
	public void displayMemberList(ArrayList<Member> list) {
		System.out.println("\n조회된 데이터는 다음과 같습니다\n");	
//		//for loop
		for(int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
		
		//for each
//		for(Member m : )
//		
	}
	public void displayMember(Member m) {
		System.out.println("\n조회된 데이터는 다음과 같습니다.");
		System.out.println(m);
	}
	
	public void updateMember() {
		System.out.println("\n=====회원 정보 변경=====");
		
		String userId = inputMemberId();
		
		System.out.println("변경 비밀번호");
		String userPwd = sc.nextLine();
		System.out.println("변경 이메일");
		String email = sc.nextLine();
		System.out.println("변경 전화번호");
		String phone = sc.nextLine();
		System.out.println("변경 주소");
		String address = sc.nextLine();
		mc.updateMember(userId, userPwd, email, phone, address);
	}
	
	public void deleteMember(String userId) {
		int result = new MemberDao().deleteMember(userId);
		
		if (result > 0) {
			new MemberMenu().displaySuccess("성공적으로 회원탈퇴 처리 하였습니다.");
		}else {
			new MemberMenu().displayFail("회원탈퇴에 실패하였습니다.");
		}
	}
	
	
	
	
	
	
}
