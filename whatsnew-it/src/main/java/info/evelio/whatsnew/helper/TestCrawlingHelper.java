package info.evelio.whatsnew.helper;

import junit.framework.TestCase;

import static info.evelio.whatsnew.util.StringUtils.isNotEmpty;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class TestCrawlingHelper extends TestCase {

  public void testGetChangeLog() throws Exception {
    CrawlingHelper helper = new CrawlingHelper("en");
    String changeLog = helper.getChangeLog("com.telly");
    assertTrue(isNotEmpty(changeLog));
    // Meh I know, this might fail, but I always leave this link
    assertTrue(changeLog.contains("telly-android-change-log"));
  }
}
