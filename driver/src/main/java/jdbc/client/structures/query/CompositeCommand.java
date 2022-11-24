package jdbc.client.structures.query;

import jdbc.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.Protocol.Command;
import redis.clients.jedis.Protocol.Keyword;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CompositeCommand {
    private final Command command;
    private final Keyword keyword;
    private final String[] params;
    private Set<String> paramsSet;

    public CompositeCommand(@NotNull Command command,
                            @Nullable Keyword keyword,
                            @NotNull String[] params) {
        this.command = command;
        this.keyword = keyword;
        this.params = params;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CompositeCommand)) return false;
        CompositeCommand c = (CompositeCommand) o;
        return Objects.equals(c.command, command) && Objects.equals(c.keyword, keyword);
    }

    @Override
    public int hashCode() {
        return command.hashCode() ^ (keyword == null ? 0 : keyword.hashCode());
    }

    @NotNull
    public Command getCommand() {
        return command;
    }

    @NotNull
    public String[] getParams() {
        return params;
    }

    public boolean containsParam(@NotNull Keyword keyword) {
        if (paramsSet == null) {
            paramsSet = Arrays.stream(params).map(Utils::toUpperCase).collect(Collectors.toSet());
        }
        return paramsSet.contains(keyword.name());
    }

    public static CompositeCommand create(@NotNull Command command, @Nullable Keyword keyword) {
        return  new CompositeCommand(command, keyword, new String[0]);
    }

    public static CompositeCommand create(@NotNull Command command) {
        return create(command, null);
    }
}
