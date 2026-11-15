package studio.yukilem.yukilem.libs.nixlib.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.yukilem.yukilem.libs.nixlib.command.CommandAction;
import studio.yukilem.yukilem.libs.nixlib.command.SuggestionProvider;

public final class CommandSpec {
    private final String label;
    private final List<String> aliases;
    private final String description;
    private final String permission;
    private final CommandAction rootAction;
    private final CommandAction fallback;
    private final CommandAction permissionDenied;
    private final List<Sub> subs;

    private CommandSpec(Builder builder) {
        this.label = builder.label;
        this.aliases = List.copyOf(builder.aliases);
        this.description = builder.description;
        this.permission = builder.permission;
        this.rootAction = builder.rootAction;
        this.fallback = builder.fallback;
        this.permissionDenied = builder.permissionDenied;
        this.subs = List.copyOf(builder.subs);
    }

    public static Builder builder(@NotNull String string) {
        return new Builder(string);
    }

    public static Sub.Builder sub(@NotNull String string) {
        return new Sub.Builder(string);
    }

    @NotNull
    public String label() {
        return this.label;
    }

    @NotNull
    public List<String> aliases() {
        return this.aliases;
    }

    @NotNull
    public String description() {
        return this.description;
    }

    @Nullable
    public String permission() {
        return this.permission;
    }

    @Nullable
    public CommandAction rootAction() {
        return this.rootAction;
    }

    @Nullable
    public CommandAction fallback() {
        return this.fallback;
    }

    @Nullable
    public CommandAction permissionDenied() {
        return this.permissionDenied;
    }

    @NotNull
    public List<Sub> subs() {
        return this.subs;
    }

    @Nullable
    public Sub findSub(@NotNull String string) {
        for (Sub sub : this.subs) {
            Iterator<String> iterator = sub.names().iterator();
            while (iterator.hasNext()) {
                if (!iterator.next().equalsIgnoreCase(string)) continue;
                return sub;
            }
        }
        return null;
    }

    public static final class Builder {
        private final String label;
        private final List<String> aliases = new ArrayList<String>();
        private String description = "";
        private String permission;
        private CommandAction rootAction;
        private CommandAction fallback;
        private CommandAction permissionDenied;
        private final List<Sub> subs = new ArrayList<Sub>();

        Builder(@NotNull String string) {
            this.label = string;
        }

        public Builder aliases(@Nullable Collection<String> collection) {
            if (collection != null) {
                for (String string : collection) {
                    if (string == null || string.isBlank()) continue;
                    this.aliases.add(string);
                }
            }
            return this;
        }

        public Builder description(@NotNull String string) {
            this.description = string;
            return this;
        }

        public Builder permission(@Nullable String string) {
            this.permission = string;
            return this;
        }

        public Builder onRoot(@Nullable CommandAction commandAction) {
            this.rootAction = commandAction;
            return this;
        }

        public Builder onUnknown(@Nullable CommandAction commandAction) {
            this.fallback = commandAction;
            return this;
        }

        public Builder onPermissionDenied(@Nullable CommandAction commandAction) {
            this.permissionDenied = commandAction;
            return this;
        }

        public Builder sub(@NotNull Sub sub) {
            this.subs.add(sub);
            return this;
        }

        public Builder sub(@NotNull Sub.Builder builder) {
            return this.sub(builder.build());
        }

        public CommandSpec build() {
            return new CommandSpec(this);
        }
    }

    public static final class Sub {
        private final List<String> names;
        private final String permission;
        private final CommandAction action;
        private final List<SuggestionProvider> argSuggestions;

        private Sub(Builder builder) {
            this.names = List.copyOf(builder.names);
            this.permission = builder.permission;
            this.action = builder.action;
            this.argSuggestions = List.copyOf(builder.argSuggestions);
        }

        @NotNull
        public List<String> names() {
            return this.names;
        }

        @NotNull
        public String primary() {
            return this.names.get(0);
        }

        @Nullable
        public String permission() {
            return this.permission;
        }

        @Nullable
        public CommandAction action() {
            return this.action;
        }

        @NotNull
        public List<SuggestionProvider> argSuggestions() {
            return this.argSuggestions;
        }

        public static final class Builder {
            private final List<String> names = new ArrayList<String>();
            private String permission;
            private CommandAction action;
            private final List<SuggestionProvider> argSuggestions = new ArrayList<SuggestionProvider>();

            Builder(@NotNull String string) {
                this.names.add(string);
            }

            public Builder aliases(@Nullable Collection<String> collection) {
                if (collection != null) {
                    for (String string : collection) {
                        if (string == null || string.isBlank()) continue;
                        this.names.add(string);
                    }
                }
                return this;
            }

            public Builder permission(@Nullable String string) {
                this.permission = string;
                return this;
            }

            public Builder action(@Nullable CommandAction commandAction) {
                this.action = commandAction;
                return this;
            }

            public Builder arg(@NotNull SuggestionProvider suggestionProvider) {
                this.argSuggestions.add(suggestionProvider);
                return this;
            }

            public Builder arg(@NotNull Collection<String> collection) {
                List<String> list = List.copyOf(collection);
                this.argSuggestions.add(commandContext -> list);
                return this;
            }

            public Builder argNone() {
                this.argSuggestions.add(commandContext -> Collections.emptyList());
                return this;
            }

            public Sub build() {
                return new Sub(this);
            }
        }
    }
}

