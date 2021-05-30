package net.langreader.language;

import lombok.*;
import net.langreader.model.Language;

@Getter @Setter @NoArgsConstructor @ToString @AllArgsConstructor
public class LangStatistics {
    private Language language;
    private int knownCount;
    private int studiedCount;
    private int ignoredCount;
}
