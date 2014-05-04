package pl.krakow.vlo.jpks;

import java.util.ArrayList;

import static java.util.AbstractMap.SimpleEntry;
import static java.util.Map.Entry;

/**
 * The class that is used to encode and decode Strings in "JPKS encoding". It is a simple encoding
 * to allow for using polish diacritics and sending them as ASCII text.
 */
public abstract class JpksStringCoder {
    private static final ArrayList<Entry<String, String>> CODER_MAP = new ArrayList<>();

    static {
        CODER_MAP.add(new SimpleEntry<>("ą", "%a"));
        CODER_MAP.add(new SimpleEntry<>("ć", "%c"));
        CODER_MAP.add(new SimpleEntry<>("ę", "%e"));
        CODER_MAP.add(new SimpleEntry<>("ł", "%l"));
        CODER_MAP.add(new SimpleEntry<>("ń", "%n"));
        CODER_MAP.add(new SimpleEntry<>("ó", "%o"));
        CODER_MAP.add(new SimpleEntry<>("ś", "%s"));
        CODER_MAP.add(new SimpleEntry<>("ź", "%x"));
        CODER_MAP.add(new SimpleEntry<>("ż", "%z"));
        CODER_MAP.add(new SimpleEntry<>("Ą", "%A"));
        CODER_MAP.add(new SimpleEntry<>("Ć", "%C"));
        CODER_MAP.add(new SimpleEntry<>("Ę", "%E"));
        CODER_MAP.add(new SimpleEntry<>("Ł", "%L"));
        CODER_MAP.add(new SimpleEntry<>("Ń", "%N"));
        CODER_MAP.add(new SimpleEntry<>("Ó", "%O"));
        CODER_MAP.add(new SimpleEntry<>("Ś", "%S"));
        CODER_MAP.add(new SimpleEntry<>("Ź", "%X"));
        CODER_MAP.add(new SimpleEntry<>("Ż", "%Z"));
    }

    /**
     * Encodes a string, producing ready to send sequence of characters in JPKS encoding.
     *
     * @param string the string to encode
     * @return the string to send
     */
    public static String encodeString(String string) {
        for (Entry<String, String> entry : CODER_MAP) {
            string = string.replaceAll(entry.getKey(), entry.getValue());
        }
        return string;
    }

    /**
     * Decodes a JPKS-encoded string, producing a regular UTF-8 sequence of characters.
     *
     * @param string the string received
     * @return UTF-8 string
     */
    public static String decodeString(String string) {
        for (Entry<String, String> entry : CODER_MAP) {
            string = string.replaceAll(entry.getValue(), entry.getKey());
        }
        return string;
    }
}
