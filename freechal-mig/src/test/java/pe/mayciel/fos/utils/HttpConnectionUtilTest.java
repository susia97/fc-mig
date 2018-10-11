package pe.mayciel.fos.utils;

import org.junit.Test;

public class HttpConnectionUtilTest {

	@Test
	public void getFile() throws Exception {
		String[] urls = new String[]{"http://editor.freechal.com/GetFile.asp?mnf=406762%3FGCOM02%3F1%3F128733323%3F157889.jpg"};
		String saveDir = "d:/freechal/attach";
		for (String url : urls) {
			HttpConnectionUtil.saveFile(url, saveDir, null);
		}
	}
}