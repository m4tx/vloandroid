package pl.krakow.vlo.jpks;

import java.util.ArrayList;

import static java.util.AbstractMap.SimpleEntry;
import static java.util.Map.Entry;

/**
 * Created by m4tx3 on 5/4/14.
 */
public abstract class JPKSStringCoder {
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

    public static String encodeString(String string) {
        for (Entry<String, String> entry : CODER_MAP) {
            string = string.replaceAll(entry.getKey(), entry.getValue());
        }
        return string;
    }

    public static String decodeString(String string) {
        for (Entry<String, String> entry : CODER_MAP) {
            string = string.replaceAll(entry.getValue(), entry.getKey());
        }
        return string;
    }
}
