package studio.yukilem.yukilem.config.settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.yukilem.yukilem.config.ConfigView;

public record DiscordSettings(boolean enabled, String webhookUrl, String username, String avatarUrl, List<String> content, Embed embed) {
    private static final int DEFAULT_COLOR = 0xFF3333;

    @NotNull
    public static DiscordSettings disabled() {
        List<String> list = Collections.emptyList();
        List<String> list2 = Collections.emptyList();
        Author author = new Author("", "", "");
        Footer footer = new Footer("", "");
        List<Field> list3 = Collections.emptyList();
        Embed embed = new Embed(false, "", "", list2, 0xFF3333, false, author, footer, "", "", list3);
        DiscordSettings discordSettings = new DiscordSettings(false, "", "", "", list, embed);
        return discordSettings;
    }

    @NotNull
    public static DiscordSettings load(@NotNull ConfigView configView) {
        boolean bl;
        String string;
        String string2 = string = configView.getString("discord.webhook-url", "").trim();
        boolean bl2 = bl = DiscordSettings.isConfiguredWebhook(string2);
        String string3 = string;
        String string4 = configView.getString("discord.username", "").trim();
        String string5 = configView.getString("discord.avatar-url", "").trim();
        List<String> list = DiscordSettings.readStringOrList(configView.getRaw("discord.content"));
        Embed embed = Embed.load(configView);
        DiscordSettings discordSettings = new DiscordSettings(bl2, string3, string4, string5, list, embed);
        return discordSettings;
    }

    private static boolean isConfiguredWebhook(@NotNull String string) {
        String string2;
        int n;
        int n2;
        String string3;
        int n3;
        int n4;
        if (string.isEmpty() || !string.regionMatches(true, 0, "http", 0, 4)) {
            return false;
        }
        int n5 = string.indexOf("/webhooks/");
        int n6 = n5;
        if (n6 < 0) {
            return false;
        }
        int n7 = n5;
        int length = "/webhooks/".length();
        int n8 = n7 + length;
        String string4 = string.substring(n8);
        String string5 = string4;
        int n9 = string5.indexOf(47);
        int n10 = n9;
        if (n10 <= 0 || (n4 = n9) >= (n3 = (string3 = string4).length() - 1)) {
            return false;
        }
        String string6 = string4;
        int n11 = n9;
        String string7 = string6.substring(0, n11);
        int n12 = 0;
        while ((n2 = n12) < (n = (string2 = string7).length())) {
            int n13;
            String string8 = string7;
            if (Character.isDigit(string8.charAt(n13 = n12++))) continue;
            return false;
        }
        return true;
    }

    @NotNull
    private static List<String> readStringOrList(@Nullable Object object) {
        ArrayList<String> arrayList;
        if (object == null) {
            return Collections.emptyList();
        }
        if (object instanceof List) {
            Iterator iterator;
            ArrayList<String> arrayList2;
            List list;
            List list2 = list = (List)object;
            int size = list2.size();
            arrayList = arrayList2 = new ArrayList<String>(size);
            List list3 = list;
            Iterator iterator2 = list3.iterator();
            while ((iterator = iterator2).hasNext()) {
                Iterator iterator3 = iterator2;
                Object e = iterator3.next();
                Object e2 = e;
                if (e2 == null) {
                    ArrayList<String> arrayList3 = arrayList;
                    boolean bl = arrayList3.add("");
                    continue;
                }
                ArrayList<String> arrayList4 = arrayList;
                Object e3 = e;
                String string = String.valueOf(e3);
                boolean bl = arrayList4.add(string);
            }
        } else {
            List<String> list;
            String string = String.valueOf(object);
            String string2 = string;
            if (string2.isEmpty()) {
                list = Collections.emptyList();
            } else {
                String string3 = string;
                list = List.of(string3);
            }
            return list;
        }
        ArrayList<String> arrayList5 = arrayList;
        return arrayList5;
    }

    private static int parseColor(@Nullable Object object) {
        String string;
        if (object == null) {
            return 0xFF3333;
        }
        if (object instanceof Number) {
            return ((Number)object).intValue() & 0xFFFFFF;
        }
        String string2 = String.valueOf(object).trim();
        String string3 = string2;
        if (string3.isEmpty()) {
            return 0xFF3333;
        }
        String string4 = string = string2;
        int n2 = 16;
        String string5 = string4;
        if (string5.startsWith("#")) {
            String string6 = string4;
            string4 = string6.substring(1);
        } else {
            String string7 = string4;
            if (string7.regionMatches(true, 0, "0x", 0, 2)) {
                String string8 = string4;
                string4 = string8.substring(2);
            } else {
                IntPredicate intPredicate;
                String string9 = string4;
                IntStream intStream = string9.chars();
                if (intStream.allMatch(intPredicate = n -> {
                    boolean bl = Character.isDigit(n) || n == 45;
                    return bl;
                })) {
                    n2 = 10;
                }
            }
        }
        try {
            String string10 = string4;
            int n3 = n2;
            long l = Long.parseLong(string10, n3);
            long l2 = 0xFFFFFFL;
            int n4 = (int)(l & l2);
            return n4;
        }
        catch (NumberFormatException e) {
            NumberFormatException e2 = e;
            return 0xFF3333;
        }
    }

    @NotNull
    private static List<Field> readFields(@Nullable Object object) {
        Iterator iterator;
        ArrayList<Field> arrayList;
        List list;
        List list2;
        if (!(object instanceof List) || (list2 = (list = (List)object)).isEmpty()) {
            return Collections.emptyList();
        }
        List list3 = list;
        int size = list3.size();
        ArrayList<Field> arrayList2 = arrayList = new ArrayList<Field>(size);
        List list4 = list;
        Iterator iterator2 = list4.iterator();
        while ((iterator = iterator2).hasNext()) {
            Object v;
            String string;
            Map map;
            Iterator iterator3 = iterator2;
            Object e = iterator3.next();
            Object e2 = e;
            if (!(e2 instanceof Map)) continue;
            Object e3 = e;
            Map map2 = map = (Map)e3;
            String string2 = DiscordSettings.stringOrEmpty(map2.get("name"));
            Map map3 = map;
            String string3 = DiscordSettings.stringOrEmpty(map3.get("value"));
            String string4 = string2;
            if (string4.isEmpty() && (string = string3).isEmpty()) continue;
            Map map4 = map;
            Object v2 = map4.get("inline");
            Object v3 = v2;
            boolean bl = v3 == null || DiscordSettings.parseBoolean(v = v2, true);
            boolean bl2 = bl;
            ArrayList<Field> arrayList3 = arrayList2;
            String string5 = string2;
            String string6 = string3;
            boolean bl3 = bl2;
            Field field = new Field(string5, string6, bl3);
            boolean bl4 = arrayList3.add(field);
        }
        ArrayList<Field> arrayList4 = arrayList2;
        return Collections.unmodifiableList(arrayList4);
    }

    @NotNull
    private static String stringOrEmpty(@Nullable Object object) {
        String string = object == null ? "" : String.valueOf(object);
        return string;
    }

    private static boolean parseBoolean(@NotNull Object object, boolean bl) {
        if (object instanceof Boolean) {
            return (Boolean)object;
        }
        String string = String.valueOf(object).trim();
        Locale locale = Locale.ROOT;
        String lowerCase = string.toLowerCase(locale);
        int n = -1;
        String string2 = lowerCase;
        switch (string2.hashCode()) {
            case 109935: {
                String string3 = lowerCase;
                if (!string3.equals("off")) break;
                n = 6;
                break;
            }
            case 49: {
                String string4 = lowerCase;
                if (!string4.equals("1")) break;
                n = 3;
                break;
            }
            case 97196323: {
                String string5 = lowerCase;
                if (!string5.equals("false")) break;
                n = 4;
                break;
            }
            case 119527: {
                String string6 = lowerCase;
                if (!string6.equals("yes")) break;
                n = 1;
                break;
            }
            case 3521: {
                String string7 = lowerCase;
                if (!string7.equals("no")) break;
                n = 5;
                break;
            }
            case 3551: {
                String string8 = lowerCase;
                if (!string8.equals("on")) break;
                n = 2;
                break;
            }
            case 3569038: {
                String string9 = lowerCase;
                if (!string9.equals("true")) break;
                n = 0;
            }
        }
        int n2 = n;
        return switch (n2) {
            case 4, 5, 6, 7 -> false;
            case 0, 1, 2, 3 -> true;
            default -> bl;
        };
    }

    public record Embed(boolean enabled, String title, String url, List<String> description, int color, boolean timestamp, Author author, Footer footer, String imageUrl, String thumbnailUrl, List<Field> fields) {
        @NotNull
        static Embed load(@NotNull ConfigView configView) {
            boolean boolean_ = configView.getBoolean("discord.embed.enabled", true);
            String string = configView.getString("discord.embed.title", "").trim();
            String string2 = configView.getString("discord.embed.url", "").trim();
            List<String> list = DiscordSettings.readStringOrList(configView.getRaw("discord.embed.description"));
            int n = DiscordSettings.parseColor(configView.getRaw("discord.embed.color"));
            boolean boolean_2 = configView.getBoolean("discord.embed.timestamp", true);
            Author author = Author.load(configView);
            Footer footer = Footer.load(configView);
            String string3 = configView.getString("discord.embed.image.url", "").trim();
            String string4 = configView.getString("discord.embed.thumbnail.url", "").trim();
            List<Field> list2 = DiscordSettings.readFields(configView.getRaw("discord.embed.fields"));
            Embed embed = new Embed(boolean_, string, string2, list, n, boolean_2, author, footer, string3, string4, list2);
            return embed;
        }
    }

    public record Author(String name, String url, String iconUrl) {
        @NotNull
        static Author load(@NotNull ConfigView configView) {
            String string = configView.getString("discord.embed.author.name", "").trim();
            String string2 = configView.getString("discord.embed.author.url", "").trim();
            String string3 = configView.getString("discord.embed.author.icon-url", "").trim();
            Author author = new Author(string, string2, string3);
            return author;
        }
    }

    public record Footer(String text, String iconUrl) {
        @NotNull
        static Footer load(@NotNull ConfigView configView) {
            String string = configView.getString("discord.embed.footer.text", "").trim();
            String string2 = configView.getString("discord.embed.footer.icon-url", "").trim();
            Footer footer = new Footer(string, string2);
            return footer;
        }
    }

    public record Field(String name, String value, boolean inline) {
    }
}

