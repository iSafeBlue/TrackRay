package com.trackray.base.utils;

import com.trackray.base.utils.RegexUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class RegexUtilTest {

  @Test
  public void testExtractStr() {
    Assert.assertNull(RegexUtil.extractStr("foobar", null));
    Assert.assertNull(RegexUtil.extractStr(null, "^.+(bar)"));
    Assert.assertEquals("bar", RegexUtil.extractStr("foobar", "^.+(bar)"));
    Assert.assertEquals("bar", RegexUtil.extractStr("foobar", "^.+(bar)", false));
    Assert.assertNull(RegexUtil.extractStr("foobaz", "^.+(bar)"));
  }

  @Test
  public void testExtractDouble() {
    Assert.assertEquals(0, RegexUtil.extractDouble("bar0.1", "^foo(.+)"), 0);
    Assert.assertEquals(0, RegexUtil.extractDouble("foo0a.1", "^foo(.+)"), 0);
    Assert.assertEquals(0.1, RegexUtil.extractDouble("foo0.1", "^foo(.+)"), 0);
  }

  @Test
  public void testExtractInt() {
    Assert.assertEquals(0, RegexUtil.extractInt("bar1", "^foo(.+)"));
    Assert.assertEquals(0, RegexUtil.extractInt("fooa", "^foo(.+)"));
    Assert.assertEquals(1, RegexUtil.extractInt("foo1", "^foo(.+)"));
  }

  @Test
  public void testExtractLong() {
    Assert.assertEquals(0L, RegexUtil.extractLong("bar1", "^foo(.+)"));
    Assert.assertEquals(0L, RegexUtil.extractLong("fooa", "^foo(.+)"));
    Assert.assertEquals(1L, RegexUtil.extractLong("foo1", "^foo(.+)"));
  }

  @Test
  public void testExtractDate() throws ParseException {
    Assert.assertNull(RegexUtil.extractDate("bar", "^foo(.+)", "yyyy-MM-dd"));
    Assert.assertNull(RegexUtil.extractDate("foobar", "^foo(.+)", "yyyy-MM-dd"));
    Assert.assertEquals(
      new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-01"),
      RegexUtil.extractDate("foo2019-02-01", "^foo(.+)", "yyyy-MM-dd")
    );
  }

  @Test
  public void testExtractProperties() {
    Map<String, String> regexps = new HashMap<>();
    regexps.put("foobar", "^.+(bar)");

    Map<String, String> expected = new HashMap<>();
    Assert.assertEquals(expected, RegexUtil.extractProperties("foo", regexps));

    expected.put("foobar", "bar");
    Assert.assertEquals(expected, RegexUtil.extractProperties("foobar", regexps));
  }

  @Test
  public void testMatches() {
    Assert.assertTrue(RegExpUtil.matches("foo", "^foo$"));
    Assert.assertFalse(RegExpUtil.matches("bar", "^foo$"));
  }

  @Test
  public void testGetRegContent() {
    Assert.assertEquals("foo", RegExpUtil.getRegContent("foo", "^foo$"));
    Assert.assertEquals("", RegExpUtil.getRegContent("bar", "^foo$"));
  }

  @Test
  public void testGetRegContentWithIndex() {
    Assert.assertEquals("", RegExpUtil.getRegContent("", "^foo$", 0));
    Assert.assertEquals("foo", RegExpUtil.getRegContent("foo", "^foo$", 0));
    Assert.assertEquals("", RegExpUtil.getRegContent("bar", "^foo$", 0));
  }
}
