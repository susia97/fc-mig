package pe.mayciel.freechal.service;

import static pe.mayciel.freechal.domain.ContentType.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pe.mayciel.fos.junit.AbstractTestCaseRunWithSpring;
import pe.mayciel.freechal.domain.ContentInfo;
import pe.mayciel.freechal.domain.SiteInfo;

public class FreechalMigServiceLauncher extends AbstractTestCaseRunWithSpring {
	@Autowired
	private FreechalMigService service;
	@Autowired
	private ContentInsertService insertService;

	public static String fusr = "9MvNR6vlVL5VftTN1MPeRb1iFcyIKHHc0bSCPZnkK6ndRrLHN6OrLo9cOcvzR4nlVL4ePYfN8MOVRdriP6zNKLncNbTTPc1kIsnbRrTHJcPSLrjcQsvVR0vl3uVVe5nNMMPeRbLiRczzKJjcELSXPWHkCsmERoXH16PLLrbcEBZNgYDlLr5NPYnN0MOuRWDiUMy9KNLcLLTNPcbkVMnCRtrHN6OYLpPc5suWR15lVL5rPbrNNsPaRbPiJ6zMKLrcN5T2PcbkLsnjRr5HNsPtLtHcOAzzR6PlLL5PPbbNLcO8RZDiOMypKJjcTrTqPW";

	@Test
	public void test() throws Exception {
		for (ContentInfo info : getTarget()) {
			 service.doMigration(info, info.getStartPage(), info.getEndPage(),
			 20, false);
		}
		for (SiteInfo info : getSiteList()) {
			insertService.insertSiteInfo(info);
		}
	}

	private List<ContentInfo> getTarget() {
		List<ContentInfo> rt = new ArrayList<ContentInfo>();
//		rt.add(new ContentInfo(406762, 1, NTC, 1, "공지사항", 1, 2));
//		rt.add(new ContentInfo(406762, 1, BBS, 2, "천기누설 III", 1, 9999));
//		rt.add(new ContentInfo(406762, 5, BBS, 3, "온라인 선본", 1, 9999));
//		rt.add(new ContentInfo(406762, 3, BBS, 4, "낡은 일기장II", 1, 9999));
//		rt.add(new ContentInfo(406762, 4, BBS, 5, "임원 게시판", 1, 9999));
//		rt.add(new ContentInfo(406762, 2, BBS, 6, "소모임 게시판", 1, 9999));
//		rt.add(new ContentInfo(406762, 1, PDS, 7, "일반 자료실", 1, 9999));
//		rt.add(new ContentInfo(406762, 2, PDS, 8, "학술 자료실", 1, 9999));
//		rt.add(new ContentInfo(406762, 1, ALB, 9, "일반사진", 1, 8));
//		rt.add(new ContentInfo(406762, 2, ALB, 10, "천체사진", 1, 4));
//
//		rt.add(new ContentInfo(167165, 1, NTC, 1, "공지사항", 1, 2));
//		rt.add(new ContentInfo(167165, 1, BBS, 2, "일상의 목소리", 1, 9999));
//		rt.add(new ContentInfo(167165, 3, BBS, 3, "To. 승환", 1, 9999));
//		rt.add(new ContentInfo(167165, 2, ABS, 4, "마음의 목소리", 1, 9999));
//		rt.add(new ContentInfo(167165, 2, BBS, 5, "회원탐방", 1, 9999));
//		rt.add(new ContentInfo(167165, 1, PDS, 6, "일반 자료실", 1, 9999));
//		rt.add(new ContentInfo(167165, 2, PDS, 7, "소스 창고", 1, 9999));
//		rt.add(new ContentInfo(167165, 1, ALB, 8, "앨범", 1, 12));
//
//		rt.add(new ContentInfo(544357, 1, NTC, 1, "소.리.통.", 1, 3));
//		rt.add(new ContentInfo(544357, 1, BBS, 2, "일.기.장.", 1, 999));
//		rt.add(new ContentInfo(544357, 1, ABS, 3, "쥔없는 이야기", 1, 999));
//		rt.add(new ContentInfo(544357, 2, BBS, 4, "회.원.탐.방.", 1, 999));
//		rt.add(new ContentInfo(544357, 3, BBS, 5, "릴레이.소설.", 1, 999));
//		rt.add(new ContentInfo(544357, 1, PDS, 6, "자.료.있어요.", 1, 999));
//		rt.add(new ContentInfo(544357, 1, ALB, 7, "앨범", 1, 7));
//		rt.add(new ContentInfo(544357, 6, BBS, 8, "01 푸로젝투!!", 1, 999));
//
//		rt.add(new ContentInfo(2371513, 1, NTC, 1, "주목!!!", 1, 1));
//		rt.add(new ContentInfo(2371513, 1, BBS, 2, "별별 이야기들~★", 1, 999));
//		rt.add(new ContentInfo(2371513, 4, BBS, 3, "03프로젝트의견란^-^", 1, 999));
//		rt.add(new ContentInfo(2371513, 5, BBS, 4, "수강후기에 대해서~^-^", 1, 999));
//		rt.add(new ContentInfo(2371513, 1, ABS, 5, "마음속얘기", 1, 999));
//		rt.add(new ContentInfo(2371513, 2, BBS, 6, "msn 주소록", 1, 999));
//		rt.add(new ContentInfo(2371513, 1, ALB, 7, "인물,풍경 사진", 1, 4));
//		rt.add(new ContentInfo(2371513, 2, ALB, 8, "천체 사진", 1, 1));
//		rt.add(new ContentInfo(2371513, 1, PDS, 9, "자료실", 1, 1));
//
//		rt.add(new ContentInfo(1330611, 1, NTC, 1, "모두들 주목!!", 1, 1));
//		rt.add(new ContentInfo(1330611, 1, BBS, 2, "우리들 이야기", 1, 999));
//		rt.add(new ContentInfo(1330611, 1, ABS, 3, "소근소근...", 1, 999));
//		rt.add(new ContentInfo(1330611, 3, BBS, 4, "인탐게시판", 1, 999));
//		rt.add(new ContentInfo(1330611, 2, BBS, 5, "연락처", 1, 999));
		
//		rt.add(new ContentInfo(1091699, 1, BBS, 5, "뭘 쓰지?", 1, 999));
//		rt.add(new ContentInfo(1091699, 1, PDS, 5, "자료실", 1, 999));
		
//		rt.add(new ContentInfo(455865, 3, BBS, 1, "그들만의 세상", 1, 999));
//		rt.add(new ContentInfo(455865, 5, BBS, 2, "about 'LIGHT'", 1, 999));
//		rt.add(new ContentInfo(455865, 6, ALB, 3, "DiarywithFoto", 1, 2));
//		rt.add(new ContentInfo(455865, 7, ALB, 4, "ETC.", 1, 2));
//		rt.add(new ContentInfo(455865, 1, EBS, 5, "떠돌아다닌흔적들", 1, 999));
//		rt.add(new ContentInfo(455865, 9, ALB, 6, "추억의 사진들", 1, 2));
		
//		rt.add(new ContentInfo(455865, 6, PDS, 7, "ForStudies...", 1, 1));
//		rt.add(new ContentInfo(455865, 4, PDS, 8, "기타자료저장실", 1, 1));
		
//		rt.add(new ContentInfo(2169235, 1, BBS, 9, "숙덕임", 1, 999));
//		rt.add(new ContentInfo(2169235, 1, ABS, 9, "속삭임", 1, 999));
//		rt.add(new ContentInfo(2169235, 1, ALB, 9, "나보기", 1, 7));
//		rt.add(new ContentInfo(2169235, 1, PDS, 4, "나눔", 1, 1));

		return rt;
	}

	private List<SiteInfo> getSiteList() {
		List<SiteInfo> rt = new ArrayList<SiteInfo>();

//		rt.add(new SiteInfo(406762, "서울대학교 아마추어 천문회 통합 게시판", "acsnuaaa","2001.01.14"));
//		rt.add(new SiteInfo(167165, "00AAA-하나를 위해!", "00AAA","2000.08.23"));
//		rt.add(new SiteInfo(544357, "AAA 01 학번 커뮤니티 힘찬 내일을 향해~~~", "01aaa","2001.03.16"));
//		rt.add(new SiteInfo(2371513, "멋진~ AAA 03학번 커뮤니티~~☆★", "aaa03", "2003.03.29"));
//		rt.add(new SiteInfo(1330611, "2001 배남논술학원 \"봄날은 온다\"", "comingspring","2001.11.27"));
//		rt.add(new SiteInfo(1091699, "00AAA-자료실", "00aaapds","2001.09.06"));
//		rt.add(new SiteInfo(455865, "focusLightLikesBlue", "focuslight","2001.02.07"));
//		rt.add(new SiteInfo(2169235, "워니나라~^0^", "swoninara","2002.09.03"));

		return rt;
	}
}