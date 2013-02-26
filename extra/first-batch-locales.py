#!/env/python
import os

locale_codes = ['af', 'ms', 'ca', 'cs', 'da', 'de', 'et', 'en', 'es', 'tl', 'fr', 'hr', 'zu', 'it', 'sw', 'lv', 'lt', 'hu', 'nl', 'no', 'pl', 'pt', 'ro', 'sk', 'sl', 'fi', 'sv', 'vi', 'tr', 'el', 'be', 'bg', 'ru', 'sr', 'uk', 'am', 'hi', 'th', 'ko', 'ja', 'zh']
filename = 'strings.xml'
content_template = '<?xml version="1.0" encoding="utf-8"?>\n<resources>\n  <string name="crawling_lang_code" translatable="false">%s</string>\n</resources>'

for code in locale_codes:
  directory = 'values-' + code
  if not os.path.exists(directory):
    os.makedirs(directory)
  file = open(os.path.join(directory, filename), 'wb')
  content = content_template % code
  file.write(content)
  file.close