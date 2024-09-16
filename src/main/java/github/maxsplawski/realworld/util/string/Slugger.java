package github.maxsplawski.realworld.util.string;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class Slugger {

    private static final Pattern NON_LATIN = Pattern.compile("[^\\w_-]");
    private static final Pattern SEPARATORS = Pattern.compile("[\\s\\p{Punct}&&[^-]]");

    public static String slugifyFrom(String input) {
        String noSeparators = SEPARATORS.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(noSeparators, Normalizer.Form.NFD);
        String slug = NON_LATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH).replaceAll("-{2,}","-").replaceAll("^-|-$","");
    }
}
