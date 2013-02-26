package info.evelio.whatsnew.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;

import static info.evelio.whatsnew.util.StringUtils.isEmpty;

/**
 * As Play Store does not offer an API we have to crawl
 * Note: Most of this methods are non main thread targeted
 *
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class CrawlingHelper {
  /**
   * Chrome Beta for Android with "Request desktop site" enabled as for now
   */
  private static final String USER_AGENT =
      "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.108 Safari/537.22";
  private final String mLanguageCode;
  // :\
  private static final String URL_TEMPLATE = "https://play.google.com/store/apps/details?id=%s&hl=%s";
  private Cleaner mCleaner;

  public CrawlingHelper(final String languageCode) {
    mLanguageCode = languageCode;
  }

  public String getChangeLog(String packageName) throws IOException {
    if (isEmpty(packageName)) {
      return null;
    }
    final Document doc = Jsoup.connect(buildUrl(packageName)).userAgent(USER_AGENT).get();
    // What's new container
    Elements elements = doc.select("div.doc-whatsnew-container");
    // Usually is just one, we will report if it does not
    Element div = elements.get(0);
    // Remove "what's new in this version" paragraph
    div.child(0).remove();
    return toText(div); // All the bacon
  }

  private String toText(Element div) {
    if (div == null || isEmpty(div.text().trim())) {
      return null;
    }
    // (Read with British accent) No class or style at all
    div.children().removeAttr("class");
    div.children().removeAttr("style");
    // yup we gotta parse it again, for sake of Cleaner
    Document doc = Jsoup.parse(div.html());

    if (mCleaner == null) {
      mCleaner = new Cleaner(new Whitelist()
                      .addTags("b", "em", "i", "strong", "u", "br", "font", "big", "small"));
    }
    return mCleaner.clean(doc).body().html();
  }


  private String buildUrl(String packageName) {
    return String.format(URL_TEMPLATE, packageName, mLanguageCode);
  }
}
